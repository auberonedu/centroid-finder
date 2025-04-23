import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DfsBinaryGroupFinderTest {

    private DfsBinaryGroupFinder finder;

    @BeforeEach
    void setUp() {
        finder = new DfsBinaryGroupFinder();
    }

    @Test
    void testSingleGroup() {
        int[][] image = {
            {1, 1, 0},
            {1, 1, 0},
            {0, 0, 0}
        };

        List<Group> groups = finder.findConnectedGroups(image);

        assertEquals(1, groups.size());
        Group group = groups.get(0);
        assertEquals(4, group.size());
        assertEquals(new Coordinate(0, 0), group.centroid()); // Centroid of [(0,0),(0,1),(1,0),(1,1)] is (0+1+0+1)/4 = 0, (0+0+1+1)/4 = 0
    }

    @Test
    void testMultipleGroups() {
        int[][] image = {
            {1, 0, 1},
            {0, 0, 0},
            {0, 0, 1}
        };

        List<Group> groups = finder.findConnectedGroups(image);

        assertEquals(3, groups.size());

        Group first = groups.get(0);
        Group second = groups.get(1);
        Group third = groups.get(2);

        // Both groups have size 1, so order is by descending Y then descending X
        assertEquals(1, first.size());
        assertEquals(new Coordinate(2, 2), first.centroid());

        assertEquals(1, second.size());
        assertEquals(new Coordinate(2, 0), second.centroid());

        assertEquals(1, third.size());
        assertEquals(new Coordinate(0, 0), third.centroid());
    }

    @Test
    void testNullImage() {
        assertThrows(NullPointerException.class, () -> finder.findConnectedGroups(null));
    }

    @Test
    void testNullRow() {
        int[][] image = {
            null,
            {1, 0}
        };
        assertThrows(NullPointerException.class, () -> finder.findConnectedGroups(image));
    }

    @Test
    void testEmptyGroups() {
        int[][] image = {
            {0, 0},
            {0, 0}
        };

        List<Group> groups = finder.findConnectedGroups(image);
        assertTrue(groups.isEmpty());
    }
}