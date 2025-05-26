import { Router } from "express";
import { processVid, getJobStatus, getJobs } from "../controllers/controller.js";

const router = Router();

router.post("/process-video", processVid);
router.get("/jobs/:jobId", getJobStatus);
router.get("/jobs", getJobs);


export default router;