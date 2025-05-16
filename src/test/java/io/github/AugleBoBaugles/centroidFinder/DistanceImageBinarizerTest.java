package io.github.AugleBoBaugles.centroidFinder;

import org.junit.jupiter.api.Test;

import io.github.AugleBoBaugles.centroidFinder.ColorDistanceFinder;
import io.github.AugleBoBaugles.centroidFinder.DistanceImageBinarizer;
import io.github.AugleBoBaugles.centroidFinder.EuclideanColorDistance;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.image.BufferedImage;
import java.util.List;
import java.util.ArrayList;

public class DistanceImageBinarizerTest {

    // toBinaryArray
    @Test
    public void testToBinaryArray_fourPixelBlackWhite() {
        // Create a 2 x 2 buffered image
        BufferedImage bufferedImage = new BufferedImage(2, 2, BufferedImage.TYPE_INT_RGB);
        bufferedImage.setRGB(0, 0, 0x000000);
        bufferedImage.setRGB(0, 1, 0xFFFFFF);
        bufferedImage.setRGB(1, 0, 0xFFFFFF);
        bufferedImage.setRGB(1, 1, 0x000000);

        int[][] expect = new int[][]{
            {0, 1},
            {1, 0}
        };

        EuclideanColorDistance testDistanceFinder = new EuclideanColorDistance();
        DistanceImageBinarizer testBinarizer = new DistanceImageBinarizer(testDistanceFinder, 0xFFFFFF, 1);

        int[][] actual = testBinarizer.toBinaryArray(bufferedImage);

        for (int i = 0; i < expect.length; i++) {
            assertArrayEquals(expect[i], actual[i]);
        }
    }

    private static class MockColorDistanceFinder implements ColorDistanceFinder {
        private final double[][] mockDistances;
    
        public MockColorDistanceFinder(double[][] mockDistances) {
            this.mockDistances = mockDistances;
        }
    
        @Override
        public double distance(int c1, int c2) {
            // Just return a fixed value per position for simplicity in this test
            int x = (c1 >> 16) & 0xFF; // Treat R as X
            int y = (c1 >> 8) & 0xFF;  // Treat G as Y
            return mockDistances[x][y];
        }
    }

    @Test
    public void testToBinaryArray_pixelsBelowThreshold_areWhite() {
        double[][] mockDistances = {
            {10.0, 10.0},
            {10.0, 10.0}
        };
        BufferedImage image = new BufferedImage(2, 2, BufferedImage.TYPE_INT_RGB);
        image.setRGB(0, 0, 0x000100); // Simulated (x=1, y=0)
        image.setRGB(0, 1, 0x000101); // Simulated (x=1, y=1)
        image.setRGB(1, 0, 0x000000);
        image.setRGB(1, 1, 0x000001);

        DistanceImageBinarizer binarizer = new DistanceImageBinarizer(
            new MockColorDistanceFinder(mockDistances),
            0x000000,
            15
        );

        int[][] result = binarizer.toBinaryArray(image);

        assertArrayEquals(new int[][] {
            {1, 1},
            {1, 1}
        }, result);
    }

    @Test
    public void testToBinaryArray_pixelsAboveThreshold_areBlack() {
        double[][] mockDistances = {
            {20.0, 20.0},
            {20.0, 20.0}
        };
        BufferedImage image = new BufferedImage(2, 2, BufferedImage.TYPE_INT_RGB);
        image.setRGB(0, 0, 0x000100);
        image.setRGB(0, 1, 0x000101);
        image.setRGB(1, 0, 0x000000);
        image.setRGB(1, 1, 0x000001);

        DistanceImageBinarizer binarizer = new DistanceImageBinarizer(
            new MockColorDistanceFinder(mockDistances),
            0x000000,
            15
        );

        int[][] result = binarizer.toBinaryArray(image);

        assertArrayEquals(new int[][] {
            {0, 0},
            {0, 0}
        }, result);
    }
    
    @Test
    public void testToBufferedImage_createsCorrectColors() {
        DistanceImageBinarizer binarizer = new DistanceImageBinarizer(null, 0, 0);
        int[][] binary = {
            {1, 0},
            {0, 1}
        };

        BufferedImage result = binarizer.toBufferedImage(binary);

        assertEquals(0xFFFFFFFF, result.getRGB(0, 0)); // white
        assertEquals(0xFF000000, result.getRGB(0, 1)); // black
        assertEquals(0xFF000000, result.getRGB(1, 0)); // black
        assertEquals(0xFFFFFFFF, result.getRGB(1, 1)); // white
    }

    @Test
    public void testToBinaryArray_customTargetColorAndThreshold() {
        // Set up a mock ColorDistanceFinder
        ColorDistanceFinder mockDistanceFinder = new ColorDistanceFinder() {
            @Override
            public double distance(int color1, int color2) {
                // For testing, we'll calculate the absolute difference in RGB components
                return Math.abs(color1 - color2);
            }
        };

        // Target color (green)
        int targetColor = 0xFF00FF00;
        int threshold = 100;

        DistanceImageBinarizer binarizer = new DistanceImageBinarizer(mockDistanceFinder, targetColor, threshold);

        // Create a 2x2 image with varying distances to the target color
        BufferedImage dummyImage = new BufferedImage(2, 2, BufferedImage.TYPE_INT_RGB);
        dummyImage.setRGB(0, 0, 0xFF00FF00); // Green (same as target)
        dummyImage.setRGB(0, 1, 0xFF0000FF); // Blue
        dummyImage.setRGB(1, 0, 0xFFFF0000); // Red
        dummyImage.setRGB(1, 1, 0xFFFFFFFF); // White

        // Convert the image to a binary array
        int[][] binaryArray = binarizer.toBinaryArray(dummyImage);

        // Assertions: verify that pixels are correctly binarized
        assertEquals(1, binaryArray[0][0]); // Green pixel should be white (same as target color)
        assertEquals(0, binaryArray[0][1]); // Blue pixel should be black (far from target)
        assertEquals(0, binaryArray[1][0]); // Red pixel should be black (far from target)
        assertEquals(0, binaryArray[1][1]); // White pixel should be black (far from target)
    }

    @Test
    public void testToBinaryArray_largeImageWithMixedColors() {
        // Set up a mock ColorDistanceFinder
        ColorDistanceFinder mockDistanceFinder = new ColorDistanceFinder() {
            @Override
            public double distance(int color1, int color2) {
                // For testing, we'll calculate the absolute difference in RGB components
                return Math.abs(color1 - color2);
            }
        };

        // Target color (yellow)
        int targetColor = 0xFFFFFF00; // Yellow color
        int threshold = 200; // Threshold for binarization

        DistanceImageBinarizer binarizer = new DistanceImageBinarizer(mockDistanceFinder, targetColor, threshold);

        // Create a 4x4 image with various colors
        BufferedImage dummyImage = new BufferedImage(4, 4, BufferedImage.TYPE_INT_RGB);
        dummyImage.setRGB(0, 0, 0xFFFFFF00); // Yellow (same as target)
        dummyImage.setRGB(1, 0, 0xFFFF0000); // Red
        dummyImage.setRGB(2, 0, 0xFF00FF00); // Green
        dummyImage.setRGB(3, 0, 0xFF0000FF); // Blue

        dummyImage.setRGB(0, 1, 0xFFFFFFFF); // White
        dummyImage.setRGB(1, 1, 0xFF000000); // Black
        dummyImage.setRGB(2, 1, 0xFFFFFF00); // Yellow (same as target)
        dummyImage.setRGB(3, 1, 0xFFFF00FF); // Magenta

        dummyImage.setRGB(0, 2, 0xFF00FF00); // Green
        dummyImage.setRGB(1, 2, 0xFFFF0000); // Red
        dummyImage.setRGB(2, 2, 0xFF0000FF); // Blue
        dummyImage.setRGB(3, 2, 0xFFFFFF00); // Yellow (same as target)

        dummyImage.setRGB(0, 3, 0xFFFFFFFF); // White
        dummyImage.setRGB(1, 3, 0xFF000000); // Black
        dummyImage.setRGB(2, 3, 0xFFFF0000); // Red
        dummyImage.setRGB(3, 3, 0xFF00FF00); // Green

        // Convert the image to a binary array
        int[][] binaryArray = binarizer.toBinaryArray(dummyImage);

        // Assertions: verify that pixels are correctly binarized
        assertEquals(1, binaryArray[0][0]); // Yellow should be white (same as target)
        assertEquals(0, binaryArray[1][0]); // Red should be black
        assertEquals(0, binaryArray[2][0]); // Green should be black
        assertEquals(0, binaryArray[3][0]); // Blue should be black

        assertEquals(0, binaryArray[0][1]); // White should be black (distance > threshold)
        assertEquals(0, binaryArray[1][1]); // Black should be black
        assertEquals(1, binaryArray[2][1]); // Yellow should be white (same as target)
        assertEquals(0, binaryArray[3][1]); // Magenta should be black

        assertEquals(0, binaryArray[0][2]); // Green should be black
        assertEquals(0, binaryArray[1][2]); // Red should be black
        assertEquals(0, binaryArray[2][2]); // Blue should be black
        assertEquals(1, binaryArray[3][2]); // Yellow should be white (same as target)

        assertEquals(0, binaryArray[0][3]); // White should be black (distance > threshold)
        assertEquals(0, binaryArray[1][3]); // Black should be black
        assertEquals(0, binaryArray[2][3]); // Red should be black
        assertEquals(0, binaryArray[3][3]); // Green should be black
    }

}
