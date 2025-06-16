import express from 'express';
import controller from './../controllers/controller.js'

const router = express.Router();

const { getVideos, getThumbnail, postVideo, getStatus } = controller;

router.get('/', (req, res) => {
  res.send('🚀 Centroid Finder API is running! Available routes: <ul> <li>/api/videos</li> <li>/thumbnail/:filename</li> <li>/process/:filename</li> <li>/process/:jobId/status</li> <ul>');
});

// serve static results at /results path
router.use("/results", express.static(process.env.RESULTS_DIR));

router.get("/api/videos", getVideos);
router.get("/thumbnail/:filename", getThumbnail);
router.get("/process/:jobId/status", getStatus);
router.post("/process/:filename", postVideo);

export default router;