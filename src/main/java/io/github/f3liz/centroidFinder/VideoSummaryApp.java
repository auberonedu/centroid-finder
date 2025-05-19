package io.github.f3liz.centroidFinder;

import java.util.List;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.PrintWriter;

import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;

/**
 * Command-line tool to process each frame of an MP4 video file to find the
 * largest centroid and write
 * to a CSV that centroids x and y coordinates by frame
 */

public class VideoSummaryApp {
    public static void main(String[] args) {
        // Logic to make sure only 4 arguments are given
        if (args.length < 4) {
            System.out.println("Usage: java -jar videoprocessor.jar <inputPath> <outputCsv> <targetColor> <threshold>");
            return;
        }

        // Take in and parse the command line arguments
        String inputPath = args[0];
        String outputCsv = args[1];
        int targetColor = Integer.parseInt(args[2], 16);
        int threshold = Integer.parseInt(args[3]);

        // Create the DistanceImageBinarizer with a EuclideanColorDistance instance.
        ColorDistanceFinder distanceFinder = new EuclideanColorDistance();
        ImageBinarizer binarizer = new DistanceImageBinarizer(distanceFinder, targetColor, threshold);

        // Set up the logic to find largest group
        ImageGroupFinder groupFinder = new BinarizingImageGroupFinder(binarizer, new DfsBinaryGroupFinder());

        // Conversion for Frame to BufferedImage
        FrameToBufferedImageConverter converter = new FrameToBufferedImageConverter();

        // Grabber to read frames and writer to write to the output CSV
        try (FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(new File(inputPath));
                PrintWriter writer = new PrintWriter(outputCsv)) {

            grabber.start();

            // Write header line in required format
            writer.println("time,x,y");

            int totalFrames = grabber.getLengthInFrames();
            double frameRate = grabber.getFrameRate();

            System.out.printf("Video frame count: %d\n", totalFrames);
            System.out.printf("Frame rate: %.2f fps\n", frameRate);

            int frameNum = 0;
            Frame frame;

            // Loops through each frame and records the x and y coordinates
            while ((frame = grabber.grabImage()) != null) {
                BufferedImage image = converter.convert(frame);
                List<Group> groups = groupFinder.findConnectedGroups(image);

                int xCoord = -1;
                int yCoord = -1;

                // Only update coordinates if a group was found
                if (!groups.isEmpty()) {
                    Group biggest = groups.get(0);
                    xCoord = biggest.centroid().x();
                    yCoord = biggest.centroid().y();
                }

                // Compute the time in seconds
                double seconds = frameNum / frameRate;

                // Write the time and coordinates to CSV
                writer.printf("%.3f,%d,%d%n", seconds, xCoord, yCoord);

                if (frameNum % 100 == 0) {
                    System.out.printf("Processed frame %d (%.2f seconds)\n", frameNum, seconds);
                }
                
                frameNum++;
            }

            // Stops/ends the reading of frames
            grabber.stop();
            System.out.println("Processing complete, saved to: " + outputCsv);
        } catch (Exception e) {
            System.out.println("Error processing video: ");
            e.printStackTrace();
        }
    }
}