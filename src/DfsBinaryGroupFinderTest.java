import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

// Used AI to generate and fix tests for DfsBinaryGroupFinder.java
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

        // Group 1: (0,0), (1,0), (1,1), (2,1), (2,2) => size 5
        // Group 2: (0,2) => size 1
        assertEquals(2, groups.size());
        assertEquals(5, groups.get(0).size());  // Largest group
        assertEquals(1, groups.get(1).size());
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

        // 5 separate 1s, each unconnected — 5 groups of size 1
        assertEquals(5, groups.size());
        groups.forEach(g -> assertEquals(1, g.size()));
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

        assertEquals(2, groups.size());

        Group group1 = groups.get(0); // Largest group first
        Group group2 = groups.get(1);

        assertEquals(6, group1.size());
        assertEquals(2, group2.size());

        // Centroid for group 1: (2,2)
        assertEquals(2, group1.centroid().x());
        assertEquals(2, group1.centroid().y());

        // Centroid for group 2: (0,0)
        assertEquals(0, group2.centroid().x());
        assertEquals(0, group2.centroid().y());
    }
}
