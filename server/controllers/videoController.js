import fs from 'fs/promises';
import path from 'path';
import { fileURLToPath } from 'url'; // for resolving ES module paths
import ffmpeg from 'fluent-ffmpeg';
import ffmpegInstaller from '@ffmpeg-installer/ffmpeg'; // need to run through npm and not install locally

// for ffmpegInstaller
ffmpeg.setFfmpegPath(ffmpegInstaller.path);

const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);

// Absolute path to the public/videos directory
// Move this later to the .env file using dotenv
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
    // get filename from the params in the url
    const { filename } = req.params;

    const videoPath = path.join(VIDEO_DIR, filename);

    // access the file
    await fs.access(videoPath);

    // set content-type
    res.setHeader('Content-Type', 'image/jpeg');

    // use ffmpeg to get first frame and set back as .jpg
    ffmpeg(videoPath)
      .on('error', (err) => {
        console.error('FFmpeg error:', err);
        // send 500 if it fails
        res.status(500).json({ error: 'Error generating thumbnail' });
      })
      .frames(1) // first frame
      .format('image2') // needed to output a single image
      .output(res) // send to response
      .run();

  } catch (err) {
    // for other errors such as file not found
    console.error("Error generating thumbnail", err);
    res.status(500).json({ error: "Error generating thumbnail" });
  }
};

export default { getAllVideos, getVideoThumbnail };