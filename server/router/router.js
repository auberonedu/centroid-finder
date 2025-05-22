import { Router } from "express";
import controller from "../controllers/controller.js";

const router = Router();

router.get('/api/videos', controller.getVideos);

router.get('/thumbnail/:filename', controller.getThumbnail); 

router.post('/process/:filename', controller.startVideoProcess);

export default router;
