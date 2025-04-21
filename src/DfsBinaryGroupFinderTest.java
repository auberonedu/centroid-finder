
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.util.List;

public class DfsBinaryGroupFinderTest {

    private final DfsBinaryGroupFinder finder = new DfsBinaryGroupFinder();

    // 1. Empty pixel group
    @Test
    void testEmptyImage() {
        int[][] image = {
            {0, 0},
            {0, 0}
        };
        List<Group> groups = finder.findConnectedGroups(image);
        assertTrue(groups.isEmpty());
    }

    // 2. Single pixel group
    @Test
    void testSinglePixelGroup() {
        int[][] image = {
            {0, 0},
            {0, 1}
        };
        List<Group> groups = finder.findConnectedGroups(image);
        assertEquals(1, groups.size());
        assertEquals(1, groups.get(0).size());
        assertEquals(new Coordinate(1, 1), groups.get(0).centroid());
    }

    // 3. Diagonal 1s should not connect
    @Test
    void testNoDiagonalConnection() {
        int[][] image = {
            {1, 0},
            {0, 1}
        };
        List<Group> groups = finder.findConnectedGroups(image);
        assertEquals(2, groups.size());
        for (Group g : groups) {
            assertEquals(1, g.size());
        }
    }

    // 4. One 4-connected block
    @Test
    void testFourConnectedBlock() {
        int[][] image = {
            {1, 1},
            {1, 1}
        };
        List<Group> groups = finder.findConnectedGroups(image);
        assertEquals(1, groups.size());
        Group g = groups.get(0);
        assertEquals(4, g.size());
        assertEquals(new Coordinate(0, 0), g.centroid()); // integer division of 0+1+0+1 / 4
    }

    // 5. Sorting logic - different sizes
    @Test
    void testSortingBySize() {
        int[][] image = {
            {1, 1, 0},
            {0, 1, 0},
            {0, 0, 1}
        };
        List<Group> groups = finder.findConnectedGroups(image);
        assertEquals(2, groups.size());
        assertTrue(groups.get(0).size() > groups.get(1).size());
    }

    // 6a. Equal size - tiebreaker by x
    @Test
void testEqualSizeTiebreakByX_withEdgeCaseTrigger() {
    int[][] image = {
        {1, 0, 0},  // y=0, x=0
        {0, 0, 0},
        {0, 0, 0},
        {0, 0, 0},
        {0, 0, 1}   // y=4, x=2 → triggers bug when height/width flipped
    };
    List<Group> groups = finder.findConnectedGroups(image);
    assertEquals(2, groups.size());  // This will fail if DFS was broken
    assertTrue(groups.get(0).centroid().x() > groups.get(1).centroid().x());
}


    // 6b. Equal size and x - tiebreaker by y
    @Test
    void testEqualSizeTiebreakByY() {
        int[][] image = {
            {0, 0, 0},
            {0, 1, 0},
            {0, 0, 0},
            {0, 1, 0}
        };
        List<Group> groups = finder.findConnectedGroups(image);
        assertEquals(2, groups.size());
        assertTrue(groups.get(0).centroid().x() == groups.get(1).centroid().x());
        assertTrue(groups.get(0).centroid().y() > groups.get(1).centroid().y());
    }

    // 7. Non-rectangular data
    @Test
    void testNonRectangularArrayThrowsException() {
        int[][] image = {
            {1, 0},
            {1}
        };
        assertThrows(IllegalArgumentException.class, () -> finder.findConnectedGroups(image));
    }

    // 8a. Null input array
    @Test
    void testNullInputThrowsNPE() {
        assertThrows(NullPointerException.class, () -> finder.findConnectedGroups(null));
    }

    // 8b. Null row inside array
    @Test
    void testNullRowThrowsNPE() {
        int[][] image = {
            null,
            {1, 1}
        };
        assertThrows(NullPointerException.class, () -> finder.findConnectedGroups(image));
    }

    // 9. Checkerboard pattern
    @Test
    void testCheckerboardPatternNoFalseGroups() {
        int[][] image = {
            {1, 0, 1},
            {0, 1, 0},
            {1, 0, 1}
        };
        List<Group> groups = finder.findConnectedGroups(image);
        assertEquals(5, groups.size());
        for (Group g : groups) {
            assertEquals(1, g.size());
        }
    }

    // 10. Odd shaped group (like a salamander)
    @Test
    void testOddShapedGroup() {
        int[][] image = {
            {0, 1, 0},
            {1, 1, 1},
            {0, 1, 0},
            {0, 1, 0}
        };
        List<Group> groups = finder.findConnectedGroups(image);
        assertEquals(1, groups.size());
        Group g = groups.get(0);
        assertEquals(6, g.size());
        assertEquals(new Coordinate(1, 1), g.centroid()); // central symmetry
    }
}
