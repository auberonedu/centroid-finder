import fs from 'fs/promises';
import path from 'path';
import ffmpeg from 'fluent-ffmpeg';
import { spawn } from 'child_process';

// this is built in into node so no need to install 
import { randomUUID } from 'crypto';

const getVideos = async (req, res) => {

  // console.log(process.env.VIDEO_PATH)

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

}

  const startVideoProcess = (req, res) => {
    const { filename } = req.params;
    const { targetColor, threshold } = req.query;

    // Validate required query params
    if (!targetColor || !threshold) {
      return res.status(400).json({
        error: 'Missing targetColor or threshold query parameter.'
      });
    }

    // Validate threshold is a number
    const thresholdNum = Number(threshold);
    if (Number.isNaN(thresholdNum)) {
      return res.status(400).json({
        error: 'Threshold must be a valid number.'
      });
    }

    try {
      const jobId = randomUUID();

      // Spawn detached child process to run the Java jar asynchronously
      const child = spawn('java', [
        '-jar',
        '../Processor/target/centroidFinderVideo-jar-with-dependencies.jar',
        "videos/" + filename,
        targetColor,
        thresholdNum.toString(),
        jobId
      ], {
        detached: true,
        stdio: 'inherit'
      });

      // Let child run independently
      child.unref();

      // Immediately respond with 202 and jobId
      res.status(202).json({ jobId });
    } catch (err) {
      console.error('Error starting job:', err);
      res.status(500).json({ error: 'Error starting job' });
    }
  };



export default {
  getVideos,
  getThumbnail,
  startVideoProcess
};
