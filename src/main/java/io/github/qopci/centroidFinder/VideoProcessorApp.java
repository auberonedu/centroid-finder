package io.github.qopci.centroidFinder;

import java.awt.image.BufferedImage;

public class VideoProcessorApp {
    public static void main(String[] args) {
        try {
            CommandLineParser parser = new CommandLineParser(args);
            FrameProcessor processor = new FrameProcessor(parser.targetColor, parser.threshold);

            try (VideoReader reader = new VideoReader(parser.inputPath);
                 CsvWriter writer = new CsvWriter(parser.outputCsv)) {

                // Hardcoded frame rate, ensantina has a frame rate of 23.98
                // But we'll still with 24 fps
                double frameRate = 24;  
                int frameCount = 0;

                BufferedImage frame;
                while ((frame = reader.nextFrame()) != null) {
                    if (reader.shouldProcessThisFrame()) {
                        Coordinate centroid = processor.process(frame);
                        double timeInSeconds = frameCount / frameRate;
                        writer.write(timeInSeconds, centroid);
                        frameCount++;
                    }
                }
            }

            System.out.println("Done. CSV written to " + parser.outputCsv);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
