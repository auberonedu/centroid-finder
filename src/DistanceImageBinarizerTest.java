import org.junit.jupiter.api.Test;
import java.awt.image.BufferedImage;
import static org.junit.jupiter.api.Assertions.*;


public class DistanceImageBinarizerTest {
    static class FixedDistanceFinder implements ColorDistanceFinder {
        @Override
        public double distance(int c1, int c2) {
            // Make the result predictable:
            // if c1 == 0x111111 -> return 90
            // if c1 == 0x222222 -> return 120
            // if c1 == 0x333333 -> return 50
            // else return 150
            return switch (c1) {
                case 0x111111 -> 90;
                case 0x222222 -> 120;
                case 0x333333 -> 50;
                default -> 150;
            };
        }
    }

    @Test
    void testToBinaryArray() {
        ColorDistanceFinder finder = new FixedDistanceFinder();
        int targetColor = 0x000000; // Doesn't matter in stub
        int threshold = 100;

        DistanceImageBinarizer binarizer = new DistanceImageBinarizer(finder, targetColor, threshold);

        BufferedImage inputImage = new BufferedImage(2, 2, BufferedImage.TYPE_INT_RGB);
        inputImage.setRGB(0, 0, 0x111111); // 90 < 100 -> white (1)
        inputImage.setRGB(1, 0, 0x222222); // 120 > 100 -> black (0)
        inputImage.setRGB(0, 1, 0x333333); // 50 < 100 -> white (1)
        inputImage.setRGB(1, 1, 0x444444); // 150 > 100 -> black (0)

        int[][] expected = {
            {1, 0},
            {1, 0}
        };

        int[][] result = binarizer.toBinaryArray(inputImage);

        assertArrayEquals(expected[0], result[0]);
        assertArrayEquals(expected[1], result[1]);
    }

    @Test
    void testToBufferedImage() {
        DistanceImageBinarizer binarizer = new DistanceImageBinarizer(new FixedDistanceFinder(), 0, 0);

        int[][] binaryArray = {
            {1, 0},
            {0, 1}
        };

        BufferedImage result = binarizer.toBufferedImage(binaryArray);

        assertEquals(0xFFFFFF, result.getRGB(0, 0) & 0xFFFFFF);
        assertEquals(0x000000, result.getRGB(1, 0) & 0xFFFFFF);
        assertEquals(0x000000, result.getRGB(0, 1) & 0xFFFFFF);
        assertEquals(0xFFFFFF, result.getRGB(1, 1) & 0xFFFFFF);
    }
}


