package io.github.jameson789.app;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.PrintWriter;
import java.util.List;
import javax.imageio.ImageIO;

import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Java2DFrameConverter;
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
        if (args.length < 4) {
            throw new IllegalArgumentException("Usage: java ImageSummaryApp <input_video> <hex_target_color> <threshold> <task_id>");
        }

        String videoPath = args[0];
        String hexTargetColor = args[1];
        String taskId = args[3];
        int targetColor;
        int threshold;

        try {
            targetColor = Integer.parseInt(hexTargetColor, 16);
            threshold = Integer.parseInt(args[2]);
        } catch (Exception e) {
            System.err.println("Error parsing color or threshold.");
            return;
        }

        try (FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(videoPath);
             PrintWriter writer = new PrintWriter(new File(taskId + ".csv"))) {

            grabber.start();
            Java2DFrameConverter converter = new Java2DFrameConverter();
            ImageProcessor processor = new ImageProcessor(targetColor, threshold);

            double fps = grabber.getFrameRate();
            int frameCount = grabber.getLengthInFrames();
            System.out.printf("Total frames: %d, FPS: %.2f%n", frameCount, fps);

            for (int i = 0; i < frameCount; i++) {
                var frame = grabber.grabImage();
                if (frame == null) continue;

                BufferedImage image = converter.getBufferedImage(frame);
                CentroidResult result = processor.processImage(image);

                if (result != null) {
                    double timestampSeconds = i / fps;
                    writer.printf("%.2f,%d,%d%n", timestampSeconds, result.x(), result.y());
                }
            }

            grabber.stop();
            System.out.println("Processing complete. Output: " + taskId + ".csv");

        } catch (Exception e) {
            System.err.println("Error processing video.");
            e.printStackTrace();
        }
    }
}
