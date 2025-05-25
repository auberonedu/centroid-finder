const express = require('express');
const router = express.Router();
const { spawn } = require('child_process');
const fs = require('fs');
const path = require('path');
const { v4: uuidv4 } = require('uuid');

// load environment variables
const VIDEO_DIR = path.resolve(process.env.VIDEO_DIR);
const RESULTS_DIR = path.resolve(process.env.RESULTS_DIR);
const JAR_PATH = path.resolve(process.env.JAR_PATH);

// Directory to store job statuses
const JOBS_DIR = path.join(__dirname, '../utils/jobs');
if (!fs.existsSync(JOBS_DIR)) {
  fs.mkdirSync(JOBS_DIR, { recursive: true });
}

// POST /process/:filename - start async job
router.post('/:filename', (req, res) => {
  const { filename } = req.params;
  const { targetColor, threshold } = req.query;

  if (!targetColor || !threshold) {
    return res.status(400).json({ error: 'Missing targetColor or threshold query parameter.' });
  }

  const inputPath = path.join(VIDEO_DIR, filename);
  const jobId = uuidv4();
  const outputCsv = path.join(RESULTS_DIR, `${jobId}.csv`);
  const statusFile = path.join(JOBS_DIR, `${jobId}.status`);

  // Write "processing" status
  fs.writeFileSync(statusFile, 'processing');

  const args = [
    '-jar',
    JAR_PATH,
    inputPath,
    outputCsv,
    targetColor,
    threshold
  ];

  const child = spawn('java', args, {
    detached: true,
    stdio: 'ignore'
  });

  child.unref();

  // when the process ends, update the status file
  child.on('exit', (code) => {
    if (code === 0) {
      fs.writeFileSync(statusFile, `done:${path.relative(process.cwd(), outputCsv)}`);
    } else {
      fs.writeFileSync(statusFile, `error:Process exited with code ${code}`);
    }
  });

  res.status(202).json({ jobId });
});

// GET /process/:jobId/status - check job status
router.get('/:jobId/status', (req, res) => {
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
});

module.exports = router;