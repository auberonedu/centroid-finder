import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.awt.Color;
import java.awt.image.BufferedImage;

import org.junit.Test;

public class DistanceImageBinarizerTest {

    @Test
    public void testToBinaryArray_Basic() {
        BufferedImage image = new BufferedImage(2, 2, BufferedImage.TYPE_INT_RGB);
        image.setRGB(0, 0, new Color(255, 255, 255).getRGB());
        image.setRGB(1, 0, new Color(200, 200, 200).getRGB());
        image.setRGB(0, 1, new Color(0, 0, 0).getRGB());
        image.setRGB(1, 1, new Color(50, 50, 50).getRGB());

        ColorDistanceFinder distanceFinder = new EuclideanColorDistance();

        DistanceImageBinarizer imageBinarizer = new DistanceImageBinarizer(distanceFinder, 0xFFFFFF, 100);

        int[][] actual = imageBinarizer.toBinaryArray(image);

        int[][] expected = {
                { 1, 1 },
                { 0, 0 }
        };

        assertArrayEquals(expected, actual);
    }

    @Test
    public void testToBinaryArray_ThresholdBoundary() {
        BufferedImage image = new BufferedImage(2, 1, BufferedImage.TYPE_INT_RGB);
        image.setRGB(0, 0, new Color(250, 250, 250).getRGB()); // very close to white
        image.setRGB(1, 0, new Color(200, 200, 200).getRGB()); // farther from white

        ColorDistanceFinder distanceFinder = new EuclideanColorDistance();
        DistanceImageBinarizer binarizer = new DistanceImageBinarizer(distanceFinder, 0xFFFFFF, 100);

        int[][] actual = binarizer.toBinaryArray(image);

        int[][] expected = {
                { 1, 1 } // 250,250,250 and 200,200,200 are both close enough here
        };

        assertArrayEquals(expected, actual);
    }

    @Test
    public void testToBinaryArray_AllBlackImage() {
        BufferedImage image = new BufferedImage(2, 2, BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < 2; y++) {
            for (int x = 0; x < 2; x++) {
                image.setRGB(x, y, new Color(0, 0, 0).getRGB());
            }
        }

        ColorDistanceFinder distanceFinder = new EuclideanColorDistance();
        DistanceImageBinarizer binarizer = new DistanceImageBinarizer(distanceFinder, 0xFFFFFF, 100);

        int[][] actual = binarizer.toBinaryArray(image);

        int[][] expected = {
                { 0, 0 },
                { 0, 0 }
        };

        assertArrayEquals(expected, actual);
    }

    @Test
    public void testToBinaryArray_AllWhiteImage() {
        BufferedImage image = new BufferedImage(3, 3, BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 3; x++) {
                image.setRGB(x, y, new Color(255, 255, 255).getRGB());
            }
        }

        ColorDistanceFinder distanceFinder = new EuclideanColorDistance();
        DistanceImageBinarizer binarizer = new DistanceImageBinarizer(distanceFinder, 0xFFFFFF, 10);

        int[][] actual = binarizer.toBinaryArray(image);

        int[][] expected = {
                { 1, 1, 1 },
                { 1, 1, 1 },
                { 1, 1, 1 }
        };

        assertArrayEquals(expected, actual);
    }

    @Test
    public void testToBinaryArray_CustomTargetColor() {
        BufferedImage image = new BufferedImage(2, 2, BufferedImage.TYPE_INT_RGB);
        image.setRGB(0, 0, new Color(255, 0, 0).getRGB()); // Red
        image.setRGB(1, 0, new Color(0, 255, 0).getRGB()); // Green
        image.setRGB(0, 1, new Color(0, 0, 255).getRGB()); // Blue
        image.setRGB(1, 1, new Color(255, 0, 10).getRGB()); // Slightly different red

        ColorDistanceFinder distanceFinder = new EuclideanColorDistance();
        DistanceImageBinarizer binarizer = new DistanceImageBinarizer(distanceFinder, 0xFF0000, 50);

        int[][] actual = binarizer.toBinaryArray(image);

        int[][] expected = {
                { 1, 0 },
                { 0, 1 }
        };

        assertArrayEquals(expected, actual);
    }

    @Test
    public void testToBinaryArray_OddlySizesImage() {
        BufferedImage image = new BufferedImage(5, 2, BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < 2; y++) {
            for (int x = 0; x < 5; x++) {
                image.setRGB(x, y, (x + y) % 2 == 0 ? Color.WHITE.getRGB() : Color.BLACK.getRGB());
            }
        }

        ColorDistanceFinder distanceFinder = new EuclideanColorDistance();
        DistanceImageBinarizer binarizer = new DistanceImageBinarizer(distanceFinder, 0xFFFFFF, 10);

        int[][] actual = binarizer.toBinaryArray(image);

        int[][] expected = {
                { 1, 0, 1, 0, 1 },
                { 0, 1, 0, 1, 0 }
        };

        assertArrayEquals(expected, actual);
    }

    @Test 
    public void testToBufferedImage_Basic() {
        int[][] image = {
            {1, 0},
            {0, 1}
        };

        ColorDistanceFinder distanceFinder = new EuclideanColorDistance();
        DistanceImageBinarizer imageBinarizer = new DistanceImageBinarizer(distanceFinder, 0xFFFFFF, 100);
    
        BufferedImage actual = imageBinarizer.toBufferedImage(image);

        int white = 0xFFFFFF;
        int black = 0x000000;

        assertEquals(white, actual.getRGB(0, 0) & 0xFFFFFF);
        assertEquals(black, actual.getRGB(1, 0) & 0xFFFFFF);
        assertEquals(black, actual.getRGB(0, 1) & 0xFFFFFF);
        assertEquals(white, actual.getRGB(1, 1) & 0xFFFFFF);
    }
}