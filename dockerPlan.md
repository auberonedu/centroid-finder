# Docker Plan for Salamander Tracker Backend

## Base Image
We will use:

node:18-jdk-slim

it includes Node.js and OpenJDK (java) in a small Debian imagem ideal for running both the Express server and the JAR processor.

## Requirements Recap

Requirement                     Planned Solution 
Run Node.js backend             Use Node.js in base image
Run Java JAR file               Use OpenJDK 17 (included in `node:18-jdk-slim`)
Run ffmpeg for thumbnails       Install via `apt-get`
Serve on port 3000              Use `EXPOSE 3000` in Dockerfile     
Mount videos/results folders    Use Docker volumes (`/videos` and `/results`)
Use environment variables       `.env` file and `ENV` in Dockerfile
Ignore unnecessary files        Add `.dockerignore`
Keep image size small           Use slim image, clean cache after installs

## Directory Volumes

Host Path               Container Path          Purpose    
`/sampleInput`          `/videos`               User uploads/input videos
`/sampleOutput`         `/results`              Output CSVs, thumbnails, logs

## Environment Variables

We will define these in `.env` (and use `dotenv` in Node):

```env
VIDEO_DIR=/videos
RESULTS_DIR=/results
JAR_PATH=/app/centroid/processor/target/centroid-1.0-SNAPSHOT-jar-with-dependencies.jar
```

## Build Strategy

1. Start with `node:18-jdk-slim`
2. Install `ffmpeg` using `apt-get`
3. Create app directory: `/app`
4. Copy `package.json` and install dependencies
5. Copy full project contents (`server`, `centroid`, etc.)
6. Set ENV vars and expose port
7. Start with `node index.js`


## ENTRYPOINT / CMD

We will use:

```dockerfile
CMD ["node", "index.js"]
```