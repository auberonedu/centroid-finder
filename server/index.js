import express from "express";
import cors from "cors";
import router from "./Router/Router.js";
import dotenv from "dotenv";
import path from "path";
import { fileURLToPath } from "url";

dotenv.config();

const app = express();

const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);

// This worked before
const framesDir = path.join(__dirname, "frames");
console.log("Serving static frames from:", framesDir);

app.use(
  "/frames",
  (req, res, next) => {
    res.setHeader("Access-Control-Allow-Origin", "*");
    next();
  },
  express.static(framesDir)
);

// NEW: Serve output CSVs statically
const outputDir = path.join(__dirname, "output");
console.log("Serving static output from:", outputDir);

app.use(
  "/output",
  (req, res, next) => {
    res.setHeader("Access-Control-Allow-Origin", "*");
    next();
  },
  express.static(outputDir)
);

// Enable CORS
app.use(cors());

// JSON body parsing
app.use(express.json());

// Routes
app.use("/", router);

const PORT = 3001;
app.listen(PORT, () => {
  console.log(`Server listening at http://localhost:${PORT}`);
});
