import { readFileSync, readdirSync, existsSync, writeFileSync } from 'fs';
import { spawn } from 'child_process'; // start a new background process
import dotenv from 'dotenv';
import path from 'path'; // join file paths
import { v4 as uuidv4 } from 'uuid'; // Generate a unique job ID
import ffmpeg from 'fluent-ffmpeg';

// read in env congif environment variables
dotenv.config({
    path: "./.env"
})

const statusOK = 200;
const statusAccepted = 202;
const statusBadRequest = 400;
const statusNotFound = 404;
const statusServerError = 500;

const jobIDArray = [];
// For testing purposes, include one fake job ID
jobIDArray.push("123")

const getVideos = (req, res) => {
    console.log("getVideos successfully called!")

    try {
        const directory_name = process.env.video_directory_path; // stored in the config.env

        //  get current filenames in directory
        let filenames = readdirSync(directory_name);

        // Create empty videos array
        let videos = []
        // Add filenames to videos array
        filenames.forEach((file) => {
            // console.log("File:", file);
            if (file.endsWith(".mp4")) {
                videos.push({ video: file });
                // console.log("Videos:", videos);
            }
        });
        // console.log("All videos:", videos)

        res.json([videos])
        res.status(statusOK);

    } catch {
        res.status(statusServerError);
        // TODO: Add error json
    }
};

const getThumbnail = (req, res) => {
    // TODO: Return thumbnail
    // Augy
    // ffmpeg package

    console.log("getThumbnail successfully called!")

    // get the video frame
    const { filename } = req.params;
    const outputImagePath = '../output/frame1.jpg'; // could be wrong

    ffmpeg(filename).on('end', function(){
        console.log('First frame has been extracted successfull!');
    })
    .on('error', function(err) {
        console.error('Error: ' + err.message);
    })
    .screenshots({
        count: 1,
        folder: 'output', // could be wrong
        filename: 'frame1.jpg',
        timemarks: ['00:00:00.000']
    })
    
        
    
}
        

const postVideo = (req, res) => {
    // console.log("postVideo successfully called!")

    // Try catch to check that parameters are correct
    try {
        const { filename } = req.params; // Extracts the video filename from the URL
        const { targetColor, threshold } = req.query; // Extracts query parameters from the request URL
    } catch {
        res.status(statusBadRequest).json({ "error": "Missing targetColor or threshold query parameter." })
    }
    

    // Try catch to check that can job can start
    try {
        const jobId = uuidv4(); // Unique job ID for tracking the processing
        jobIDArray.push(jobID);

        // Create a started marker (AI helped write this)
        const startMarker = path.join(process.env.output_directory_path, `${jobId}.started`);
        writeFileSync(startMarker, '');

        const jarPath = process.env.video_processor_jar_path; // Path to the JAR file
        const inputPath = path.join(process.env.video_directory_path, filename); // The full path to the input video file
        const outputPath = path.join(process.env.output_directory_path, `${jobId}.csv`); // Path to where the DSV output will be saved

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
        const checkInterval = 3000; // Check every 3 seconds
        const maxChecks = 100; // Optional: stop after 100 checks (~5 minutes)
        let checks = 0;

        const interval = setInterval(() => {
            checks++;
            if (fs.existsSync(outputPath)) {
                // Job is complete
                fs.unlink(startMarker, err => {
                    if (err) console.error(`Error deleting start marker: ${err}`);
                });
                clearInterval(interval);
            } else if (checks >= maxChecks) {
                // Optional: stop polling after some time
                clearInterval(interval);
            }
        }, checkInterval);

        // Response to the client with the job id
        res.status(statusAccepted).json({ "jobId": jobId });

    } catch {
        res.status(statusServerError).json({ "error": "Error starting job" })
    }
    
};

const getStatus = (req, res) => {
    // console.log("getStatus successfully called!")
    // Rebecca
    const { jobId } = req.params;
    if (!jobIDArray.includes(jobId)) {
        res.status(statusNotFound).json({"error":"Job ID not found"})
    }

    const startMarker = path.join(process.env.output_directory_path, `${jobId}.started`);
    const outputPath = path.join(process.env.output_directory_path, `${jobId}.csv`);


    if (existsSync(outputPath)) {
        res.status(statusOK).json({"status":"done", "result": outputPath})
    }
    if (existsSync(startMarker)) {
        res.status(statusOK).json({"status":"processing"})
    }

    res.status(statusServerError).json({ "error":"Error fetching job status"})

    // 200 OK error: error processing

    // 500 error fetching job status
};

// Meeting: Wed 4:15pm


export default { getVideos, getThumbnail, postVideo, getStatus };
