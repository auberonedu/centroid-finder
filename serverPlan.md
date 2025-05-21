# Understanding the API

## The Salamander API has a few endpoints that help the frontend work with video files and run processing jobs:

- `GET /api/videos` — lists all video files stored on the server. These videos are available at `/videos/VIDEO_NAME`.

- `GET /thumbnail/{filename}` — returns the first frame of a video as a JPEG image.

- `POST /process/{filename}?targetColor=...&threshold=...` — starts a new job to analyze a video for a specific color. It returns a `jobId` so we can check the job later.

- `GET /process/{jobId}/status` — checks the status of the job. It can be:
  - `"processing"` if still running,
  - `"done"` with a link to the result,
  - or `"error"` if something went wrong.

### Questions:
1. Does Docker slows down the program with its containers?
2. How do we ensure our Centroid finder is working as intended? 
-----------------------------------------------------------------------------------------------------------------------------------------
# Server Plan
1. Understand the Salamander API 
2. Define and make the .env variables use dotenv for easy access
3. Job Management Strats 
    - UUID 
    - Use filesystem to track job statuses
    - Store input video and output here
4. API endpoints 
    - Define the route for salamander API
    - Start jobs async, use 'child_process' (look at hints)
    - Plan routes to check job status and show results
5. Handle Video and Results
    - Express.static to serve videos and processed results
6. Error Handling/Edge Cases 
    - Plan for missing files, invalid/missing args/paramenter, process errors
-----------------------------------------------------------------------------------------------------------------------------------------
