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
    // Salamander API exact error response for directory read failure
    res.status(500).json({ error: 'Error reading video directory' });
  }
}

// GET /thumbnail/:filename
export function generateThumbnail(req, res) {
  const { filename } = req.params;
  const inputPath = path.join(VIDEOS_DIR, filename);

  if (!fs.existsSync(inputPath)) {
    // Salamander API does NOT specify 404 for thumbnail but makes sense to send 500 with error msg:
    return res.status(500).json({ error: 'Error generating thumbnail' });
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

  let errorOccurred = false;

  ffmpeg.stderr.on('data', data => {
    console.error(`FFmpeg error: ${data}`);
    errorOccurred = true;
  });

  ffmpeg.on('error', () => {
    errorOccurred = true;
    res.status(500).json({ error: 'Error generating thumbnail' });
  });

  ffmpeg.on('close', (code) => {
    if (errorOccurred || code !== 0) {
      // If ffmpeg failed, send Salamander API error response
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
    // Exactly as Salamander API expects for missing params
    return res.status(400).json({ error: 'Missing targetColor or threshold query parameter.' });
  }

  const inputPath = path.join(VIDEOS_DIR, filename);

  if (!fs.existsSync(inputPath)) {
    // Salamander API doesn't explicitly specify 404 here but let's be consistent:
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

    const child = spawn('java', javaArgs, {
      detached: true,
      stdio: 'ignore'
    });

    child.unref();

    // Return jobId with 202 Accepted
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
    // Salamander API 404 response when job id not found
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
    // Salamander API exact error message for job status fetch failure
    res.status(500).json({ error: 'Error fetching job status' });
  }
}
