# Base image with Java
FROM eclipse-temurin:21

# Set working directory
WORKDIR /app

# Set default environment variables
ENV VIDEO_DIR=/app/videos
ENV RESULTS_DIR=/app/results
ENV JOBS_DIR=/app/jobs
ENV JAR_PATH=/app/processor/videoprocessor.jar

# Install curl and Node.js
RUN apt-get update && \
    apt-get install -y curl gnupg && \
    curl -fsSL https://deb.nodesource.com/setup_20.x | bash - && \
    apt-get install -y nodejs && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*

# Copy Node.js package files and install dependencies early for layer caching
COPY server/package*.json ./server/
RUN cd server && npm install

# Copy remaining server code and Java JAR
COPY server ./server
COPY processor/target/videoprocessor.jar ./processor/videoprocessor.jar

# Expose backend port
EXPOSE 3000

# Start the Node server
CMD ["node", "server/app.js"]
