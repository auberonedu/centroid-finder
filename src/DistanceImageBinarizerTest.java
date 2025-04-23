import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.awt.Color;
import java.awt.image.BufferedImage;

class DistanceImageBinarizerTest {

    @Test
    void testToBinaryArray_AllPixelsMatchTargetColor_ShouldBeWhite() {
        BufferedImage image = new BufferedImage(2, 2, BufferedImage.TYPE_INT_RGB);
        int targetColor = new Color(100, 150, 200).getRGB();

        // Set all pixels to the target color
        for (int r = 0; r < 2; r++) {
            for (int c = 0; c < 2; c++) {
                image.setRGB(c, r, targetColor);
            }
        }

        DistanceImageBinarizer binarizer = new DistanceImageBinarizer(new EuclideanColorDistance(), targetColor, 1);
        int[][] result = binarizer.toBinaryArray(image);

        for (int[] row : result) {
            for (int val : row) {
                assertEquals(1, val); // All pixels should be white (1)
            }
        }
    }

    @Test
    void testToBinaryArray_AllPixelsFarFromTargetColor_ShouldBeBlack() {
        BufferedImage image = new BufferedImage(2, 2, BufferedImage.TYPE_INT_RGB);
        int farColor = new Color(0, 0, 0).getRGB(); // Very far from white
        int targetColor = new Color(255, 255, 255).getRGB(); // White

        // Set all pixels to black
        for (int r = 0; r < 2; r++) {
            for (int c = 0; c < 2; c++) {
                image.setRGB(c, r, farColor);
            }
        }

        DistanceImageBinarizer binarizer = new DistanceImageBinarizer(new EuclideanColorDistance(), targetColor, 100);
        int[][] result = binarizer.toBinaryArray(image);

        for (int[] row : result) {
            for (int val : row) {
                assertEquals(0, val); // All pixels should be black (0)
            }
        }
    }

    @Test
    void testToBinaryArray_MixedPixels() {
        BufferedImage image = new BufferedImage(2, 2, BufferedImage.TYPE_INT_RGB);

        int white = new Color(255, 255, 255).getRGB();
        int darkGray = new Color(50, 50, 50).getRGB();

        image.setRGB(0, 0, white);     // Should be white
        image.setRGB(0, 1, darkGray);  // Should be black
        image.setRGB(1, 0, white);     // Should be white
        image.setRGB(1, 1, darkGray);  // Should be black

        DistanceImageBinarizer binarizer = new DistanceImageBinarizer(new EuclideanColorDistance(), white, 100);
        int[][] result = binarizer.toBinaryArray(image);

        assertEquals(1, result[0][0]);
        assertEquals(0, result[0][1]);
        assertEquals(1, result[1][0]);
        assertEquals(0, result[1][1]);
    }

    @Test
    void testToBinaryArray_NullImage_ShouldThrowException() {
        DistanceImageBinarizer binarizer = new DistanceImageBinarizer(new EuclideanColorDistance(), 0, 50);
        assertThrows(NullPointerException.class, () -> binarizer.toBinaryArray(null));
    }
}
