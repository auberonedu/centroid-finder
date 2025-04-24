import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.awt.image.BufferedImage;

class DistanceImageBinarizerTest {

    // A fake distance calculator that returns 0 if colors are the same, 9999
    // otherwise
    static class FakeDistanceFinder implements ColorDistanceFinder {
        @Override
        public double distance(int color1, int color2) {
            return (color1 == color2) ? 0 : 9999;
        }
    }

    @Test
    void testToBinaryArray_singleMatchPixel_shouldBeWhite() {
        int width = 1;
        int height = 1;
        int targetColor = 0x112233;

        // Create a 1x1 image with the target color
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        image.setRGB(0, 0, targetColor);

        // Use the fake distance finder
        DistanceImageBinarizer binarizer = new DistanceImageBinarizer(new FakeDistanceFinder(), targetColor, 10);
        int[][] result = binarizer.toBinaryArray(image);

        // The single pixel should be 1 (white)
        assertEquals(1, result[0][0]);
    }

    @Test
    void testToBinaryArray_differentColor_shouldBeBlack() {
        int width = 1;
        int height = 1;
        int targetColor = 0x112233;
        int otherColor = 0x445566;

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        image.setRGB(0, 0, otherColor);

        DistanceImageBinarizer binarizer = new DistanceImageBinarizer(new FakeDistanceFinder(), targetColor, 10);
        int[][] result = binarizer.toBinaryArray(image);

        // The single pixel should be 0 (black)
        assertEquals(0, result[0][0]);
    }

    @Test
    void testToBufferedImage_shouldConvertBinaryArrayToWhiteAndBlackPixels() {
        int[][] binaryArray = {
                { 1, 0 },
                { 0, 1 }
        };

        DistanceImageBinarizer binarizer = new DistanceImageBinarizer(new FakeDistanceFinder(), 0, 0);
        BufferedImage output = binarizer.toBufferedImage(binaryArray);

        // Check specific pixel colors
        assertEquals(0xFFFFFF, output.getRGB(0, 0) & 0xFFFFFF); // white
        assertEquals(0x000000, output.getRGB(1, 0) & 0xFFFFFF); // black
        assertEquals(0x000000, output.getRGB(0, 1) & 0xFFFFFF); // black
        assertEquals(0xFFFFFF, output.getRGB(1, 1) & 0xFFFFFF); // white
    }

    @Test
    void testToBinaryArray_nullImage_throwsException() {
        DistanceImageBinarizer binarizer = new DistanceImageBinarizer(new FakeDistanceFinder(), 0x000000, 10);
        assertThrows(NullPointerException.class, () -> {
            binarizer.toBinaryArray(null);
        });
    }

    @Test
    void testToBinaryArray_exactThresholdMatch_shouldBeWhite() {
        // Custom finder that always returns the threshold
        ColorDistanceFinder exactDistanceFinder = (color1, color2) -> 10.0;

        BufferedImage image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
        image.setRGB(0, 0, 0x123456); // any color

        DistanceImageBinarizer binarizer = new DistanceImageBinarizer(exactDistanceFinder, 0x123456, 10);
        int[][] result = binarizer.toBinaryArray(image);

        assertEquals(1, result[0][0]); // Should be 1 because distance == threshold
    }

    @Test
    void testToBinaryArray_allPixelsMatch_shouldReturnAllOnes() {
        int width = 3;
        int height = 3;
        int targetColor = 0xABCDEF;

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                image.setRGB(x, y, targetColor);
            }
        }

        DistanceImageBinarizer binarizer = new DistanceImageBinarizer(new FakeDistanceFinder(), targetColor, 10);
        int[][] result = binarizer.toBinaryArray(image);

        for (int[] row : result) {
            for (int pixel : row) {
                assertEquals(1, pixel);
            }
        }
    }

    @Test
    void testToBinaryArray_allPixelsFail_shouldReturnAllZeros() {
        int width = 2;
        int height = 2;
        int targetColor = 0x112233;
        int otherColor = 0x998877;

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                image.setRGB(x, y, otherColor);
            }
        }

        DistanceImageBinarizer binarizer = new DistanceImageBinarizer(new FakeDistanceFinder(), targetColor, 10);
        int[][] result = binarizer.toBinaryArray(image);

        for (int[] row : result) {
            for (int pixel : row) {
                assertEquals(0, pixel);
            }
        }
    }

    @Test
    void testToBinaryArray_mixedImage_shouldReturnCorrectBinaryArray() {
        int[][] expected = {
                { 1, 0 },
                { 0, 1 }
        };

        BufferedImage image = new BufferedImage(2, 2, BufferedImage.TYPE_INT_RGB);
        image.setRGB(0, 0, 0x111111); // match
        image.setRGB(1, 0, 0x222222); // no match
        image.setRGB(0, 1, 0x333333); // no match
        image.setRGB(1, 1, 0x111111); // match

        ColorDistanceFinder finder = (c1, c2) -> (c1 == c2 ? 0 : 9999);
        DistanceImageBinarizer binarizer = new DistanceImageBinarizer(finder, 0x111111, 10);
        int[][] result = binarizer.toBinaryArray(image);

        assertArrayEquals(expected[0], result[0]);
        assertArrayEquals(expected[1], result[1]);
    }

    @Test
    void testToBinaryArray_nonSquareImage_shouldReturnCorrectDimensions() {
        BufferedImage image = new BufferedImage(2, 3, BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 2; x++) {
                image.setRGB(x, y, 0x123456); // matching color
            }
        }

        DistanceImageBinarizer binarizer = new DistanceImageBinarizer(new FakeDistanceFinder(), 0x123456, 10);
        int[][] result = binarizer.toBinaryArray(image);

        assertEquals(3, result.length);     
        assertEquals(2, result[0].length);    
    }

    @Test
    void testToBinaryArray_pixelDistanceJustOverThreshold_shouldBeBlack() {
        ColorDistanceFinder almostMatch = (a, b) -> 10.01;

        BufferedImage image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
        image.setRGB(0, 0, 0x123456);

        DistanceImageBinarizer binarizer = new DistanceImageBinarizer(almostMatch, 0x123456, 10);
        int[][] result = binarizer.toBinaryArray(image);

        assertEquals(0, result[0][0]); 
    }
    
    @Test
    void testToBufferedImage_shouldHaveMatchingDimensions() {
        int[][] binary = {
            {1, 0},
            {0, 1},
            {1, 1}
        };

        DistanceImageBinarizer binarizer = new DistanceImageBinarizer(new FakeDistanceFinder(), 0, 0);
        BufferedImage output = binarizer.toBufferedImage(binary);

        assertEquals(2, output.getWidth());
        assertEquals(3, output.getHeight());
    }

}
