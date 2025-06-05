import fs from "fs/promises";
import path from "path";
import ffmpeg from "fluent-ffmpeg";
import { spawn } from "child_process";
import { randomUUID } from "crypto";

const jobStatus = {}; // In-memory job tracking

const getVideos = async (req, res) => {
  try {
    const videoDir = process.env.VIDEO_PATH;
    const files = await fs.readdir(videoDir);
    const videoURLs = files.map((file) => `${file}`);
    res.json(videoURLs);
  } catch (err) {
    console.error("Error reading video directory:", err);
    res.status(500).json({ error: "Failed to read video directory" });
  }
};

const getThumbnail = (req, res) => {
  try {
    const videoDir = process.env.VIDEO_PATH;
    const thumbnailId = req.params.filename;
    const videoPath = path.join(videoDir, thumbnailId);

    res.setHeader("Content-Type", "image/jpeg");

    ffmpeg(videoPath)
      .inputOptions(["-ss 0"])
      .outputOptions(["-frames:v 1"])
      .format("mjpeg")
      .pipe(res, { end: true });
  } catch (err) {
    console.error("Error generating thumbnail:", err);
    res.status(500).json({ error: "Error generating thumbnail" });
  }
};

const startVideoProcess = async (req, res) => {
  const { filename } = req.params;
  const { targetColor, threshold } = req.query;

  // Check if params are missing
  if (!targetColor || !threshold) {
    return res
      .status(400)
      .json({ error: "Missing targetColor or threshold query parameter." });
  }

  // Validate hex color
  const hexColorRegex = /^[0-9A-Fa-f]{6}$/;
  if (!hexColorRegex.test(targetColor)) {
    return res
      .status(400)
      .json({ error: "targetColor must be a valid 6-digit hex code (e.g., FF0000)" });
  }

  // Validate threshold number
  const thresholdNum = Number(threshold);
  if (Number.isNaN(thresholdNum) || thresholdNum < 0 || thresholdNum > 255) {
    return res.status(400).json({ error: "Threshold must be a number between 0 and 255." });
  }

  try {
    const videoDir = process.env.VIDEO_PATH;
    const files = await fs.readdir(videoDir);

    if (!files.includes(filename)) {
      return res.status(404).json({ error: "Video file not found on the server." });
    }

    const jobId = randomUUID();
    jobStatus[jobId] = { status: "processing" };

    const child = spawn(
      "java",
      [
        "-jar",
        "../Processor/target/centroidFinderVideo-jar-with-dependencies.jar",
        path.join("videos", filename),
        targetColor,
        thresholdNum.toString(),
        jobId,
      ],
      {
        stdio: ["ignore", "pipe", "ignore"],
      }
    );

    let output = "";

    child.stdout.on("data", (data) => {
      const text = data.toString();
      output += text;
      console.log(`[JAR OUTPUT ${jobId}]: ${text.trim()}`);
    });

    child.on("exit", (code) => {
      jobStatus[jobId] = { status: code === 0 ? "done" : "error" };
    });

    child.on("error", (err) => {
      jobStatus[jobId] = {
        status: "error",
        error: `Error processing video: ${err.message}`,
      };
    });

    res.status(202).json({ jobId });
  } catch (err) {
    console.error("Error starting job:", err);
    res.status(500).json({ error: "Error starting job" });
  }
};


const getJobStatus = (req, res) => {
  const { jobId } = req.params;
  const job = jobStatus[jobId];

  if (!job) {
    return res.status(404).json({ error: "Job ID not found" });
  }

  if (job.status === "processing") {
    return res.status(200).json({ status: "processing" });
  } else if (job.status === "done") {
    return res.status(200).json({ status: "done", result: job.result });
  } else if (job.status === "error") {
    return res.status(200).json({ status: "error", error: job.error });
  }

  return res.status(500).json({ error: "Error fetching job status" });
};

export default {
  getVideos,
  getThumbnail,
  startVideoProcess,
  getJobStatus,
};
