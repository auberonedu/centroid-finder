import express from 'express';
import router from "./routes/routers.js"
import cors from 'cors';
import path from 'path';
import dotenv from 'dotenv';
import { fileURLToPath } from 'url'; // for resolving ES module paths

// Get __dirname from ES modules
const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);

// Load environment variables from the .env file in the project root
dotenv.config({ path: path.resolve(__dirname, '../.env') });

const app = express();
const PORT = process.env.PORT || 3000;

app.use(cors());

// read in JSON payloads in request body
app.use(express.json());

// serve static files from /videos
app.use('/videos', express.static(path.resolve('public/videos')));

app.use("/", router);

app.listen(PORT, console.log(`Listening on http://localhost:${PORT}`));