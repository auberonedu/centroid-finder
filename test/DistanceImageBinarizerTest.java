import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.awt.image.BufferedImage;

public class DistanceImageBinarizerTest {

    private static class FakeColorDistanceFinder implements ColorDistanceFinder {
        private final double[][] distances;

        public FakeColorDistanceFinder(double[][] distances) {
            this.distances = distances;
        }

        @Override
        public double distance(int a, int b) {
            // Simply return pre-defined distance from the matrix for each pixel
            int y = (a >> 8) & 0xFF;
            int x = a & 0xFF;
            return distances[y][x];
        }
    }

    @Test
    public void testToBinaryArray_WithThreshold() {
        int[][] expectedBinary = {
            {1, 0},
            {0, 1}
        };

        // Create a fake image with pixel colors as (y << 8 | x) so we can track them
        BufferedImage image = new BufferedImage(2, 2, BufferedImage.TYPE_INT_RGB);
        image.setRGB(0, 0, (0 << 8) | 0); // distance = 5
        image.setRGB(1, 0, (0 << 8) | 1); // distance = 10
        image.setRGB(0, 1, (1 << 8) | 0); // distance = 15
        image.setRGB(1, 1, (1 << 8) | 1); // distance = 2

        double[][] fakeDistances = {
            {5, 10},
            {15, 2}
        };

        FakeColorDistanceFinder fakeFinder = new FakeColorDistanceFinder(fakeDistances);
        DistanceImageBinarizer binarizer = new DistanceImageBinarizer(fakeFinder, 0, 6);

        int[][] actual = binarizer.toBinaryArray(image);

        assertArrayEquals(expectedBinary, actual);
    }

    @Test
    public void testToBufferedImage() {
        int[][] binaryArray = {
            {1, 0},
            {0, 1}
        };

        DistanceImageBinarizer binarizer = new DistanceImageBinarizer(null, 0, 0); // finder not used

        BufferedImage result = binarizer.toBufferedImage(binaryArray);

        assertEquals(0xFFFFFF, result.getRGB(0, 0) & 0xFFFFFF);
        assertEquals(0x000000, result.getRGB(1, 0) & 0xFFFFFF);
        assertEquals(0x000000, result.getRGB(0, 1) & 0xFFFFFF);
        assertEquals(0xFFFFFF, result.getRGB(1, 1) & 0xFFFFFF);
    }
}