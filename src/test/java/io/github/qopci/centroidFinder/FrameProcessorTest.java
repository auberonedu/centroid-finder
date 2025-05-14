package io.github.qopci.centroidFinder;

import org.junit.jupiter.api.Test;

import java.awt.Color;
import java.awt.image.BufferedImage;

import static org.junit.jupiter.api.Assertions.*;

// AI Used to generate tests for FrameProcessorTest.java
class FrameProcessorTest {
    @Test
    void returnsNegativeOneWhenNoMatchingPixels() {
        // Create a 10x10 image filled with black
        BufferedImage image = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);
        int targetColor = new Color(255, 0, 0).getRGB(); // Red
        int threshold = 10;

        FrameProcessor processor = new FrameProcessor(targetColor, threshold);
        Coordinate result = processor.process(image);

        assertEquals(new Coordinate(-1, -1), result);
    }

    @Test
    void findsCentroidOfRedBlock() {
        // Create a 10x10 image with a red square in the middle
        BufferedImage image = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);
        int red = new Color(255, 0, 0).getRGB();

        for (int y = 3; y <= 5; y++) {
            for (int x = 3; x <= 5; x++) {
                image.setRGB(x, y, red);
            }
        }

        FrameProcessor processor = new FrameProcessor(red, 10); // Low threshold for exact match
        Coordinate result = processor.process(image);

        // Expected centroid: (4,4) for a 3x3 block
        assertEquals(new Coordinate(4, 4), result);
    }
}
