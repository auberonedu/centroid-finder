import fs from 'fs';
import path from 'path';
import { fileURLToPath } from 'url';
import { spawn } from 'child_process';
import { v4 as uuidv4 } from 'uuid';
import dotenv from 'dotenv';
import ffmpegPath from 'ffmpeg-static'; 

const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);

dotenv.config({ path: path.resolve(__dirname, '..', '..', '.env') });

const ROOT_DIR = path.resolve(__dirname, '..', '..');

const VIDEOS_DIR = path.resolve(ROOT_DIR, process.env.VIDEO_DIR);
const RESULTS_DIR = path.resolve(ROOT_DIR, process.env.RESULTS_DIR);
const JOBS_DIR = path.resolve(ROOT_DIR, process.env.JOBS_DIR);
const JAR_PATH = path.resolve(ROOT_DIR, process.env.JAR_PATH);

// Ensure directories exist
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
    '-i', inputPath, //video from which to take the frame from 
    '-ss', '00:00:01.000', //timestamp that frame is captured
    '-vframes', '1', //extract only 1 frame
    '-f', 'image2pipe', //output format
    '-vcodec', 'mjpeg', //mjpeg ensures its in jpeg format
    'pipe:1' 
  ];

  //ensure content is jpeg
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
    return res.status(400).json({ error: 'Missing targetColor or threshold query parameter' });
  }

  if (threshold < 0) {
    return res.status(400).json({ error: 'Invalid threshold. Must be greater than 0' });
  }

  if (targetColor.length > 6 || targetColor.length < 6) {
    return res.status(400).json({ error: 'Invalid HEX color format' });
  }

  const inputPath = path.join(VIDEOS_DIR, filename);
  if (!fs.existsSync(inputPath)) {
    return res.status(500).json({ error: 'Video file not found' });
  }

  try {
    //generate unique jobID using UUID
    const jobId = uuidv4();
    const jobFile = path.join(JOBS_DIR, `${jobId}.json`);

    //store the job status as JSON
    const initialStatus = {
      status: 'processing',
      resultFile: `${filename}.csv`
    };

    //write job status to job file
    fs.writeFileSync(jobFile, JSON.stringify(initialStatus));

    //CSV file results
    const outputPath = path.join(RESULTS_DIR, `${filename}.csv`);
    const javaArgs = [
      '-jar',
      JAR_PATH,
      inputPath,
      outputPath,
      targetColor,
      threshold
    ];

    //spawn java process
    const child = spawn('java', javaArgs, {
      stdio: ['ignore', 'pipe', 'pipe'],
      detached: true
    });
    
    child.stdout.on('data', (data) => {
      console.log(`Java output: ${data}`);
    });
    child.stderr.on('data', (data) => {
      console.error(`Java error: ${data}`);
    });
    
    //handle errors that occur when attempting to start process
    child.on('error', (err) => {
      console.error('Failed to start Java process:', err);
      // Update job status file to error
      fs.writeFileSync(jobFile, JSON.stringify({
        status: 'error',
        error: err.message,
        resultFile: `${filename}.csv`
      }));
    });

    child.on('exit', (code, signal) => {
      if (code === 0) {
        console.log(`Java process finished successfully for job ${jobId}`);
        fs.writeFileSync(jobFile, JSON.stringify({
          status: 'done',
          resultFile: `${filename}.csv`
        }));
      } else {
        console.error(`Java process exited with code ${code} or signal ${signal} for job ${jobId}`);
        fs.writeFileSync(jobFile, JSON.stringify({
          status: 'error',
          error: `Process exited with code ${code} or signal ${signal}`,
          resultFile: `${filename}.csv`
        }));
      }
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
