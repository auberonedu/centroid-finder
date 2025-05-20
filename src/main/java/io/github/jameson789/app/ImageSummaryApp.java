package io.github.jameson789.app;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.PrintWriter;
import java.util.List;
import javax.imageio.ImageIO;

/**
 * The Image Summary Application.
 * 
 * This application takes three command-line arguments:
 * 1. The path to an input image file (for example, "image.png").
 * 2. A target hex color in the format RRGGBB (for example, "FF0000" for red).
 * 3. An integer threshold for binarization.
 * 
 * The application performs the following steps:
 * 
 * 1. Loads the input image.
 * 2. Parses the target color from the hex string into a 24-bit integer.
 * 3. Binarizes the image by comparing each pixel's Euclidean color distance to
 * the target color.
 * A pixel is marked white (1) if its distance is less than the threshold;
 * otherwise, it is marked black (0).
 * 4. Converts the binary array back to a BufferedImage and writes the binarized
 * image to disk as "binarized.png".
 * 5. Finds connected groups of white pixels in the binary image.
 * Pixels are connected vertically and horizontally (not diagonally).
 * For each group, the size (number of pixels) and the centroid (calculated
 * using integer division) are computed.
 * 6. Writes a CSV file named "groups.csv" containing one row per group in the
 * format "size,x,y".
 * Coordinates follow the convention: (x:0, y:0) is the top-left, with x
 * increasing to the right and y increasing downward.
 * 
 * Usage:
 * java ImageSummaryApp <input_image> <hex_target_color> <threshold>
 */
public class ImageSummaryApp {
    public static void main(String[] args) {
        if (args.length < 3) {
            // System.out.println("Usage: java ImageSummaryApp <input_video> <hex_target_color> <threshold>");
            throw new IllegalArgumentException("Usage: java ImageSummaryApp <input_video> <hex_target_color> <threshold>");
        }

        String videoPath = args[0];
        int targetColor;
        int threshold;

        try {
            targetColor = Integer.parseInt(args[1], 16);
            threshold = Integer.parseInt(args[2]);
        } catch (Exception e) {
            System.err.println("Error parsing color or threshold.");
            return;
        }

        try (org.bytedeco.javacv.FFmpegFrameGrabber grabber = new org.bytedeco.javacv.FFmpegFrameGrabber(videoPath);
                PrintWriter writer = new PrintWriter(new File("frame_centroids.csv"))) {

            grabber.start();
            new File("binarized_frames").mkdirs();
            org.bytedeco.javacv.Java2DFrameConverter converter = new org.bytedeco.javacv.Java2DFrameConverter();
            ImageProcessor processor = new ImageProcessor(targetColor, threshold);

            int frameCount = grabber.getLengthInFrames();
            System.out.println("Total frames to process: " + frameCount);
            for (int i = 0; i < frameCount; i++) {

                var frame = grabber.grabImage();
                System.out.println("Processing frame " + i);
                System.out.println("Frame is null: " + (frame == null));
                if (frame == null){
                    continue;
                } 
                BufferedImage image = converter.getBufferedImage(frame);
                CentroidResult result = processor.processImage(image);
                if (i % 1000 == 0) {
                    BufferedImage bin = processor.getBinarizedImage(image);
                    if (bin != null) {
                        String filename = String.format("binarized_frames/frame_%03d.png", i);
                        try {
                            javax.imageio.ImageIO.write(bin, "png", new File(filename));
                            System.out.println("Saved: " + filename);
                        } catch (Exception e) {
                            System.err.println("Failed to save " + filename);
                            e.printStackTrace();
                        }
                    } else {
                        System.out.println("Null binary image at frame " + i);
                    }
                }

                if (result != null) {
                    writer.printf("%d,%d,%d%n", i, result.x(), result.y());
                }
            }

            grabber.stop();
            System.out.println("Processing complete. Output: frame_centroids.csv");

        } catch (Exception e) {
            System.err.println("Error processing video.");
            e.printStackTrace();
        }
    }

}
