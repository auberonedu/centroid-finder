# server/Dockerfile

FROM openjdk:17-slim

# Install Node.js 20
RUN apt-get update && \
    apt-get install -y curl && \
    curl -fsSL https://deb.nodesource.com/setup_20.x | bash - && \
    apt-get install -y nodejs && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*

WORKDIR /app

# Copy server files
COPY server/package*.json ./
RUN npm install
COPY server/. .

# Copy the JAR to the expected location inside the container
RUN mkdir -p /processor/target
COPY processor/target/salamander-jar-with-dependencies.jar /processor/target/

EXPOSE 3001

CMD ["node", "index.js"]
