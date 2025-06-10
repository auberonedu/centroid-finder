# server/Dockerfile

FROM openjdk:17-slim

# Install ffmpeg (includes ffprobe) and Node.js 20
RUN apt-get update && \
    apt-get install -y curl ffmpeg && \
    curl -fsSL https://deb.nodesource.com/setup_20.x | bash - && \
    apt-get install -y nodejs && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*

# Set working directory
WORKDIR /app

# Copy and install server dependencies first (improves Docker layer caching)
COPY server/package*.json ./
RUN npm install

# Copy all server source code
COPY server/. .

# Copy the JAR to the expected location
RUN mkdir -p /processor/target
COPY processor/target/salamander-jar-with-dependencies.jar /processor/target/

# Expose backend port
EXPOSE 3001

# Start the Express server
CMD ["node", "index.js"]
