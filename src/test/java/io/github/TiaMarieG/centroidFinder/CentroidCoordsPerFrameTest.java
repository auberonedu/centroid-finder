package io.github.TiaMarieG.centroidFinder;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Files;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CentroidCoordsPerFrameTest {

    @Test
    void testCsvExportCreatesOutput() throws Exception {
        // Arrange
        List<CentroidFinderPerFrame.DataPoint> data = List.of(
            new CentroidFinderPerFrame.DataPoint(0, 50, 40),
            new CentroidFinderPerFrame.DataPoint(1, 48, 39)
        );
        String outputPath = "test_output/cb_bg_centroids_test.csv";

        // Act
        CentroidCoordsPerFrame exporter = new CentroidCoordsPerFrame();
        exporter.exportToCsv(data, outputPath);

        // Assert
        File file = new File(outputPath);
        assertTrue(file.exists(), "CSV file should be created");

        List<String> lines = Files.readAllLines(file.toPath());
        assertEquals("frame,x,y", lines.get(0));
        assertEquals("0,50,40", lines.get(1));
        assertEquals("1,48,39", lines.get(2));
    }
}
