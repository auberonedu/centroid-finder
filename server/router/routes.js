import express from 'express';
import fs from 'fs';
import path from 'path';
import { v4 as uuidv4 } from 'uuid';
import { spawn } from 'child_process';
import dotenv from 'dotenv';
import ffmpegPath from 'ffmpeg-static'; // Using ffmpeg-static for local binary

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
            console.error('Error reading video directory:', err);
            return res.status(500).json({ error: 'Error reading video directory' });
        }
        // Filter for video files only (e.g., .mp4, .mov, .avi)
        const videoFiles = files.filter(f => /\.(mp4|mov|avi|mkv|webm)$/i.test(f));
        res.json(videoFiles);
    });
});

router.get("/thumbnail/:filename", (req, res) => {
    const videoDir = process.env.VIDEO_DIR;
    const { filename } = req.params;

    if (!filename) {
        return res.status(400).json({ error: "Filename is required" });
    }

    const videoPath = path.join(videoDir, filename);

    // Check if the file exists
    if (!fs.existsSync(videoPath)) {
        return res.status(404).json({ error: "Video not found" });
    }

    try {
        // Set Content-Type to JPEG
        res.setHeader('Content-Type', 'image/jpeg');

        // Run ffmpeg to extract the first frame as a JPEG using fluent-ffmpeg
        const ffmpeg = spawn(ffmpegPath, [
            '-i', videoPath,        // input file
            '-frames:v', '1',       // only 1 frame
            '-f', 'image2',         // force image format
            '-q:v', '2',            // quality
            'pipe:1'                // output to stdout
        ]);

        // Pipe ffmpeg output to response
        ffmpeg.stdout.pipe(res);

        // Capture any stderr (error) output from ffmpeg
        ffmpeg.stderr.on('data', (data) => {
            console.error('ffmpeg stderr:', data.toString());
        });

        // Handle errors when spawning the ffmpeg process
        ffmpeg.on('error', (err) => {
            console.error("Failed to start ffmpeg:", err);
            res.status(500).json({ error: "Error generating thumbnail" });
        });

        // Handle ffmpeg process exit status
        ffmpeg.on('close', (code) => {
            if (code !== 0) {
                console.error(`ffmpeg exited with code ${code}`);
                if (!res.headersSent) {
                    res.status(500).json({ error: "Error generating thumbnail" });
                }
            }
        });

    } catch (err) {
        console.error("Thumbnail generation failed:", err);
        res.status(500).json({ error: "Error generating thumbnail" });
    }
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
        '-jar',
        jarPath,
        videoPath,
        resultFile,
        targetColor,
        threshold
    ];
    console.log('Spawning:', 'java', ...args);
    const javaProcess = spawn('java', args, {
        stdio: 'inherit'
    });
    javaProcess.on('error', (err) => {
        console.error('Failed to start Java process:', err);
    });
    javaProcess.on('close', (code) => {
        console.log(`Java process exited with code ${code}`);
    });

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