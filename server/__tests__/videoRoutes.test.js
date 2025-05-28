import request from 'supertest';
import app from '../index.js'; // This is why we exported app

describe('GET /api/videos', () => {
  it('should return an array of video filenames', async () => {
    const res = await request(app).get('/api/videos');
    
    expect(res.statusCode).toBe(200);
    expect(Array.isArray(res.body)).toBe(true);
    expect(res.body.some(name => typeof name === 'string')).toBe(true);
  });
});