# Project Plan: Video Centroid Tracker

## Goal

We are building a program that finds and tracks the largest colored object (centroid) in a video. It will look at each frame, find the biggest group of pixels matching a target color, and write their (x, y) location to a CSV file, one line per second of video.

If nothing is found in a frame, it writes `-1, -1` for that time.

---

### 1. `VideoProcessorApp` (Main class)

**What it does:**
- Starts the whole program
- Reads command line arguments
- Runs the processing steps

---

### 2. `CommandLineParser`

**What it does:**
- Reads and checks the arguments from the user:
  - JavaCV library
  - Output CSV file
  - Target color (like "255,0,0")
  - Threshold for how close colors should be

---

### 3. `VideoReader`

**What it does:**
- Opens the video
- Reads it one frame at a time
- Keeps track of how many seconds into the video we are

---

### 4. `FrameProcessor`

**What it does:**
- Takes a single image frame
- Uses existing code to:
  - Turn it into black and white (based on target color)
  - Find groups of matching pixels
  - Find the biggest group and get its center (centroid)
- Returns the (x, y) coordinate or (-1, -1) if nothing found

---

### 5. `CsvWriter`

**What it does:**
- Writes each frame’s result to a CSV file
- One line for each second

### Test with mp4 video.
- Used Command-line: `java -jar videoprocessor.jar inputPath outputCsv targetColor threshold`
- Ran Maven command to run the project. 
