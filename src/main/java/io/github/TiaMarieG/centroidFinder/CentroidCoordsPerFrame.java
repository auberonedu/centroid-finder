package io.github.TiaMarieG.centroidFinder;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class CentroidCoordsPerFrame {

    /**
     * Exports centroid coordinates to a CSV file.
     * If the output folder doesn't exist, it will be created automatically.
     *
     * @param data List of DataPoint records (frame, x, y)
     * @param outputPath Destination file path, e.g. "output/centroids.csv"
     */
    public void exportToCsv(List<CentroidFinderPerFrame.DataPoint> data, String outputPath) {
        try {
            File outputFile = new File(outputPath);
            File parentDir = outputFile.getParentFile();

            // Create the parent directory if it doesn't exist
            if (parentDir != null && !parentDir.exists()) {
                boolean created = parentDir.mkdirs();
                if (created) {
                    System.out.println("Created output directory: " + parentDir.getAbsolutePath());
                }
            }

            try (FileWriter writer = new FileWriter(outputFile)) {
                writer.write("frame,x,y\n"); // Header

                for (CentroidFinderPerFrame.DataPoint point : data) {
                    writer.write(point.frame() + "," + point.x() + "," + point.y() + "\n");
                }

                System.out.println("CSV export successful: " + outputFile.getAbsolutePath());
            }

        } catch (IOException e) {
            System.err.println("Error writing CSV: " + e.getMessage());
        }
    }
}
