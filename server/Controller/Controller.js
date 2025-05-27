import fs from 'fs';
import path from 'path';

import { fileURLToPath } from 'url';
import dotenv from 'dotenv';

import ffmpeg from 'fluent-ffmpeg';
import ffmpegInstaller from '@ffmpeg-installer/ffmpeg';

ffmpeg.setFfmpegPath(ffmpegInstaller.path);

// Required in ES modules to get __dirname
const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);
dotenv.config({ path: path.resolve(__dirname, '../.env') })

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
// ffmpeg(videoPath)
//   .on('end', () => {
//     res.sendFile(outputImagePath);
//   })
//   .on('error', (err) => {
//     console.error('Error extracting frame:', err);
//     res.status(500).json({ error: 'Failed to extract frame' });
//   })
//   .screenshots({
//     timestamps: ['00:00:01.000'],       // 1 second in
//     filename: 'frame.jpg',
//     folder: path.join(__dirname, '../frames')

//   });
// } 

// const startVideoProcessing = async(req,res) => {
// this will call the jar from maven 
// }

// const getJobStatus = async(req, res) => {

// }

export default {
    getVideos
    // getPreview,
    // startVideoProcessing,
    // getJobStatus
};