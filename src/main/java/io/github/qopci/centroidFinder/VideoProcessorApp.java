package io.github.qopci.centroidFinder;

import java.awt.image.BufferedImage;



public class VideoProcessorApp {
     public static void main(String[] args) {
        try {
            CommandLineParser parser = new CommandLineParser(args);
            FrameProcessor processor = new FrameProcessor(parser.targetColor, parser.threshold);

            // Open video reader and CSV writer
            try (
                VideoReader reader = new VideoReader(parser.inputPath);
                CsvWriter writer = new CsvWriter(parser.outputCsv)
            ) {
                BufferedImage frame;
                // Process each frame
                while ((frame = reader.nextFrame()) != null) {
                    if (reader.shouldProcessThisFrame()) {
                        Coordinate centroid = processor.process(frame);
                        writer.write(centroid); // Write centroid to CSV
                    }
                }
            }
            
            System.out.println("Done. CSV written to " + parser.outputCsv);
        } catch (Exception e) {
            e.printStackTrace(); // Print errors if any
        }
    }
}
