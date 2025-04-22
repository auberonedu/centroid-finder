import static org.junit.Assert.*;
import org.junit.Test;
import java.awt.image.BufferedImage;

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
    // TODO: toBufferedImage


    

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
}
