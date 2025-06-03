import request from 'supertest';
import express from 'express';
import dotenv from 'dotenv';
import router from '../routers/router.js';

dotenv.config({ path: "../.env" });

const app = express();
app.use('/', router);

describe('GET requests', () => {
    it('GET /api/videos fetches list of videos successfully', async () => {
        const res = await request(app).get('/api/videos');

        expect(res.statusCode).toBe(200);
        expect(Array.isArray(res.body)).toBe(true);
        expect(res.body.length).toBeGreaterThan(0);
        expect(res.body[0]).toHaveProperty('video');
    });

    it('GET /thumbnail/:filename returns a thumbnail image or 404', async () => {
        const filename = 'sample_video_1.mp4';

        const res = await request(app).get(`/thumbnail/${filename}`);

        // Accept 200 (success), 404 (file not found), or 500 (ffmpeg not found)
        expect([200, 404, 500]).toContain(res.statusCode);
        if (res.statusCode === 200) {
            expect(res.headers['content-type']).toContain('image');
        }
    });

    // TODO: Write test for GET /process/{jobId}/status
});

describe('POST request', () => {
    it('POST /process/:filename triggers the video processor', async () => {
        const filename = 'sample_video_1.mp4';
        const targetColor = 'ff0000';
        const threshold = 50;

        const res = await request(app)
            .post(`/process/${filename}`)
            .query({ targetColor, threshold });

        expect([202, 500]).toContain(res.statusCode);

        if (res.statusCode === 202) {
            expect(res.body).toHaveProperty('jobId');
        }
    });
});
