import { spawn } from "child_process";
import { v4 as uuid } from "uuid";
import path from "path";
import fs from "fs";
import ffmpegPath from "ffmpeg-static"; // ✅ Static ffmpeg binary

const jobStatus = new Map();

export const processVid = (req, res) => {
    const { file, color = "255,0,0", threshold = 95 } = req.body;

    const inputPath = path.join(process.env.VIDEO_DIR, file);
    const jobId = uuid();
    const outputFile = `${jobId}.csv`;
    const outputPath = path.join(process.env.OUTPUT_DIR, outputFile);

    if (!fs.existsSync(process.env.OUTPUT_DIR)) {
        fs.mkdirSync(process.env.OUTPUT_DIR, { recursive: true });
    }

    const jarInput = [
        "-jar",
        process.env.JAR_PATH,
        inputPath,
        outputPath,
        color,
        threshold.toString()
    ];

    const jar = spawn("java", jarInput, {
        detached: true,
        stdio: "ignore"
    });

    jar.unref();

    jobStatus.set(jobId, {
        status: "processing",
        output: outputFile
    });

    res.json({ jobId });
};

export const getJobStatus = (req, res) => {
    const job = jobStatus.get(req.params.jobId);

    if (!job) {
        return res.status(404).json({ error: "Job not found" });
    }
    res.json(job);
};

export const getJobs = (req, res) => {
    const jobs = [];

    for (const [jobId, data] of jobStatus.entries()) {
        jobs.push({ jobId, ...data });
    }
    res.json(jobs);
};

export const videos = (req, res) => {
    const videoDir = process.env.VIDEO_DIR;

    fs.readdir(videoDir, (err, files) => {
        if (err) {
            console.error("Cannot read from video directory: ", err);
            return res.status(500).json({ error: "Cannot read from video directory" });
        }

        const videoFiles = files.filter(file => file.endsWith(".mp4"));
        res.status(200).json(videoFiles);
    });
};

export const thumbnail = (req, res) => {
    // Extract the video file name from the request URL
    const fileName = req.params.filename;

    // Resolve the full path to the video file
    const videoPath = path.join(process.env.VIDEO_DIR, fileName);

    // If the video doesn't exist, return a 404 error
    if (!fs.existsSync(videoPath)) {
        return res.status(404).json({ error: "Video not found" });
    }

    // Define the output directory for storing generated thumbnails
    const thumbnailDir = path.join(process.cwd(), "thumbnails");

    // Define the output file path for the thumbnail image
    // e.g., thumbnails/sample.mp4.jpg
    const thumbnailFile = `${fileName}.jpg`;
    const thumbnailPath = path.join(thumbnailDir, thumbnailFile);

    // Ensure the thumbnail directory exists — create it if not
    if (!fs.existsSync(thumbnailDir)) {
        fs.mkdirSync(thumbnailDir, { recursive: true });
    }

    // Use Node.js `child_process.spawn()` to call ffmpeg via ffmpeg-static
    const ffmpeg = spawn(ffmpegPath, [
        "-y",               // Overwrite output file if it exists
        "-i", videoPath,    // Input video file path
        "-ss", "00:00:01",  // Seek to 1 second into the video
        "-vframes", "1",    // Extract exactly one video frame
        thumbnailPath       // Output path for the image
    ]);

    // When ffmpeg finishes
    ffmpeg.on("close", (code) => {
        // If ffmpeg succeeded and the file was created
        if (code === 0 && fs.existsSync(thumbnailPath)) {
            // Set the response type to JPEG
            res.set("Content-Type", "image/jpeg");

            // Pipe the JPEG file directly to the response stream
            fs.createReadStream(thumbnailPath).pipe(res);
        } else {
            // If ffmpeg failed, log and return 500
            console.error("FFmpeg failed or thumbnail not created");
            res.status(500).json({ error: "Failed to generate thumbnail" });
        }
    });

    // If ffmpeg fails to start at all (e.g., wrong binary path)
    ffmpeg.on("error", (err) => {
        console.error("FFmpeg spawn error:", err);
        res.status(500).json({ error: "Failed to spawn ffmpeg" });
    });
};