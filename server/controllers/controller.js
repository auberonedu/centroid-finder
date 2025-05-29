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

const jobIDArray = [];

// For testing purposes, include one fake job ID
jobIDArray.push("123")

// GET /api/videos
const getVideos = (req, res) => {
    // console.log("getVideos successfully called!")
    try {
        const videoDir = path.resolve(process.cwd(), process.env.video_directory_path);
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
    const outputImagePath = path.join(outputFolder, `${filename}-thumbnail.jpg`);
  
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
    // console.log("postVideo successfully called!")

    // Try catch to check that parameters are correct
    try {
        const { filename } = req.params; // Extracts the video filename from the URL
        const { targetColor, threshold } = req.query; // Extracts query parameters from the request URL
        
        if (!targetColor || !threshold) {
            return res.status(statusBadRequest).json({ error: "Missing targetColor or threshold query parameter" });
        }
    
        const jobId = uuidv4(); // Unique job ID for tracking the processing
        jobIDArray.push(jobId);

        // Create a started marker (AI helped write this)
        const startMarker = path.resolve(process.env.output_directory_path, `${jobId}.started`);
        writeFileSync(startMarker, '');
        
        const jarPath = path.resolve(process.env.video_processor_jar_path); // Path to the JAR file
        const inputPath = path.resolve(process.env.video_directory_path, filename); // The full path to the input video file
        const outputPath = path.resolve(process.env.output_directory_path, `${jobId}.csv`); // Path to where the DSV output will be saved

        mkdirSync(path.dirname(startMarker), {recursive: true });
        
        // Arguments to the pass to the backend
        const javaArgs = [
            '-jar',
            jarPath,
            inputPath,
            outputPath,
            targetColor,
            threshold
        ];

        // Spawns the Java process in detached mode
        const javaSpawn = spawn('java', javaArgs, {
            detached: true, // this ensures that the process is independent from the Node.js process
            stdio: 'ignore' // this ignores stdio, which there is no console output to Node
        });

        javaSpawn.unref(); // this allows the parent Node to exit independently of the javaSpawn

        // Cleanup start marker (AI helped write this)
        if (process.env.NODE_ENV !== 'test'){
            const checkInterval = 3000; // Check every 3 seconds
            const maxChecks = 100; // Optional: stop after 100 checks (~5 minutes)
            let checks = 0;

            const interval = setInterval(() => {
                checks++;
                if (existsSync(outputPath)) {
                    // Job is complete
                    unlink(startMarker, (err) => {
                        if (err) console.error(`Error deleting start marker: ${err}`);
                    });
                    clearInterval(interval);
                } else if (checks >= maxChecks) {
                    // Optional: stop polling after some time
                    clearInterval(interval);
                }
            }, checkInterval);
        }

        // Response to the client with the job id
        res.status(statusAccepted).json({ jobId });
    } catch (err) {
        console.log("postVideo error: ", err.message);
        res.status(statusServerError).json({ "error": "Error starting job" })
    }
    
};

const getStatus = (req, res) => {
    // console.log("getStatus successfully called!")
    const { jobId } = req.params;

    if (!jobIDArray.includes(jobId)) {
        res.status(statusNotFound).json({"error":"Job ID not found"})
    }

    const outputPath = path.resolve(process.env.output_directory_path, `${jobId}.csv`);
    const startMarker = path.resolve(process.env.output_directory_path, `${jobId}.started`);


    if (existsSync(outputPath)) {
        res.status(statusOK).json({"status":"done", "result": outputPath})
    }

    if (existsSync(startMarker)) {
        res.status(statusOK).json({"status":"processing"})
    }

    return res.status(statusServerError).json({ "error":"Error fetching job status"})
};

// TODO: Create a getCSV function that retrieves the coordinates

export default { getVideos, getThumbnail, postVideo, getStatus };

// ** NOTE: When you run 'npm test' in the command line there will be a random generated file in the sampleOutput folder. ...
// ** ...We're going to need to write a getCSV function to see those coordinates.
