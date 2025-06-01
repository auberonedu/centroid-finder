import { Router } from "express";
import controller from "../Controller/Controller.js";

const router = Router();

router.get('/videos', controller.getVideos);
 router.get('/videos/:videoID', controller.getVideoById)
 router.post('/videos/videoID', controller.videoProcessing)
 router.get('/videos/status/:jobId', controller.getJobStatus);   

export default router;