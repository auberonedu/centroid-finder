const express = require('express');
const router = express.Router();
const fs = require('fs');
const path = require('path');

// GET /api/videos - List all video files from VIDEO_DIR
router.get('/', (req, res) => {
  const dir = process.env.VIDEO_DIR;
  fs.readdir(dir, (err, files) => {
    if (err) {
      return res.status(500).json({ error: 'Error reading video directory' });
    }
    const videoFiles = files.filter(file => file.endsWith('.mp4') || file.endsWith('.mov'));
    res.json(videoFiles);
  });
});

module.exports = router;