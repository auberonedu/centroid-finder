FROM node:20-slim

# Install dependencies for installing Java and ffmpeg
RUN apt-get update && apt-get install -y \
    wget \
    gnupg \
    ffmpeg \
    && apt-get clean

# Add Eclipse Adoptium GPG key and repo
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

# Copy Java JAR and sample input/output folders
COPY processor/target/centroid-finder-1.0-SNAPSHOT-jar-with-dependencies.jar /app/processor/
#COPY processor/sampleInput /app/sampleInput
#COPY processor/sampleOutput /app/sampleOutput

# Expose backend port
EXPOSE 3000

# Set environment variables
ENV VIDEO_DIR=/videos
ENV RESULTS_DIR=/results
ENV JAVA_JAR_PATH=/app/processor/centroid-finder-1.0-SNAPSHOT-jar-with-dependencies.jar

# Start the Express server
CMD ["node", "server.js"]
