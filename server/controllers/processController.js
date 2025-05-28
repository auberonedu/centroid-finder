import path from 'path'; // built in module to manipulate file paths
import fs from 'fs'; // built in module for working with file system
import dotenv from 'dotenv'; // module for accessing data in .env files
import { spawn } from 'child_process'; // built in Node.js module to run other programs/commands
import { v4 as uuidv4 } from 'uuid'; // generates universally unique ids
import { fileURLToPath } from 'url'; // for resolving ES module paths

// Get __dirname from ES modules
const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);

// Load environment variables from the .env file in the project root
dotenv.config({ path: path.resolve(__dirname, '../../.env') });

// env variables for location of videos, java jar, and output
const VIDEO_DIR = process.env.VIDEO_DIR;
const JAR_PATH = process.env.JAR_PATH;
const OUTPUT_DIR = process.env.OUTPUT_DIR || './jobs';

const startVideoProcessingJob = (req, res) => {
    // /process/:filename
    const { filename } = req.params;
    // ?targetColor=<hex>&threshold=<int>
    const { targetColor, threshold } = req.query;

    if (!targetColor || !threshold) {
        return res.status(400).json({ error: "Missing targetColor or threshold query parameter" });
    }

    const jobId = uuidv4();
    const jobDir = path.join(OUTPUT_DIR, jobId);
    fs.mkdirSync(jobDir, { recursive: true });

    const inputPath = path.join(VIDEO_DIR, filename);
    const outputCSV = path.join(jobDir, 'result.csv');


    // build java command line arguement
    const args = [
        '-jar',
        JAR_PATH,
        inputPath,
        outputCSV,
        targetColor,
        threshold
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

        return res.status(500).json({ error: "Error starting job" });
    }
};

const getProcessingJobStatus = (req, res) => {
    // gets jobId from params
    const { jobId } = req.params;
    // makes path to output
    const fileOutputPath = path.join(OUTPUT_DIR, jobId, 'result.csv');

    // access checks if file exists and can be read, constants.F_OK checks if it exists
    fs.access(fileOutputPath, fs.constants.F_OK, (err) => {
        // if it doesn't exist there is error so it is still being processed
        if (err) {
            return res.status(200).json({ status: 'processing' });
        } else {
            return res.status(200).json({ status: 'done' });
        }
    });
};

export default { startVideoProcessingJob, getProcessingJobStatus };