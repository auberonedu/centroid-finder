import express from 'express';
import * as controller from '../controllers/controller.js';

const router = express.Router();

router.get("/api/videos", controller.getAllVideos);
router.get("/thumbnail/:filename",);
router.post("/process/:filename",);
router.get("/process/:jobId/status",);

export default router;