package io.github.alstondsouza1.centroidFinder;

import org.jcodec.api.JCodecException;

import java.awt.image.BufferedImage;
import java.io.IOException;

// main class for processing a video file to track the largest centroid
public class VideoProcessorApp {
    public static void main(String[] args) throws IOException, JCodecException {
        CommandLineParser parser = new CommandLineParser(args);
        VideoReader reader = new VideoReader(parser.getInputPath());

        // set up components for centroid direction
        ColorDistanceFinder dist = new EuclideanColorDistance();
        ImageBinarizer bin = new DistanceImageBinarizer(dist, parser.getTargetColor(), parser.getThreshold());
        BinaryGroupFinder groupFinder = new DfsBinaryGroupFinder();
        ImageGroupFinder imageGroupFinder = new BinarizingImageGroupFinder(bin, groupFinder);
        FrameProcessor processor = new FrameProcessor(imageGroupFinder);
        CsvWriter csvWriter = new CsvWriter(parser.getOutputCsv());

        // process the video frame by frame
        BufferedImage frame;
        int frameIndex = 0;
        int frameRate = reader.getFrameRate();

        while ((frame = reader.getNextFrame()) != null) {
            if (frameIndex % frameRate == 0) { // one frame per second
                Coordinate c = processor.getLargestCentroid(frame);
                csvWriter.writeCentroid(frameIndex / (double) frameRate, c);
            }
            frameIndex++;
        }

        // clean up
        csvWriter.close();
        System.out.println("Video processing completed.");
    }
}