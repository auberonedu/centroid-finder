import { Router } from "express";
import controller from "../Controller/Controller.js";

const router = Router();

router.get('/videos', controller.getVideos);
 router.get('/videos/:videoID', controller.getVideoById)
router.post("/process", controller.videoProcessing); 
 router.get('/videos/status', controller.getCompletedCSVs)
 router.get('/videos/status/:jobId', controller.getStatus);   

export default router;
