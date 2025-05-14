package io.github.TiaMarieG.centroidFinder;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CentroidProcessingIntegrationTest {

    @Test
    void testCentroidPipelineWithCbBgVideo() throws Exception {
        // Arrange
        ImageBinarizer binarizer = new DistanceImageBinarizer(
            new EuclideanColorDistance(), 0xFFFFFF, 100
        );
        BinaryGroupFinder groupFinder = new DfsBinaryGroupFinder();

        CentroidFinderPerFrame processor = new CentroidFinderPerFrame(binarizer, groupFinder);
        CentroidCoordsPerFrame exporter = new CentroidCoordsPerFrame();

        // Act
        List<CentroidFinderPerFrame.DataPoint> centroids =
            processor.processVideo("videos/cb_bg.mp4", 15);

        String outputPath = "test_output/cb_bg_centroids_full_pipeline.csv";
        exporter.exportToCsv(centroids, outputPath);

        // Assert
        assertFalse(centroids.isEmpty(), "Should detect centroids from cb_bg.mp4");

        File file = new File(outputPath);
        assertTrue(file.exists(), "CSV file should be written");
    }
}
