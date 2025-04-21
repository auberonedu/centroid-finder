
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the EuclideanColorDistance class.
 * This test suite includes both functional color comparisons
 * and edge case validation based on 24-bit RGB color inputs.
 */
public class EuclideanColorDistanceTest {

    private final EuclideanColorDistance distanceFinder = new EuclideanColorDistance();

    // 1. Identical colors should have zero distance
    @Test
    void testSameColor() {
        assertEquals(0.0, distanceFinder.distance(0x112233, 0x112233), 0.01);
    }

    // 2. Red vs Green - large color difference
    @Test
    void testRedVsGreen() {
        assertEquals(360.62, distanceFinder.distance(0xFF0000, 0x00FF00), 0.01);
    }

    // 3. Red vs Blue - large color difference
    @Test
    void testRedVsBlue() {
        assertEquals(360.62, distanceFinder.distance(0xFF0000, 0x0000FF), 0.01);
    }

    // 4. Red vs White - large color difference due to green/blue
    @Test
    void testRedVsWhite() {
        assertEquals(360.62, distanceFinder.distance(0xFF0000, 0xFFFFFF), 0.01);
    }

    // 5. Black vs White - maximum color difference
    @Test
    void testBlackVsWhite() {
        assertEquals(441.67, distanceFinder.distance(0x000000, 0xFFFFFF), 0.01);
    }

    // 6. Small color difference - nearly black vs true black
    @Test
    void testOffByOne() {
        assertEquals(3.74, distanceFinder.distance(0x010203, 0x000000), 0.01);
    }

    // 7. Red vs Dark Red - moderate difference
    @Test
    void testRedVsDarkRed() {
        assertEquals(127.0, distanceFinder.distance(0xFF0000, 0x800000), 0.01);
    }

    // 8. Two close grays - very small distance
    @Test
    void testGrayVsLighterGray() {
        assertEquals(1.73, distanceFinder.distance(0x888888, 0x898989), 0.01);
    }

    // 9. Green vs Blue - full jump between axes
    @Test
    void testGreenVsBlue() {
        assertEquals(360.62, distanceFinder.distance(0x00FF00, 0x0000FF), 0.01);
    }

    // 10. Arbitrary color values - math check
    @Test
    void testCustomColors() {
        assertEquals(99.61, distanceFinder.distance(0x123456, 0x654321), 0.1);
    }

    // 11. Negative input should throw exception
    @Test
    void testNegativeColorThrows() {
        assertThrows(IllegalArgumentException.class, () -> {
            distanceFinder.distance(-1, 0x000000);
        });
    }

    // 12. Value over 0xFFFFFF should throw exception
    @Test
    void testTooLargeColorThrows() {
        assertThrows(IllegalArgumentException.class, () -> {
            distanceFinder.distance(0x1000000, 0x000000);
        });
    }

    // 13. Both values invalid
    @Test
    void testBothInvalidThrow() {
        assertThrows(IllegalArgumentException.class, () -> {
            distanceFinder.distance(-1, 0xABCDEF1); // both bad
        });
    }

    // 14. Explicit black vs black
    @Test
    void testBlackVsBlack() {
        assertEquals(0.0, distanceFinder.distance(0x000000, 0x000000), 0.01);
    }

    // 15. Explicit max value vs max value (white vs white)
    @Test
    void testMaxColorVsMaxColor() {
        assertEquals(0.0, distanceFinder.distance(0xFFFFFF, 0xFFFFFF), 0.01);
    }

    // 16. One valid, one invalid
    @Test
    void testOneBadOneGoodThrows() {
        assertThrows(IllegalArgumentException.class, () -> {
            distanceFinder.distance(0xFFFFFF, -42); // second one invalid
        });
    }
}
