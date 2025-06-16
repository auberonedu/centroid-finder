import { readdirSync } from 'fs';
import { spawn } from 'child_process'; // start a new background process
import path from 'path'; // join file paths
import { v4 as uuidv4 } from 'uuid'; // Generate a unique job ID
import ffmpeg from 'fluent-ffmpeg';
// import dotenv from 'dotenv';
// import { fileURLToPath } from 'url';

// If running in a local test environment, set up local env
// const __filename = fileURLToPath(import.meta.url);
// const __dirname = path.dirname(__filename);

// Use absolute path to .env
// dotenv.config({ path: path.resolve(__dirname, '../.env') });

// Define status codes
const statusOK = 200;
const statusAccepted = 202;
const statusBadRequest = 400;
const statusNotFound = 404;
const statusServerError = 500;

const jobIDArray = [];

// For testing purposes, include one fake job ID
jobIDArray.push("123")

// Create a job status map that contains fake job ID 123 with a status of "done"
const jobStatus = new Map([
    ["123", "done"]
]);

// GET /api/videos
const getVideos = (req, res) => {
    try {
        // get filenames object from video directory
        const filenames = readdirSync(process.env.VIDEO_DIR);

        // Create a videos object containing each mp4 video
        const videos = filenames
            .filter((file) => file.endsWith('.mp4'))
            .map((file) => ({ video: file}));

        // return all videos
        res.status(statusOK).json(videos)
    } catch (err) {
        console.log("getVideos error: ", err.message);
        res.status(statusServerError).json({"error": "Error reading video directory"});
    }
};

// GET /thumbnail/:filename
const getThumbnail = (req, res) => {
    // get filename from path parameter
    const { filename } = req.params; 
    
    // save input file, output directory, and output image paths
    const inputPath = path.resolve(process.env.VIDEO_DIR, filename);
    const outputFolder = path.resolve('./output');
    const outputImagePath = path.join(outputFolder, `${filename}-thumb.jpg`);

    // AI helped write this
    ffmpeg(inputPath)
        // this runs when ffmpeg processes input file successfully
        .on('end', () => {
            console.log('Thumbnail created at:', outputImagePath);
            res.sendFile(outputImagePath); // Send back the image
        })
        // this runs if there is an error
        .on('error', (err) => {
            console.error('FFmpeg error:', err.message);
            res.status(statusServerError).json({ error: 'Failed to generate thumbnail' });
        })
        // these are the instructions to grab a screenshot from the video
        .screenshots({
            count: 1,
            folder: outputFolder,
            filename: `${filename}-thumb.jpg`,
            timemarks: ['00:00:00.000'], // take from start of video
    }); 
}
        
// POST /process/:filename
const postVideo = (req, res) => {
    // get filename from path parameter and targetColor and threshold from query parameters
    const { filename } = req.params; 
    const { targetColor, threshold } = req.query; 
    
    // validate that targetColor and threshhold exist (further validation happens in processor)
    if (!targetColor || !threshold) {
        return res.status(statusBadRequest).json({ error: "Missing targetColor or threshold query parameter" });
    }

    const jobId = uuidv4(); // Unique job ID for tracking the processing
    // Add job ID to map with job status of "started"
    jobStatus.set(jobId, { status: "started" })

    try {
        const JAVA_JAR_PATH = path.resolve(process.env.JAVA_JAR_PATH); // Path to the JAR file 
        const VIDEO_DIR = path.resolve(process.env.VIDEO_DIR, filename); // The full path to the input video file
        
        // Arguments to the pass to the backend
        const javaArgs = [
            '-jar',
            JAVA_JAR_PATH,
            VIDEO_DIR,
            process.env.RESULTS_DIR,
            targetColor,
            threshold
        ];

        // Spawns the Java process in detached mode
        const javaSpawn = spawn('java', javaArgs, {
            // configure pipes between parent and child
            // corresponds to stdin, stdout, sterr 
            stdio: ["ignore", "pipe", "pipe"]
        });

        jobStatus.set(jobId, { status: "processing" })

        // check for error output
        javaSpawn.stderr.on("data", (data) => {
            console.error("Java stderr:", data.toString());
        });

        // print data
        javaSpawn.stdout.on("data", (data) => {
            console.log("Java stdout:", data.toString());
        });

        // on close, set job status to done if exit code '0', or error if other exit code
        javaSpawn.on("close", (code) => {
            console.log("Process closed with code:", code);
            jobStatus.set(jobId, { status: code === 0 ? "done" : "error" });
        });

        // on error, set job status to error
        javaSpawn.on("error", (err) => {
            jobStatus.set(jobId, { status: "error", error: err })
        })

        // Response to the client with the job id
        res.status(statusAccepted).json({ jobId });
    } catch (err) {
        console.log("postVideo error: ", err.message);
        res.status(statusServerError).json({ "error": "Error starting job" })
    }
};

// GET /process/:jobId/status
const getStatus = (req, res) => {
    // get job ID from path parameter
    const { jobId } = req.params;

    // Check if job ID is in job status map
    if (!jobStatus.has(jobId)) {
        return res.status(statusNotFound).json({"error":"Job ID not found"})
    } else {
        try {
            return res.status(statusOK).json(jobStatus.get(jobId))
        } catch {
            return res.status(statusServerError).json({ "error":"Error fetching job status"})
        }
    }
};

export default { getVideos, getThumbnail, postVideo, getStatus };