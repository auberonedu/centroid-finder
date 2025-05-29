import fs from 'fs/promises';
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

// Go up one directory from /server/Controller to /project/videos

const videosDir = path.resolve(__dirname, process.env.VIDEO_DIR);

console.log("Resolved videosDir:", videosDir);


const getVideoDuration = (filePath) => {
    return new Promise((resolve, reject) => {
        ffmpeg.ffprobe(filePath, (err, metadata) => {
            if (err) {
                console.error('ffprobe error:', err.message);
                return resolve(null); // Don't crash the app — just skip metadata
            }

            try {
                const seconds = metadata?.format?.duration;
                resolve(seconds ?? null);
            } catch (parseErr) {
                console.error('Error parsing metadata:', parseErr.message);
                resolve(null);
            }
        });
    });
};

    const getVideos = async (req, res) => {
        try {
            const files = await fs.readdir(videosDir)
            const videoFiles = files.filter(file =>
                ['.mp4', '.mov', '.avi'].includes(path.extname(file).toLowerCase())
            );

            const metadataList = await Promise.all(videoFiles.map(async (file) => {
            const fullPath = path.join(videosDir, file);

            // Get file stats (created/modified date)
            const stats = await fs.stat(fullPath);

                // Get video metadata using fluent-ffmpeg
            const duration = await getVideoDuration(fullPath)



            return {
                name: file,
                duration, // in seconds
                createdAt: stats.birthtime,     // when file was created
                modifiedAt: stats.mtime         // when file was last modified
            };
        }));
            res.json({ videos: metadataList  });
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