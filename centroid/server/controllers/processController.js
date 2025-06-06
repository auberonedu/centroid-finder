const path = require("path");
require("dotenv").config({ path: path.resolve(__dirname, "../../.env") });

const fs = require('fs');
const { v4: uuidv4 } = require('uuid');
const { spawn } = require('child_process');

// Resolve and verify environment variables
const VIDEO_DIR = path.resolve(__dirname, '../../../', process.env.VIDEO_DIR);
const RESULTS_DIR = path.resolve(__dirname, '../../../', process.env.RESULTS_DIR);
const JAR_PATH = path.resolve(__dirname, '../../../', process.env.JAR_PATH);
const JOBS_DIR = path.resolve(__dirname, '../utils/jobs');

// Ensure the jobs directory exists
if (!fs.existsSync(JOBS_DIR)) {
  fs.mkdirSync(JOBS_DIR, { recursive: true });
}

// start a new video processing job
exports.startJob = (req, res) => {
  const { filename } = req.params;
  const { targetColor, threshold } = req.query;

  // Validate query parameters
  if (!targetColor || !threshold) {
    return res.status(400).json({ error: 'Missing targetColor or threshold query parameter.' });
  }

  // resolve paths
  const inputPath = path.join(VIDEO_DIR, filename);
  const jobId = uuidv4();
  const outputCsv = path.join(RESULTS_DIR, `${jobId}.csv`);
  const statusFile = path.join(JOBS_DIR, `${jobId}.status`);

  // validate paths
  if (!fs.existsSync(inputPath)) {
    return res.status(404).json({ error: `Input video not found: ${inputPath}` });
  }

  if (!JAR_PATH || !fs.existsSync(JAR_PATH)) {
    return res.status(500).json({ error: `JAR file not found: ${JAR_PATH}` });
  }

  // Write "processing" status
  fs.writeFileSync(statusFile, 'processing');

  const args = ['-jar', JAR_PATH, inputPath, outputCsv, targetColor, threshold];
  console.log("Starting Java process with command: java", args.join(' '));

  const child = spawn('java', args);

  // log output for debugging
  child.stdout.on('data', (data) => {
    console.log(`stdout: ${data}`);
  });

  child.stderr.on('data', (data) => {
    console.error(`stderr: ${data}`);
  });

  child.on('exit', (code) => {
    console.log(`Java process exited with code ${code}`);
    if (code === 0) {
      fs.writeFileSync(statusFile, `done:${path.relative(process.cwd(), outputCsv)}`);
    } else {
      fs.writeFileSync(statusFile, `error:Process exited with code ${code}`);
    }
  });

  res.status(202).json({ jobId });
};

// get job status
exports.getJobStatus = (req, res) => {
  const { jobId } = req.params;
  const statusPath = path.join(JOBS_DIR, `${jobId}.status`);

  if (!fs.existsSync(statusPath)) {
    return res.status(404).json({ error: 'Job ID not found' });
  }

  const status = fs.readFileSync(statusPath, 'utf-8');

  if (status.startsWith('done:')) {
    return res.status(200).json({
      status: 'done',
      result: `/results/${path.basename(status.split(':')[1])}`
    });
  }

  if (status.startsWith('error:')) {
    return res.status(200).json({
      status: 'error',
      error: status.split(':')[1]
    });
  }

  res.status(200).json({ status: 'processing' });
};