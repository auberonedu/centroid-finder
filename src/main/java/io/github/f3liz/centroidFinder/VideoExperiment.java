package io.github.f3liz.centroidFinder;

import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.opencv_videoio.VideoCapture;

import static org.bytedeco.opencv.global.opencv_highgui.*;

public class VideoExperiment {
    public static void main(String[] args) {
        // Path to your test video file (relative to project root)
        String videoPath = "sampleInput/sample-10s.mp4";

        // Create a VideoCapture object to open the video file
        VideoCapture capture = new VideoCapture(videoPath);

        // Check if the video file was successfully opened
        if (!capture.isOpened()) {
            System.out.println("Error: Cannot open video file.");
            return;
        }

        // Create a Mat object to hold each frame
        Mat frame = new Mat();
        int frameCount = 0;

        // Loop through each frame of the video
        while (capture.read(frame)) {
            frameCount++;

            // Display the current frame in a window titled "Video Frame"
            // imshow("Video Frame", frame); // Doesn't work currently

            // Wait 30 milliseconds between frames (approx. 30 FPS)
            // If a key is pressed, break out of the loop
            if (waitKey(30) >= 0) break;

            // Print progress to the console
            System.out.println("Frame " + frameCount + " processed.");
        }

        // Release the video file and close OpenCV windows
        capture.release();
        // destroyAllWindows(); // Doesn't work currently

        System.out.println("Processed " + frameCount + " frames.");
    }
}
