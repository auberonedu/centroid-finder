import static org.junit.Assert.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.Test;

public class EuclideanColorDistanceTest {
    
    @Test
    public void testConvertHexToRGB_Black() {
        EuclideanColorDistance distance = new EuclideanColorDistance();
        assertArrayEquals(new int[] {0, 0, 0}, distance.convertHexToRGB(0x000000));
    }

    @Test
    public void testConvertHexToRGB_White() {
        EuclideanColorDistance distance = new EuclideanColorDistance();
        assertArrayEquals(new int[] {255, 255, 255}, distance.convertHexToRGB(0xFFFFFF));
    }

    @Test
    public void testConvertHexToRGB_Yellow() {
        EuclideanColorDistance distance = new EuclideanColorDistance();
        assertArrayEquals(new int[] {255, 255, 0}, distance.convertHexToRGB(0xFFFF00));
    }

    @Test
    public void testConvertHexToRGB_Blue() {
        EuclideanColorDistance distance = new EuclideanColorDistance();
        assertArrayEquals(new int[] {0, 0, 255}, distance.convertHexToRGB(0x0000FF));
    }

    @Test
    public void testConvertHexToRGB_RandomColor() {
        EuclideanColorDistance distance = new EuclideanColorDistance();
        assertArrayEquals(new int[] {18, 52, 86}, distance.convertHexToRGB(0x123456));
    }

    @Test
    public void testDistance_BlackAndBlack() {
        EuclideanColorDistance distance = new EuclideanColorDistance();
        int black = 0x000000;
        double actual = distance.distance(black, black);
        assertEquals(0.0, actual);
        
    }


}
