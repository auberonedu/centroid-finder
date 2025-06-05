import { Router } from "express";
import controller from "../controllers/controller.js";

const router = Router();

router.get('/api/videos', controller.getVideos);

router.get('/thumbnail/:filename', controller.getThumbnail);

router.post('/process/:filename', controller.startVideoProcess);

// New route to check job status
router.get('/process/:jobId/status', controller.getJobStatus);

export default router;
