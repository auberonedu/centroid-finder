import express from "express";
import cors from "cors";
import router from "./Router/Router.js";
import dotenv from "dotenv";
import path from "path";
import { fileURLToPath } from "url";

dotenv.config();

const app = express();

// Setup __dirname in ES module context
const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);

// ✅ Resolve JAR_PATH now that __dirname is defined
const jarPath = path.resolve(__dirname, process.env.JAR_PATH);
console.log("✅ Absolute path to salamander.jar:", jarPath);

// Optionally attach this to app.locals so your route can use it
app.locals.JAR_PATH = jarPath;

// 🔓 Enable CORS for all routes
app.use(cors());

// 🔍 Parse JSON bodies
app.use(express.json());

// 📂 Serve static files
const framesDir = path.join(__dirname, "frames");
console.log("Serving static frames from:", framesDir);
app.use("/frames", express.static(framesDir));

const outputDir = path.join(__dirname, "output");
console.log("Serving static output from:", outputDir);
app.use("/output", express.static(outputDir));

const thumbnailsDir = path.join(__dirname, "thumbnails");
console.log("Serving static thumbnails from:", thumbnailsDir);
app.use("/thumbnails", express.static(thumbnailsDir));

// ✅ Test route to confirm server is alive
app.get("/", (req, res) => {
  console.log("✅ GET / hit — server is responding");
  res.send("🦎 Salamander API is running!");
});

// 📦 Mount router (your actual app logic)
app.use("/", router);

// 🚀 Start the server
const PORT = 3001;
app.listen(PORT, () => {
  console.log(`Server listening at http://localhost:${PORT}`);
});
