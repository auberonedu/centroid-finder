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
    // TODO: Write a try catch that returns list of video names or status 500
    console.log("getVideos successfully called!")

    try {

    // How to display files in a directory: https://www.geeksforgeeks.org/how-to-display-all-files-in-a-directory-using-node-js/
    const directory_name = process.env.videoDirPath;

    // Function to get current filenames
    // in directory
    let filenames = readdirSync(directory_name);

    console.log("\nFilenames in directory:");
    // This currently just display names in console
    // TODO: Convert this into a json object
    filenames.forEach((file) => {
        console.log("File:", file);
    });

    res.status(statusOK);

    } catch {
        res.status(statusServerError);
    }
};

const getThumbnail = (req, res) => {
    // TODO: Return thumbnail
    const { filename } = req.params;
    console.log("getThumbnail successfully called!")
};

const postVideo = (req, res) => {
    // TODO: use queries and filename to create Java jar, return job id
    const { filename } = req.params;
    console.log("postVideo successfully called!")
};

const getStatus = (req, res) => {
    // TODO: return status
    const { jobId } = req.params;
    console.log("getStatus successfully called!")
};


export default { getVideos, getThumbnail, postVideo, getStatus };
