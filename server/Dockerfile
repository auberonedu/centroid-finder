# Use an image with OpenJDK 17 and install Node.js 20
FROM openjdk:17-slim

# Install Node.js 20
RUN apt-get update && \
    apt-get install -y curl && \
    curl -fsSL https://deb.nodesource.com/setup_20.x | bash - && \
    apt-get install -y nodejs && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*

# Set working directory
WORKDIR /app

# Copy package files and install dependencies
COPY package*.json ./
RUN npm install

# Copy the JAR file BEFORE other files to prevent overwrite
COPY ../processor/target/salamander-jar-with-dependencies.jar ./processor/target/salamander-jar-with-dependencies.jar

# Copy the rest of the server code
COPY . .

# Expose the backend port
EXPOSE 3001

# Start the server
CMD ["node", "index.js"]
