import fs from 'fs/promises';
import path from 'path';
import { fileURLToPath } from 'url';
import ffmpeg from 'fluent-ffmpeg';

const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);

// Absolute path to the public/videos directory
const VIDEO_DIR = path.resolve(__dirname, '../public/videos');

const getAllVideos = async (req, res) => {
  try {
    const files = await fs.readdir(VIDEO_DIR);

    // get all the .mp4 videos 
    const videos = files.filter(file => file.toLowerCase().endsWith('.mp4'));

    res.status(200).json(videos);
  } catch (err) {
    console.error('Error reading video directory:', err);
    res.status(500).json({ error: 'Error reading video directory' });
  }
};

const getVideoThumbnail = async (req, res) => {
  try {
    const { filename } = req.params;

    // Checking if no file name was passed
    if (!filename) {
      return res.status(400).json({ error: "Filename is required"})
    }

    const videoPath = path.join(VIDEO_DIR, filename);

    // Checking if the file was found
    try {
      await fs.access(videoPath);
    } catch {
      return res.status(404).json({ error: "Video file not found" });
    }

  } catch (err) {
    console.error("Error generating thumbnail", err);
    res.status(500).json({ error: "Error generating thumbnail"})
  }
}

export default { getAllVideos, getVideoThumbnail }