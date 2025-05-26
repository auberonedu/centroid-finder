import { Router } from "express";
import { processVid, getJobStatus } from "../controllers/controller.js";

const router = Router();

router.post("/process-video", processVid);
router.get("/jobs/:jobId", getJobStatus);

export default router;