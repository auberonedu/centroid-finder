package io.github.oakes777.salamander;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import javax.imageio.ImageIO;

/**
 * VideoProcessorMain is the command-line entry point for processing .mp4 videos
 * to track centroids.
 *
 * This class allows the user to:
 * 1. Load an input .mp4 video file.
 * 2. Preview the first video frame after applying a user-selected target color
 * and threshold.
 * 3. Save a binarized preview of the frame as "preview.png" for visual
 * validation.
 * 4. Re-enter new color/threshold settings interactively until satisfied.
 * 5. Once confirmed, process all frames of the video using the chosen settings.
 * 6. Output a CSV file with time-stamped (x, y) centroid coordinates for the
 * largest detected group in each frame.
 *
 * This class does not replace any original centroid-finder components.
 * Instead, it adds interactive front-end control logic to configure and
 * validate processing settings.
 *
 * Command-line Usage:
 * java -jar videoprocessor.jar <videoPath>
 *
 * Required Arguments:
 * videoPath - path to the input .mp4 video file (e.g.,
 * "sampleInput/ensantina.mp4")
 *
 * Author: Jonathan, Zach, Stephen
 * Version: 2.0 (Project 2 - Video Integration)
 */
public class VideoProcessorMain {
    public static void main(String[] args) {
        // Ensure that the user provides a video file path
        if (args.length < 1) {
            System.out.println("Usage: java -jar videoprocessor.jar <videoPath>");
            return;
        }

        // The path to the input .mp4 video file
        String videoPath = args[0];

        // Scanner allows us to take user input from the command line
        Scanner scanner = new Scanner(System.in);

        // Placeholder for the first frame of the video, for preview/testing
        BufferedImage firstFrame = null;

        // Use try-with-resources to automatically close the video extractor
        try (VideoFrameExtractor extractor = new VideoFrameExtractor(videoPath)) {
            // Grab the first frame from the video for preview purposes
            VideoFrame first = extractor.iterator().next();
            firstFrame = first.image(); // Get the raw image (BufferedImage)
        } catch (Exception e) {
            // If there's an error (e.g., invalid file), print the stack trace and exit
            System.err.println("Error extracting first frame:");
            e.printStackTrace();
            return;
        }

        // Begin a loop that lets the user keep testing different settings
        while (true) {
            // Ask user for a hex target color (e.g., FF0000 for red)
            System.out.print("Enter target color in RRGGBB hex (e.g., FF0000 for red): ");
            String hexColor = scanner.nextLine().trim();

            // Ask for a threshold value (typically 10–100 is reasonable)
            System.out.print("Enter threshold (suggested range 10–100): ");
            String thresholdInput = scanner.nextLine().trim();

            int targetColor;
            int threshold;

            // Try to parse the input into integers; retry if input is invalid
            try {
                targetColor = Integer.parseInt(hexColor, 16); // Parse RRGGBB as hex
                threshold = Integer.parseInt(thresholdInput); // Parse threshold as integer
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please try again.");
                continue;
            }

            // Create a FrameAnalyzer with the user's target color and threshold
            FrameAnalyzer analyzer = new FrameAnalyzer(targetColor, threshold);

            // Use the analyzer to binarize the first frame only (for preview)
            BufferedImage preview = analyzer.binarizeOnly(firstFrame);

            // Save the binarized preview image as a PNG file
            try {
                File outFile = new File("preview.png");
                ImageIO.write(preview, "png", outFile); // Write image to disk
                System.out.println("Preview saved as preview.png");
            } catch (IOException e) {
                System.err.println("Error saving preview:");
                e.printStackTrace();
            }

            // Ask the user whether they are satisfied with the current settings
            System.out.print("Are you satisfied with the preview? (yes/no): ");
            String confirm = scanner.nextLine().trim().toLowerCase();

            // If the user is happy with the preview, proceed to full video processing
            if (confirm.equals("yes")) {
                // Ask user where to save the final output CSV
                System.out.print("Enter output CSV filename (e.g., output.csv): ");
                String csvPath = scanner.nextLine().trim();

                // Run the full centroid-tracking analysis
                // try {
                // // Create the tracker with the user-provided color and threshold
                // VideoCentroidTracker tracker = new VideoCentroidTracker(targetColor,
                // threshold);

                // // Start processing the full video
                // tracker.trackCentroids(videoPath, csvPath);

                // System.out.println("Video processed. CSV saved to " + csvPath);
                // } catch (Exception e) {
                // System.err.println("Error processing video:");
                // e.printStackTrace();
                // }

                // Ask for frame interval (e.g., 1 = every frame, 2 = half, ..., 24 = 1/sec)
                System.out.print("Enter frame interval (1 = every frame, up to 24 = one per second): ");
                String intervalInput = scanner.nextLine().trim();

                int frameInterval;
                try {
                    frameInterval = Integer.parseInt(intervalInput);
                    if (frameInterval < 1 || frameInterval > 24) {
                        throw new IllegalArgumentException("Frame interval must be between 1 and 24.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid interval. Defaulting to 1 (every frame).");
                    frameInterval = 1;
                }

                // Run the full centroid-tracking analysis
                try {
                    VideoCentroidTracker tracker = new VideoCentroidTracker(targetColor, threshold);
                    tracker.trackCentroids(videoPath, csvPath, frameInterval); // 🆕 pass interval
                    System.out.println("Video processed. CSV saved to " + csvPath);
                } catch (Exception e) {
                    System.err.println("Error processing video:");
                    e.printStackTrace();
                }

                // Once processing is done, break out of the loop
                break;
            } else {
                // Let user retry with new color/threshold
                System.out.println("Let’s try new settings.\n");
            }
        }

        // Close the scanner resource
        scanner.close();
    }
}
