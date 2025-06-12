// import { jest } from '@jest/globals';
// import express from 'express';
// import request from 'supertest';

// let router;

// jest.unstable_mockModule('../../Controller/Controller.js', () => ({
//   getCompletedCSVs: jest.fn((req, res) => res.status(200).json({ status: 'completed' })),
//   getStatus: jest.fn((req, res) => res.status(200).json({ status: 'status', jobId: req.params.jobId })),
//   getVideoByFilename: jest.fn((req, res) => res.status(200).json({ videoID: req.params.videoID })),
//   getVideos: jest.fn((req, res) => res.status(200).json({ videos: [] })),
//   videoProcessing: jest.fn((req, res) => res.status(201).json({ message: 'processing started' })),
// }));

// describe('Video router integration tests', () => {
//   let app;

//   beforeAll(async () => {
//     const module = await import('../Router.js');
//     router = module.default;

//     app = express();
//     app.use(express.json());
//     app.use('/', router);
//   });

//   test('GET /videos/status returns completed status', async () => {
//     const res = await request(app).get('/videos/status');
//     expect(res.status).toBe(200);
//     expect(res.body).toEqual({ status: 'completed' });
//   });

//   test('GET /videos/status/:jobId returns status with jobId', async () => {
//     const jobId = 'abc123';
//     const res = await request(app).get(`/videos/status/${jobId}`);
//     expect(res.status).toBe(200);
//     expect(res.body).toEqual({ status: 'status', jobId });
//   });

//   test('GET /videos/:videoID returns video by videoID', async () => {
//     const videoID = 'vid789';
//     const res = await request(app).get(`/videos/${videoID}`);
//     expect(res.status).toBe(200);
//     expect(res.body).toEqual({ videoID });
//   });

//   test('GET /videos returns list of videos', async () => {
//     const res = await request(app).get('/videos');
//     expect(res.status).toBe(200);
//     expect(res.body).toEqual({ videos: [] });
//   });

//   test('POST /process starts video processing', async () => {
//     const res = await request(app).post('/process').send({ dummy: 'data' });
//     expect(res.status).toBe(201);
//     expect(res.body).toEqual({ message: 'processing started' });
//   });
// });

import { describe, it, expect, vi } from 'vitest';
import request from 'supertest';
import express from 'express';
import router from '../Router.js';
import { generateThumbnail } from '../../Utils/ffmpegHelpers.js';

// Mock the generateThumbnail function
vi.mock('../../Utils/ffmpegHelpers.js', () => ({
  generateThumbnail: vi.fn().mockResolvedValue('thumbnail.jpg')
}));

// Initialize the Express app with the router
const app = express();
app.use(express.json());
app.use(router);

describe('Router.js', () => {
  it('should respond with 200 for GET /videos', async () => {
    const response = await request(app).get('/videos');
    expect(response.status).toBe(200);
    expect(response.body).toHaveProperty('videos');
  });

const nonExistentUUID = '00000000-0000-0000-0000-000000000000'; // valid but likely not in index
const existingUUID = '0b53b320-3431-487f-b4af-910a8df440de';

it('should respond with 404 for GET /videos/:videoID when video not found', async () => {
  const response = await request(app).get(`/videos/${nonExistentUUID}`);
  expect(response.status).toBe(404);
  expect(response.body).toHaveProperty('error', 'Video not found');
});

it('should respond with 200 for GET /videos/:videoID when video exists', async () => {
  const response = await request(app).get(`/videos/${existingUUID}`);
  expect(response.status).toBe(200);
  expect(response.body).toHaveProperty('id', existingUUID);
});

  it('should respond with 400 for POST /process when required fields are missing', async () => {
    const response = await request(app)
      .post('/process')
      .send({ filename: 'video.mp4' }); // Missing color, threshold, and interval
    expect(response.status).toBe(400);
    expect(response.body).toHaveProperty('error', 'Missing required fields');
  });

  it('should respond with 200 for POST /process when all required fields are provided', async () => {
    const response = await request(app)
      .post('/process')
      .send({
        filename: 'video.mp4',
        color: { r: 255, g: 0, b: 0 },
        threshold: 0.5,
        interval: '1s'
      });
    expect(response.status).toBe(200);
    expect(response.body).toHaveProperty('jobId');
    expect(response.body).toHaveProperty('message', 'Video processing started.');
  });
});