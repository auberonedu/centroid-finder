import express from 'express';
import fs from 'fs';
import dotenv from 'dotenv';

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
            console.log(videoDir);
            return res.status(500).json({ error: 'Error reading video directory' });
        }
        // Filter for video files only (e.g., .mp4, .mov, .avi)
        const videoFiles = files.filter(f => /\.(mp4|mov|avi|mkv|webm)$/i.test(f));
        res.json(videoFiles);
    });
});

export default router;