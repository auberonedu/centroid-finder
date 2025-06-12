import { readFileSync, readdirSync, existsSync, writeFileSync, unlink, mkdirSync } from 'fs';
import { spawn } from 'child_process'; // start a new background process
// import dotenv from 'dotenv';
// import { fileURLToPath } from 'url';
import path from 'path'; // join file paths
import { v4 as uuidv4 } from 'uuid'; // Generate a unique job ID
import ffmpeg from 'fluent-ffmpeg';

// If running in a local test environment, set up local env
// const __filename = fileURLToPath(import.meta.url);
// const __dirname = path.dirname(__filename);

// Use absolute path to .env
// dotenv.config({ path: path.resolve(__dirname, '../.env') });

// 
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
        //const OUTPUT_CSV = path.resolve(process.env.RESULTS_DIR, `${jobId}.csv`); // Path to where the DSV output will be saved

        // DEV CHECK TO SEE IF jarPath EXISTS
        if (existsSync(path.resolve(JAVA_JAR_PATH))) {
        console.log('File exists!');
        } else {
        console.log('File does not exist!');
        }
        
        // Arguments to the pass to the backend
        const javaArgs = [
            '-jar',
            JAVA_JAR_PATH,
            VIDEO_DIR,
            process.env.RESULTS_DIR,
            targetColor,
            threshold
        ];

        console.log(javaArgs)

        console.log("Spawning Java process")

        // Spawns the Java process in detached mode
        const javaSpawn = spawn('java', javaArgs, {
            // configure pipes between parent and child
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

        // on close, get code to determine success
        javaSpawn.on("close", (code) => {
            console.log("Process closed with code:", code);
            jobStatus.set(jobId, { status: code === 0 ? "done" : "error" });
        });

        // error
        javaSpawn.on("error", (err) => {
            jobStatus.set(jobId, { status: "error", error: err })
        })

        //javaSpawn.unref(); // this allows the parent Node to exit independently of the javaSpawn



        // Check to see if job is done
        // const checkInterval = 3000; // Check every 3 seconds
        // const maxChecks = 200; // Optional: stop after 200 checks (~10 minutes)
        // let checks = 0;

        // const interval = setInterval(() => {
        //     checks++;
        //     if (existsSync(OUTPUT_CSV)) {
        //         // Job is complete
        //         jobStatus.set(jobId, {status: "done", result: OUTPUT_CSV})
        //         clearInterval(interval);
        //     } else if (checks >= maxChecks) {
        //         jobStatus.set(jobId, {status: "error", error: "Job timed out"})
        //         clearInterval(interval);
        //     }
        // }, checkInterval);

        // Response to the client with the job id
        res.status(statusAccepted).json({ jobId });
    } catch (err) {
        console.log("postVideo error: ", err.message);
        res.status(statusServerError).json({ "error": "Error starting job" })
    }
    
};

// GET /process/:jobId/status
const getStatus = (req, res) => {
    const { jobId } = req.params;

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