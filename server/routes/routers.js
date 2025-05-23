import express from 'express';
import videoController from '../controllers/videoController.js';

const router = express.Router();

router.get("/api/videos", videoController.getAllVideos);
router.get("/thumbnail/:filename", videoController.getVideoThumbnail);
router.post("/process/:filename",);
router.get("/process/:jobId/status",);

export default router;