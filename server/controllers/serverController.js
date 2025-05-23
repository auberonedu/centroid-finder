import fs from 'fs';
import path from 'path';
import { fileURLToPath } from 'url';
import { spawn } from 'child_process';
import { v4 as uuidv4 } from 'uuid';
import dotenv from 'dotenv';

dotenv.config();

const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);

const VIDEOS_DIR = path.resolve(__dirname, '../../videos');
const RESULTS_DIR = path.resolve(__dirname, '../../results');
const JOBS_DIR = path.resolve(__dirname, '../../jobs');
const JAR_PATH = process.env.JAR_PATH || './processor/target/video-processor.jar';

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

  const ffmpeg = spawn('ffmpeg', args);

  res.setHeader('Content-Type', 'image/jpeg');

  ffmpeg.stdout.pipe(res);

  ffmpeg.stderr.on('data', data => {
    console.error(`FFmpeg error: ${data}`);
  });

  ffmpeg.on('error', () => {
    res.status(500).json({ error: 'Error generating thumbnail' });
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
  const outputPath = path.join(RESULTS_DIR, `${filename}.csv`);

  if (!fs.existsSync(inputPath)) {
    return res.status(404).json({ error: 'Video file not found' });
  }

  const jobId = uuidv4();
  const jobFile = path.join(JOBS_DIR, `${jobId}.json`);

  const initialStatus = {
    status: 'processing',
    resultFile: `${filename}.csv`
  };

  fs.writeFileSync(jobFile, JSON.stringify(initialStatus));

  const javaArgs = [
    '-jar',
    JAR_PATH,
    inputPath,
    outputPath,
    targetColor,
    threshold
  ];

  const child = spawn('java', javaArgs, {
    detached: true,
    stdio: 'ignore'
  });

  child.unref();

  res.status(202).json({ jobId });
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
      res.json({ status: 'done', result: `/results/${jobData.resultFile}` });
    } else if (jobData.status === 'error') {
      res.json({ status: 'error', error: jobData.error });
    } else {
      res.json({ status: 'processing' });
    }
  } catch (err) {
    console.error(err);
    res.status(500).json({ error: 'Error fetching job status' });
  }
}
