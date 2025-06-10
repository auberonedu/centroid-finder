let isScanning = false;

import fs from "fs/promises";
import { existsSync } from "fs";
import path from "path";
import { fileURLToPath } from "url";
import dotenv from "dotenv";
import { v4 as uuidv4 } from "uuid";
import { spawn } from "child_process";
import { generateThumbnail } from "../Utils/ffmpegHelpers.js";

const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);
dotenv.config({ path: path.resolve(__dirname, "../.env") });

const indexFilePath = path.resolve(__dirname, "./videoIndex.json");
const videosDir = path.resolve(__dirname, process.env.VIDEO_DIR);
const thumbnailsDir = path.resolve(__dirname, "../thumbnails");
const outputDir = path.resolve(__dirname, "../output");

const jobStatusMap = new Map();

const loadIndex = async () => {
  try {
    const data = await fs.readFile(indexFilePath, "utf8");
    return JSON.parse(data);
  } catch {
    return {};
  }
};

const saveIndex = async (index) => {
  await fs.writeFile(indexFilePath, JSON.stringify(index, null, 2));
};

const getVideoDuration = (filePath) => {
  return new Promise((resolve, reject) => {
    import("fluent-ffmpeg").then(({ default: ffmpeg }) => {
      ffmpeg.ffprobe(filePath, (err, metadata) => {
        if (err) {
          console.error("ffprobe error:", err.message);
          return resolve(null);
        }
        try {
          const seconds = metadata?.format?.duration;
          resolve(seconds ?? null);
        } catch (parseErr) {
          console.error("Error parsing metadata:", parseErr.message);
          resolve(null);
        }
      });
    });
  });
};

const getVideos = async (req, res) => {
  if (isScanning) {
    console.log("getVideos is already running — skipping this call.");
    return res.status(429).json({ error: "Server busy. Try again shortly." });
  }

  isScanning = true;
  console.log("getVideos started");

  try {
    const index = await loadIndex();
    console.log("📁 Resolved videosDir:", videosDir);
    const files = await fs.readdir(videosDir);
    const videoFiles = files.filter((file) =>
      [".mp4", ".mov", ".avi"].includes(path.extname(file).toLowerCase())
    );

    await fs.mkdir(thumbnailsDir, { recursive: true });

    for (const file of videoFiles) {
      if (!index[file]) {
        index[file] = {
          id: uuidv4(),
          thumbnail: `thumbnails/placeholder.jpg`,
        };
      }

      const fullVideoPath = path.join(videosDir, file);
      try {
        const thumbFile = await generateThumbnail(fullVideoPath, thumbnailsDir);
        index[file].thumbnail = `thumbnails/${thumbFile}`;
      } catch (err) {
        console.error(`Failed to generate thumbnail for ${file}:`, err);
        index[file].thumbnail = null;
      }
    }

    await saveIndex(index);

    const metadataList = await Promise.all(
      videoFiles.map(async (file) => {
        const fullPath = path.join(videosDir, file);
        const stats = await fs.stat(fullPath);
        const duration = await getVideoDuration(fullPath);
        const { id, thumbnail } = index[file];

        return {
          id,
          name: file,
          duration,
          createdAt: stats.birthtime,
          modifiedAt: stats.mtime,
          thumbnail,
        };
      })
    );

    console.log(`getVideos finished. Found ${metadataList.length} videos.`);
    res.json({ videos: metadataList });
  } catch (err) {
    console.error("Error in getVideos:", err);
    res.status(500).json({ error: "Failed to load video metadata" });
  } finally {
    isScanning = false;
  }
};

const getVideoById = async (req, res) => {
  try {
    const { id } = req.params;
    const index = await loadIndex();

    const entry = Object.entries(index).find(
      ([filename, meta]) => meta.id === id
    );

    if (!entry) {
      return res.status(404).json({ error: "Video not found" });
    }

    const [filename, metadata] = entry;
    const fullPath = path.join(videosDir, filename);

    if (!existsSync(fullPath)) {
      return res.status(404).json({ error: "Video file missing from disk" });
    }

    const stats = await fs.stat(fullPath);
    const duration = await getVideoDuration(fullPath);

    res.json({
      id: metadata.id,
      name: filename,
      duration,
      createdAt: stats.birthtime,
      modifiedAt: stats.mtime,
      thumbnail: metadata.thumbnail,
    });
  } catch (err) {
    console.error("Error in getVideoById:", err);
    res.status(500).json({ error: "Failed to retrieve video metadata" });
  }
};

const videoProcessing = async (req, res) => {
  const jobId = uuidv4();
  const { filename, color, threshold, interval } = req.body;

  if (!filename || !color || !threshold || !interval) {
    return res.status(400).json({ error: "Missing required fields" });
  }

  const cleanInterval = interval.replace("s", "");
  const colorString = `${color.r},${color.g},${color.b}`;
  const videoPath = path.join(videosDir, filename);
  await fs.mkdir(outputDir, { recursive: true });
  const outputCsvPath = path.join(outputDir, `${jobId}.csv`);
  const jarPath = req.app.locals.JAR_PATH;

  console.log("🐾 Using JAR file at:", jarPath);
  jobStatusMap.set(jobId, { status: "processing", output: "", error: "" });

  const args = [
    "-jar",
    jarPath,
    videoPath,
    colorString,
    threshold.toString(),
    outputCsvPath,
    cleanInterval,
  ];

  console.log("DEBUG — Arguments passed to JAR:");
  console.log("java", ...args);
  console.log("Confirmed videoPath:", videoPath);
  console.log("Color string:", colorString);
  console.log("Threshold:", threshold);
  console.log("Output CSV path:", outputCsvPath);
  console.log("Frame interval (raw):", interval);
  console.log("Frame interval (cleaned):", cleanInterval);

  const java = spawn("java", args);

  let output = "";
  let error = "";

  java.stdout.on("data", (data) => {
    output += data.toString();
  });

  java.stderr.on("data", (data) => {
    error += data.toString();
  });

  java.on("close", (code) => {
    if (code === 0) {
      jobStatusMap.set(jobId, { status: "completed", output, error: "" });
    } else {
      jobStatusMap.set(jobId, { status: "failed", output, error });
    }
  });

  res.json({ jobId, message: "Video processing started." });
};

const getCompletedCSVs = async (req, res) => {
  try {
    const files = await fs.readdir(outputDir);
    const csvFiles = files.filter((file) => file.endsWith(".csv"));
    res.json({ csvFiles });
  } catch (err) {
    console.error("Error reading output directory:", err);
    res.status(500).json({ error: "Unable to list CSV files" });
  }
};

const getStatus = (req, res) => {
  const { jobId } = req.params;

  if (!jobId) {
    return res.status(400).json({ error: "Missing jobId in request" });
  }

  const jobInfo = jobStatusMap.get(jobId);

  if (!jobInfo) {
    return res.status(404).json({ error: "Job ID not found" });
  }

  res.json({
    jobId,
    status: jobInfo.status,
    output: jobInfo.output,
    error: jobInfo.error,
  });
};

export default {
  getVideos,
  getVideoById,
  videoProcessing,
  getCompletedCSVs,
  getStatus,
};
