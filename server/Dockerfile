# server/Dockerfile

FROM openjdk:17-slim

# Install ffmpeg and Node.js 20
RUN apt-get update && \
    apt-get install -y --no-install-recommends curl ffmpeg gnupg ca-certificates && \
    curl -fsSL https://deb.nodesource.com/setup_20.x | bash - && \
    apt-get install -y --no-install-recommends nodejs && \
    apt-get clean && rm -rf /var/lib/apt/lists/*

WORKDIR /app

# Install dependencies
COPY package*.json ./
RUN npm install

# Copy server source
COPY . .

# ✅ Copy pre-built JAR into image
COPY processor.jar /processor/target/salamander-jar-with-dependencies.jar

EXPOSE 3001
CMD ["node", "index.js"]
