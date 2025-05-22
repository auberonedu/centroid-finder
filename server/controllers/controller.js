import fs from 'fs/promises';
import path from 'path';
import ffmpeg from 'fluent-ffmpeg';

// this is built in into node so no need to install 
import { randomUUID } from 'crypto';

const getVideos = async (req, res) => {

  console.log(process.env.VIDEO_PATH)

  try {
    // const videoDir = path.join(process.cwd(), 'videos');
    const videoDir = process.env.VIDEO_PATH;
    const files = await fs.readdir(videoDir);

    const videoURLs = files.map(file => `${file}`);
    res.json(videoURLs);
  } catch (err) {
    console.error('Error reading video directory:', err);
    res.status(500).json({ error: 'Failed to read video directory' });
  }
};

const getThumbnail = (req, res) => {
  try {

    const videoDir = process.env.VIDEO_PATH;
    const thumbnailId = req.params.filename;
    const videoPath = path.join(videoDir, thumbnailId);

    res.setHeader('Content-Type', 'image/jpeg');

    ffmpeg(videoPath)
      .inputOptions(['-ss 0'])        // jump to start
      .outputOptions(['-frames:v 1']) // capture 1 frame
      .format('mjpeg')                // output format as JPEG
      .pipe(res, { end: true });      // stream directly to response
  } catch (err) {
    console.error('Error reading video directory:', err);
    res.status(500).json({
      error: "Error generating thumbnail"
    });
  };

  const startProcess = (req, res) => {
    const { targetColor, threshold } = req.query;

    if (!targetColor || !threshold) {
      return res.status(400).json({ error: "Missing targetColor or threshold query parameter." })
    }
    try {
      const videoDir = process.env.VIDEO_PATH;


      const jobId = randomUUID();

      // TODO: Start async job (e.g. queue system or background worker)
      console.log(`Starting processing job for ${filename}`);
      console.log(`Target Color: ${targetColor}, Threshold: ${threshold}`);
      console.log(`Job ID: ${jobId}`);

      // Respond to client with 202 Accepted and jobId
      res.status(202).json({ jobId });

    } catch (err) {
      console.error('Error starting job:', err);
      res.status(500).json({ error: 'Error starting job' });
    }
  };

}

export default {
  getVideos,
  getThumbnail
};
