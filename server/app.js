import express from "express";
import dotenv from "dotenv";
import path from "path";
import route from "./routers/router.js";
import binarizeRoute from "./routers/binarize.js";
import cors from "cors";

dotenv.config();

const app = express();

const VIDEO_DIR = process.env.VIDEO_DIR || "videos";
const OUTPUT_DIR = process.env.OUTPUT_DIR || "results";

app.use(cors());
app.use(express.json());

app.use("/", route);
app.use("/api", binarizeRoute);
app.use("/results", express.static(path.resolve(OUTPUT_DIR)));
app.use("/videos", express.static(path.resolve(VIDEO_DIR)));

export default app;
