import fs from 'fs';
import path from 'path';
import { fileURLToPath } from 'url';
import dotenv from 'dotenv';

// Required in ES modules to get __dirname
const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);
const rawVideoPath = process.env.VIDEO_DIR 
// Go up one directory from /server/Controller to /project/videos

if (!rawVideoPath) {
  throw new Error('❌ VIDEO_DIR is not set in .env or was not loaded properly');
}

const videosDir = path.resolve(__dirname, rawVideoPath);

const getVideos = async (req, res) => {
    try {
        const files = await fs.readdir(videosDir);
        const videoFiles = files.filter(file =>
            ['.mp4', '.mov', '.avi'].includes(path.extname(file).toLowerCase())
        );
        res.json({ videos: videoFiles });
    } catch (err) {
        console.error('Error accessing video folder:', err);
        res.status(500).json({ error: 'Could not read videos directory' });
    }
};

// const getPreview = async(req,res) => {

// } 

// const startVideoProcessing = async(req,res) => {

// }

// const getJobStatus = async(req, res) => {

// }

export default {
    getVideos
    // getPreview,
    // startVideoProcessing,
    // getJobStatus
};