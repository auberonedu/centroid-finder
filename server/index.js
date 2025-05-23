import express from 'express';
import router from "./routes/routers.js"
import cors from 'cors';
import path from 'path';

const app = express();
const PORT = process.env.PORT || 3000;

app.use(cors());

// read in JSON payloads in request body
app.use(express.json());

// serve static files from /videos
app.use('/videos', express.static(path.resolve('public/videos')));

app.use("/", router);

app.listen(PORT, console.log("Listening on http://localhost:3000"));