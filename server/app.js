// app.js
import express from "express";
import dotenv from "dotenv";
import path from "path";
import route from "./routers/router.js";
import cors from "cors";

dotenv.config();

const app = express();

app.use(cors());
app.use(express.json());

app.use("/", route);
app.use("/results", express.static(path.resolve(process.env.OUTPUT_DIR)));
app.use("/videos", express.static(path.resolve(process.env.VIDEO_DIR) || "videos"));

export default app;
