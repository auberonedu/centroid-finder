package io.github.qopci.centroidFinder;

import java.awt.image.BufferedImage;

public class VideoProcessorApp {
    public static void main(String[] args) {
        try {
            CommandLineParser parser = new CommandLineParser(args);
            FrameProcessor processor = new FrameProcessor(parser.targetColor, parser.threshold);

            try (VideoReader reader = new VideoReader(parser.inputPath);
                 CsvWriter writer = new CsvWriter(parser.outputCsv)) {

                double frameRate = reader.getFrameRate();
                System.out.println("Frame rate detected: " + frameRate);

                int totalFrameIndex = 0;  // counts all frames, regardless of processing

                BufferedImage frame;
                while ((frame = reader.nextFrame()) != null) {
                    double timeInSeconds = totalFrameIndex / frameRate;

                    // Use the new helper method for formatting time:
                    String formattedTime = formatTime(timeInSeconds);

                    System.out.printf("Frame %d, timeInSeconds=%.3f, formattedTime=%s%n", totalFrameIndex, timeInSeconds, formattedTime);

                    if (reader.shouldProcessThisFrame()) {
                        Coordinate centroid = processor.process(frame);
                        writer.write(formattedTime, centroid);
                    }

                    totalFrameIndex++;
                }

            }

            System.out.println("Done. CSV written to " + parser.outputCsv);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // New static helper method for time formatting
    public static String formatTime(double timeInSeconds) {
        if (timeInSeconds < 0) {
            timeInSeconds = 0;  
        }
        int totalSeconds = (int) timeInSeconds;
        int hours = totalSeconds / 3600;
        int minutes = (totalSeconds % 3600) / 60;
        int seconds = totalSeconds % 60;
    
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }
    
}
