import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.awt.image.BufferedImage;

public class DistanceImageBinarizerTest {

    // We'll use a fake color distance finder for simpler test logic
    private static class FakeColorDistanceFinder implements ColorDistanceFinder {
        @Override
        public double distance(int colorA, int colorB) {
            return (colorA == colorB) ? 0.0 : 1000.0;  // exact match = close, else = far
        }
    }

    private final ColorDistanceFinder fakeFinder = new FakeColorDistanceFinder();
    private final int targetColor = 0x123456;
    private final int threshold = 10;  // low threshold: only exact matches pass
    private final DistanceImageBinarizer binarizer = new DistanceImageBinarizer(fakeFinder, targetColor, threshold);

    // 1. Exact match should result in white (1)
    @Test
    void testExactMatchIsWhite() {
        BufferedImage image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
        image.setRGB(0, 0, targetColor);
        int[][] binary = binarizer.toBinaryArray(image);
        assertEquals(1, binary[0][0]);
    }

    // 2. Non-match should result in black (0)
    @Test
    void testNonMatchIsBlack() {
        BufferedImage image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
        image.setRGB(0, 0, 0x000000);  // far from target
        int[][] binary = binarizer.toBinaryArray(image);
        assertEquals(0, binary[0][0]);
    }

    // 3. 2x2 mixed image
    @Test
    void testMixedImageBinaryOutput() {
        BufferedImage img = new BufferedImage(2, 2, BufferedImage.TYPE_INT_RGB);
        img.setRGB(0, 0, targetColor);       // white
        img.setRGB(0, 1, 0x000000);          // black
        img.setRGB(1, 0, targetColor);       // white
        img.setRGB(1, 1, 0xFFFFFF);          // black
        int[][] result = binarizer.toBinaryArray(img);
        assertEquals(1, result[0][0]);
        assertEquals(0, result[1][0]);
        assertEquals(1, result[0][1]);
        assertEquals(0, result[1][1]);
    }

    // 4. Convert binary array to image and check RGB values
    @Test
    void testToBufferedImageOutputColors() {
        int[][] binary = {
            {1, 0},
            {0, 1}
        };
        BufferedImage output = binarizer.toBufferedImage(binary);
        assertEquals(0xFFFFFF, output.getRGB(0, 0)); // white
        assertEquals(0x000000, output.getRGB(1, 0)); // black
        assertEquals(0x000000, output.getRGB(0, 1)); // black
        assertEquals(0xFFFFFF, output.getRGB(1, 1)); // white
    }

    // 5. Binary image should match dimensions of input
    @Test
    void testBinaryArrayMatchesImageSize() {
        BufferedImage image = new BufferedImage(3, 2, BufferedImage.TYPE_INT_RGB);
        int[][] binary = binarizer.toBinaryArray(image);
        assertEquals(2, binary.length);    // height
        assertEquals(3, binary[0].length); // width
    }

    // 6. toBufferedImage should match input size
    @Test
    void testToBufferedImageSize() {
        int[][] binary = new int[4][5];
        BufferedImage image = binarizer.toBufferedImage(binary);
        assertEquals(5, image.getWidth());
        assertEquals(4, image.getHeight());
    }

    // 7. Edge case: null image input
    @Test
    void testToBinaryArrayNullInputThrows() {
        assertThrows(NullPointerException.class, () -> {
            binarizer.toBinaryArray(null);
        });
    }

    // 8. Edge case: null binary array input
    @Test
    void testToBufferedImageNullInputThrows() {
        assertThrows(NullPointerException.class, () -> {
            binarizer.toBufferedImage(null);
        });
    }

    // 9. Edge case: binary array with inconsistent row lengths
    @Test
    void testNonRectangularBinaryArrayThrows() {
        int[][] binary = new int[][] {
            {1, 0, 1},
            {0, 1}
        };
        assertThrows(IllegalArgumentException.class, () -> {
            binarizer.toBufferedImage(binary);
        });
    }

    // 10. All-black binary input to image
    @Test
    void testAllBlackBinaryImage() {
        int[][] binary = {
            {0, 0},
            {0, 0}
        };
        BufferedImage image = binarizer.toBufferedImage(binary);
        assertEquals(0x000000, image.getRGB(0, 0));
        assertEquals(0x000000, image.getRGB(1, 1));
    }
}
