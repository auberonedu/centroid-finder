# Use Node.js base image
FROM node:20-alpine

# Install Java (OpenJDK 17)
RUN apk add --no-cache openjdk21

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
