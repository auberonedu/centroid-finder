package io.github.brittLiban.centroidFinder;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class EuclideanColorDistanceTest {

    private final EuclideanColorDistance dist = new EuclideanColorDistance();

    /** Identical colors should have distance 0 */
    @Test
    public void identicalColors() {
        int color = 0xABCDEF;
        assertEquals(0.0, dist.distance(color, color), 1e-6);
    }

    /** Black (0x000000) to white (0xFFFFFF) is sqrt(3·255²) */
    @Test
    public void blackToWhite() {
        double expected = Math.sqrt(3 * 255 * 255);
        assertEquals(expected, dist.distance(0x000000, 0xFFFFFF), 1e-6);
    }

    /** Red to green should be sqrt(255² + 255²) */
    @Test
    public void redToGreen() {
        double expected = Math.sqrt(255*255 + 255*255);
        assertEquals(expected, dist.distance(0xFF0000, 0x00FF00), 1e-6);
    }

    /** Blue to yellow should be sqrt(255² + 255² + 255²) */
    @Test
    public void blueToYellow() {
        double expected = Math.sqrt(3 * 255 * 255);
        assertEquals(expected, dist.distance(0x0000FF, 0xFFFF00), 1e-6);
    }

    /** Arbitrary pair of colors */
    @Test
    public void arbitraryPair() {
        int c1 = 0x3366CC; // (51,102,204)
        int c2 = 0xCC9933; // (204,153, 51)
        int dr = 51  - 204;
        int dg = 102 - 153;
        int db = 204 -  51;
        double expected = Math.sqrt(dr*dr + dg*dg + db*db);
        assertEquals(expected, dist.distance(c1, c2), 1e-6);
    }

    /** Distance should be symmetric: d(a,b) == d(b,a) */
    @Test
    public void symmetricDistance() {
        int a = 0x123456;
        int b = 0xFEDCBA;
        assertEquals(dist.distance(a, b), dist.distance(b, a), 1e-6);
    }

    /** White to red: ΔG=255, ΔB=255 → sqrt(2·255²) */
    @Test
    public void whiteToRed() {
        double expected = Math.sqrt(2 * 255 * 255);
        assertEquals(expected, dist.distance(0xFFFFFF, 0xFF0000), 1e-6);
    }

    /** White to green: ΔR=255, ΔB=255 → sqrt(2·255²) */
    @Test
    public void whiteToGreen() {
        double expected = Math.sqrt(2 * 255 * 255);
        assertEquals(expected, dist.distance(0xFFFFFF, 0x00FF00), 1e-6);
    }

    /** White to blue: ΔR=255, ΔG=255 → sqrt(2·255²) */
    @Test
    public void whiteToBlue() {
        double expected = Math.sqrt(2 * 255 * 255);
        assertEquals(expected, dist.distance(0xFFFFFF, 0x0000FF), 1e-6);
    }

    /** Gray shades: 0x808080 to 0x404040 → Δ=64 per channel */
    @Test
    public void grayShades() {
        double expected = Math.sqrt(3 * 64 * 64);
        assertEquals(expected, dist.distance(0x808080, 0x404040), 1e-6);
    }

    /** Red to blue: ΔR=255, ΔG=0, ΔB=-255 → sqrt(2·255²) */
    @Test
    public void redToBlue() {
        double expected = Math.sqrt(2 * 255 * 255);
        assertEquals(expected, dist.distance(0xFF0000, 0x0000FF), 1e-6);
    }
    /** Only red channel differs by 128 — distance should be exactly 128 */
    @Test
    public void onlyRedDifference() {
        double expected = 128.0;
        assertEquals(expected, dist.distance(0x800000, 0x000000), 1e-6);
    }

    /** Only green channel differs by 200 — distance should be exactly 200 */
    @Test
    public void onlyGreenDifference() {
        double expected = 200.0;
        assertEquals(expected, dist.distance(0x00C800, 0x000000), 1e-6);
    }

    /** Only blue channel differs by 10 — distance should be exactly 10 */
    @Test
    public void onlyBlueDifference() {
        double expected = 10.0;
        assertEquals(expected, dist.distance(0x00000A, 0x000000), 1e-6);
    }

    /** Max red difference from 0xFF0000 to black — should equal 255 */
    @Test
    public void maxRedDifference() {
        assertEquals(255.0, dist.distance(0xFF0000, 0x000000), 1e-6);
    }

    /** Max green difference from 0x00FF00 to black — should equal 255 */
    @Test
    public void maxGreenDifference() {
        assertEquals(255.0, dist.distance(0x00FF00, 0x000000), 1e-6);
    }

    /** Max blue difference from 0x0000FF to black — should equal 255 */
    @Test
    public void maxBlueDifference() {
        assertEquals(255.0, dist.distance(0x0000FF, 0x000000), 1e-6);
    }

    /** Mid-gray to black (0x7F7F7F → 0x000000) — each channel differs by 127 */
    @Test
    public void midGrayToBlack() {
        int gray = 0x7F7F7F;
        double expected = Math.sqrt(3 * 127 * 127);
        assertEquals(expected, dist.distance(gray, 0x000000), 1e-6);
    }

    /** Distance should ignore alpha — these colors are the same RGB */
    @Test
    public void ignoresAlphaChannel() {
        int a = 0xFF123456; // alpha = FF
        int b = 0x00123456; // alpha = 00
        assertEquals(0.0, dist.distance(a & 0xFFFFFF, b & 0xFFFFFF), 1e-6);
    }

    /** Symmetry property stress test — d(a, b) == d(b, a) for lots of random RGBs */
    @Test
    public void symmetryStressTest() {
        for (int r = 0; r <= 255; r += 64) {
            for (int g = 0; g <= 255; g += 64) {
                for (int b = 0; b <= 255; b += 64) {
                    int color1 = (r << 16) | (g << 8) | b;
                    int color2 = 0x123456;
                    assertEquals(dist.distance(color1, color2), dist.distance(color2, color1), 1e-6);
                }
            }
        }
    }

}
