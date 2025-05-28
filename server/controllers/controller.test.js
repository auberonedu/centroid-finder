import request from 'supertest'; // Supertest allows HTTP assertions
import express from 'express';
import dotenv from 'dotenv';
import router from '../routers/router.js'; // Import the full router, not the controller directly

dotenv.config({ path: '../config.env' }); // Load environment variables

const app = express(); // Create a new Express app instance
app.use('/', router); // Mount the router under root

describe('GET requests', () => {
    it('GET /api/videos fetches list of videos successfully', async () => {
        // Act: make a GET request to /api/videos
        const res = await request(app).get('/api/videos');

        // Assert: check if response is 200 and data is returned
        expect(res.statusCode).toBe(200);
        expect(Array.isArray(res.body)).toBe(true); // Should return an array
    });
});

describe('POST request', () => {
    it('POST /process/:filename triggers the video processor', async () => {
        const filename = 'sample_video_1.mp4';
        const targetColor = 'ff0000';
        const threshold = 50;

        const res = await request(app)
            .post(`/process/${filename}`)
            .query({ targetColor, threshold });

        // Check response structure
        expect(res.statusCode).toBe(202);
        expect(res.body).toHaveProperty('jobId');
    });
});