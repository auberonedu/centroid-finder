import static org.junit.Assert.assertArrayEquals;

import org.junit.Test;

public class EuclideanColorDistanceTest {
    
    @Test
    public void testConvertHexToRGB_Black() {
        EuclideanColorDistance distance = new EuclideanColorDistance();
        assertArrayEquals(new int[] {0, 0, 0}, distance.convertHexToRGB(0x000000));
    }
}
