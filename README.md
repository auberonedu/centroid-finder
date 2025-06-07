# Salamander Centroid Tracker

Welcome to the Salamander Centroid Finder, a full-stack video processing application built for scientists studying salamander chemical communication behavior. This tool allows you to:

- Upload and select salamander videos
- Click to choose the animal's color
- Preview and process video frames to identify centroids
- Download the resulting CSV data for further analysis

---

## Project Structure

- **client/** — React + Next.js front end
- **server/** — Express.js backend API
- **processor/** — Java JAR for image processing
- **output/** — Where final CSVs are stored
- **thumbnails/** — For video list previews

---

## Requirements

- [Docker](https://www.docker.com/products/docker-desktop/) installed
- Git clone of this repository

---

## Getting Started

1. **Clone the repo**

```bash
git clone https://github.com/oakes777/centroid-finder.git
cd centroid-finder
```

2. **Place your videos** inside a folder on your laptop. For example:

```bash
/path/to/my_salamander_videos/
```

3. **Update Docker Compose to point to your local video directory**:

Open `docker-compose.yml` and update this section:

```yaml
    volumes:
      - /absolute/path/to/my_salamander_videos:/app/../processor/sampleInput
```

4. **Start the app**

```bash
docker compose up --build
```

Once running, visit [http://localhost:3000/videos](http://localhost:3000/videos)

---

## How to Use the App

### Page 1: **Video Chooser**
- Route: `/videos`
- Lists available videos (MP4, MOV, AVI) from your local folder
- Each video shows:
  - Duration
  - Thumbnail preview
- Click on a video to begin processing

### Page 2: **Preview & Parameter Selection**
- Route: `/preview?filename=...`
- Features:
  - Static first frame of video
  - Click the frame to select target salamander color
  - Slider to adjust threshold sensitivity
  - Interval buttons (e.g., process every 10s)
  - Real-time binarized preview updates below
- Click **"Process Video With These Settings"** to begin

### Page 3: **Results & Download**
- Route: `/results/[jobId]`
- Monitors background job progress
- Displays:
  - Progress bar
  - Output logs
  - Error logs (if any)
  - Final CSV download link once complete

---

## Where are my results?

Once finished, your CSV is saved to:
```bash
/output/[jobId].csv
```
Which is mounted on your host machine based on this Docker volume:
```yaml
  - ./server/output:/app/output
```
You can modify this to point to a different local folder.

---

## Troubleshooting

- **Video not showing up?**
  - Ensure it’s `.mp4`, `.mov`, or `.avi`
  - Check the volume path is correct and absolute

- **JAR not found?**
  - Confirm `JAR_PATH` is set correctly in `.env`
  - Confirm the path is mounted in Docker

- **Live preview not updating?**
  - Make sure color was selected from the image
  - Try adjusting the threshold slider

- **Docker restart advice**
  - Always use `docker compose down` before re-running

---

## Credits
Built by Zachary Springer, Stephen Franada, and Jonathan Sule for salamander scientists, enthusiasts, and behavioral researchers.

---

## License
See License file in code main folder

---

## Cleanup & Future Features
- Better error handling in UI
- Heatmap visualizations
- Metadata tagging by experiment/session

---

> "Sometimes the smallest creatures lead to the biggest discoveries."

---
