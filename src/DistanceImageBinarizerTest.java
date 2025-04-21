import static org.junit.Assert.*;
import org.junit.Test;
import java.awt.image.BufferedImage;

public class DistanceImageBinarizerTest {

    // TODO: ADD TESTS

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


    // toBufferedImage
    
}
