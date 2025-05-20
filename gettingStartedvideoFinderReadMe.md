Got it! Here's a **high-quality, partner-ready `README.md`** formatted for someone who:

* Already has the project repo cloned or will `git pull` it.
* Needs to set up OpenCV on their machine.
* Will build and run the video processor via command line.
* Needs context on what the project does and how to use it.

---

````markdown
# 🎯 Centroid Video Tracker

This Java project processes `.mp4` videos to detect the **largest centroid** per second and records the results into a CSV file. The output contains timestamps (`mm:ss`) and centroid `(x,y)` coordinates. This feature is useful for object tracking and motion analysis in video footage.

## 📁 What You Should Already Have

- A cloned copy of the repository (or run `git pull` to sync latest changes).
- Java 11+ installed.
- Maven installed (`mvn -v` to check).
- The `.mp4` video file placed inside the project root or a relative path (e.g., `ensantina.mp4`).

---

## 🧱 Step 1: Install OpenCV Locally

> ⚠️ This project depends on OpenCV's native Java bindings.

1. Download OpenCV 4.x from the official site:
   https://opencv.org/releases/

2. Unzip it to any location (e.g., `C:\opencv` or `/usr/local/opencv`).

3. Find the native library `.dll` or `.so` files:
   - Windows: `opencv/build/java/x64/opencv_java460.dll`
   - macOS/Linux: `opencv/build/lib/libopencv_java460.so`

4. Add this native path to your VM options or set as a runtime system property.

> ✅ We auto-load the native library using the custom `OpenCVLoader` class (already in the project).

---

## 🧱 Step 2: Build the Project and Generate JAR

1. In terminal, navigate to the project root.

2. Run:

   ```bash
   mvn clean package
````

3. This will generate a JAR file at:

   ```
   target/centroidfinder-1.0-SNAPSHOT.jar
   ```

   Or, if you want to create a shaded (fat) JAR with dependencies, run:

   ```bash
   mvn clean package shade:shade
   ```

---

## 🚀 Step 3: Run the Video Processor

From the terminal in the project root, rud:

```bash
java -jar target/centroidfinder-1.0-SNAPSHOT.jar <inputVideo.mp4> <output.csv> <targetColor> <threshold>
```

Example:

```bash
java -jar target/centroidfinder-1.0-SNAPSHOT.jar ensantina.mp4 videoResults.csv 5d0213 60
```

### 🔹 Arguments:

* `inputVideo.mp4`: relative path to your video file.
* `videoResults.csv`: desired CSV output filename.
* `targetColor`: 6-digit hex color (e.g., `FFA200`).
* `threshold`: integer threshold (e.g., `164`) for color similarity.

---

## 🧪 Output Format

The resulting CSV will look like:

```
time,x,y
00:00,(221,420)
00:01,(215,437)
...
```

* `time`: minute\:second timestamp in the video.
* `(x,y)`: pixel coordinates of the largest centroid.
* If no centroid found at a second, the output is: `mm:ss,-1,-1`.

---

## 🧭 Coordinate System Reference

* The top-left corner of the frame is `(0, 0)`.
* `x` increases → to the right.
* `y` increases ↓ downward.

---

## ❗ Common Gotchas

* Make sure the `.mp4` file is in the **correct relative path**.
* If you get a `NoClassDefFoundError`, check that:

  * You built the correct `.jar` file.
  * The OpenCV native library is accessible at runtime.
* Always clean & repackage if dependencies or logic changes:

  ```bash
  mvn clean package
  ```

---

## 👥 Who Needs To Do This

If you're pulling the latest version of the project:

1. `git pull`
2. Follow setup from Step 1 (OpenCV) and Step 2 (build).
3. Run the command as shown in Step 3.

You're ready to analyze videos!

```

---


