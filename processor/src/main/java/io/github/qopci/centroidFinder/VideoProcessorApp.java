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
                    double timeInSeconds = ((double) totalFrameIndex) / frameRate;

                    int totalSeconds = (int) timeInSeconds;
                    int hours = totalSeconds / 3600;
                    int minutes = (totalSeconds % 3600) / 60;
                    double secondsWithMillis = timeInSeconds % 60;

                    String formattedTime = String.format("%02d:%02d:%06.3f", hours, minutes, secondsWithMillis);

                    // DEBUG INFO
                    System.out.println("===== DEBUG INFO =====");
                    System.out.println("Frame rate: " + frameRate);
                    System.out.println("Frame index: " + totalFrameIndex);
                    System.out.println("Raw time in seconds: " + timeInSeconds);
                    System.out.println("Formatted time: " + formattedTime);
                    System.out.println("======================");

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
}
