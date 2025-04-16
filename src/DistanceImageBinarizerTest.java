import static org.junit.jupiter.api.Assertions.*;

import java.awt.image.BufferedImage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

//AI used to create Junit testcases for DistanceImageBinarizerTest.java
public class DistanceImageBinarizerTest {

    private static class MockColorDistanceFinder implements ColorDistanceFinder {
        @Override
        public double distance(int color1, int color2) {
            // Simple Euclidean distance between RGB components
            int r1 = (color1 >> 16) & 0xFF;
            int g1 = (color1 >> 8) & 0xFF;
            int b1 = color1 & 0xFF;

            int r2 = (color2 >> 16) & 0xFF;
            int g2 = (color2 >> 8) & 0xFF;
            int b2 = color2 & 0xFF;

            return Math.sqrt(Math.pow(r1 - r2, 2) + Math.pow(g1 - g2, 2) + Math.pow(b1 - b2, 2));
        }
    }

    private DistanceImageBinarizer binarizer;
    private final int targetColor = 0xFFFFFF; // white
    private final int threshold = 100;

    @BeforeEach
    public void setUp() {
        binarizer = new DistanceImageBinarizer(new MockColorDistanceFinder(), targetColor, threshold);
    }

    @Test
    public void testToBinaryArray_AllWhitePixels() {
        BufferedImage img = new BufferedImage(2, 2, BufferedImage.TYPE_INT_RGB);
        img.setRGB(0, 0, 0xFFFFFF);
        img.setRGB(0, 1, 0xFFFFFF);
        img.setRGB(1, 0, 0xFFFFFF);
        img.setRGB(1, 1, 0xFFFFFF);

        int[][] binary = binarizer.toBinaryArray(img);

        assertEquals(1, binary[0][0]);
        assertEquals(1, binary[1][0]);
        assertEquals(1, binary[0][1]);
        assertEquals(1, binary[1][1]);
    }

    @Test
    public void testToBinaryArray_AllBlackPixels() {
        BufferedImage img = new BufferedImage(2, 2, BufferedImage.TYPE_INT_RGB);
        img.setRGB(0, 0, 0x000000);
        img.setRGB(0, 1, 0x000000);
        img.setRGB(1, 0, 0x000000);
        img.setRGB(1, 1, 0x000000);

        int[][] binary = binarizer.toBinaryArray(img);

        assertEquals(0, binary[0][0]);
        assertEquals(0, binary[1][0]);
        assertEquals(0, binary[0][1]);
        assertEquals(0, binary[1][1]);
    }

    @Test
    public void testToBufferedImage() {
        int[][] binaryArray = {
            {1, 0},
            {0, 1}
        };

        BufferedImage image = binarizer.toBufferedImage(binaryArray);

        assertEquals(0xFFFFFF, image.getRGB(0, 0) & 0xFFFFFF); // white
        assertEquals(0x000000, image.getRGB(1, 0) & 0xFFFFFF); // black
        assertEquals(0x000000, image.getRGB(0, 1) & 0xFFFFFF); // black
        assertEquals(0xFFFFFF, image.getRGB(1, 1) & 0xFFFFFF); // white
    }

    @Test
    public void testToBinaryArray_MixedColors() {
        BufferedImage img = new BufferedImage(2, 1, BufferedImage.TYPE_INT_RGB);
        img.setRGB(0, 0, 0xFFFFFF); // close to white
        img.setRGB(1, 0, 0x000000); // far from white

        int[][] binary = binarizer.toBinaryArray(img);

        assertEquals(1, binary[0][0]);
        assertEquals(0, binary[0][1]);
    }
}
