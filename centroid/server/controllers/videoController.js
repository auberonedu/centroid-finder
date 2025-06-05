const path = require("path");
require("dotenv").config({ path: path.resolve(__dirname, "../../.env") });

const fs = require("fs");
const { exec } = require("child_process");

// resolve environment variables
const VIDEO_DIR = path.resolve(__dirname, '../../../', process.env.VIDEO_DIR);
const OUTPUT_DIR = path.resolve(__dirname, "../../", process.env.RESULTS_DIR);

console.log("Resolved VIDEO_DIR:", VIDEO_DIR);

// function lists all video files in the video directory
exports.listVideos = async (req, res) => {
  try {
    const files = await fs.promises.readdir(VIDEO_DIR);
    const videoFiles = files.filter(file => file.endsWith(".mp4") || file.endsWith(".mov"));
    res.status(200).json(videoFiles);
  } catch (error) {
    res.status(500).json({ error: "Error reading video directory" });
  }
};

// function generates a thumbnail (first frame) for a video file
exports.generateThumbnail = (req, res) => {
  const filename = req.params.filename;
  const videoPath = path.join(VIDEO_DIR, filename);
  const outputPath = path.join(OUTPUT_DIR, `${filename}.jpg`);

  // makes a output directory if it doesn't exist
  if (!fs.existsSync(OUTPUT_DIR)) {
    fs.mkdirSync(OUTPUT_DIR, { recursive: true });
  }

  // ffmpeg command to extract the first frame as a thumbnail
  const command = `ffmpeg -y -i "${videoPath}" -vf "select=eq(n\\,0)" -q:v 3 "${outputPath}"`;

  // execute the command
  exec(command, (err) => {
    if (err) {
      return res.status(500).json({ error: "Error generating thumbnail" });
    }
    res.sendFile(path.resolve(outputPath));
  });
};