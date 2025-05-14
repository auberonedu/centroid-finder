package io.github.TiaMarieG.centroidFinder;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CentroidFinderPerFrameTest {

    @Test
    void testProcessCbBgVideoReturnsCentroids() throws Exception {
        // Arrange
        ImageBinarizer binarizer = new DistanceImageBinarizer(
            new EuclideanColorDistance(), 0xFFFFFF, 100
        );
        BinaryGroupFinder groupFinder = new DfsBinaryGroupFinder();
        CentroidFinderPerFrame processor = new CentroidFinderPerFrame(binarizer, groupFinder);

        // Act
        List<CentroidFinderPerFrame.DataPoint> results = processor.processVideo("videos/cb_bg.mp4", 15);

        // Assert
        assertNotNull(results, "Results should not be null");
        assertFalse(results.isEmpty(), "Should return at least one centroid");
        System.out.println("Sample centroid: " + results.get(0));
    }
}
