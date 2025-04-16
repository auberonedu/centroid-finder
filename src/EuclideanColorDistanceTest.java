import static org.junit.Assert.*;
import org.junit.Test;

// COLORS USED IN TESTS (Google Color Picker: https://g.co/kgs/DG41Bvm)
/*

BLUE
1e3296
30, 50, 150

RED
dc1e1e
220, 30, 30

 */

public class EuclideanColorDistanceTest {
    // distance tests (input: hex int colorA and colorB, output: double distance between them)

    @Test void testDistance_redVersusBlue() {
        // blue, red: (30, 50, 150), (220, 30, 30)
        double expected = Math.sqrt((30 - 220)^2 + (50 - 30)^2 + (150 - 30)^2);

        EuclideanColorDistance distanceTester = new EuclideanColorDistance();
        // blue, red: 1e3296, dc1e1e
        double actual = distanceTester.distance(0x1e3296, 0xdc1e1e);

        assertEquals(expected, actual);

    }


    // hexToRGB tests (converts hex int into int[] R,G,B)

    @Test 
    public void testHexToRGB_blue() {
        int[] expected = new int[]{30, 50, 150};
        int[] actual = EuclideanColorDistance.hexToRGB(0x1e3296);

        assertArrayEquals(expected, actual);
    }

    public void testHexToRGB_red() {
        int[] expected = new int[]{220, 30, 30};
        int[] actual = EuclideanColorDistance.hexToRGB(0xdc1e1e);

        assertArrayEquals(expected, actual);
    }

    //BLUE
    // 1e3296
    // 30, 50, 150

    // RED
    //dc1e1e
    //220, 30, 30
}
