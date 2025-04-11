import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class DfsBinaryGroupFinderTest {

    //AI used to generate JUNIT tests for DfsBinaryGroupFinder.java
    private final DfsBinaryGroupFinder finder = new DfsBinaryGroupFinder();

    @Test
    public void testSingleGroup() {
        int[][] image = {
            {1, 1},
            {1, 1}
        };

        List<Group> groups = finder.findConnectedGroups(image);
        assertEquals(1, groups.size());
        assertEquals(4, groups.get(0).getSize()); // assuming getSize() exists
    }

    @Test
    public void testTwoDisconnectedGroups() {
        int[][] image = {
            {1, 0},
            {0, 1}
        };

        List<Group> groups = finder.findConnectedGroups(image);
        assertEquals(2, groups.size());
        assertTrue(groups.stream().allMatch(g -> g.getSize() == 1));
    }

    @Test
    public void testEmptyImage() {
        int[][] image = {
            {0, 0},
            {0, 0}
        };

        List<Group> groups = finder.findConnectedGroups(image);
        assertEquals(0, groups.size());
    }

    @Test
    public void testInvalidImageThrows() {
        int[][] image = new int[0][0];
        assertThrows(IllegalArgumentException.class, () -> finder.findConnectedGroups(image));
    }

    @Test
    public void testNullSubarrayThrows() {
        int[][] image = {
            {1, 1},
            null
        };

        assertThrows(NullPointerException.class, () -> finder.findConnectedGroups(image));
    }

    @Test
    public void testNullImageThrows() {
        assertThrows(NullPointerException.class, () -> finder.findConnectedGroups(null));
    }
}
