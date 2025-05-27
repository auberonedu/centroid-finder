import { readFileSync, readdirSync } from 'fs';
import { spawn } from 'child_process'; // start a new background process
import dotenv from 'dotenv';
import path from 'path'; // join file paths
import { v4 as uuidv4 } from 'uuid'; // Generate a unique job ID

// read in env congif environment variables
dotenv.config({
    path: "./.env"
})

const statusOK = 200;
const statusUserError = 404;
const statusServerError = 500;

const getVideos = (req, res) => {
    // console.log("getVideos successfully called!")

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
    const { filename } = req.params;
    console.log("getThumbnail successfully called!")
};

const postVideo = (req, res) => {
    const { filename } = req.params; // Extracts the video filename from the URL
    const { targetColor, threshold } = req.query; // Extracts query parameters from the request URL

    const jobId = uuidv4(); // Unique job ID for tracking the processing

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

    // Response to the client with the job id
    res.status(statusOK).json({ jobId });
    console.log("postVideo successfully called!")
};

const getStatus = (req, res) => {
    // TODO: return status
    // Rebecca
    // figure out how... (see hint)
    const { jobId } = req.params;
    console.log("getStatus successfully called!")
};

// Meeting: Wed 4:15pm


export default { getVideos, getThumbnail, postVideo, getStatus };
