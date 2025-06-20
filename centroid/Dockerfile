# centroid/Dockerfile
FROM node:18-bullseye

RUN apt-get update \
 && apt-get install -y openjdk-17-jdk ffmpeg \
 && apt-get clean

# Bring in your Java processor (the JAR gets built into processor/target)
COPY processor /app/processor

# Install & bundle your Express server
WORKDIR /app/server
COPY server/package*.json ./
RUN npm install
COPY server ./

EXPOSE 3001
CMD ["node","index.js"]