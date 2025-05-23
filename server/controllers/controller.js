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
    console.log("getVideos successfully called!")

    // How to display files in a directory: https://www.geeksforgeeks.org/how-to-display-all-files-in-a-directory-using-node-js/
    const directory_name = process.env.videoDirPath;

    // Function to get current filenames
    // in directory
    let filenames = readdirSync(directory_name);

    console.log("\nFilenames in directory:");
    filenames.forEach((file) => {
        console.log("File:", file);
    });

    res.status(statusOK);
    // if okay, return res.status(statusOK) and object list of files
    // if not okay, return res

};


export default { getVideos };
