import fs from 'fs';
import path from 'path';
import { fileURLToPath } from 'url';
import { spawn } from 'child_process';
import { v4 as uuidv4 } from 'uuid';
import dotenv from 'dotenv';
import ffmpegPath from 'ffmpeg-static'; 

dotenv.config();

const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);

// Resolve environment paths relative to project root
const VIDEOS_DIR = path.resolve(__dirname, '..', '..', process.env.VIDEO_DIR || 'videos');
const RESULTS_DIR = path.resolve(__dirname, '..', '..', process.env.RESULTS_DIR || 'results');
const JOBS_DIR = path.resolve(__dirname, '..', '..', 'jobs');
const JAR_PATH = path.resolve(__dirname, '..', '..', process.env.JAR_PATH || 'processor/target/videoprocessor.jar');

// Ensure required directories exist
fs.mkdirSync(VIDEOS_DIR, { recursive: true });
fs.mkdirSync(RESULTS_DIR, { recursive: true });
fs.mkdirSync(JOBS_DIR, { recursive: true });

// GET /api/videos
export async function listVideos(req, res) {
  try {
    const files = await fs.promises.readdir(VIDEOS_DIR);
    const videoFiles = files.filter(file => /\.(mp4|mov|avi)$/i.test(file));
    res.status(200).json(videoFiles);
  } catch (err) {
    console.error(err);
    res.status(500).json({ error: 'Error reading video directory' });
  }
}

// GET /thumbnail/:filename
export function generateThumbnail(req, res) {
  const { filename } = req.params;
  const inputPath = path.join(VIDEOS_DIR, filename);

  if (!fs.existsSync(inputPath)) {
    return res.status(404).json({ error: 'Video file not found' });
  }

  const args = [
    '-i', inputPath,
    '-ss', '00:00:01.000',
    '-vframes', '1',
    '-f', 'image2pipe',
    '-vcodec', 'mjpeg',
    'pipe:1'
  ];

  const ffmpeg = spawn(ffmpegPath, args); 
  res.setHeader('Content-Type', 'image/jpeg');
  ffmpeg.stdout.pipe(res);

  let errorOccurred = false;

  ffmpeg.stderr.on('data', data => {
    console.error(`FFmpeg error: ${data}`);
    errorOccurred = true;
  });

  ffmpeg.on('error', () => {
    errorOccurred = true;
    if (!res.headersSent) {
      res.status(500).json({ error: 'Error generating thumbnail' });
    }
  });

  ffmpeg.on('close', code => {
    if (errorOccurred || code !== 0) {
      if (!res.headersSent) {
        res.status(500).json({ error: 'Error generating thumbnail' });
      }
    }
  });
}

// POST /process/:filename
export function startProcessingJob(req, res) {
  const { filename } = req.params;
  const { targetColor, threshold } = req.query;

  if (!targetColor || !threshold) {
    return res.status(400).json({ error: 'Missing targetColor or threshold query parameter.' });
  }

  const inputPath = path.join(VIDEOS_DIR, filename);
  if (!fs.existsSync(inputPath)) {
    return res.status(500).json({ error: 'Error starting job' });
  }

  try {
    const jobId = uuidv4();
    const jobFile = path.join(JOBS_DIR, `${jobId}.json`);

    const initialStatus = {
      status: 'processing',
      resultFile: `${filename}.csv`
    };

    fs.writeFileSync(jobFile, JSON.stringify(initialStatus));

    const outputPath = path.join(RESULTS_DIR, `${filename}.csv`);
    const javaArgs = [
      '-jar',
      JAR_PATH,
      inputPath,
      outputPath,
      targetColor,
      threshold
    ];

    console.log(javaArgs);

    const child = spawn('java', javaArgs, {
      detached: true,
      stdio: 'ignore'
    });

    child.unref();
    res.status(202).json({ jobId });
  } catch (err) {
    console.error(err);
    res.status(500).json({ error: 'Error starting job' });
  }
}

// GET /process/:jobId/status
export function getJobStatus(req, res) {
  const { jobId } = req.params;
  const jobFile = path.join(JOBS_DIR, `${jobId}.json`);

  if (!fs.existsSync(jobFile)) {
    return res.status(404).json({ error: 'Job ID not found' });
  }

  try {
    const jobData = JSON.parse(fs.readFileSync(jobFile, 'utf-8'));

    if (jobData.status === 'done') {
      return res.status(200).json({ status: 'done', result: `/results/${jobData.resultFile}` });
    } else if (jobData.status === 'error') {
      return res.status(200).json({ status: 'error', error: jobData.error });
    } else {
      return res.status(200).json({ status: 'processing' });
    }
  } catch (err) {
    console.error(err);
    res.status(500).json({ error: 'Error fetching job status' });
  }
}
