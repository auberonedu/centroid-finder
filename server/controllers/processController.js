import path from 'path'; // built in module to manipulate file paths
import fs from 'fs'; // built in module for working with file system
import dotenv from 'dotenv';
import { spawn } from 'child_process'; // built in Node.js module to run other programs/commands
import { v4 as uuidv4 } from 'uuid'; // generates universally unique ids

dotenv.config(); // load env variables

// env variables for location of videos, java jar, and output
const VIDEO_DIR = process.env.VIDEO_DIR;
const JAR_PATH = process.env.JAR_PATH;
const OUTPUT_DIR = process.env.OUTPUT_DIR || './jobs';

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
    // generate unique ID
    const jobId = uuidv4();

    // build java command line arguement
    const args = [
        '-jar',
        JAR_PATH,
        inputPath,
        targetColor,
        threshold,
        outputPath
    ];

    // try catch block to run the jar command
    try {
        const child = spawn('java', args, {
            detached: true, // run independent (node wont wait for it to finish)
            stdio: 'ignore' // runs detached (not in node event loop)
        });

        child.unref(); // lets java run in background without program waiting on it

        return res.status(202).json({ jobId });
    } catch (err) {
        console.error("Failed to start processing job: ", err);
        
        return res.status(500).json({ error: "Error starting job"});
    }
};

const getProcessingJobStatus = (req, res) => {

};

export default { startVideoProcessingJob, getProcessingJobStatus };