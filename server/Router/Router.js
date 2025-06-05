import { Router } from "express";
import controller from "../Controller/Controller.js";

const router = Router();

router.get("/videos", controller.getVideos);
router.get("/videos/:videoID", controller.getVideoById);
router.post("/process", controller.videoProcessing); // ✅ fixed route
router.get("/videos/status/:jobId", controller.getStatus); // ✅ fixed method name

export default router;
