# Create the base image for Java with OpenJDK 25
FROM openjdk:25-slim

# Install curl so we can fetch the Node.js setup script
# Also install git so we can clone the frontend repo
RUN apt-get update && \
    apt-get install -y curl gnupg git && \
    curl -fsSL https://deb.nodesource.com/setup_24.x | bash - && \
    apt-get install -y nodejs && \
    apt-get clean && rm -rf /var/lib/apt/lists/*

# Set working directory in the container to a new directory called app
WORKDIR /app

# Copy the .env file into the container
COPY .env .env

# Clone the React frontend repo (built with Next.js)
RUN git clone https://github.com/f3liz/centroid-finder-frontend.git frontend

# Install frontend dependencies and build it
RUN cd frontend && \
    npm install && \
    npm run build && \
    npm run export

# Copy backend's package.json and package-lock.json
COPY server/package*.json ./server/

# Install all dependencies for the backend server
RUN cd server && npm install

# Use later when finished:
# Omit the dev dependencies with npm clean install
# RUN cd server && npm ci --omit=dev

# Copy the backend source code
COPY server ./server

# Copy the static frontend files into the backend's public directory to display at localhost:3000
RUN mkdir -p server/public && \
    cp -r frontend/out/* server/public/

# Copy the Java processor JAR file
COPY processor/videoprocessor.jar ./processor/videoprocessor.jar

# Expose port 3000 for the Express backend server
EXPOSE 3000

# Start the backend server
CMD ["node", "server/index.js"]