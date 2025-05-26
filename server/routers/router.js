import { Router } from "express";
import { processVid, getJobStatus, getJobs } from "../controllers/controller.js";

const router = Router();

router.post("/process", processVid);
router.get("/process/:jobId/status", getJobStatus);
router.get("/jobs", getJobs);


export default router;