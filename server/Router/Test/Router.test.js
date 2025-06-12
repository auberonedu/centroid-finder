import { jest } from '@jest/globals';
import express from 'express';
import request from 'supertest';

let router;

jest.unstable_mockModule('../../Controller/Controller.js', () => ({
  getCompletedCSVs: jest.fn((req, res) => res.status(200).json({ status: 'completed' })),
  getStatus: jest.fn((req, res) => res.status(200).json({ status: 'status', jobId: req.params.jobId })),
  getVideoByFilename: jest.fn((req, res) => res.status(200).json({ videoID: req.params.videoID })),
  getVideos: jest.fn((req, res) => res.status(200).json({ videos: [] })),
  videoProcessing: jest.fn((req, res) => res.status(201).json({ message: 'processing started' })),
}));

describe('Video router integration tests', () => {
  let app;

  beforeAll(async () => {
    const module = await import('../Router.js');
    router = module.default;

    app = express();
    app.use(express.json());
    app.use('/', router);
  });

  test('GET /videos/status returns completed status', async () => {
    const res = await request(app).get('/videos/status');
    expect(res.status).toBe(200);
    expect(res.body).toEqual({ status: 'completed' });
  });

  test('GET /videos/status/:jobId returns status with jobId', async () => {
    const jobId = 'abc123';
    const res = await request(app).get(`/videos/status/${jobId}`);
    expect(res.status).toBe(200);
    expect(res.body).toEqual({ status: 'status', jobId });
  });

  test('GET /videos/:videoID returns video by videoID', async () => {
    const videoID = 'vid789';
    const res = await request(app).get(`/videos/${videoID}`);
    expect(res.status).toBe(200);
    expect(res.body).toEqual({ videoID });
  });

  test('GET /videos returns list of videos', async () => {
    const res = await request(app).get('/videos');
    expect(res.status).toBe(200);
    expect(res.body).toEqual({ videos: [] });
  });

  test('POST /process starts video processing', async () => {
    const res = await request(app).post('/process').send({ dummy: 'data' });
    expect(res.status).toBe(201);
    expect(res.body).toEqual({ message: 'processing started' });
  });
});