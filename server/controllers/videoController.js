import fs from 'fs/promises';
import path from 'path';
import { fileURLToPath } from 'url';
import ffmpeg from 'fluent-ffmpeg';

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
    // Retrieving the filename from the url using req.params
    const { filename } = req.params;

    // Validating that a filename was provided
    if (!filename) {
      return res.status(400).json({ error: "Filename is required" })
    }

    // Creating the full path to the video
    const videoPath = path.join(VIDEO_DIR, filename);

    // Checking that the video file exists
    try {
      await fs.access(videoPath);
    } catch {
      return res.status(404).json({ error: "Video file not found" });
    }

    // Using ffmpeg to extract the first frame at timestamp 0
    ffmpeg(videoPath)
      .on('error', (err) => {
        // Logging and returning an error if ffmpeg fails
        console.error('FFmpeg error:', err);
        return res.status(500).json({ error: 'Error generating thumbnail' });
      })
      // Error catching for development with a more detailed error message
      // .on('error', (err) => {
      //   console.error('FFmpeg thumbnail generation error:', err.message);
      //   res.status(500).json({ error: 'Error generating thumbnail' });
      // })
      .screenshots({
        count: 1, // Only take 1 screenshot
        timestamps: ['0'], // Take the screenshot at 0 seconds
        filename: 'thumbnail.jpg', // Name of the temporary jpeg
        folder: '/tmp', // Name of the temporary directory to store the image. Move this later to the .env file using dotenv
        size: '320x240', // Resizing the output image
      })
      .on('end', () => {
        // When the screenshot/thumbnail is finished, send it back to the client
        const thumbPath = path.join('/tmp', 'thumbnail.jpg');
        res.setHeader('Content-Type', 'image/jpeg');
        res.sendFile(thumbPath, (err) => {
          if (err) {
            console.error('Error sending thumbnail:', err);
            res.status(500).json({ error: 'Error sending thumbnail' });
          }
        });
      });
  } catch (err) {
    // Catch any unexpected errors and respond with a status code of 500 and a message
    console.error("Error generating thumbnail", err);
    res.status(500).json({ error: "Error generating thumbnail" })
  }
};

export default { getAllVideos, getVideoThumbnail };