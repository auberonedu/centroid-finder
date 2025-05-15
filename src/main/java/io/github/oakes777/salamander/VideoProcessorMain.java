package io.github.oakes777.salamander;

// Import required classes for working with images, file I/O, CLI input, and JCodec
import org.jcodec.api.JCodecException;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import javax.imageio.ImageIO;

/**
 * VideoProcessorMain is the command-line entry point to run the full video processing pipeline.
 * 
 * This controller replaces the original ImageSummaryApp as the main entry point.
 * It modularly wires together these components:
 *   - VideoFrameExtractor: loads video frames from an .mp4 file
 *   - FrameAnalyzer: analyzes each BufferedImage for white pixel groups
 *   - VideoCentroidTracker: loops over the full video and outputs CSV data
 * 
 * Key Features:
 *   1. Parses user CLI input: video path, output CSV, hex color, and threshold.
 *   2. Generates a preview of frame 0 after binarization to visually verify that the target color and threshold are reasonable.
 *   3. Pauses and prompts the user for confirmation before processing the full video.
 *   4. If confirmed, runs the full centroid tracking logic and generates the output .csv file.
 * 
 * Usage:
 *   java -jar videoprocessor.jar inputVideo.mp4 output.csv FF8800 35
 * 
 * Arguments:
 *   inputVideo.mp4    - path to .mp4 video file
 *   output.csv        - path to output CSV file
 *   FF8800            - target color (hex string with no "#" symbol)
 *   35                - threshold (integer from ~20–60)
 */
public class VideoProcessorMain {
    public static void main(String[] args) {
        // Step 1: Validate the number of command-line arguments
        if (args.length < 4) {
            System.out.println("Usage: java -jar videoprocessor.jar <videoPath> <outputCsv> <hexTargetColor> <threshold>");
            return; // Exit if not enough arguments were provided
        }

        // Step 2: Extract each CLI argument into variables
        String videoPath = args[0];       // Path to the input .mp4 video file
        String outputCsv = args[1];       // Path to the CSV output file
        String hexColor = args[2];        // Hex string of the target color (e.g., "FF0000")
        String thresholdStr = args[3];    // Threshold string to be parsed into an int

        int targetColor;
        int threshold;

        // Step 3: Attempt to parse color and threshold inputs
        try {
            // Convert hexColor (e.g., "FF0000") into an integer (e.g., 0xFF0000)
            targetColor = Integer.parseInt(hexColor, 16);
            // Convert threshold string into an integer
            threshold = Integer.parseInt(thresholdStr);
        } catch (NumberFormatException e) {
            // Print error message if the user inputs invalid values
            System.err.println("Error: Color must be in hex (RRGGBB) and threshold must be an integer.");
            return;
        }

        try {
            // Step 4: Generate a binarized preview of frame 0 so the user can verify the input values

            // Open video file and grab the first frame
            try (VideoFrameExtractor extractor = new VideoFrameExtractor(videoPath)) {
                // Grab the first VideoFrame object
                VideoFrame first = extractor.iterator().next();

                // Create a FrameAnalyzer with the user's color and threshold
                FrameAnalyzer analyzer = new FrameAnalyzer(targetColor, threshold);

                // Binarize only the first frame (do not find groups yet)
                BufferedImage preview = analyzer.binarizeOnly(first.image());

                // Save the result to disk as a PNG file
                ImageIO.write(preview, "png", new File("frame0_binarized.png"));
                System.out.println("Preview image saved as: frame0_binarized.png");
                System.out.println("Please open and review this image to confirm the salamander is correctly isolated.");
            }

            // Step 5: Ask user whether to proceed with full video processing
            System.out.println("\nContinue processing the full video and writing output to " + outputCsv + "? (y/n)");

            Scanner scanner = new Scanner(System.in); // Set up CLI input
            String response = scanner.nextLine();     // Wait for user to enter a response
            scanner.close();                          // Close scanner to free the input stream

            // If user says no, exit early
            if (!response.equalsIgnoreCase("y")) {
                System.out.println("Aborted: full video processing was canceled by the user.");
                return;
            }

            // Step 6: Run full video processing and CSV output
            VideoCentroidTracker tracker = new VideoCentroidTracker(targetColor, threshold);
            tracker.trackCentroids(videoPath, outputCsv);
            System.out.println("Video successfully processed. Output saved to: " + outputCsv);

        } catch (IOException | JCodecException e) {
            // Catch and print any I/O or JCodec-related errors
            System.err.println("Processing error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
