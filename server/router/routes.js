import express from 'express';
import fs from 'fs';
import path from 'path';
import { v4 as uuidv4 } from 'uuid';
import { spawn } from 'child_process';
import dotenv from 'dotenv';

dotenv.config({ path: '../.env' });
const router = express.Router();

const JOBS_FILE = process.env.JOBS_FILE;

const readJobs = () => {
    try {
        if (!fs.existsSync(JOBS_FILE)) return {};
        return JSON.parse(fs.readFileSync(JOBS_FILE, 'utf-8'));
    } catch (e) {
        return {};
    }
};

const writeJobs = (jobs) => {
    fs.writeFileSync(JOBS_FILE, JSON.stringify(jobs, null, 2));
};

router.get("/home", (req, res) => {
    res.send('Salamander API server is running!');
});

router.get("/api/videos", (req, res) => {
    const videoDir = process.env.VIDEO_DIR;
    if (!videoDir) {
        return res.status(500).json({ error: 'VIDEO_DIR not set in environment' });
    }
    fs.readdir(videoDir, (err, files) => {
        if (err) {
            console.log(videoDir);
            return res.status(500).json({ error: 'Error reading video directory' });
        }
        // Filter for video files only (e.g., .mp4, .mov, .avi)
        const videoFiles = files.filter(f => /\.(mp4|mov|avi|mkv|webm)$/i.test(f));
        res.json(videoFiles);
    });
});

// POST /process/:filename?targetColor=<hex>&threshold=<int>
router.post('/process/:filename', (req, res) => {
    const { filename } = req.params;
    const { targetColor, threshold } = req.query;
    const videoDir = process.env.VIDEO_DIR;
    const jarPath = process.env.JAR_PATH;
    const resultsDir = process.env.RESULTS_DIR;

    if (!filename || !targetColor || !threshold) {
        return res.status(400).json({ error: 'Missing filename, targetColor, or threshold' });
    }
    const videoPath = path.resolve(videoDir, filename);
    if (!fs.existsSync(videoPath)) {
        return res.status(400).json({ error: 'Video file does not exist' });
    }
    if (!fs.existsSync(jarPath)) {
        return res.status(500).json({ error: 'JAR file not found' });
    }
    if (!fs.existsSync(resultsDir)) {
        fs.mkdirSync(resultsDir, { recursive: true });
    }

    const jobId = uuidv4();
    const jobs = readJobs();
    jobs[jobId] = { status: 'processing', filename, result: null };
    writeJobs(jobs);

    // Build args for the JAR (customize as needed for your JAR's CLI)
    const resultFile = path.resolve(resultsDir, `${jobId}.csv`);
    const args = [
        '-jar', jarPath,
        videoPath,
        targetColor,
        threshold,
        resultFile
    ];
    const javaProcess = spawn('java', args, {
        detached: true,
        stdio: 'ignore'
    });
    javaProcess.unref();

    // Poll for result file (simple approach)
    const checkInterval = setInterval(() => {
        if (fs.existsSync(resultFile)) {
            clearInterval(checkInterval);
            const jobs = readJobs();
            jobs[jobId] = { status: 'done', filename, result: path.basename(resultFile) };
            writeJobs(jobs);
        }
    }, 2000);

    res.status(202).json({ jobId });
});

// GET /process/:jobId/status
router.get('/process/:jobId/status', (req, res) => {
    const { jobId } = req.params;
    const jobs = readJobs();
    if (!jobs[jobId]) {
        return res.status(404).json({ error: 'Job not found' });
    }
    res.json(jobs[jobId]);
});

export default router;