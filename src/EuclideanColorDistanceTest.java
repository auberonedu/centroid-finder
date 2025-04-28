import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class EuclideanColorDistanceTest {

    private final EuclideanColorDistance distanceFinder = new EuclideanColorDistance();

    @Test
    void testSameColorShouldReturnZero() {
        int color = 0x336699;
        double result = distanceFinder.distance(color, color);
        assertEquals(0.0, result, 0.0001);
    }
    @Test
    void testBlackVsWhite(){ 
        double result = distanceFinder.distance(0x000000, 0xFFFFFF);
        assertEquals(441.67, result, 0.01);

    }

    @Test
    void testCloseColors() {
        int color1 = 0x112233;
        int color2 = 0x113344;
        int dr = 0x11 - 0x11;
        int dg = 0x33 - 0x22;
        int db = 0x44 - 0x33;
        double expected = Math.sqrt(dr * dr + dg * dg + db * db);
        double result = distanceFinder.distance(color1, color2);
        assertEquals(expected, result, 0.0001);
    }

    @Test
    void testColorWithMaxRedOnly() {
        int maxRed = 0xFF0000;
        int black = 0x000000;
        double expected = 255.0;
        assertEquals(expected, distanceFinder.distance(maxRed, black), 0.0001);
    }

    @Test
    void testColorWithMaxGreenOnly() {
        int maxGreen = 0x00FF00;
        int black = 0x000000;
        double expected = 255.0;
        assertEquals(expected, distanceFinder.distance(maxGreen, black), 0.0001);
    }

    @Test
    void testColorWithMaxBlueOnly() {
        int maxBlue = 0x0000FF;
        int black = 0x000000;
        double expected = 255.0;
        assertEquals(expected, distanceFinder.distance(maxBlue, black), 0.0001);
    }

    @Test 
    void testRandomColor() {
        int colorA = 0x135955;
        int colorB = 0x3E918B;

        int dr = 0x13 - 0x3E;
        int dg = 0x59 - 0x91;
        int db = 0x55 - 0x8B;

        double expected = Math.sqrt(dr * dr + dg * dg + db * db);
        double result = distanceFinder.distance(colorA, colorB);
        assertEquals(expected, result, 0.0001);
    }

}