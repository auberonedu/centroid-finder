package io.github.f3liz.centroidFinder;

import org.bytedeco.javacv.CanvasFrame;
import org.bytedeco.javacv.OpenCVFrameConverter;
import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.opencv_videoio.VideoCapture;

import javax.swing.*;

public class VideoExperiment {
    public static void main(String[] args) {
        // Path to your test video file (relative to project root)
        String videoPath = "sampleInput/sample-10s.mp4";

        // Open video file
        VideoCapture capture = new VideoCapture(videoPath);
        // Check if it can open, if not exit program
        if (!capture.isOpened()) {
            System.out.println("Error: Cannot open video file.");
            return;
        }

        // OpenCV Mat object
        Mat frame = new Mat();

        // counter for frames processed
        int frameCount = 0;

        // Create CanvasFrame for displaying video
        CanvasFrame canvas = new CanvasFrame("Video Frame", CanvasFrame.getDefaultGamma() / 2.2);

        // closes java program if video window is closed
        canvas.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // convert Mat frames to Frame objects
        OpenCVFrameConverter.ToMat converter = new OpenCVFrameConverter.ToMat();

        // to process the frames
        try {
            while (capture.read(frame)) {
                if (frame.empty()) break;
                frameCount++;

                // Show frame
                canvas.showImage(converter.convert(frame));

                // Exit if window is closed
                if (!canvas.isVisible()) break;

                // Delay for ~30 FPS
                Thread.sleep(30);

                System.out.println("Frame " + frameCount + " processed.");
            }
        } catch (InterruptedException e) {
            System.err.println("Interrupted: " + e.getMessage());
        } finally {
            // Clean up
            capture.release();
            canvas.dispose();
            System.out.println("Processed " + frameCount + " frames.");
        }
    }
}
// metadata and extracting frame data can be done using .get() from the VideoCapture class
// ex: capture.get(org.bytedeco.opencv.global.opencv_videoio.CAP_PROP_FRAME_WIDTH) for width of the frames