SDEV 334 Salamander Back-end Plan

Express and Java - Steps and Responsibilities

1. Serve static files: use express.static() to serve /videos and /results folders
2. GET /api/videos: read the /public/videos/ directory and return list of available video filenames
3. GET /thumbnail/:filename: use ffmpeg to extract first frame from selected video; return as .jpeg
4. POST /process/:filename: accept targetColor, threshold, and timeInterval as query parameters; generate a jobId using UUID; use child_process.spawn() (detached) to launch the Java JAR; write a jobs/jobId.json file with “status”: “processing”
5. Java JAR processing: process the video using given parameters; output results to /public/results/jobId.csv
6. Job completion detection: server checks if jobId.csv exists; if yes, update jobId.json to “status”: “done” and add result path
7. GET /process/:jobId/status: read the corresponding jobId.json and return the current status to client (including error message if processing fails)

╭────────────────────────────────────────────────────────────╮
│                      React Front End                       │
│                                                            │
│  • User picks video:   /api/videos                         │
│  • Sees frame preview:  /thumbnail/:filename               │
│  • Picks color + threshold + interval                      │
│  • Clicks "Submit" → POST /process/:filename               │
│  • Sees status → GET /process/:jobId/status                │
╰────────────────────────────────────────────────────────────╯
                             │
╭────────────────────────────────────────────────────────────╮
│                    Express Server (Node.js)                │
│                                                            │
│  • Uses dotenv to load paths to:                           │
│      - videos folder                                       │
│      - results folder                                      │
│      - Java JAR path                                       │
│                                                            │
│  • Serves static files from:                               │
│      - /public/videos                                      │
│      - /public/results                                     │
│                                                            │
│  • Handles:                                                │
│      - GET /api/videos:  list video filenames              │
│      - GET /thumbnail/:filename:  extract frame via ffmpeg │
│      - POST /process/:filename                             │
│         - generates jobId (UUID)                           │
│         - spawns Java JAR with args (detached)             │
│         - writes jobs/jobId.json: {"status": "processing"} │
│                                                            │
│      - GET /process/:jobId/status                          │
│         - reads jobId.json                                 │
│         - checks if result CSV exists                      │
│         - returns status + optional result path            │
╰────────────────────────────────────────────────────────────╯
                             │
╭────────────────────────────────────────────────────────────╮
│                Java JAR (videoProcessor.jar)               │
│                                                            │
│  • Reads video (.mp4) from /public/videos/                 │
│  • Applies thresholding + centroid logic                   │
│  • Writes CSV result to /public/results/jobId.csv          │
│                                                            │
│  • Optional: could support extracting preview frame        │
│    in future version if ffmpeg not used                    │
╰────────────────────────────────────────────────────────────╯
