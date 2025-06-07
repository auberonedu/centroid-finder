import { exec } from "child_process";
import path from "path";
import fs from "fs";

/**
 * Generate thumbnail for the given video file.
 * @param {string} videoPath - Absolute path to the video file
 * @param {string} outputDir - Absolute path to the thumbnail output folder
 * @returns {Promise<string>} - Resolves to the thumbnail filename
 */
export function generateThumbnail(videoPath, outputDir) {
  return new Promise((resolve, reject) => {
    const filename = path.basename(videoPath); // e.g., ensantina.mp4
    const thumbnailPath = path.join(outputDir, `${filename}.jpg`);

    // If it already exists, don't regenerate
    if (fs.existsSync(thumbnailPath)) {
      console.log(`🖼️ Thumbnail already exists for ${filename}`);
      return resolve(`${filename}.jpg`);
    }

    const command = `ffmpeg -y -i "${videoPath}" -frames:v 1 "${thumbnailPath}"`;

    exec(command, (error, stdout, stderr) => {
      if (error) {
        console.error(`❌ FFmpeg thumbnail error for ${filename}:`, stderr);
        return reject(error);
      }
      console.log(`✅ Generated thumbnail for ${filename}`);
      resolve(`${filename}.jpg`);
    });
  });
}
