const path = require('path');
const fs = require('fs');
const { v4: uuidv4 } = require('uuid');
const { spawn } = require('child_process');

// resolve environment variables
const VIDEO_DIR = path.resolve(__dirname, '../../../', process.env.VIDEO_DIR);
const RESULTS_DIR = path.resolve(__dirname, '../../../', process.env.RESULTS_DIR);
const JAR_PATH = path.resolve(__dirname, '../../../', process.env.JAR_PATH);
const JOBS_DIR = path.resolve(__dirname, '../utils/jobs');

// creats directories if they do not exist
if (!fs.existsSync(JOBS_DIR)) {
  fs.mkdirSync(JOBS_DIR, { recursive: true });
}

// starts a new video processing job
exports.startJob = (req, res) => {
  const { filename } = req.params;
  const { targetColor, threshold } = req.query;

  if (!targetColor || !threshold) {
    return res.status(400).json({ error: 'Missing targetColor or threshold query parameter.' });
  }

  // defines input and output paths
  const inputPath = path.join(VIDEO_DIR, filename);
  const jobId = uuidv4();
  const outputCsv = path.join(RESULTS_DIR, `${jobId}.csv`);
  const statusFile = path.join(JOBS_DIR, `${jobId}.status`);

  fs.writeFileSync(statusFile, 'processing');

  const args = ['-jar', JAR_PATH, inputPath, outputCsv, targetColor, threshold];
  const child = spawn('java', args, { detached: true, stdio: 'ignore' });

  child.unref();

  child.on('exit', (code) => {
    if (code === 0) {
      fs.writeFileSync(statusFile, `done:${path.relative(process.cwd(), outputCsv)}`);
    } else {
      fs.writeFileSync(statusFile, `error:Process exited with code ${code}`);
    }
  });

  res.status(202).json({ jobId });
};

// gets the status of prev started job
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

  // if failed, return error
  if (status.startsWith('error:')) {
    return res.status(200).json({
      status: 'error',
      error: status.split(':')[1]
    });
  }

  res.status(200).json({ status: 'processing' });
};