const path = require("path");
require("dotenv").config({ path: path.resolve(__dirname, "../../.env") });

const fs = require("fs");
const { exec } = require("child_process");

const VIDEO_DIR = path.resolve(__dirname, '../../../', process.env.VIDEO_DIR);
const OUTPUT_DIR = path.resolve(__dirname, "../../", process.env.RESULTS_DIR);

console.log("Resolved VIDEO_DIR:", VIDEO_DIR);

exports.listVideos = async (req, res) => {
  try {
    const files = await fs.promises.readdir(VIDEO_DIR);
    const videoFiles = files.filter(file => file.endsWith(".mp4") || file.endsWith(".mov"));
    res.status(200).json(videoFiles);
  } catch (error) {
    res.status(500).json({ error: "Error reading video directory" });
  }
};

exports.generateThumbnail = (req, res) => {
  const filename = req.params.filename;
  const videoPath = path.join(VIDEO_DIR, filename);
  const outputPath = path.join(OUTPUT_DIR, `${filename}.jpg`);

  if (!fs.existsSync(OUTPUT_DIR)) {
    fs.mkdirSync(OUTPUT_DIR, { recursive: true });
  }

  const command = `ffmpeg -y -i "${videoPath}" -vf "select=eq(n\\,0)" -q:v 3 "${outputPath}"`;

  exec(command, (err) => {
    if (err) {
      return res.status(500).json({ error: "Error generating thumbnail" });
    }
    res.sendFile(path.resolve(outputPath));
  });
};