import express from 'express';
import fs from 'fs';
import dotenv from 'dotenv';
import path from 'path';
import { spawn } from 'child_process';
import ffmpegPath from 'ffmpeg-static'; // Using ffmpeg-static for local binary

dotenv.config({ path: '../.env' });
const router = express.Router();

router.get("/home", (req, res) => {
    res.send('Salamander API server is running!');
});

router.get("/api/videos", (req, res) => {
    const videoDir = process.env.VIDEO_DIR;
    if (!videoDir) {
        return res.status(500).json({ error: 'VIDEO_DIR not set in environment' });
    }
    fs.readdir(videoDir, (err, files) => {
        if (err) {
            console.error('Error reading video directory:', err);
            return res.status(500).json({ error: 'Error reading video directory' });
        }
        // Filter for video files only (e.g., .mp4, .mov, .avi)
        const videoFiles = files.filter(f => /\.(mp4|mov|avi|mkv|webm)$/i.test(f));
        res.json(videoFiles);
    });
});

router.get("/thumbnail/:filename", (req, res) => {
    const videoDir = process.env.VIDEO_DIR;
    const { filename } = req.params;

    if (!filename) {
        return res.status(400).json({ error: "Filename is required" });
    }

    const videoPath = path.join(videoDir, filename);

    // Check if the file exists
    if (!fs.existsSync(videoPath)) {
        return res.status(404).json({ error: "Video not found" });
    }

    try {
        // Set Content-Type to JPEG
        res.setHeader('Content-Type', 'image/jpeg');

        // Run ffmpeg to extract the first frame as a JPEG using fluent-ffmpeg
        const ffmpeg = spawn(ffmpegPath, [
            '-i', videoPath,        // input file
            '-frames:v', '1',       // only 1 frame
            '-f', 'image2',         // force image format
            '-q:v', '2',            // quality
            'pipe:1'                // output to stdout
        ]);

        // Pipe ffmpeg output to response
        ffmpeg.stdout.pipe(res);

        // Capture any stderr (error) output from ffmpeg
        ffmpeg.stderr.on('data', (data) => {
            console.error('ffmpeg stderr:', data.toString());
        });

        // Handle errors when spawning the ffmpeg process
        ffmpeg.on('error', (err) => {
            console.error("Failed to start ffmpeg:", err);
            res.status(500).json({ error: "Error generating thumbnail" });
        });

        // Handle ffmpeg process exit status
        ffmpeg.on('close', (code) => {
            if (code !== 0) {
                console.error(`ffmpeg exited with code ${code}`);
                if (!res.headersSent) {
                    res.status(500).json({ error: "Error generating thumbnail" });
                }
            }
        });

    } catch (err) {
        console.error("Thumbnail generation failed:", err);
        res.status(500).json({ error: "Error generating thumbnail" });
    }
});

export default router;