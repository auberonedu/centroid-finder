package io.github.qopci.centroidFinder;

import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.OpenCVFrameConverter;
import org.bytedeco.opencv.opencv_core.Mat;
import static org.bytedeco.opencv.global.opencv_imgcodecs.imwrite;

public class VideoExperiment {

    public static void main(String[] args) {
        String videoPath = "chiikawa.mp4"; // Make sure this file is in your project root

        try (FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(videoPath)) {
            grabber.start();

            // Print basic metadata
            System.out.println("Video Info:");
            System.out.println("Width: " + grabber.getImageWidth());
            System.out.println("Height: " + grabber.getImageHeight());
            System.out.println("Frame Rate: " + grabber.getFrameRate());
            System.out.println("Total Frames: " + grabber.getLengthInFrames());

            // Grab the first image frame
            Frame frame = null;
            while ((frame = grabber.grabImage()) == null) {
                // Skip any non-image frames (e.g., audio)
            }

            if (frame != null) {
                OpenCVFrameConverter.ToMat converter = new OpenCVFrameConverter.ToMat();
                Mat mat = converter.convert(frame);
                if (mat != null) {
                    imwrite("first_frame.jpg", mat); // Saves frame as image
                    System.out.println("Saved first frame as 'first_frame.jpg'");
                } else {
                    System.out.println("Could not convert frame to Mat.");
                }
            } else {
                System.out.println("No image frame found in video.");
            }

            grabber.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
