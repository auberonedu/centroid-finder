import org.junit.jupiter.api.Test;

import java.awt.image.BufferedImage;

import static org.junit.jupiter.api.Assertions.*;

public class DistanceImageBinarizerTest {

    @Test
    public void testAllPixelsBelowThreshold() {
        // 2x2 image, all pixels = 0x123456
        BufferedImage image = new BufferedImage(2, 2, BufferedImage.TYPE_INT_RGB);
        int color = 0x123456;
        image.setRGB(0, 0, color);
        image.setRGB(0, 1, color);
        image.setRGB(1, 0, color);
        image.setRGB(1, 1, color);

        ColorDistanceFinder alwaysLow = (a, b) -> 5.0; // Always below threshold
        DistanceImageBinarizer binarizer = new DistanceImageBinarizer(alwaysLow, 0x000000, 10);

        int[][] expected = {
                {1, 1},
                {1, 1}
        };

        assertArrayEquals(expected, binarizer.toBinaryArray(image));
    }

    @Test
    public void testAllPixelsAboveThreshold() {
        BufferedImage image = new BufferedImage(2, 2, BufferedImage.TYPE_INT_RGB);
        int color = 0xABCDEF;
        image.setRGB(0, 0, color);
        image.setRGB(0, 1, color);
        image.setRGB(1, 0, color);
        image.setRGB(1, 1, color);

        ColorDistanceFinder alwaysHigh = (a, b) -> 100.0; // Always above threshold
        DistanceImageBinarizer binarizer = new DistanceImageBinarizer(alwaysHigh, 0x000000, 10);

        int[][] expected = {
                {0, 0},
                {0, 0}
        };

        assertArrayEquals(expected, binarizer.toBinaryArray(image));
    }

    @Test
    public void testMixedPixels() {
        BufferedImage image = new BufferedImage(2, 2, BufferedImage.TYPE_INT_RGB);

        // Full 32-bit ARGB values (alpha = FF)
        int red = 0xFFFF0000;
        int green = 0xFF00FF00;

        image.setRGB(0, 0, red);   // Top-left
        image.setRGB(0, 1, green); // Bottom-left
        image.setRGB(1, 0, green); // Top-right
        image.setRGB(1, 1, red);   // Bottom-right

        // Mock distance function: only red matches target
        ColorDistanceFinder redMatchOnly = (rgb1, rgb2) -> rgb1 == rgb2 ? 0.0 : 100.0;

        DistanceImageBinarizer binarizer = new DistanceImageBinarizer(redMatchOnly, red, 10);

        int[][] expected = {
                {1, 0},
                {0, 1}
        };

        assertArrayEquals(expected, binarizer.toBinaryArray(image));
    }
}
