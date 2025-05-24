//AI used to generate Jest mock tests for serverController.test.js

import { jest, describe, test, expect, beforeAll, beforeEach, afterEach, afterAll } from '@jest/globals';
import dotenv from 'dotenv';
import path from 'path';

import * as uuid from 'uuid'; // real uuid
import fs from 'fs';

// Mock 'child_process' correctly
jest.mock('child_process', () => ({
  spawn: jest.fn(() => ({
    on: jest.fn(),
    unref: jest.fn(),
  })),
}));

import { spawn } from 'child_process';

// Mock 'fs' module
jest.mock('fs');

dotenv.config({ path: path.resolve(process.cwd(), '.env') });

process.env.VIDEO_DIR = process.env.VIDEO_DIR;
process.env.RESULTS_DIR = process.env.RESULTS_DIR;
process.env.JOBS_DIR = process.env.JOBS_DIR;
process.env.JAR_PATH = process.env.JAR_PATH;

fs.existsSync = jest.fn();
fs.readFileSync = jest.fn();
fs.writeFileSync = jest.fn();
jest.spyOn(fs.promises, 'readdir').mockImplementation(() => Promise.resolve([]));

let serverController;

beforeAll(async () => {
  jest.spyOn(console, 'error').mockImplementation(() => {}); // suppress error logs
  serverController = await import('../controllers/serverController.js');
});

afterAll(() => {
  console.error.mockRestore();
});

describe('serverController', () => {
  let req, res;

  beforeEach(() => {
    req = { params: {}, query: {} };
    res = {
      status: jest.fn().mockReturnThis(),
      json: jest.fn(),
      setHeader: jest.fn(),
    };
  });

  afterEach(() => {
    jest.clearAllMocks();
  });

  describe('listVideos', () => {
    test('lists only video files', async () => {
      fs.promises.readdir.mockResolvedValue(['video1.mp4', 'text.txt', 'movie.avi']);
      await serverController.listVideos(req, res);
      expect(res.status).toHaveBeenCalledWith(200);
      expect(res.json).toHaveBeenCalledWith(['video1.mp4', 'movie.avi']);
    });

    test('handles fs error gracefully', async () => {
      fs.promises.readdir.mockRejectedValue(new Error('FS error'));
      await serverController.listVideos(req, res);
      expect(res.status).toHaveBeenCalledWith(500);
      expect(res.json).toHaveBeenCalledWith({ error: 'Error reading video directory' });
    });
  });

  describe('generateThumbnail', () => {
    test('returns 404 if file not found', () => {
      req.params.filename = 'nonexistent.mp4';
      fs.existsSync.mockReturnValue(false);
      serverController.generateThumbnail(req, res);
      expect(res.status).toHaveBeenCalledWith(404);
      expect(res.json).toHaveBeenCalledWith({ error: 'Video file not found' });
    });
  });

  describe('startProcessingJob', () => {
    test('returns 400 if missing query params', () => {
      req.params.filename = 'video.mp4';
      serverController.startProcessingJob(req, res);
      expect(res.status).toHaveBeenCalledWith(400);
    });

    test('returns 400 for negative threshold', () => {
      req.params.filename = 'video.mp4';
      req.query = { targetColor: 'ff0000', threshold: '-1' };
      serverController.startProcessingJob(req, res);
      expect(res.status).toHaveBeenCalledWith(400);
    });

    test('returns 400 for invalid HEX', () => {
      req.params.filename = 'video.mp4';
      req.query = { targetColor: 'f00', threshold: '100' };
      serverController.startProcessingJob(req, res);
      expect(res.status).toHaveBeenCalledWith(400);
    });

    test('returns 500 if input file not found', () => {
      req.params.filename = 'video.mp4';
      req.query = { targetColor: 'ff0000', threshold: '100' };
      fs.existsSync.mockReturnValue(false);
      serverController.startProcessingJob(req, res);
      expect(res.status).toHaveBeenCalledWith(500);
      expect(res.json).toHaveBeenCalledWith({ error: 'Video file not found' });
    });

    test('spawns job and returns jobId', () => {
      req.params.filename = 'video.mp4';
      req.query = { targetColor: 'ff0000', threshold: '100' };
      fs.existsSync.mockReturnValue(true);
      fs.writeFileSync.mockImplementation(() => {});

      serverController.startProcessingJob(req, res);

      expect(res.status).toHaveBeenCalledWith(202);

      // Check jobId is a non-empty string
      const responseArg = res.json.mock.calls[0][0];
      expect(responseArg).toHaveProperty('jobId');
      expect(typeof responseArg.jobId).toBe('string');
      expect(responseArg.jobId.length).toBeGreaterThan(0);
    });
  });

  describe('getJobStatus', () => {
    test('returns 404 if job not found', () => {
      req.params.jobId = 'missing-job';
      fs.existsSync.mockReturnValue(false);
      serverController.getJobStatus(req, res);
      expect(res.status).toHaveBeenCalledWith(404);
      expect(res.json).toHaveBeenCalledWith({ error: 'Job ID not found' });
    });

    test('returns processing status', () => {
      req.params.jobId = 'job123';
      fs.existsSync.mockReturnValue(true);
      fs.readFileSync.mockReturnValue(JSON.stringify({ status: 'processing' }));
      serverController.getJobStatus(req, res);
      expect(res.status).toHaveBeenCalledWith(200);
      expect(res.json).toHaveBeenCalledWith({ status: 'processing' });
    });

    test('returns done with result file', () => {
      req.params.jobId = 'jobDone';
      fs.existsSync.mockReturnValue(true);
      fs.readFileSync.mockReturnValue(JSON.stringify({ status: 'done', resultFile: 'video.csv' }));
      serverController.getJobStatus(req, res);
      expect(res.status).toHaveBeenCalledWith(200);
      expect(res.json).toHaveBeenCalledWith({ status: 'done', result: '/results/video.csv' });
    });

    test('returns error with message', () => {
      req.params.jobId = 'jobError';
      fs.existsSync.mockReturnValue(true);
      fs.readFileSync.mockReturnValue(JSON.stringify({ status: 'error', error: 'Failure reason' }));
      serverController.getJobStatus(req, res);
      expect(res.status).toHaveBeenCalledWith(200);
      expect(res.json).toHaveBeenCalledWith({ status: 'error', error: 'Failure reason' });
    });

    test('handles file read error', () => {
      req.params.jobId = 'badRead';
      fs.existsSync.mockReturnValue(true);
      fs.readFileSync.mockImplementation(() => { throw new Error('read fail'); });
      serverController.getJobStatus(req, res);
      expect(res.status).toHaveBeenCalledWith(500);
      expect(res.json).toHaveBeenCalledWith({ error: 'Error fetching job status' });
    });
  });
});
