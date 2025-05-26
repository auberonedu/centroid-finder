
const request = require('supertest');
const express = require('express');
const app = express();

// load routes
const processRoutes = require('./routes/processRoutes');

// middleware
app.use(express.json());
app.use('/api/process', processRoutes);

describe('POST /api/process/:filename', () => {
  it('should start a processing job and return jobId', async () => {
    const response = await request(app)
      .post('/api/process/sample.mp4?targetColor=0,255,0&threshold=20');

    expect(response.statusCode).toBe(202);
    expect(response.body).toHaveProperty('jobId');
  });

  it('should return 400 if query params are missing', async () => {
    const response = await request(app)
      .post('/api/process/sample.mp4');

    expect(response.statusCode).toBe(400);
    expect(response.body).toHaveProperty('error');
  });
});

describe('GET /api/process/:jobId/status', () => {
  it('should return 404 for non-existing jobId', async () => {
    const response = await request(app)
      .get('/api/process/non-existent-job-id/status');

    expect(response.statusCode).toBe(404);
  });
});