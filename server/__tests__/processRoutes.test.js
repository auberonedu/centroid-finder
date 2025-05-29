import request from 'supertest'; // Import for using request from supertest
import app from '../index.js'; // Import app from index.js

// Test suite for processing routes
describe('Process Controller Routes', () => {
    // Test inputs
    const testVideo = 'Salamander5Seconds.mp4';
    const targetColor = '2D0508';
    const threshold = 180;

    let jobId = null; // jobId that will be returned from the server

    // Test suite for POST /process/:filename route
    describe('POST /process/:filename', () => {
        // Test Case 1: Missing query parameters
        it('should return 400 if query params are missing', async () => {
            const response = await request(app).post(`/process/${testVideo}`);

            // Expecting a status code of 400 Bad Request and in error property in the response
            expect(response.status).toBe(400);
            expect(response.body).toHaveProperty('error');
        });

        // Test Case 2: Proper request to start a job
        it('should start a processing job and return a jobId', async () => {
            const response = await request(app)
                .post(`/process/${testVideo}?targetColor=${targetColor}&threshold=${threshold}`);

            // Expecting a status code of 202 Accepted and a jobId
            expect(response.status).toBe(202);
            expect(response.body).toHaveProperty('jobId');

            // Storing the jobId for the getJobStatus test case
            jobId = response.body.jobId;
        });
    });

    // Test suite for GET /process/:jobId/status route
    describe('GET /process/:jobId/status', () => {
        // Simple test to check whether the job is either still processing or done
        it('should return "processing" or "done" status', async () => {
            // Skip if jobId was not set from previous test
            if (!jobId) return;

            const response = await request(app).get(`/process/${jobId}/status`);

            // Expecting a status code of 200 Ok, a status property in the response, and a status of either 'processing' or 'done'
            expect(response.status).toBe(200);
            expect(response.body).toHaveProperty('status');
            expect(['processing', 'done']).toContain(response.body.status);
        });
    });
});