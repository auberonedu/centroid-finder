import { Router } from "express";
import {
   processVid,
   getJobStatus,
   getJobs,
   videos,
   thumbnail,
} from "../controllers/controller.js";

const router = Router();

// Submit video processing job
router.post("/process", processVid);

// Check job status
router.get("/process/:jobId/status", getJobStatus);

// List all jobs (useful for debugging/testing)
router.get("/jobs", getJobs);

// List available videos (from salamander-api or static folder)
router.get("/api/videos", videos);

// Generate or serve video thumbnail
router.get("/thumbnail/:filename", thumbnail);

export default router;
