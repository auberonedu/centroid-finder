import { Router } from "express";
import controller from "../controllers/controller.js";
import multer from "multer";

const router = Router();

router.get('/api/videos', controller.getVideos);

router.get('/thumbnail/:filename', controller.getThumbnail);

router.post('/process/:filename', controller.startVideoProcess);

// New route to check job status
router.get('/process/:jobId/status', controller.getJobStatus);

router.post('/api/upload', controller.upload.single('video'), controller.UploadVideo);

router.get("/api/completed", controller.getCompletedJobs);

router.delete("/api/completed/:jobId", controller.deleteCompletedJob);

router.delete("/api/completed", controller.clearAllCompletedJobs);



export default router;
