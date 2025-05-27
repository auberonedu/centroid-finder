import path from 'path';
import fs from 'fs';
import dotenv from 'dotenv';

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
};

const getProcessingJobStatus = (req, res) => {

};

export default { startVideoProcessingJob, getProcessingJobStatus };