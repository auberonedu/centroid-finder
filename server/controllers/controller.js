import { spawn } from "child_process";
import { v4 as uuid } from "uuid";
import * as fs from "fs";
import path, { dirname } from "path";
import ffmpegPath from "ffmpeg-static";
import { fileURLToPath } from "url";

const __filename = fileURLToPath(import.meta.url);
const __dirname = dirname(__filename);

export const jobStatus = new Map();

export const processVid = (req, res) => {
   const { file, color = "255,0,0", threshold = 95 } = req.body;

   const inputPath = path.join(process.env.VIDEO_DIR || "videos", file);
   const jobId = uuid();
   const outputFile = `${jobId}.csv`;

   const outputDir = process.env.OUTPUT_DIR || path.join(__dirname, "output");
   const outputPath = path.join(outputDir, outputFile);
   if (!fs.existsSync(outputDir)) {
      fs.mkdirSync(outputDir, { recursive: true });
   }

   // Prepare thumbnail directory and path
   const thumbnailDir = path.join(__dirname, "thumbnails");
   const thumbnailFile = `${file}.jpg`;
   const thumbnailPath = path.join(thumbnailDir, thumbnailFile);
   if (!fs.existsSync(thumbnailDir)) {
      fs.mkdirSync(thumbnailDir, { recursive: true });
   }

   // Spawn JAR processing in detached mode
   const jarInput = [
      "-jar",
      process.env.JAR_PATH,
      inputPath,
      outputPath,
      color,
      threshold.toString(),
   ];

   const jar = spawn("java", jarInput, {
      detached: true,
      stdio: "ignore",
   });

   if (jar && typeof jar.unref === "function") {
      jar.unref();
   }

   // Spawn FFmpeg for thumbnail in detached mode
   const ffmpeg = spawn(ffmpegPath, [
      "-y",
      "-i", inputPath,
      "-ss", "00:00:01",
      "-vframes", "1",
      thumbnailPath,
   ], {
      detached: true,
      stdio: "ignore",
   });

   if (ffmpeg && typeof ffmpeg.unref === "function") {
      ffmpeg.unref();
   }

   jobStatus.set(jobId, {
      status: "processing",
      output: outputFile,
   });

   res.json({ jobId });
};

export const getJobStatus = (req, res) => {
   const job = jobStatus.get(req.params.jobId);
   if (!job) {
      return res.status(404).json({ error: "Job not found" });
   }
   res.json(job);
};

export const getJobs = (req, res) => {
   const jobs = [];
   for (const [jobId, data] of jobStatus.entries()) {
      jobs.push({ jobId, ...data });
   }
   res.status(200).json(jobs);
};

export const videos = (req, res) => {
   const videoDir = process.env.VIDEO_DIR || "videos";

   fs.readdir(videoDir, (err, files) => {
      if (err) {
         console.error("Cannot read from video directory: ", err);
         return res.status(500).json({ error: "Cannot read from video directory" });
      }

      const videoFiles = files.filter((file) => file.endsWith(".mp4"));
      res.status(200).json(videoFiles);
   });
};

export const thumbnail = (req, res) => {
   const { filename } = req.params;

   const videoPath = path.join(process.env.VIDEO_DIR || "videos", filename);
   const thumbnailDir = path.join("thumbnails");
   const thumbnailFile = `${filename}.jpg`;
   const thumbnailPath = path.join(thumbnailDir, thumbnailFile);

   if (!fs.existsSync(videoPath)) {
      return res.status(404).json({ error: "Video not found" });
   }

   if (!fs.existsSync(thumbnailDir)) {
      fs.mkdirSync(thumbnailDir, { recursive: true });
   }

   // Serve existing thumbnail if already generated
   if (fs.existsSync(thumbnailPath)) {
      res.set("Content-Type", "image/jpeg");
      return fs.createReadStream(thumbnailPath).pipe(res);
   }

   // Spawn ffmpeg to create thumbnail (wait for it to finish)
   const ffmpeg = spawn(ffmpegPath, [
      "-y",
      "-i", videoPath,
      "-ss", "00:00:01",
      "-vframes", "1",
      thumbnailPath,
   ]);

   ffmpeg.on("close", (code) => {
      if (code === 0 && fs.existsSync(thumbnailPath)) {
         res.set("Content-Type", "image/jpeg");
         fs.createReadStream(thumbnailPath).pipe(res);
      } else {
         console.error("FFmpeg failed or thumbnail not created");
         res.status(500).json({ error: "Failed to generate thumbnail" });
      }
   });

   ffmpeg.on("error", (err) => {
      console.error("FFmpeg spawn error:", err);
      res.status(500).json({ error: "Failed to spawn ffmpeg" });
   });
};