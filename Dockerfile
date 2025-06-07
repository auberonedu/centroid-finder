# Create the base image for Java with OpenJDK 25
FROM openjdk:25-slim

# Install curl so we can fetch the Node.js setup script
RUN apt-get update && apt-get install -y curl gnupg && \
    curl -fsSL https://deb.nodesource.com/setup_24.x | bash - && \
    apt-get install -y nodejs && \
    apt-get clean && rm -rf /var/lib/apt/lists/*

# Set working directory in container to a new directory called app
WORKDIR /app

# Copy backend's package.json and package-lock.json
COPY server/package*.json ./server/

# Install all dependencies for the backend server
RUN cd server && npm install


# Use later when finished:
# Omit the dev dependencies with npm clean install
# RUN cd server && npm ci --omit=dev

# Copy the backend source code
COPY server ./server

# Copy the Java processor JAR file
COPY processor/videoprocessor.jar ./processor/videoprocessor.jar

# Expose port 3000 for the Express backend server
EXPOSE 3000

# Start the backend server
CMD ["node", "server/index.js"]
