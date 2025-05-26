import { Router } from "express";
import { processVid, getJobStatus, getJobs, videos } from "../controllers/controller.js";

const router = Router();

router.post("/process", processVid);
router.get("/process/:jobId/status", getJobStatus);
router.get("/jobs", getJobs);
router.get("/api/videos", videos);


export default router;