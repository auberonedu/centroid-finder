import fs from 'fs/promises';
import path from 'path';

const getVideos = async (req, res) => {
  try {
    const videoDir = path.join(process.cwd(), 'videos');
    const files = await fs.readdir(videoDir);

    const videoURLs = files.map(file => `/videos/${file}`);
    res.json(videoURLs);
  } catch (err) {
    console.error('Error reading video directory:', err);
    res.status(500).json({ error: 'Failed to read video directory' });
  }
};

export default {
    getVideos
};
