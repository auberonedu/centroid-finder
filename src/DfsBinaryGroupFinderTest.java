import static org.junit.Assert.*;
import java.util.*;
import org.junit.Test;

public class DfsBinaryGroupFinderTest {

    @Test
    public void testSingleGroupAndIsolatedPixel() {
        int[][] image = {
                {1, 1, 0},
                {1, 0, 0},
                {0, 0, 1}
        };

        DfsBinaryGroupFinder finder = new DfsBinaryGroupFinder();
        List<Group> groups = finder.findConnectedGroups(image);

        assertEquals(2, groups.size());

        // First group (3 pixels): (0,0), (0,1), (1,0)
        Group g1 = groups.get(0);
        assertEquals(3, g1.size());
        assertEquals(0, g1.centroid().x()); // (0+1+0)/3 = 0
        assertEquals(0, g1.centroid().y()); // (0+0+1)/3 = 0

        // Second group (1 pixel): (2,2)
        Group g2 = groups.get(1);
        assertEquals(1, g2.size());
        assertEquals(2, g2.centroid().x());
        assertEquals(2, g2.centroid().y());
    }

    @Test
    public void testNoGroups() {
        int[][] image = {
                {0, 0},
                {0, 0}
        };

        DfsBinaryGroupFinder finder = new DfsBinaryGroupFinder();
        List<Group> groups = finder.findConnectedGroups(image);

        assertTrue(groups.isEmpty());
    }

    @Test
    public void testThreeSeparateGroups() {
        int[][] image = {
                {1, 0, 1},
                {0, 0, 0},
                {1, 1, 0}
        };

        DfsBinaryGroupFinder finder = new DfsBinaryGroupFinder();
        List<Group> groups = finder.findConnectedGroups(image);

        assertEquals(3, groups.size());

        // Groups should be sorted: largest first
        Group largest = groups.get(0);
        assertEquals(2, largest.size());
        assertEquals(0, largest.centroid().x()); // (0+1)/2 = 0
        assertEquals(2, largest.centroid().y()); // (2+2)/2 = 2

        Group middle = groups.get(1);
        assertEquals(1, middle.size());
        assertEquals(2, middle.centroid().x());
        assertEquals(0, middle.centroid().y());

        Group smallest = groups.get(2);
        assertEquals(1, smallest.size());
        assertEquals(0, smallest.centroid().x());
        assertEquals(0, smallest.centroid().y());
    }
}
