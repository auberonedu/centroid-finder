import { Router } from "express";
import controller from "../Controller/Controller.js";

const router = Router();

router.get('/videos', controller.getVideos);
router.get('/preview/:videoID', controller.getPreview)
router.post('/videos/:jobID', controller.start)
router.get('/videos/process/:jobId/status', controller.getJobStatus);   

export default router;