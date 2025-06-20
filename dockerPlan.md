
# 🦎 Salamander Tracker – Centroid Finder Backend

This project is a full-featured backend for processing salamander videos using color thresholding and centroid detection. It includes a Java-based video processor, an Express.js server, and Docker integration for seamless deployment.

---

## 📦 Project Structure

```bash
.
├── centroid/processor/           # Java video processor
├── centroid/server/              # Node.js + Express backend
├── sampleInput/                  # Input video mount point
├── sampleOutput/                 # Output CSV & thumbnails
├── Dockerfile                    # Docker image definition
├── .env                          # Environment variables
└── dockerPlan.md                 # Docker setup explanation
```

---

## 🚀 Backend Features

- Process videos frame-by-frame and extract centroid data
- Generate thumbnails with `ffmpeg`
- Java JAR integration using `child_process`
- REST API:
  - `GET /api/videos`
  - `GET /api/videos/thumbnail/:filename`
  - `GET /api/videos/preview/:filename`
  - `POST /api/process/:filename`
  - `GET /api/process/:jobId/status`

---

## 🧱 Docker Plan for Salamander Tracker Backend

### Base Image

We will use:

```
node:18-jdk-slim
```

This includes Node.js and OpenJDK (Java) in a small Debian image—ideal for running both the Express server and the Java JAR processor.

---

### Requirements Recap

| Requirement                    | Planned Solution                                   |
|-------------------------------|----------------------------------------------------|
| Run Node.js backend           | Use Node.js in base image                         |
| Run Java JAR file             | Use OpenJDK 17 (included in image)                |
| Run ffmpeg for thumbnails     | Install via `apt-get`                             |
| Serve on port 3001            | `EXPOSE 3001` in Dockerfile                       |
| Mount video/results folders   | Docker volumes (`/videos`, `/results`)            |
| Use environment variables     | `.env` and `ENV` in Dockerfile                    |
| Keep image size small         | Use `slim` variant and clean cache after install  |
| Ignore unneeded files         | Use `.dockerignore`                               |

---

### 🗂 Directory Volumes

| Host Path         | Container Path | Purpose                      |
|------------------|----------------|------------------------------|
| `sampleInput`     | `/videos`      | Input MP4 videos             |
| `sampleOutput`    | `/results`     | Output CSVs, thumbnails, etc |

---

### 🌍 Environment Variables

In `.env` and also configured in `Dockerfile`:

```env
VIDEO_DIR=/videos
RESULTS_DIR=/results
JAR_PATH=/app/centroid/processor/target/centroid-1.0-SNAPSHOT-jar-with-dependencies.jar
PORT=3001
```

---

### 🔧 Build Strategy

1. Start with `node:18-jdk-slim`
2. Install `ffmpeg` via `apt-get`
3. Set working directory: `/app`
4. Copy `package.json` and run `npm install`
5. Copy the full project contents
6. Set `ENV` variables
7. `EXPOSE 3001`
8. `CMD ["node", "centroid/server/index.js"]`

---

### 🏗 Docker Build & Run Commands

Build the image:

```bash
docker build -t salamander-backend .
```

Run the image:

```bash
docker run -it \
  -p 3001:3001 \
  -v "$(pwd)/centroid/processor/sampleInput:/videos" \
  -v "$(pwd)/sampleOutput:/results" \
  salamander-backend
```

Or run the GHCR-published version:

```bash
docker run \
  -p 3001:3001 \
  -v "$VIDEO_DIRECTORY:/videos" \
  -v "$RESULTS_DIRECTORY:/results" \
  ghcr.io/alstondsouza1/salamander:latest
```

---

## 🧪 Testing

- Use [Postman](https://www.postman.com/) or a frontend to hit API endpoints.
- Ensure `.env` and volumes are properly configured.
- You can view logs from `console.log` and `child.stderr` for debugging Java processes.

---

## 📸 Architecture Diagram

> See `Docker Architecture.jpeg` or `ArchitecturePlan.jpg` for full request-flow overview.

---