import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

public class DfsBinaryGroupFinderTest {

    private DfsBinaryGroupFinder finder;

    @BeforeEach
    public void setUp() {
        finder = new DfsBinaryGroupFinder();
    }

    @Test
    public void testValidImage() {
        int[][] image = {
                {1, 0, 1},
                {1, 1, 0},
                {0, 1, 1}
        };

        List<Group> groups = finder.findConnectedGroups(image);

        // Expecting 2 groups:
        // Group 1: (2,1) and (1,0) with centroid (1,0), size = 2
        // Group 2: (0,0), (0,2), (2,2) with centroid (0,2), size = 3
        assertEquals(2, groups.size());
        assertEquals(3, groups.get(0).size());  // Largest group should be first
        assertEquals(2, groups.get(1).size());
    }

    @Test
    public void testImageWithOneGroup() {
        int[][] image = {
                {1, 1, 1},
                {1, 1, 1},
                {1, 1, 1}
        };

        List<Group> groups = finder.findConnectedGroups(image);

        // Expecting 1 group with size 9 (full image)
        assertEquals(1, groups.size());
        assertEquals(9, groups.get(0).size());
    }

    @Test
    public void testEmptyImage() {
        int[][] image = new int[0][0];

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            finder.findConnectedGroups(image);
        });

        assertEquals("The array is Invalid.", thrown.getMessage());
    }

    @Test
    public void testNullImage() {
        int[][] image = null;

        NullPointerException thrown = assertThrows(NullPointerException.class, () -> {
            finder.findConnectedGroups(image);
        });

        assertEquals("The image array cannot be null.", thrown.getMessage());
    }

    @Test
    public void testImageWithNullRow() {
        int[][] image = {
                {1, 0},
                null,
                {0, 1}
        };

        NullPointerException thrown = assertThrows(NullPointerException.class, () -> {
            finder.findConnectedGroups(image);
        });

        assertEquals("The subarrays cannot be null.", thrown.getMessage());
    }

    @Test
    public void testImageWithNoConnectedPixels() {
        int[][] image = {
                {0, 0, 0},
                {0, 0, 0},
                {0, 0, 0}
        };

        List<Group> groups = finder.findConnectedGroups(image);

        // No connected pixels, expect empty result
        assertTrue(groups.isEmpty());
    }

    @Test
    public void testImageWithSinglePixel() {
        int[][] image = {
                {1}
        };

        List<Group> groups = finder.findConnectedGroups(image);

        // Only one group, size 1
        assertEquals(1, groups.size());
        assertEquals(1, groups.get(0).size());
    }

    @Test
    public void testMultipleSmallGroups() {
        int[][] image = {
                {1, 0, 1},
                {0, 1, 0},
                {1, 0, 1}
        };

        List<Group> groups = finder.findConnectedGroups(image);

        // Expecting 4 groups, each of size 1
        assertEquals(4, groups.size());
        assertEquals(1, groups.get(0).size());
        assertEquals(1, groups.get(1).size());
        assertEquals(1, groups.get(2).size());
        assertEquals(1, groups.get(3).size());
    }

    @Test
    public void testImageWithMultipleGroupsAndCentroidCalculation() {
        int[][] image = {
                {1, 0, 0, 1},
                {1, 0, 0, 1},
                {0, 0, 0, 1},
                {0, 1, 1, 1}
        };

        List<Group> groups = finder.findConnectedGroups(image);

        // Group 1: (0,0), (1,0), (0,3), (1,3) with centroid (1,1), size = 4
        // Group 2: (3,1), (3,2), (3,3) with centroid (3,2), size = 3
        assertEquals(2, groups.size());
        assertEquals(4, groups.get(0).size());
        assertEquals(3, groups.get(1).size());
        assertEquals(1, groups.get(1).centroid().x());
        assertEquals(3, groups.get(1).centroid().y());
    }
}
