# AI Generated summary:
    
# This Dockerfile:
    # 1. Starts with a Node.js environment.
    # 2. Adds tools to download and verify software.
    # 3. Installs video tools and Java 21.
    # 4. Copies your code into the container.
    # 5. Sets up environment variables.
    # 6. Starts your web server.

# Base image with minimal packages needed to run node
FROM node:20-slim

# Install dependencies for installing Java and ffmpeg
    # wget is a tool to download files from the internet
    # gnupg verifies downloaded software
    # ffmpeg is a video processing library
RUN apt-get update && apt-get install -y \
    wget \
    gnupg \
    ffmpeg \
    && apt-get clean 
# apt-get clean removes extra files, keeps image small

# Add Eclipse Adoptium GPG key and repo (AI helped create this)
    # make a keyrings directory to store security keys
    # download a security key so system knows Adoptium Java packages are trustworthy
    # add adoptium repo to system's list of software sources
    # install Java 21 to run Java jar
    # clean up and delete cached files
RUN mkdir -p /etc/apt/keyrings && \
    wget -O- https://packages.adoptium.net/artifactory/api/gpg/key/public | gpg --dearmor > /etc/apt/keyrings/adoptium.gpg && \
    echo "deb [signed-by=/etc/apt/keyrings/adoptium.gpg] https://packages.adoptium.net/artifactory/deb bullseye main" > /etc/apt/sources.list.d/adoptium.list && \
    apt-get update && \
    apt-get install -y temurin-21-jdk && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*

# Set working directory inside container
WORKDIR /app/server

# Copy package files and install dependencies
COPY server/package*.json ./
RUN npm install

# Copy all server code into the container
COPY server/ ./

# Copy Java JAR into processor directory
COPY processor/target/centroid-finder-1.0-SNAPSHOT-jar-with-dependencies.jar /app/processor/

# Expose backend port
EXPOSE 3000

# Set environment variables
ENV VIDEO_DIR=/videos
ENV RESULTS_DIR=/results
ENV JAVA_JAR_PATH=/app/processor/centroid-finder-1.0-SNAPSHOT-jar-with-dependencies.jar

# Start the Express server
CMD ["node", "server.js"]
