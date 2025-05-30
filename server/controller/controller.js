import dotenv from 'dotenv';
import fs from 'fs';
import path from 'path';
import ffmpegPath from 'ffmpeg-static';
import { spawn } from 'child_process';
import { v4 as uuidv4 } from 'uuid';

dotenv.config({ path: '../.env' });

const listVideos = (req, res) => {
    const videoDir = process.env.VIDEO_DIR;
    if (!videoDir) {
        return res.status(500).json({ error: 'VIDEO_DIR not set in environment' });
    }
    // read list of files in videoDir
    fs.readdir(videoDir, (err, files) => {
        if (err) {
            console.error('Error reading video directory:', err);
            return res.status(500).json({ error: 'Error reading video directory' });
        }
        // filter for video files only
        const videoFiles = files.filter(f => /\.(mp4|mov|avi|mkv|webm)$/i.test(f));
        res.json(videoFiles);
    });
};

const getThumbnail = (req, res) => {
    const videoDir = process.env.VIDEO_DIR; // file env location
    const { filename } = req.params; // url filename
    const videoPath = path.join(videoDir, filename); // construct filepath

    // check for file name and video existence
    if (!filename) {
        return res.status(400).json({ error: "Filename is required" });
    }
    if (!fs.existsSync(videoPath)) {
        return res.status(404).json({ error: "Video not found" });
    }

    try {
        // set response content-type to jpeg
        res.setHeader('Content-Type', 'image/jpeg');

        // run ffmpeg to extract the first frame as a JPEG using fluent-ffmpeg
        const ffmpeg = spawn(ffmpegPath, [
            '-i', videoPath,   // input video
            '-frames:v', '1',  // only 1 frame
            '-f', 'image2',    // force image format
            '-q:v', '2',       // set image quality
            '-update', '1',    // update the output pipe with 1 frame
            'pipe:1',          // output to stdout
            '-loglevel', 'quiet' // suppress all output except errors
        ]);

        // pipe ffmpeg output to response (pipe streams image directly to response)
        ffmpeg.stdout.pipe(res);

        // capture any stderr (error) output from ffmpeg
        ffmpeg.stderr.on('data', (data) => {
            console.error('ffmpeg stderr:', data.toString());
        });

        // handle errors when spawning the ffmpeg process
        ffmpeg.on('error', (err) => {
            console.error("Failed to start ffmpeg:", err);
            res.status(500).json({ error: "Error generating thumbnail" });
        });

        // handle ffmpeg process exit status
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
};

const processVideo = async (req, res) => {
    // extract filename from the req params and target color and threshold from the query string
    const { filename } = req.params;
    const { targetColor, threshold } = req.query;
    const videoDir = process.env.VIDEO_DIR;
    const jarPath = process.env.JAR_PATH;
    const resultsDir = process.env.RESULTS_DIR;

    // combine video directory and filename to resolve full file path of video 
    const videoPath = path.resolve(videoDir, filename);

    // validate
    if (!filename || !targetColor || !threshold) {
        return res.status(400).json({ error: 'Missing filename, targetColor, or threshold' });
    }
    if (!fs.existsSync(videoPath)) {
        return res.status(400).json({ error: 'Video file does not exist' });
    }
    if (!fs.existsSync(jarPath)) {
        return res.status(500).json({ error: 'JAR file not found' });
    }
    if (!fs.existsSync(resultsDir)) {
        fs.mkdirSync(resultsDir, { recursive: true });
    }

    const jobId = uuidv4(); // generate unique job ID for this request
    const jobs = readJobs(); // read current jobs
    jobs[jobId] = { status: 'processing', filename, result: null }; // add new job
    writeJobs(jobs); // write updated job

    // build args for the JAR (prepare args to pass to JAR to execute processing)
    const resultFile = path.resolve(resultsDir, `${jobId}.csv`);
    const args = [
        '-jar',            // specify JAR file
        jarPath,           // path to the JAR file
        videoPath,         // path to the video file to be processed
        resultFile,        // path where the results should be saved (CSV format)
        targetColor,       // target color to look for in the video
        threshold          // threshold value for color matching in the video
    ];
    // spawn java process to run JAR async (spawn runs java process in background)
    const javaProcess = spawn('java', args, {
        detached: true, // run process independently
        stdio: 'ignore' // ignore standard input/output of process
    });
    javaProcess.unref(); // unref the child process to allow background run

    // poll for result file (set interval to check result file every 2 sec, if exists status="done")
    const checkInterval = setInterval(() => {
        if (fs.existsSync(resultFile)) {
            clearInterval(checkInterval);
            const jobs = readJobs();
            jobs[jobId] = { status: 'done', filename, result: path.basename(resultFile) };
            writeJobs(jobs);
        }
    }, 2000);

    res.status(202).json({ jobId });
};

const processJobStatus = (req, res) => {
    // extract jobId req params and stick it to readJobs func, then respond with json if exists
    const { jobId } = req.params;
    const jobs = readJobs();
    if (!jobs[jobId]) {
        return res.status(404).json({ error: 'Job not found' });
    }
    res.json(jobs[jobId]);
}

// helper functions to read and write jobs
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


export default {
    listVideos,
    getThumbnail,
    processVideo,
    processJobStatus
};