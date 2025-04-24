import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Simple, self‑contained JUnit 5 test suite for DfsBinaryGroupFinder.
 * One shared finder instance is enough because the class is stateless.
 */
public class DfsBinaryGroupFinderTest {

    private final DfsBinaryGroupFinder finder = new DfsBinaryGroupFinder();

    /** One pixel → one group of size 1, centroid (0,0) */
    @Test
    public void singlePixel() {
        int[][] img = { { 1 } };
        List<Group> g = finder.findConnectedGroups(img);
        assertEquals(1, g.size());
        assertEquals(1, g.get(0).size());
        assertEquals(new Coordinate(0, 0), g.get(0).centroid());
    }

    /** 2×2 block of ones should return a single 4‑pixel group */
    @Test
    public void fullTwoByTwo() {
        int[][] img = { { 1, 1 }, { 1, 1 } };
        List<Group> g = finder.findConnectedGroups(img);
        assertEquals(1, g.size());
        assertEquals(4, g.get(0).size());
    }

    /** Diagonal ones are not connected, so we expect two separate 1‑pixel groups */
    @Test
    public void diagonalsSeparate() {
        int[][] img = { { 1, 0 }, { 0, 1 } };
        List<Group> g = finder.findConnectedGroups(img);
        assertEquals(2, g.size());
        assertTrue(g.stream().allMatch(x -> x.size() == 1));
    }

    /** Two blobs (sizes 3 and 1) must come back in descending‑size order */
    @Test
    public void twoBlobsSizeOrder() {
        int[][] img = {
                { 1, 1, 0 },
                { 1, 0, 0 },
                { 0, 0, 1 }
        };
        List<Group> g = finder.findConnectedGroups(img);
        assertEquals(2, g.size());
        assertEquals(3, g.get(0).size());   // larger blob first
        assertEquals(1, g.get(1).size());
    }

    /** Tie‑break: single‑pixel blobs sorted by x then y, both descending */
    @Test
    public void tieBreakerOrder() {
        int[][] img = {
                { 1, 0, 1 },
                { 0, 0, 0 },
                { 1, 0, 1 }
        };
        List<Group> g = finder.findConnectedGroups(img);
        assertEquals(new Coordinate(2, 2), g.get(0).centroid()); // highest x,y
        assertEquals(new Coordinate(0, 0), g.get(3).centroid()); // lowest x,y
    }

    /** All‑zero grid should return an empty list */
    @Test
    public void allZeros() {
        int[][] img = { { 0, 0 }, { 0, 0 } };
        assertTrue(finder.findConnectedGroups(img).isEmpty());
    }

    /** Centroid uses integer division (truncates toward 0) */
    @Test
    public void centroidTruncates() {
        int[][] img = { { 1, 1, 0 } };   // pixels at (0,0) and (1,0)
        Group g = finder.findConnectedGroups(img).get(0);
        assertEquals(new Coordinate(0, 0), g.centroid()); // (1/2, 0/2) → (0,0)
    }

    /** Five single pixels spread across grid → five separate 1‑pixel groups */
    @Test
    public void manySingles() {
        int[][] img = {
                { 1, 0, 1 },
                { 0, 1, 0 },
                { 1, 0, 1 }
        };
        List<Group> g = finder.findConnectedGroups(img);
        assertEquals(5, g.size());
        assertTrue(g.stream().allMatch(x -> x.size() == 1));
    }

    /** Passing null must throw NullPointerException */
    @Test
    public void nullImageThrows() {
        assertThrows(NullPointerException.class, () -> finder.findConnectedGroups(null));
    }

    /** Empty 0×0 array must throw IllegalArgumentException */
    @Test
    public void emptyGridThrows() {
        int[][] img = new int[0][0];
        assertThrows(IllegalArgumentException.class, () -> finder.findConnectedGroups(img));
    }

    /** First row null should trigger IllegalArgumentException */
    @Test
    public void firstRowNullThrows() {
        int[][] img = { null };
        assertThrows(IllegalArgumentException.class, () -> finder.findConnectedGroups(img));
    }
    /** 200×200 checkerboard runs quickly and doesn’t throw */
    @Test
    public void checkerboardRunsQuickly() {
        int n = 200;
        int[][] img = new int[n][n];
        for (int y = 0; y < n; y++)
            for (int x = 0; x < n; x++)
                img[y][x] = (x + y) % 2;
        assertDoesNotThrow(() -> finder.findConnectedGroups(img));
    }

    /** Large full‑ones grid should complete without stack overflow (visited works) */
    @Test
    public void visitedPreventsStackOverflow() {
        int[][] img = new int[30][30];
        for (int y = 0; y < 30; y++)
            for (int x = 0; x < 30; x++)
                img[y][x] = 1;
        assertDoesNotThrow(() -> finder.findConnectedGroups(img));
    }

        // new: tie‑break two single‑pixel groups by x then y descending
        @Test
        public void singlePixelTieBreaker() {
        int[][] img = {
                { 0, 1 },
                { 1, 0 }
        };
        List<Group> g = finder.findConnectedGroups(img);
        assertEquals(2, g.size());
        assertEquals(new Coordinate(1, 0), g.get(0).centroid());
        assertEquals(new Coordinate(0, 1), g.get(1).centroid());
        }

}
