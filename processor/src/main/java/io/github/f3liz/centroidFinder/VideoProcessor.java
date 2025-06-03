package io.github.f3liz.centroidFinder;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.PrintWriter;
import java.util.List;
import javax.imageio.ImageIO;

import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;

/**
 * Class contains method/logic to process each frame
 * of an MP4 video file to find the largest centroid and write
 * to a CSV that centroids x and y coordinates by frame
 */
public class VideoProcessor {
    private final String inputPath;
    private final String outputCsv;
    private final int targetColor;
    private final int threshold;

    public VideoProcessor(String inputPath, String outputCsv, int targetColor, int threshold) {
        this.inputPath = inputPath;
        this.outputCsv = outputCsv;
        this.targetColor = targetColor;
        this.threshold = threshold;
    }

    // Main logic for processing video and writing centroid coordinates to CSV
    public void processVideo() throws Exception {
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

            System.out.println("Video frame count: " + totalFrames);
            System.out.println("Frame rate: " + frameRate + " fps");

            int frameNum = 0;
            Frame frame;

            // Gets rid of pixel warning in terminal
            org.bytedeco.ffmpeg.global.avutil.av_log_set_level(org.bytedeco.ffmpeg.global.avutil.AV_LOG_ERROR);

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
                    System.out.println("Processed frame " + frameNum + " (" + seconds + " seconds)");
                }

                frameNum++;
            }

            grabber.stop();
        }
    }
}

