package io.github.qopci.centroidFinder;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

// Used AI to generate and fix tests for EuclideanColorDistance.java
public class EuclideanColorDistanceTest {

    private final EuclideanColorDistance distanceFinder = new EuclideanColorDistance();

    @Test
    public void testSameColorReturnsZero() {
        int color = 0x123456;
        assertEquals(0.0, distanceFinder.distance(color, color), 0.0001);
    }

    @Test
    public void testBlackAndWhite() {
        int black = 0x000000;
        int white = 0xFFFFFF;
        double expected = Math.sqrt(255 * 255 * 3);
        assertEquals(expected, distanceFinder.distance(black, white), 0.0001);
    }

    @Test
    public void testRedToGreen() {
        int red = 0xFF0000;
        int green = 0x00FF00;
        double expected = Math.sqrt(255 * 255 * 2);
        assertEquals(expected, distanceFinder.distance(red, green), 0.0001);
    }

    @Test
    public void testColorDistanceExample() {
        int colorA = 0x123456; // RGB: 18, 52, 86
        int colorB = 0x654321; // RGB: 101, 67, 33
        double expected = Math.sqrt(
                Math.pow(18 - 101, 2) +
                Math.pow(52 - 67, 2) +
                Math.pow(86 - 33, 2)
        );
        assertEquals(expected, distanceFinder.distance(colorA, colorB), 0.0001);
    }

    @Test
    public void testBlueToCyan() {
        int blue = 0x0000FF;
        int cyan = 0x00FFFF;
        double expected = Math.sqrt(Math.pow(0 - 0, 2) + Math.pow(0 - 255, 2) + Math.pow(255 - 255, 2));
        assertEquals(expected, distanceFinder.distance(blue, cyan), 0.0001);
    }
}
