package io.github.oakes777.salamander;

import java.awt.image.BufferedImage;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import org.jcodec.api.JCodecException;

/**
 * The VideoCentroidTracker class processes an entire MP4 video file frame by
 * frame.
 * 
 * For each frame, it uses the existing centroid-finder logic to:
 * - Binarize the image using a target RGB color and threshold
 * - Find all groups of connected white pixels
 * - Identify the largest group and extract its centroid
 * - Record the timestamp and (x, y) centroid coordinates to a CSV
 * 
 * If no group is found in a frame, (-1, -1) is recorded for the coordinates.
 *
 * This class does not replace or modify any original components. It augments
 * the original project by modularly combining:
 *
 * VideoFrameExtractor-loads individual frames and timestamps from a video.
 * FrameAnalyzer-analyzes a single frame using existing binarizer and group
 * finder logic.
 * Group-reused directly to extract centroid coordinates.
 * 
 * Output is saved in CSV format: one row per frame, in the form:
 * timestamp_in_seconds,x_coordinate,y_coordinate
 *
 * This is the core logic engine for video-based centroid tracking in Project 2.
 *
 * @author Jonathan, Zach, Stephen
 * @version 2.0
 */
public class VideoCentroidTracker {

    private final FrameAnalyzer analyzer;

    /**
     * Constructs a VideoCentroidTracker with a specific color thresholding
     * strategy.
     *
     * @param targetColor RGB color to match (in 0xRRGGBB format)
     * @param threshold   max Euclidean color distance for inclusion as 'white'
     */
    public VideoCentroidTracker(int targetColor, int threshold) {
        this.analyzer = new FrameAnalyzer(targetColor, threshold);
    }

    /**
     * Processes an input video file and writes centroid tracking data to a CSV.
     *
     * @param videoPath path to the input .mp4 video file
     * @param outputCsv path to the output CSV file to create
     * @throws IOException     if reading or writing files fails
     * @throws JCodecException if the video decoding fails
     */
    public void trackCentroids(String videoPath, String outputCsv) throws IOException, JCodecException {
        try (
                VideoFrameExtractor extractor = new VideoFrameExtractor(videoPath); // AutoCloseable
                PrintWriter writer = new PrintWriter(new FileWriter(outputCsv))) {
            for (VideoFrame frame : extractor) {
                BufferedImage image = frame.image();
                double timestamp = frame.timestampSeconds();

                Group largestGroup = analyzer.findLargestGroup(image);



                int x = -1;
                int y = -1;

                if (largestGroup != null) {
                    x = largestGroup.centroid().x();
                    y = largestGroup.centroid().y();
                }



                // Write row: timestamp,x,y
                writer.printf("%.3f,%d,%d%n", timestamp, x, y);
            }

            System.out.println("✅ Centroid CSV written to " + outputCsv);
        }
    }

    /**
     * Same as above, but only processes every Nth frame.
     *
     * @param frameInterval how often to process frames.
     * (1 = every frame, 2 = every other, etc.)
     * range 1-24; at 24fps 24 would give one frame/second.
     * 
     */
    public void trackCentroids(String videoPath, String outputCsv, int frameInterval)
            throws IOException, JCodecException {
        try (
            VideoFrameExtractor extractor = new VideoFrameExtractor(videoPath);
            PrintWriter writer = new PrintWriter(new FileWriter(outputCsv))) {
       
       
                int frameIndex = 0;
        for (VideoFrame frame : extractor) {
            if (frameIndex % frameInterval == 0) {
                BufferedImage image = frame.image();
                double timestamp = frame.timestampSeconds();

                Group largestGroup = analyzer.findLargestGroup(image);

                if (largestGroup == null) {
                    System.out.println("Frame " + frameIndex + ": No groups found");
                } else {
                    System.out.println("Frame " + frameIndex + ": Group found with centroid at (" +
                        largestGroup.centroid().x() + ", " + largestGroup.centroid().y() + ")");
                }

                int x = -1, y = -1;
                if (largestGroup != null) {
                    x = largestGroup.centroid().x();
                    y = largestGroup.centroid().y();
                }
                writer.printf("%.3f,%d,%d%n", timestamp, x, y);
            }
            frameIndex++;
        }

            System.out.println("Centroid CSV written to " + outputCsv);
        }
    }
}
