import path from 'path';
import fs from 'fs';
import dotenv from 'dotenv';
import { spawn } from 'child_process';
import { v4 as uuidv4 } from 'uuid';

dotenv.config();

const VIDEO_DIR = process.env.VIDEO_DIR;
const JAR_PATH = process.env.JAR_PATH;
const OUTPUT_DIR = process.env.OUTPUT_DIR;

const startVideoProcessingJob = (req, res) => {
    // /process/:filename
    const { fileName } = req.params;
    // ?targetColor=<hex>&threshold=<int>
    const { targetColor, threshold } = req.query;

    if (!targetColor || !threshold) {
        return res.status(400).json({ error: "Missing targetColor or threshold query parameter"});
    }

    const inputPath = path.join(VIDEO_DIR, fileName);
    const outputPath = path.join(OUTPUT_DIR);
    const jobId = uuidv4();

    const args = [
        '-jar',
        JAR_PATH,
        inputPath,
        targetColor,
        threshold,
        outputPath
    ];

    try {
        const child = spawn('java', args, {
            detached: true,
            stdio: 'ignore'
        });

        child.unref();
        return res.status(202).json({ jobId });
    } catch (err) {
        console.error("Failed to start processing job: ", err);
        return res.status(500).json({ error: "Error starting job"});
    }
};

const getProcessingJobStatus = (req, res) => {

};

export default { startVideoProcessingJob, getProcessingJobStatus };