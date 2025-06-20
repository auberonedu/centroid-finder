const path = require("path");
require("dotenv").config({ path: path.resolve(__dirname, "../../.env") });

const express = require('express');
const router = express.Router();
const { generatePreview } = require("../controllers/videoController");

// import the video controller
const videoController = require('../controllers/videoController');

router.get('/', videoController.listVideos);
router.get('/thumbnail/:filename', videoController.generateThumbnail);
router.get("/preview/:filename", videoController.generatePreview);

module.exports = router;