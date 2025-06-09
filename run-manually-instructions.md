Manual Run Instructions – Salamander Tracker

This guide provides manual Docker commands to run the full-stack Salamander Tracker app without using the run-salamander.sh script.

You will start two Docker containers manually:
* One for the backend (Java + Express API)
* One for the frontend (Next.js React app)

Prerequisites
Make sure Docker is installed and working

Step 1: Set Your Folder Paths
Before running the containers, determine the full absolute paths to:
* Your videos folder (contains .mp4 files)
* Your results/output folder (CSV results will be saved here)

Step 2: Run the Backend
Replace the folder paths below with your actual directories:

export VIDEO_DIRECTORY=/full/path/to/your/videos
export RESULTS_DIRECTORY=/full/path/to/your/results

docker run \
  -p 3001:3001 \
  -v "$VIDEO_DIRECTORY:/videos" \
  -v "$RESULTS_DIRECTORY:/results" \
  ghcr.io/oakes777/salamander:latest

(This starts the backend on port 3001)

Step 3: Run the Frontend
In a new terminal window, run:

docker run \
  -p 3000:3000 \
  ghcr.io/oakes777/salamander-client:latest

(This starts the frontend on port 3000)

Step 4: Open the App

Visit http://localhost:3000 in your browser to use the Salamander Tracker.

Notes
* These containers are prebuilt — no npm or Java setup required.
* The backend expects .mp4 videos inside /videos and writes CSVs to /results.
* To stop the app, press Ctrl + C in both terminal windows.