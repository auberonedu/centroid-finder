Summary: This Docker image uses a slim Java base with Node.js installed to support both Java backend video processing and React frontend UI, exposed via ports 3000 and 3001, with volume mounts for /videos and /results.

Docker Plan for Salamander Backend

Objective:
Create a Docker image that packages our Express backend and Java integration so anyone can run it using:

docker run -p 3000:3000 -v "$VIDEO_DIRECTORY:/videos" -v "$RESULTS_DIRECTORY:/results" ghcr.io/YOUR_GITHUB_USERNAME/salamander:latest

Base Image:
We are using openjdk:17-slim. This image provides a lightweight Java runtime, which is required to run our centroid finder JAR file. Since our server is written in Node.js, we will also install Node.js version 20 manually during the Docker build process.

Directory Layout Inside Container:
/app - Root project directory
/app/server - Node.js Express backend source files
/app/processor - Java JAR file and helper code
/videos - Mounted host directory for input videos
/results - Mounted host directory for output files like CSV and PNGs

Environment Configuration:
We will use environment variables to refer to the mounted video and result directories:
VIDEO_DIR=/videos
RESULTS_DIR=/results
These variables will be available at runtime, and the Docker volumes will be mounted externally using the -v flag.

Ports and Volumes:
We expose port 3000 inside the container.
The host will mount two volumes:
-v "$VIDEO_DIRECTORY:/videos"
-v "$RESULTS_DIRECTORY:/results"
This allows any input/output to remain on the host system without changing the container.

Testing Strategy:
We will test locally with the following steps:

Build the image
docker build -t salamander-local .
Run the image with mounted test folders
docker run -p 3000:3000 -v $(pwd)/videos:/videos -v $(pwd)/results:/results salamander-local
Use Postman or the React frontend to call API endpoints like /videos and /process
Check that output appears in the host's results folder
Optimization Strategy:

Use .dockerignore to exclude node_modules and local files
Use npm ci --only=production to install only runtime dependencies
Combine RUN statements in Dockerfile to reduce image layers
Use a small base image (openjdk:17-slim) to minimize final image size
GHCR Deployment Plan:

Login to GHCR
docker login ghcr.io
(Use GitHub username and Personal Access Token as password)
Build and tag the image
docker build -t ghcr.io/YOUR_GITHUB_USERNAME/salamander .
Push the image
docker push ghcr.io/YOUR_GITHUB_USERNAME/salamander
Make package public
Go to https://github.com/YOUR_GITHUB_USERNAME?tab=packages
Click the salamander package
Click "Package Settings"
Scroll to Danger Zone and set to Public
Validation:
Have your partner run:

docker run -p 3000:3000 -v "$VIDEO_DIRECTORY:/videos" -v "$RESULTS_DIRECTORY:/results" ghcr.io/YOUR_GITHUB_USERNAME/salamander:latest

Check that all endpoints work and that results are generated properly in the host results folder.

Done.