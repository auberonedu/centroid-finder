package io.github.oakes777.salamander;

import java.io.File;

public class VideoProcessorMain {
    public static void main(String[] args) {
        if (args.length < 5) {
            System.out.println(
                    """
                            Usage: java -jar salamander.jar <videoPath> <targetColorRGB> <threshold> <outputCsvPath> <intervalLabel>
                            Example: java -jar salamander.jar ensantina.mp4 255,0,0 50 output.csv 0.5s
                            """);
            return;
        }

        String videoPath = args[0];
        String rgbString = args[1];
        String thresholdInput = args[2];
        String csvPath = args[3];
        String intervalLabel = args[4];

        int threshold;
        int targetColor; // Packed RGB int
        int frameInterval;

        try {
            // Parse RGB string
            String[] rgb = rgbString.split(",");
            if (rgb.length != 3) {
                System.err.println("Invalid RGB format. Use R,G,B — e.g., 255,0,0");
                return;
            }
            int r = Integer.parseInt(rgb[0].trim());
            int g = Integer.parseInt(rgb[1].trim());
            int b = Integer.parseInt(rgb[2].trim());

            // Pack into single int
            targetColor = (r << 16) | (g << 8) | b;

            threshold = Integer.parseInt(thresholdInput);

            System.out.printf("""
                    🎬 Processing Parameters:
                    Video Path: %s
                    Target Color: R=%d G=%d B=%d (Packed: %d)
                    Threshold: %d
                    Output CSV: %s
                    Interval: %s
                    """, videoPath, r, g, b, targetColor, threshold, csvPath, intervalLabel);

        } catch (NumberFormatException e) {
            System.err.println("Error: Invalid number format in color or threshold.");
            return;
        }

        try (VideoFrameExtractor extractor = new VideoFrameExtractor(videoPath)) {
            double fps = extractor.getFrameRate();

            frameInterval = switch (intervalLabel.toLowerCase()) {
                case "all" -> 1;
                case "0.5s" -> Math.max((int) Math.round(fps * 0.5), 1);
                case "1.0s" -> Math.max((int) Math.round(fps * 1.0), 1);
                case "10s" -> Math.max((int) Math.round(fps * 10.0), 1);
                default -> {
                    System.err.println("Invalid interval label: " + intervalLabel + ". Defaulting to all frames.");
                    yield 1;
                }
            };

            System.out.println("FPS = " + fps + " → Frame interval = " + frameInterval);

            // Core processing logic
            VideoCentroidTracker tracker = new VideoCentroidTracker(targetColor, threshold);
            tracker.trackCentroids(videoPath, csvPath, frameInterval);

            System.out.println("Processing complete! CSV saved to: " + csvPath);

        } catch (Exception e) {
            System.err.println("Error during video processing:");
            e.printStackTrace();
        }
    }
}
