import controller from './controller.js';

const { getVideos, getThumbnail, postVideo, getStatus } = controller;

// Guide to API testing with Jest: https://devot.team/blog/jest-api-testing

describe('GET requests', () => {
    // All GET requests here
    it('GET /api/videos fetches list of videos successfully', async() => {
        // Arrange
        // Act
        // Assert
    })
});

describe('POST request', () => {
    // All GET requests here
});