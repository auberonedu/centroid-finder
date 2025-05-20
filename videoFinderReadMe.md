## 🗃️ Code Breakdown

### `VideoProcessor.java`

Main driver class that:

1. Loads OpenCV.
2. Parses command-line arguments.
3. Opens and reads video using OpenCV.
4. Extracts 1 frame per second.
5. Uses your centroid finder logic to find the largest matching group.
6. Outputs the `(x, y)` of the centroid or `(-1, -1)` if none found.

**Key methods & functionality:**

* `OpenCVLoader loader = new OpenCVLoader();`

  * Loads native OpenCV support.

* `VideoCapture video = new VideoCapture(inputPath);`

  * Opens the video file for reading.

* `List<BufferedImage> frames = analyzer.processVideo(video, 24);`

  * Extracts 1 frame per second assuming video is \~24fps.

* For each frame:

  * `groupFinder.findConnectedGroups(frame);`
  * Determines regions matching the target color.
  * Chooses the largest region.
  * If found, writes its centroid as `(x, y)`.
  * If not, writes `(-1, -1)`.

* The time is written as `mm:ss` using `String.format("%02d:%02d", second / 60, second % 60);`

### `VideoAnalyzer.java`

* Takes a video and frame interval.
* Converts `Mat` objects to `BufferedImage` objects.
* Collects every Nth frame (in our case, every 24th frame).

### `OpenCVUtils.java`

* Converts OpenCV `Mat` objects into `BufferedImage`.
* Uses JPEG encoding for compatibility.

### Binarizer & Group Finder (from existing logic)

* `DistanceImageBinarizer` checks if each pixel is within threshold color distance.
* `DfsBinaryGroupFinder` finds connected regions.
* `Group` object gives access to `.centroid()` and `.size()`.

---

## 📦 Output

The final output is a CSV file with:

```csv
time,x,y
00:00,(221,420)
00:01,(215,437)
...
```

This can be used for tracking movement or highlighting color-based object presence per second.

---

## 🧠 Summary



* Analyze any video.
* Track color presence.
* Generate real data in CSV format.


