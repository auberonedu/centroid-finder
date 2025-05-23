import express from 'express';
import controller from './../controllers/controller.js'

const router = express.Router();

const { getVideos } = controller;

router.get("/api/videos", getVideos);

export default router;