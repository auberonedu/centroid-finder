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
                // But we'll stick with 24 fps
                double frameRate = 24;  
                int frameCount = 0;

                BufferedImage frame;
                while ((frame = reader.nextFrame()) != null) {
                    if (reader.shouldProcessThisFrame()) {
                        Coordinate centroid = processor.process(frame);
                        double timeInSeconds = frameCount / frameRate;

                        int totalSeconds = (int) timeInSeconds;
                        int hours = totalSeconds / 3600;
                        int minutes = (totalSeconds % 3600) / 60;
                        int seconds = totalSeconds % 60;
                        String formattedTime = String.format("%02d:%02d:%02d", hours, minutes, seconds);

                        writer.write(formattedTime, centroid);
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
