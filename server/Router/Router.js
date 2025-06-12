import { Router } from "express";
import controller from "../Controller/Controller.js";

const router = Router();

// ✅ Specific routes FIRST
router.get('/videos/status', controller.getCompletedCSVs);
router.get('/videos/status/:jobId', controller.getStatus);

// 🛡 Safer dynamic route: Only match valid UUIDs
router.get('/videos/:videoID', controller.getVideoByFilename);


// 🟢 Other routes
router.get('/videos', controller.getVideos);
router.post('/process', controller.videoProcessing);

export default router;
