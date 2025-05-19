package io.github.TiaMarieG.centroidFinder;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class CentroidCoordsPerFrame {

    private final CentroidFinderPerFrame frameProcessor;

    public CentroidCoordsPerFrame(
        LargestCentroidPerFrameSelector unused,
        CentroidFinderPerFrame frameProcessor
    ) {
        this.frameProcessor = frameProcessor;
    }

    public Coordinate findCentroid(BufferedImage image) {
        List<Group> groups = frameProcessor.findGroups(image);
        Group largest = LargestCentroidPerFrameSelector.selectLargest(groups);

        if (largest == null) {
            return new Coordinate(-1, -1);
        }
        return largest.centroid();
    }

    public void exportToCsv(List<CentroidFinderPerFrame.DataPoint> data, String outputPath) {
        try {
            File outputFile = new File(outputPath);
            File parentDir = outputFile.getParentFile();

            if (parentDir != null && !parentDir.exists()) {
                boolean created = parentDir.mkdirs();
                if (created) {
                    System.out.println("Created output directory: " + parentDir.getAbsolutePath());
                }
            }

            try (FileWriter writer = new FileWriter(outputFile)) {
                writer.write("Frame,X,Y\n");
                for (CentroidFinderPerFrame.DataPoint point : data) {
                    writer.write(point.frame() + "," + point.x() + "," + point.y() + "\n");
                }
            }

            System.out.println("Centroid CSV written to: " + outputPath);

        } catch (IOException e) {
            System.err.println("Error writing centroid CSV: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
