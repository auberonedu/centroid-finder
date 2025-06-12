import { Router } from "express";
import {
  getCompletedCSVs,
  getStatus,
  getVideoByFilename,
  getVideos,
  videoProcessing,
} from "../Controller/Controller.js";

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
