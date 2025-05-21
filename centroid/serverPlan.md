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
