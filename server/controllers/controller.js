import { readFileSync, readdirSync, existsSync, writeFileSync, unlink, mkdirSync } from 'fs';
import { spawn } from 'child_process'; // start a new background process
import dotenv from 'dotenv';
import path from 'path'; // join file paths
import { fileURLToPath } from 'url';
import { v4 as uuidv4 } from 'uuid'; // Generate a unique job ID
import ffmpeg from 'fluent-ffmpeg';

// Get __dirname equivalent in ES module scope
const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);

// Use absolute path to .env
dotenv.config({ path: path.resolve(__dirname, '../.env') });

const statusOK = 200;
const statusAccepted = 202;
const statusBadRequest = 400;
const statusNotFound = 404;
const statusServerError = 500;

// Create a job status map that contains fake job ID 123 with a status of "done"
const jobStatus = new Map([
    ["123", {status: "done"}]
]);

const getVideos = (req, res) => {
    // console.log("getVideos successfully called!")
    try {
        const videoDir = process.env.video_directory_path;
        const filenames = readdirSync(videoDir);

        const videos = filenames
            .filter((file) => file.endsWith('.mp4'))
            .map((file) => ({ video: file}));

        res.status(statusOK).json(videos)
    } catch (err) {
        console.log("getVideos error: ", err.message);
        res.status(statusServerError).json({"error": "Error reading video directory"});
    }
};

const getThumbnail = (req, res) => {
    console.log("getThumbnail successfully called!")
    const { filename } = req.params; // get the video frame
    
    const inputPath = path.resolve(process.env.video_directory_path, filename);
    const outputFolder = path.resolve('./output');
    const outputImagePath = path.join(outputFolder, `${filename}-thumb.jpg`);
  
    ffmpeg(inputPath)
        .on('end', () => {
            console.log('Thumbnail created at:', outputImagePath);
            res.sendFile(outputImagePath); // Send back the image
        })
        .on('error', (err) => {
            console.error('FFmpeg error:', err.message);
            res.status(statusServerError).json({ error: 'Failed to generate thumbnail' });
        })
        .screenshots({
            count: 1,
            folder: outputFolder,
            filename: `${filename}-thumb.jpg`,
            timemarks: ['00:00:00.000'],
    }); 
}
        

const postVideo = (req, res) => {
    // Try catch to check that parameters are valid
    try {
        const { filename } = req.params; // Extracts the video filename from the URL
        const { targetColor, threshold } = req.query; // Extracts query parameters from the request URL
        
        if (!targetColor || !threshold) {
            return res.status(statusBadRequest).json({ error: "Missing targetColor or threshold query parameter" });
        }
    
        const jobId = uuidv4(); // Unique job ID for tracking the processing
        // jobIDArray.push(jobId);
        // Add job ID to map with job status of "started"
        jobStatus.set(jobId, { status: "started" })

        // Create a started marker (AI helped write this)
        // const startMarker = path.resolve(process.env.output_directory_path, `${jobId}.started`);
        // writeFileSync(startMarker, '');
        
        const jarPath = path.resolve(process.env.video_processor_jar_path); // Path to the JAR file
        const inputPath = path.resolve(process.env.video_directory_path, filename); // The full path to the input video file
        const outputPath = path.resolve(process.env.output_directory_path, `${jobId}.csv`); // Path to where the DSV output will be saved

        // TODO: Check whether recursive should be set to true
        // Consider a try catch to see if directory exist?
        // Only run this line if file directory doesn't exist 
        // mkdirSync(path.dirname(startMarker), {recursive: true });
        
        // Arguments to the pass to the backend
        const javaArgs = [
            '-jar',
            jarPath,
            inputPath,
            outputPath,
            targetColor,
            threshold
        ];

        // console.log("Running Java with:")
        // console.log("java", javaArgs.join(" "));

        // Spawns the Java process in detached mode
        const javaSpawn = spawn('java', javaArgs, {
            detached: true, // this ensures that the process is independent from the Node.js process
            stdio: 'inherit' // changed from 'inherit' to 'ignore'
        });

        javaSpawn.unref(); // this allows the parent Node to exit independently of the javaSpawn

        jobStatus.set(jobId, { status: "processing" })

        // Check to see if job is done
        const checkInterval = 3000; // Check every 3 seconds
        const maxChecks = 200; // Optional: stop after 200 checks (~10 minutes)
        let checks = 0;

        const interval = setInterval(() => {
            checks++;
            if (existsSync(outputPath)) {
                // Job is complete
                jobStatus.set(jobId, {status: "done", result: outputPath})
                clearInterval(interval);
            } else if (checks >= maxChecks) {
                jobStatus.set(jobId, {status: "error", error: "Job timed out"})
                clearInterval(interval);
            }
        }, checkInterval);

        // Response to the client with the job id
        res.status(statusAccepted).json({ jobId });
    } catch (err) {
        console.log("postVideo error: ", err.message);
        res.status(statusServerError).json({ "error": "Error starting job" })
    }
    
};

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

// TODO: Create a getCSV function that retrieves the coordinates

export default { getVideos, getThumbnail, postVideo, getStatus };

// ** NOTE: When you run 'npm test' in the command line there will be a random generated file in the sampleOutput folder. ...
// ** ...We're going to need to write a getCSV function to see those coordinates.
