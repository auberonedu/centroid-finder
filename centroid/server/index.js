const path = require("path");
require("dotenv").config({ path: path.resolve(__dirname, "../../.env") });

// import express framework and create an instance of it
const express = require('express');
const cors = require('cors');
const app = express();

const videoRoutes = require('./routes/videoRoutes');
const processRoutes = require('./routes/processRoutes');

app.use(cors({
  origin: 'http://localhost:3000' 
}));

// middleware to parse JSON bodies
app.use(express.json());

// server static videos
app.use('/videos', express.static(path.resolve(process.env.VIDEO_DIR)));

// routes
app.use('/api/videos', videoRoutes);
app.use('/api/process', processRoutes);

console.log("Loaded ENV:", {
  VIDEO_DIR: process.env.VIDEO_DIR,
  RESULTS_DIR: process.env.RESULTS_DIR,
  JAR_PATH: process.env.JAR_PATH,
});

// start the server on specified port
const PORT = process.env.PORT || 3001;
app.listen(PORT, () => console.log(`Server running on port ${PORT}`));