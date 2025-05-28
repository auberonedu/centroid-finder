import request from 'supertest'; // Import for using request from supertest
import app from '../index.js'; // Import app from index.js

// Test suite for video routes
describe('Video Controller Routes', () => {

    // Tests for the GET /api/videos route
    describe('GET /api/videos', () => {
        it('should return an array of video filenames', async () => {
            const response = await request(app).get('/api/videos');

            // Checking for a status code of 200 OK response
            expect(response.status).toBe(200);

            // Checking that the response body is an array
            expect(Array.isArray(response.body)).toBe(true);
        });
    });

    // Tests for the GET /thumbnail/:filename route
    describe('GET /thumbnail/:filename', () => {
        const existingFile = 'Salamander5Seconds.mp4'; // A real video in public/videos
        const nonExistentFile = 'notfound.mp4'; // A fake video for testing

        it('should return 404 if filename param is missing', async () => {
            // No filename was passed, expecting a 404 due to no matching route
            const response = await request(app).get('/thumbnail/');
            expect(response.status).toBe(404); // No matching route
        });

        it('should return 404 if video file does not exist', async () => {
            // Requesting a thumbnail for a non-existent video
            const response = await request(app).get(`/thumbnail/${nonExistentFile}`);

            // Checking that a status code of 404 with a proper error message is returned
            expect(response.status).toBe(404);
            expect(response.body).toHaveProperty('error', 'Video file not found');
        });

        it('should return 200 and a JPEG image if the file exists', async () => {
            // Requesting a valid video thumbnail
            const response = await request(app).get(`/thumbnail/${existingFile}`);

            // Checking that a status code of 200 with a JPEG is returned
            expect(response.status).toBe(200);
            expect(response.headers['content-type']).toMatch(/jpeg/);
        });
    });
});