# Salamander Tracker - Sever Planning (Wave 2)

## What we understood from the API

we are building a backend using Express.js that connects to our exisiting Java JAR to process videos and generate CSV outputs and the server should have API endpoints

1. **GET /api/videos**
- returns a list of all videos in a public folder
- will be used to pick which video to analyze

2. **GET /thumbnail/:filename**
- grabs the first frame from a video using ffmpeg and sends it as a JPEG
- helps user preview the video

3. **POST /process/:filename?targetColor=FF0000&threshold=100**
- starts the video processing job bu calling our Java JAR
- this must be asynchronous and returns a unique job ID

4. **GET /process/:jobId/status**
- returns the current status of the processing job like `processing`, `done`, or `error`
- if done then returns the path to th CSV result

-------------------------

##  Implementation Plan (Express)

### .env file
store the following 
- `VIDEO_DIR=absolute/path/to/videos`
- `JAR_PATH=absolute/path/to/centroid-1.0-SNAPSHOT-jar-with-dependencies.jar`
- `RESULTS_DIR=absolute/path/to/results`

### Express Routes Cutdown

Route                               Description                         What it does
`GET /api/videos`                   list video filenames                reads files from VIDEO_DIR
`GET /thumbnail/:filename`          gets the first video frame          uses ffmpeg to extract frame 0 as JPEG
`POST /process/:filename`           starts processing video             spams detached child process to run the jar
`GET /process/:jobId/status`        gets the job status                 checks statsu file 


## Server Processor Architecture

User/Frontend
↓
Express Server (Node.js)
↓
Video Processor (Java JAR)
↓
CSV Output → Status File → Result Folder

- the express server calls the JAR with child_process.spawn
- the jar runs detached and writes the result to the /results folder
- Job stats is tracked in a jobs.json file or as per .status files


## Tools and NPM Packages

dotenv → Load paths from .env
express → Handle routing
uuid → Generate jobIds
child_process.spawn → Call Java JAR
fs/promises → File ops
path → Resolve directories
ffmpeg → Create thumbnails


## Testing Plan

- Use of the Postman to test all endpoints
- add some console logs for key server actions
- mock the JAR call for development
- validating that the output CSV appears in right directory

## Questions We Still Have

- should we have `.mov` and `.mp4` videos or should we just restrict to one type?
- how should we clean up the old results?
- how will we display job errors in the frontend or do we have to log them?