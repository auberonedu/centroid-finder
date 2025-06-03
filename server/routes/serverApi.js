import express from 'express';
import { listVideos, generateThumbnail, startProcessingJob, getJobStatus, } from '../controllers/serverController.js';

const router = express.Router();

// GET /api/videos
router.get('/api/videos', listVideos);

// GET /thumbnail/:filename
router.get('/thumbnail/:filename', generateThumbnail);

// POST /process/:filename
router.post('/process/:filename', startProcessingJob);

// GET /process/:jobId/status
router.get('/process/:jobId/status', getJobStatus);

export default router;
