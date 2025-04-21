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
            {1, 1},
            {0, 0}
        };

        assertArrayEquals(expected, actual);
    }
}
