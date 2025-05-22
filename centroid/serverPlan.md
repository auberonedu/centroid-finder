# Salamander Tracker - Sever Planning (Wave 2)

## What we understood from the API

we are building an Express.js server that will handle the endpoints as mentioned in the Salamander API

1. **GET /api/videos**
- returns a list of all available video filenames from a directory (eg. "salamander1.mp4")

2. **GET /thumbnail/:filename**
- returns the first frame of a given video as a JPEG image

3. **POST /process/:filename?targetColor=FF0000&threshold=100**
- starts the video processing job bu calling our Java JAR
- this must be asynchronous and returns a unique job ID

4. **GET /process/:jobId/status**
- returns the current status of the processing job like `processing`, `done`, or `error`

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


## Tools Needs

`child_process.spawn` in detached mode to run the JAR
`uuid` package to generate unique job IDs
`dotenv` toaccess paths from .env
`fs.promises` and `path` for file operation
`ffmpeg` to generate thumbnails

## Testing Plan

- Use of the Postman to test all endpoints
- add some console logs for key server actions
- mock the JAR call for development
- validating that the output CSV appears in right directory

## Questions We Still Have

- should we have `.mov` and `.mp4` videos or should we just restrict to one type?
- how should we clean up the old results?
- how will we display job errors in the frontend or do we have to log them?