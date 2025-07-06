# 📸 Centroid Finder — End-to-End Salamander Tracking System

Centroid Finder is a full-stack application built to process video and image files by detecting color-based centroids — ideal for lab experiments like salamander pheromone preference tracking. It combines a powerful Java backend for image/video analysis, a Next.js frontend for interaction and control, and an Express API to manage processing jobs.

---

## 🧩 Project Structure

| Layer        | Description |
|--------------|-------------|
| **Frontend** | React + Next.js interface for selecting videos, adjusting color threshold, previewing results, and downloading CSVs |
| **Backend**  | Express.js API that manages processing jobs and interacts with the Java engine |
| **Processor**| Java-based binary image processor that detects pixel clusters and outputs centroid data |
| **Docker**   | Containerizes the system for reproducible deployments |
| **Testing**  | End-to-end testing powered by Cypress and JUnit |

---

## 🖼️ Key Features

### ✅ Interactive Frontend
- Video chooser and thumbnail preview
- Live binarization with threshold & color picker
- Process button triggers full backend job
- Displays job status and provides downloadable results
- Built with Material UI and tested via Cypress

### 🧠 Java Backend Processor
- Converts image or video frame to binary based on target color
- Identifies and groups white pixel regions using DFS
- Outputs `binarized.png` and `groups.csv` with centroid data
- JavaCV used for video frame extraction

### 🔁 Node.js Express Server
- REST API for job submission and tracking
- Passes job details to Java JAR processor
- Serves result files and job status via endpoints

### 📦 Docker Deployment
- Runs Java + Node in a unified container
- Volumes handle input/output for media and result files
- ENV variables used for path config

---

## 🛠️ Running the Project

- Make sure to have Docker installed on your machine
- Run: 
```bash
docker pull ghcr.io/jameson789/salamander:latest 
docker run \
  -p 3000:3000 \
  -p 3001:3001 \
  -v localVideoPath:/videos \
  -v localResultsPath:/results \
  ghcr.io/jameson789/salamander:latest 
  ```
  - You can use any ports you want as long as they are different from one another and the ports after the : stay at 3000 and 3001