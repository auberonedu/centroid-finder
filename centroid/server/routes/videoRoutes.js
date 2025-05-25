const path = require('path');
require('dotenv').config({ path: path.resolve(__dirname, '../../.env') });

const express = require('express');
const router = express.Router();
const fs = require('fs');
const { exec } = require('child_process');

const VIDEO_DIR = path.resolve(__dirname, process.env.VIDEO_DIR);
const RESULTS_DIR = path.resolve(__dirname, process.env.RESULTS_DIR);
const JAR_PATH = path.resolve(__dirname, process.env.JAR_PATH);

// GET /api/videos - List video files in VIDEO_DIR
router.get('/', async (req, res) => {
  try {
    const files = await fs.promises.readdir(VIDEO_DIR);
    const videoFiles = files.filter(file => file.endsWith('.mp4') || file.endsWith('.mov'));
    res.status(200).json(videoFiles);
  } catch (error) {
    res.status(500).json({ error: 'Error reading video directory' });
  }
});

// GET /api/videos/thumbnail/:filename - Generate and return first frame
router.get('/thumbnail/:filename', (req, res) => {
  const filename = req.params.filename;
  const videoPath = path.join(VIDEO_DIR, filename);
  const outputPath = path.join('sampleOutput', `${filename}.jpg`);

  // Check if the video file exists21qa`
  if (!fs.existsSync('sampleOutput')) {
    fs.mkdirSync('sampleOutput');
  }

  const command = `ffmpeg -y -i "${videoPath}" -vf "select=eq(n\\,0)" -q:v 3 "${outputPath}"`;

  exec(command, (err) => {
    if (err) {
      return res.status(500).json({ error: 'Error generating thumbnail' });
    }
    res.sendFile(path.resolve(outputPath));
  });
});

module.exports = router;