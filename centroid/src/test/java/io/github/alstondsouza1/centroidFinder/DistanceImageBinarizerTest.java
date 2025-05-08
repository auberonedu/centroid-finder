package io.github.alstondsouza1.centroidFinder;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.awt.image.BufferedImage;

public class DistanceImageBinarizerTest {

    private static class MockColorDistanceFinder implements ColorDistanceFinder {
        @Override
        public double distance(int colorA, int colorB) {
            int r1 = (colorA >> 16) & 0xFF;
            int g1 = (colorA >> 8) & 0xFF;
            int b1 = colorA & 0xFF;

            int r2 = (colorB >> 16) & 0xFF;
            int g2 = (colorB >> 8) & 0xFF;
            int b2 = colorB & 0xFF;

            return Math.sqrt(Math.pow(r1 - r2, 2) + Math.pow(g1 - g2, 2) + Math.pow(b1 - b2, 2));
        }
    }

    private DistanceImageBinarizer binarizer;
    private final int targetColor = 0xFFFFFF; // white
    private final int threshold = 100;

    @BeforeEach
    public void setup() {
        binarizer = new DistanceImageBinarizer(new MockColorDistanceFinder(), targetColor, threshold);
    }

    @Test
    public void testAllWhitePixels() {
        // all white pixels should be converted to 1
        BufferedImage img = new BufferedImage(2, 2, BufferedImage.TYPE_INT_RGB);
        img.setRGB(0, 0, 0xFFFFFF);
        img.setRGB(1, 0, 0xFFFFFF);
        img.setRGB(0, 1, 0xFFFFFF);
        img.setRGB(1, 1, 0xFFFFFF);

        int[][] binary = binarizer.toBinaryArray(img);
        assertEquals(1, binary[0][0]);
        assertEquals(1, binary[0][1]);
        assertEquals(1, binary[1][0]);
        assertEquals(1, binary[1][1]);
    }

    @Test
    public void testAllBlackPixels() {
        // all black pixels should be converted to 0
        BufferedImage img = new BufferedImage(2, 2, BufferedImage.TYPE_INT_RGB);
        img.setRGB(0, 0, 0x000000);
        img.setRGB(1, 0, 0x000000);
        img.setRGB(0, 1, 0x000000);
        img.setRGB(1, 1, 0x000000);

        int[][] binary = binarizer.toBinaryArray(img);
        assertEquals(0, binary[0][0]);
        assertEquals(0, binary[0][1]);
        assertEquals(0, binary[1][0]);
        assertEquals(0, binary[1][1]);
    }

    @Test
    public void testMixedColorPixels() {
        // mixed colors should be converted based on the threshold
        BufferedImage img = new BufferedImage(2, 2, BufferedImage.TYPE_INT_RGB);
        img.setRGB(0, 0, 0xFFFFFF); 
        img.setRGB(1, 0, 0x646464); 
        img.setRGB(0, 1, 0x000000); 
        img.setRGB(1, 1, 0xFFFF00); 

        int[][] binary = binarizer.toBinaryArray(img);
        assertEquals(1, binary[0][0]); 
        assertEquals(0, binary[0][1]); 
        assertEquals(0, binary[1][0]); 
        assertEquals(0, binary[1][1]); 
    }

    @Test
    public void testSinglePixelImage() {
        // single pixel image should be converted based on the threshold
        BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
        img.setRGB(0, 0, 0xFFFFFF);

        int[][] binary = binarizer.toBinaryArray(img);
        assertEquals(1, binary[0][0]);
    }

    @Test
    public void testToBufferedImageConversion() {
        // test binary array back to an image
        int[][] binaryArray = {
            {1, 0},
            {0, 1}
        };

        BufferedImage img = binarizer.toBufferedImage(binaryArray);
        assertEquals(0xFFFFFF, img.getRGB(0, 0) & 0xFFFFFF);
        assertEquals(0x000000, img.getRGB(1, 0) & 0xFFFFFF);
        assertEquals(0x000000, img.getRGB(0, 1) & 0xFFFFFF);
        assertEquals(0xFFFFFF, img.getRGB(1, 1) & 0xFFFFFF);
    }

    @Test
    public void testExactThresholdMatch() {
        // pixel exactly at the threshold should return 1, or else 0
        DistanceImageBinarizer strictBinarizer = new DistanceImageBinarizer(new MockColorDistanceFinder(), 0xFFFFFF, 0);
        BufferedImage img = new BufferedImage(2, 1, BufferedImage.TYPE_INT_RGB);
        img.setRGB(0, 0, 0xFFFFFF); 
        img.setRGB(1, 0, 0xFFFFFE); 

        int[][] binary = strictBinarizer.toBinaryArray(img);
        assertEquals(1, binary[0][0]);
        assertEquals(0, binary[0][1]);
    }

    @Test
    public void testVeryHighThreshold() {
        // large threshold should should convert all pixels to 1
        DistanceImageBinarizer lenientBinarizer = new DistanceImageBinarizer(new MockColorDistanceFinder(), 0xFFFFFF, 1000);
        BufferedImage img = new BufferedImage(2, 1, BufferedImage.TYPE_INT_RGB);
        img.setRGB(0, 0, 0x000000);
        img.setRGB(1, 0, 0x123456);

        int[][] binary = lenientBinarizer.toBinaryArray(img);
        assertEquals(1, binary[0][0]);
        assertEquals(1, binary[0][1]);
    }
}
