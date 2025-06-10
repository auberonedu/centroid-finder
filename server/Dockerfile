# server/Dockerfile

FROM openjdk:17-slim

# Install ffmpeg (includes ffprobe) and Node.js 20
RUN apt-get update && \
    apt-get install -y --no-install-recommends curl ffmpeg gnupg ca-certificates && \
    curl -fsSL https://deb.nodesource.com/setup_20.x | bash - && \
    apt-get install -y --no-install-recommends nodejs && \
    apt-get clean && rm -rf /var/lib/apt/lists/*

# Set working directory
WORKDIR /app

# Copy and install server dependencies first (leverages Docker cache)
COPY package*.json ./
RUN npm install

# Copy all server source code
COPY . .

# Copy the Java JAR to the correct location
RUN mkdir -p /processor/target

# 🔧 Copy the Java JAR explicitly into the image
COPY processor/target/salamander-jar-with-dependencies.jar /processor/target/


# Expose backend port
EXPOSE 3001

# Start the Express server
CMD ["node", "index.js"]
