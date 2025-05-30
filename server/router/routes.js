import express from 'express';
import controller from "../controller/controller.js"

const router = express.Router();
const { listVideos, getThumbnail, processVideo, processJobStatus } = controller;

router.get("/videos", listVideos);
router.get("/thumbnail/:filename", getThumbnail);
// POST /process/:filename?targetColor=<hex>&threshold=<int>
router.post('/process/:filename', processVideo);
// GET /process/:jobId/status
router.get('/process/:jobId/status', processJobStatus);

export default router;