package io.github.AugleBoBaugles.centroidFinder;

import java.io.File;


/* 
    User types in String args, if there are less than 3 args, get error. 

    arg 1 = path to video
    arg 2 = target color
    arg 3 = threshold
 */
public class Main {
    public static void main(String[] args) {
        if (args.length < 3) {
            System.out.println("Usage: java Main <input_video> <hex_target_color> <threshold>");
            return;
        }
        
        String videoPath = args[0];
        String hexTargetColor = args[1];
        int threshold;

        // TEST
        // String videoPath = "sampleInput/sample_video_1.mp4";
        // String hexTargetColor = "#FF0000";
        // int threshold = 50;


        // Handling that threshold is a valid number.
        try {
            threshold = Integer.parseInt(args[2]);
        } catch (NumberFormatException e) {
            System.err.println("Threshold must be an integer.");
            return;
        }

        // Check that video exists, is a file, ends with ".mp4", and can be opened (written with AI assist)
        // TODO: Test this!!
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
        // Optionally: try opening a stream to test access
        try (var fis = new java.io.FileInputStream(file)) {
                // Just opening and closing to verify we can read it
        } catch (Exception e) {
            System.out.println("An error occurred while checking the file: " + e.getMessage());
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
        VideoProcessor processor = new VideoProcessor(videoPath, targetColor, threshold); 
        // tell processor to process data
        processor.extractFrames();

        System.out.println("Video processing complete. Output saved to sampleOutput/largestCentroids.csv");
    }
    
}
