How to Run the Salamander Tracker

This guide walks you through running the full-stack Salamander Tracker app using Docker. You will be running two prebuilt containers:
* The backend (Java + Express API)
* The frontend (Next.js React app)

Prerequisites
1. Install Docker Desktop

Download from: https://www.docker.com/products/docker-desktop

After installing, confirm Docker is working by running:

docker run hello-world

2. Clone the repository

git clone https://github.com/oakes777/centroid-finder.git
cd centroid-finder

Running the App
1. Make the script executable (in terminal run this line of code):

chmod +x run-salamander.sh

2. Run the script

./run-salamander.sh

You will be prompted to enter two full folder paths:
    * A folder on your machine that holds videos (for example, mp4 files)
    * A folder where output CSV files should be saved

3. Example:

Enter full path to your videos folder 

(e.g. /Users/you/videos)

Enter full path to your output results folder 

(e.g. /Users/you/results)

Notes
* The backend will start on port 3001
* The frontend will start on port 3000

* Once both are running, open your browser and go to:

http://localhost:3000

* No manual configuration is needed. Everything runs from the Docker containers.

Optional: Manual Run Instructions
If you prefer to run the backend and frontend manually instead of using the script, see the manual-run-instructions.md file for details.