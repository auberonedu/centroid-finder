package io.github.AugleBoBaugles.centroidFinder;

import org.junit.jupiter.api.Test;
import io.github.AugleBoBaugles.centroidFinder.EuclideanColorDistance;
import static org.junit.jupiter.api.Assertions.*;

// COLORS USED IN TESTS (Google Color Picker: https://g.co/kgs/DG41Bvm)
// Also from Rapid Tables: https://www.rapidtables.com/web/color/RGB_Color.html
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

    @Test 
    public void testDistance_redVersusBlue() {
        // blue, red: (30, 50, 150), (220, 30, 30)
        double expected = Math.sqrt(Math.pow(30 - 220,2) + Math.pow(50 - 30,2) + Math.pow(150 - 30,2));

        EuclideanColorDistance distanceTester = new EuclideanColorDistance();
        // blue, red: 1e3296, dc1e1e
        double actual = distanceTester.distance(0x1e3296, 0xdc1e1e);

        assertEquals(expected, actual, 0.00001);
    }

    // Red to Blue Distance with 0 alpha value (assuming argb NOT rgba)
    @Test
    public void testDistance_redVersusBlueWithAlphaZero(){
        // blue, red: (0, 30, 50, 150), (0, 220, 30, 30)
        double expected = Math.sqrt(Math.pow(30 - 220,2) + Math.pow(50 - 30,2) + Math.pow(150 - 30,2));

        EuclideanColorDistance distanceTester = new EuclideanColorDistance();
        // blue, red: 1e3296, dc1e1e
        double actual = distanceTester.distance(0x001e3296, 0x00dc1e1e);

        assertEquals(expected, actual, 0.00001);
    }

    // hexToRGB tests (converts hex int into int[] R,G,B)
    // Default Red - via Rapid Tables
    @Test
    public void testHexToRGB_DefaultRed() {
        int[] expected = new int[]{0, 255, 0, 0};
        int[] actual = EuclideanColorDistance.hexToRGB(0xFF0000);

        assertArrayEquals(expected, actual);
    }
    
    // A Shade of Red
    @Test
    public void testHexToRGB_ShadeOfRed() {
        int[] expected = new int[]{0, 220, 30, 30};
        int[] actual = EuclideanColorDistance.hexToRGB(0xdc1e1e);

        assertArrayEquals(expected, actual);
    }
    
    // Default Green - via Rapid Tables
    @Test
    public void testHexToRGB_DefaultGreen() {
        int[] expected = new int[]{0, 0, 255, 0};
        int[] actual = EuclideanColorDistance.hexToRGB(0x00FF00);

        assertArrayEquals(expected, actual);
    }

    // A Shade of Green
    @Test
    public void testHexToRGB_ShadeOfGreen() {
        int[] expected = new int[]{0, 0, 186, 22};
        int[] actual = EuclideanColorDistance.hexToRGB(0x00ba16);

        assertArrayEquals(expected, actual);
    }

    // Default Blue - via Rapid Tables
    @Test
    public void testHexToRGB_DefaultBlue() {
        int[] expected = new int[]{0, 0, 0, 255};
        int[] actual = EuclideanColorDistance.hexToRGB(0x0000FF);

        assertArrayEquals(expected, actual);
    }
    
    // A Shade of Blue
    @Test 
    public void testHexToRGB_ShadeOfBlue() {
        int[] expected = new int[]{0, 30, 50, 150};
        int[] actual = EuclideanColorDistance.hexToRGB(0x1e3296);

        assertArrayEquals(expected, actual);
    }

    // Yellow - via Rapid Tables
    @Test
    public void testHexToRGB_yellow() {
        int[] expected = new int[]{0, 255, 255, 0};
        int[] actual = EuclideanColorDistance.hexToRGB(0xFFFF00);

        assertArrayEquals(expected, actual);
    }

    // Magenta - via Rapid Tables
    @Test
    public void testHexToRGB_magenta() {
        int[] expected = new int[]{0, 255, 0, 255};
        int[] actual = EuclideanColorDistance.hexToRGB(0xFF00FF);

        assertArrayEquals(expected, actual);
    }

    // Red with 0 alpha (assuming argb NOT rgba)
    @Test
    public void testHexToRGB_DefaultRedWithAlpha() {
        int[] expected = new int[]{0, 255, 0, 0};
        int[] actual = EuclideanColorDistance.hexToRGB(0x00FF0000);

        assertArrayEquals(expected, actual);
    }
}
