# 🐳 Docker Plan for Centroid Finder Backend

This `Dockerfile` sets up a lightweight Docker container to run a Node.js + Java-based video processing backend. The container installs Java and `ffmpeg`, installs dependencies for a Node.js Express server, and prepares a JAR file for processing video input.

---

## 🧱 Base Image

```dockerfile
FROM node:20-slim
```

- Uses the official Node.js v20 slim image as a base.
- Chosen for small size and compatibility with Debian (which is needed for apt tools).

---

## 📦 Install System Dependencies

```dockerfile
RUN apt-get update && apt-get install -y \
    wget \
    gnupg \
    ffmpeg \
    && apt-get clean
```

- `wget` – used to download files (like GPG keys).
- `gnupg` – for verifying GPG signatures.
- `ffmpeg` – required for processing video frames.
- `apt-get clean` – cleans up the cache to reduce image size.

---

## 🔐 Install Java (Temurin 21 JDK)

```dockerfile
RUN mkdir -p /etc/apt/keyrings && \
    wget -O- https://packages.adoptium.net/artifactory/api/gpg/key/public | gpg --dearmor > /etc/apt/keyrings/adoptium.gpg && \
    echo "deb [signed-by=/etc/apt/keyrings/adoptium.gpg] https://packages.adoptium.net/artifactory/deb bullseye main" > /etc/apt/sources.list.d/adoptium.list && \
    apt-get update && \
    apt-get install -y temurin-21-jdk && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*
```

- Adds Eclipse Adoptium repository to install Java 21.
- Uses GPG verification for secure package installation.
- Cleans up to keep image small.

---

## 📁 Set Working Directory

```dockerfile
WORKDIR /app/server
```

- All subsequent paths will be relative to this directory.
- Keeps the app structure clean and scoped to `/app`.

---

## 📦 Install Node Dependencies

```dockerfile
COPY server/package*.json ./
RUN npm install
```

- Copies only package files first to leverage Docker layer caching.
- Installs Express backend dependencies.

---

## 📂 Copy Application Code

```dockerfile
COPY server/ ./
```

- Brings in all server-side application code.

---

## ☕ Add Java Processor JAR

```dockerfile
COPY processor/target/centroid-finder-1.0-SNAPSHOT-jar-with-dependencies.jar /app/processor/
```

- Adds the compiled Java JAR to a known directory for execution by the Node server.

---

## 🌐 Expose Port

```dockerfile
EXPOSE 3000
```

- Opens port `3000` for external access to the backend server.

---

## ⚙️ Environment Variables

```dockerfile
ENV VIDEO_DIR=/videos
ENV RESULTS_DIR=/results
ENV JAVA_JAR_PATH=/app/processor/centroid-finder-1.0-SNAPSHOT-jar-with-dependencies.jar
```

- Allows flexible runtime configuration of input/output directories and the JAR path.

---

## 🚀 Start Server

```dockerfile
CMD ["node", "server.js"]
```

- Launches the Node.js Express backend.


