import { Router } from "express";
import controller from "../Controller/Controller.js";

const { 
  getVideos,
  getVideoByFilename,
  videoProcessing,
  getCompletedCSVs,
  getStatus
} = controller;

const router = Router();

// ✅ Specific routes FIRST
router.get('/videos/status', getCompletedCSVs);
router.get('/videos/status/:jobId', getStatus);

// 🛡 Safer dynamic route: Only match valid UUIDs
router.get('/videos/:videoID', getVideoByFilename);


// 🟢 Other routes
router.get('/videos', getVideos);
router.post('/process', videoProcessing);

export default router;
