SDEV 334 Salamander Back-end Plan

Express and Java - Steps and Responsibilities

1. Serve static files: use express.static() to serve /videos and /results folders
2. GET /api/videos: read the /public/videos/ directory and return list of available video filenames
3. GET /thumbnail/:filename: use ffmpeg to extract first frame from selected video; return as .jpeg
4. POST /process/:filename: accept targetColor, threshold, and timeInterval as query parameters; generate a jobId using UUID; use child_process.spawn() (detached) to launch the Java JAR; write a jobs/jobId.json file with “status”: “processing”
5. Java JAR processing: process the video using given parameters; output results to /public/results/jobId.csv
6. Job completion detection: server checks if jobId.csv exists; if yes, update jobId.json to “status”: “done” and add result path
7. GET /process/:jobId/status: read the corresponding jobId.json and return the current status to client (including error message if processing fails)
