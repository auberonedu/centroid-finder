# Docker Plan for Salamander Tracker Backend

## Base Image
Use `node:18-jdk-slim` – lightweight, includes Node.js and Java.

## Requirements
- Run Express server (Node.js)
- Run Java JAR
- Mount volumes for videos and results
- Pass in environment variables (.env)
- Expose port 3000

## Build Strategy
- Use `.dockerignore` to skip unnecessary files
- Install dependencies via `package.json`
- Copy server and processor directories
- ENTRYPOINT should run `node index.js`