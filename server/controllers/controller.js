import { spawn } from "child_process";
import { v4 as uuid } from "uuid";
import path from "path";
import fs from "fs";

const jobStatus = new Map();

export const processVid = (req, res) => {
    // Destructuring variables into the body
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
        // Using ... spread operator to unpack all the key-value pairs inside the data object
        jobs.push({ jobId, ...data});
    }
    res.json(jobs);
};

export const videos = (req, res) => {
    const videoDir = process.env.VIDEO_DIR;

    fs.readdir(videoDir, (err, files) => {
        // Checking if the videos files can be read through
        if (err) {
            console.error("Cannot read from video directory: ", err);
            return res.status(500).json({error: "Cannot read from video directory"});
        }

        const videoFiles = files.filter(file => file.endsWith(".mp4"));
        res.status(200).json(videoFiles);
    });
};