package io.github.AugleBoBaugles.centroidFinder;

import java.io.File;


/**
 * ART (Automated Resource Tracking) Salamander App
 * Created by: Augy, Rebecca, and Tyler.
 * 
 * This application takes three comand-line arguments:
 * 1. The path to an input video (for example: "/videos/salamander_7.mp4").
 * 2. A target hexcolor in the format RRGGBB or #RRGGBB (for example, "FF0000" for red).
 * 3. An integer threshold for binarization (optional, defaults to 0).
 * 
 * The application performs the following steps:
 * 
 * 1. Loads the input video.
 * 2. Parses the target color from the hex string into a 24-bit integer.
 * 3. Binarizes each frame of the video by comparing each pixel's Euclidean color distance to the target color.
 *    A pixel is marked white (1) if its distance is less than the threshold; otherwise, it is marked black (0).
 * 4. Converts the binary array back to a BufferedImage and writes the binarized image to disk as "binarized.png".
 * 5. Finds connected groups of white pixels in the binary image.
 *    Pixels are connected vertically and horizontally (not diagonally).
 *    For each group, the size (number of pixels) and the centroid (calculated using integer division) are computed.
 * 6. Writes a CSV file named "largestCentroids.csv" containing one row per frame in the format "time, centroid x, centroid y".
 *    Coordinates follow the convention: (x:0, y:0) is the top-left, with x increasing to the right and y increasing downward.
 * 
 * Usage:
 *   java Main <input_video> <hex_target_color> <threshold>
 */
public class Main {
    public static void main(String[] args) {

        // validate the correct number of arguments are there
        if (args.length < 4) {
            System.out.println("Usage: java Main <input_video> <output_path> <hex_target_color> <threshold>");
            return;
        }
        
        // store the arguments
        String videoPath = args[0];
        String outputPath = args[1];
        String hexTargetColor = args[2];
        int threshold = 0;

        // Handling that threshold is a valid number.
        try {
            threshold = Integer.parseInt(args[3]);
        } catch (NumberFormatException e) {
            System.err.println("Threshold must be an integer.");
            return;
        }

        // Check that video exists, is a file, ends with ".mp4", and can be opened (written with AI assist)
        File file = new File(videoPath);
        if (!file.exists()) {
            System.out.println("File does not exist.");
            return;
        }
        if (!file.isFile()) {
            System.out.println("Path is not a file.");
            return;
        }
        if (!videoPath.toLowerCase().endsWith(".mp4")) {
            System.out.println("File is not an mp4.");
            return;
        }

        // Support optional '#' prefix in hex color
        if (hexTargetColor.startsWith("#")) hexTargetColor = hexTargetColor.substring(1);

        // Parse the target color from a hex string (format RRGGBB) into a 24-bit integer (0xRRGGBB)
        int targetColor;

        // Handling that target color is a valid color
        try {
            targetColor = Integer.parseInt(hexTargetColor, 16);
        } catch (NumberFormatException e) {
            System.err.println("Invalid hex target color. Please provide a color in RRGGBB format.");
            return;
        }

        // Instantiate VideoProcessor
        VideoProcessor processor = new VideoProcessor(videoPath, outputPath, targetColor, threshold); 

        // Run the processor
        processor.extractFrames();

        System.out.println("Video processing complete.");
    }
    
}
