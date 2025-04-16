import org.junit.Test;

import static org.junit.Assert.assertEquals;

import java.awt.image.BufferedImage;
public class DistanceImageBinarizerTest {
    @Test
    public void testToBinaryArray_oneExactMatch(){
        ColorDistanceFinder distanceFinder = new EuclideanColorDistance();
        
        // target = black
        int targetColor = 0x000000;
        int threshold = 100;

        DistanceImageBinarizer binarizer = new DistanceImageBinarizer(distanceFinder, targetColor, threshold);

        // Creaye 2x1 pixel image
        BufferedImage image = new BufferedImage(2, 1, BufferedImage.TYPE_INT_RGB);
        image.setRGB(0, 0, 0x000000);
        image.setRGB(1, 0, 0xffffff);

        int[][] binary = binarizer.toBinaryArray(image);

        assertEquals(1, binary[0][0]);
        assertEquals(0, binary[0][1]);
    }

    @Test
    public void testToBinaryArray_noMatches(){
        ColorDistanceFinder distanceFinder = new EuclideanColorDistance();
        
        // target = black
        int targetColor = 0x000000;
        int threshold = 100;

        DistanceImageBinarizer binarizer = new DistanceImageBinarizer(distanceFinder, targetColor, threshold);

        // Create 3x1 pixel image
        BufferedImage image = new BufferedImage(3, 1, BufferedImage.TYPE_INT_RGB);
        image.setRGB(0, 0, 0xff0000);
        image.setRGB(1, 0, 0xffffff);
        image.setRGB(2, 0, 0x00ffff);

        int[][] binary = binarizer.toBinaryArray(image);

        assertEquals(0, binary[0][0]);
        assertEquals(0, binary[0][1]);
        assertEquals(0, binary[0][2]);
    }

    @Test
    public void testToBinaryArray_smallThreshold(){
        ColorDistanceFinder distanceFinder = new EuclideanColorDistance();
        
        int targetColor = 0xb69a7b;
        int threshold = 30;

        DistanceImageBinarizer binarizer = new DistanceImageBinarizer(distanceFinder, targetColor, threshold);

        // Create 3x3 pixel image
        BufferedImage image = new BufferedImage(3, 3, BufferedImage.TYPE_INT_RGB);
        image.setRGB(0, 0, 0xb69a7a); // 1
        image.setRGB(1, 0, 0xb69a98); // 1
        image.setRGB(2, 0, 0xb69a9a); // 0
        image.setRGB(0, 1, 0xb69a9f); // 0
        image.setRGB(1, 1, 0xb69a7f); // 1
        image.setRGB(2, 1, 0xbffa7b); // 0
        image.setRGB(0, 2, 0xf6fa7b); // 0
        image.setRGB(1, 2, 0xb69a76); // 1
        image.setRGB(2, 2, 0x00ffff); // 0

        int[][] binary = binarizer.toBinaryArray(image);

        assertEquals(1, binary[0][0]);
        assertEquals(1, binary[0][1]);
        assertEquals(0, binary[0][2]);
        assertEquals(0, binary[1][0]);
        assertEquals(1, binary[1][1]);
        assertEquals(0, binary[1][2]);
        assertEquals(0, binary[2][0]);
        assertEquals(1, binary[2][1]);
        assertEquals(0, binary[2][2]);
    }

    @Test
    public void testToBinaryArray_largeThreshold(){
        ColorDistanceFinder distanceFinder = new EuclideanColorDistance();
        
        // target = black
        int targetColor = 0x000000;
        int threshold = 200;

        DistanceImageBinarizer binarizer = new DistanceImageBinarizer(distanceFinder, targetColor, threshold);

        // Create 3x1 pixel image
        BufferedImage image = new BufferedImage(3, 1, BufferedImage.TYPE_INT_RGB);
        image.setRGB(0, 0, 0xff0000); // diff = 255 
        image.setRGB(1, 0, 0x1f1f1f);
        image.setRGB(2, 0, 0x0000c1); // diff = 192

        int[][] binary = binarizer.toBinaryArray(image);

        assertEquals(0, binary[0][0]);
        assertEquals(1, binary[0][1]);
        assertEquals(1, binary[0][2]);
    }

}
