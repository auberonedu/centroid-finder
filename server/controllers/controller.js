import { readFileSync, readdirSync } from 'fs';
import dotenv from 'dotenv';

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
    // TODO: use queries and filename to create Java jar, return job id
    // Tyler
    // get query info
    // call processor with data
    // figure out how to run this ... separately? child_process to run in the background, use detached mode ... (see hint)
    const { filename } = req.params;
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
