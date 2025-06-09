# Since this a multistage build, we will need to build the node and java artifacts
FROM node:20-bullseye AS builder

# Install OpenJDK 17
RUN apt update && \
    apt install -y openjdk-17-jdk && \
    apt clean

# Set working directory
WORKDIR /app

# Copy server and Java code
COPY . .

# Install Node dependencies
RUN npm install

# Build the Java JAR
RUN cd java && ./mvnw clean package

# -------- Stage 2: Runtime --------
FROM node:20-slim

# Install OpenJDK 17 in runtime image
RUN apt update && \
    apt install -y openjdk-17-jre && \
    apt clean

# Set working directory
WORKDIR /app

# Copy server files from builder stage
COPY --from=builder /app /app

# Expose backend port
EXPOSE 3000

# Set environment variables (can be overridden by user)
ENV VIDEO_DIR=/videos
ENV OUTPUT_DIR=/results
ENV JAR_PATH=java/target/centroid-finder-1.0-SNAPSHOT-jar-with-dependencies.jar

# Start the server
CMD ["node", "index.js"]
