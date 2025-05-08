package io.github.f3liz.centroidFinder;

import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.opencv_videoio.VideoCapture;
import static org.bytedeco.opencv.global.opencv_imgcodecs.*;
import static org.bytedeco.opencv.global.opencv_highgui.*;

public class VideoExperiment {
    public static void main(String[] args) {
        String videoPath = "path/to/your/video.mp4";  // replace with your file
        VideoCapture capture = new VideoCapture(videoPath);

        if (!capture.isOpened()) {
            System.out.println("Error: Cannot open video file.");
            return;
        }

        Mat frame = new Mat();
        int frameCount = 0;

        while (capture.read(frame)) {
            frameCount++;
            imshow("Video Frame", frame); // show the frame in a window
            if (waitKey(30) >= 0) break; // wait 30ms or exit on key press
        }

        capture.release();
        destroyAllWindows();
        System.out.println("Processed " + frameCount + " frames.");
    }
}

