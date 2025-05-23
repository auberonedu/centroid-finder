require('dotenv').config();

// import express framework and create an instance of it
const express = require('express');
const app = express();
const path = require('path');

const videoRoutes = require('./routes/videoRoutes');
const processRoutes = require('./routes/processRoutes');

// middleware to parse JSON bodies
app.use(express.json());

// server static videos
app.use('/videos', express.static(path.resolve(process.env.VIDEO_DIR)));

// routes
app.use('/api/videos', videoRoutes);
app.use('/process', processRoutes);

// start the server on specified port
const PORT = process.env.PORT || 3000;
app.listen(PORT, () => console.log(`Server running on port ${PORT}`));