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

    @Test
    void findsCentroidInFullRedImage() {
        BufferedImage image = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);
        int red = new Color(255, 0, 0).getRGB();
        for (int y = 0; y < 10; y++) {
            for (int x = 0; x < 10; x++) {
                image.setRGB(x, y, red);
            }
        }

        FrameProcessor processor = new FrameProcessor(red, 5);
        Coordinate result = processor.process(image);

        // Full image centroid: (4,4) or (5,5) depending on implementation
        assertTrue(
            result.equals(new Coordinate(4, 4)) || result.equals(new Coordinate(5, 5)),
            "Centroid should be around the center for full red image"
        );
    }

    @Test
    void ignoresColorsOutsideThreshold() {
        BufferedImage image = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);
        int red = new Color(255, 0, 0).getRGB();
        int almostRed = new Color(200, 50, 50).getRGB();

        // Only one pixel is exactly red
        image.setRGB(5, 5, red);
        // Rest are almost red but outside threshold
        for (int y = 0; y < 10; y++) {
            for (int x = 0; x < 10; x++) {
                if (x != 5 || y != 5) {
                    image.setRGB(x, y, almostRed);
                }
            }
        }

        FrameProcessor processor = new FrameProcessor(red, 10); // Too strict
        Coordinate result = processor.process(image);
        assertEquals(new Coordinate(5, 5), result);
    }
}
