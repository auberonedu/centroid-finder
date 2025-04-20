import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class EuclideanColorDistanceTest {

    @Test
    public void testconverColor_black() {
        int[] rgb = EuclideanColorDistance.convertColor(0x000000);
        assertArrayEquals(new int[]{0, 0, 0}, rgb);
    }

    @Test
    public void testconvertColor_white() {
        int[] rgb = EuclideanColorDistance.convertColor(0xFFFFFF);
        assertArrayEquals(new int[]{255, 255, 255}, rgb);
    }

    @Test
    public void testConvertColor_red() {
        int[] rgb = EuclideanColorDistance.convertColor(0xFF0000);
        assertArrayEquals(new int[]{255, 0, 0}, rgb);
    }

    @Test
    public void testConvertColor_green() {
        int[] rgb = EuclideanColorDistance.convertColor(0x00FF00);
        assertArrayEquals(new int[]{0, 255, 0}, rgb);
    }

    @Test
    public void testConvertColor_blue() {
        int[] rgb = EuclideanColorDistance.convertColor(0x0000FF);
        assertArrayEquals(new int[]{0, 0, 255}, rgb);
    }

    @Test
    public void testConvertColor_randomColor() {
        int[] rgb = EuclideanColorDistance.convertColor(0x1A2B3C);
        assertArrayEquals(new int[]{26, 43, 60}, rgb);
    }
}
