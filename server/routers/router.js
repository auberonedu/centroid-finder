import express from 'express';
import controller from './../controllers/controller.js'

const router = express.Router();

const { getVideos, getThumbnail, postVideo, getStatus } = controller;

router.get('/', (req, res) => {
  res.send('🚀 Centroid Finder API is running! Available routes: /api/videos, /process/:filename');
});

router.get("/api/videos", getVideos);
router.get("/thumbnail/:filename", getThumbnail);
router.get("/process/:jobId/status", getStatus);
router.post("/process/:filename", postVideo);

export default router;