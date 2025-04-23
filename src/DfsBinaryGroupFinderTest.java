import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

public class DfsBinaryGroupFinderTest {

    private DfsBinaryGroupFinder finder;

    @BeforeEach
    void setUp() {
        finder = new DfsBinaryGroupFinder();
    }

    @Test
    @DisplayName("Test a simple 3x3 grid with one group")
    void testSimpleGrid() {
        int[][] image = {
                { 1, 1, 0 },
                { 1, 1, 0 },
                { 0, 0, 0 }
        };

        List<Group> expected = Collections.singletonList(new Group(4, new Coordinate(0, 0)));
        List<Group> actual = finder.findConnectedGroups(image);

        assertEquals(1, actual.size(), "Should find exactly one group");
        assertEquals(expected.get(0).size(), actual.get(0).size(), "Group should have 4 pixels");
        assertEquals(expected.get(0).centroid().x(), actual.get(0).centroid().x(), "Centroid X should be 0");
        assertEquals(expected.get(0).centroid().y(), actual.get(0).centroid().y(), "Centroid Y should be 0");
    }

    @Test
    @DisplayName("Test grid with multiple groups of different sizes")
    void testMultipleGroups() {
        int[][] image = {
                { 1, 0, 1, 1 },
                { 0, 0, 0, 1 },
                { 1, 1, 0, 0 },
                { 1, 0, 0, 1 }
        };

        List<Group> actual = finder.findConnectedGroups(image);

        assertEquals(4, actual.size(), "Should find exactly 4 groups");

        // Verify groups are sorted by size (descending)
        assertTrue(actual.get(0).size() >= actual.get(1).size(),
                "First group should be larger than or equal to second group");

        // Check the largest group (3 pixels at top right)
        Group largestGroup = actual.get(0);
        assertEquals(3, largestGroup.size(), "Largest group should have 3 pixels");
        assertEquals(2, largestGroup.centroid().x(), "Largest group centroid X should be 2");
        assertEquals(0, largestGroup.centroid().y(), "Largest group centroid Y should be 0");

        // Check other groups
        assertEquals(2, actual.get(1).size(), "Second group should have 2 pixels");
        assertEquals(1, actual.get(2).size(), "Third group should have 1 pixel");
        assertEquals(1, actual.get(3).size(), "Fourth group should have 1 pixel");
    }

    @Test
    @DisplayName("Test empty grid (all zeros)")
    void testEmptyGrid() {
        int[][] image = {
                { 0, 0, 0 },
                { 0, 0, 0 },
                { 0, 0, 0 }
        };

        List<Group> groups = finder.findConnectedGroups(image);

        assertTrue(groups.isEmpty(), "Should find no groups in an empty grid");
    }

    @Test
    @DisplayName("Test full grid (all ones)")
    void testFullGrid() {
        int[][] image = {
                { 1, 1, 1 },
                { 1, 1, 1 },
                { 1, 1, 1 }
        };

        List<Group> expected = Collections.singletonList(new Group(9, new Coordinate(1, 1)));
        List<Group> actual = finder.findConnectedGroups(image);

        assertEquals(1, actual.size(), "Should find exactly one group");
        assertEquals(expected.get(0).size(), actual.get(0).size(), "Group should have 9 pixels");
        assertEquals(expected.get(0).centroid().x(), actual.get(0).centroid().x(), "Centroid X should be 1");
        assertEquals(expected.get(0).centroid().y(), actual.get(0).centroid().y(), "Centroid Y should be 1");
    }

    @Test
    @DisplayName("Test grid with horizontal line")
    void testHorizontalLine() {
        int[][] image = {
                { 0, 0, 0, 0, 0 },
                { 0, 0, 0, 0, 0 },
                { 1, 1, 1, 1, 1 },
                { 0, 0, 0, 0, 0 },
                { 0, 0, 0, 0, 0 }
        };

        List<Group> expected = Collections.singletonList(new Group(5, new Coordinate(2, 2)));
        List<Group> actual = finder.findConnectedGroups(image);

        assertEquals(1, actual.size(), "Should find exactly one group");
        assertEquals(expected.get(0).size(), actual.get(0).size(), "Group should have 5 pixels");
        assertEquals(expected.get(0).centroid().x(), actual.get(0).centroid().x(), "Centroid X should be 2");
        assertEquals(expected.get(0).centroid().y(), actual.get(0).centroid().y(), "Centroid Y should be 2");
    }

    @Test
    @DisplayName("Test grid with vertical line")
    void testVerticalLine() {
        int[][] image = {
                { 0, 0, 1, 0, 0 },
                { 0, 0, 1, 0, 0 },
                { 0, 0, 1, 0, 0 },
                { 0, 0, 1, 0, 0 },
                { 0, 0, 1, 0, 0 }
        };

        List<Group> expected = Collections.singletonList(new Group(5, new Coordinate(2, 2)));
        List<Group> actual = finder.findConnectedGroups(image);

        assertEquals(1, actual.size(), "Should find exactly one group");
        assertEquals(expected.get(0).size(), actual.get(0).size(), "Group should have 5 pixels");
        assertEquals(expected.get(0).centroid().x(), actual.get(0).centroid().x(), "Centroid X should be 2");
        assertEquals(expected.get(0).centroid().y(), actual.get(0).centroid().y(), "Centroid Y should be 2");
    }

    @Test
    @DisplayName("Test grid with groups that require tiebreaking")
    void testGroupTiebreaking() {
        int[][] image = {
                { 1, 1, 0, 0, 0 },
                { 0, 0, 0, 0, 0 },
                { 0, 0, 0, 1, 1 },
                { 0, 0, 0, 0, 0 },
                { 1, 1, 0, 0, 0 }
        };

        List<Group> actual = finder.findConnectedGroups(image);

        assertEquals(3, actual.size(), "Should find exactly 3 groups");

        // All groups have size 2, so they should be sorted by centroid X (descending)
        assertEquals(4, actual.get(0).centroid().y(), "First group should have highest Y");
        assertEquals(2, actual.get(1).centroid().y(), "Second group should have middle Y");
        assertEquals(0, actual.get(2).centroid().y(), "Third group should have lowest Y");
    }

    @Test
    @DisplayName("Test grid with groups of same size and Y but different X")
    void testGroupTiebreakingWithX() {
        int[][] image = {
                { 1, 1, 0, 0, 1, 1 },
                { 0, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 0, 0, 0 }
        };

        List<Group> actual = finder.findConnectedGroups(image);

        assertEquals(2, actual.size(), "Should find exactly 2 groups");

        // Both groups have size 2 and same Y, so they should be sorted by X
        // (descending)
        assertTrue(actual.get(0).centroid().x() > actual.get(1).centroid().x(),
                "First group should have higher X value");
    }

    @Test
    @DisplayName("Test 1x1 grid with one pixel")
    void testSinglePixelGrid() {
        int[][] image = { { 1 } };

        List<Group> expected = Collections.singletonList(new Group(1, new Coordinate(0, 0)));
        List<Group> actual = finder.findConnectedGroups(image);

        assertEquals(1, actual.size(), "Should find exactly one group");
        assertEquals(expected.get(0).size(), actual.get(0).size(), "Group should have 1 pixel");
        assertEquals(expected.get(0).centroid().x(), actual.get(0).centroid().x(), "Centroid X should be 0");
        assertEquals(expected.get(0).centroid().y(), actual.get(0).centroid().y(), "Centroid Y should be 0");
    }

    @Test
    @DisplayName("Test non-rectangular grid throws exception")
    void testNonRectangularGrid() {
        int[][] image = {
                { 1, 1, 1 },
                { 1, 1 },
                { 1, 1, 1 }
        };

        assertThrows(IllegalArgumentException.class,
                () -> finder.findConnectedGroups(image),
                "Should throw IllegalArgumentException for non-rectangular grid");
    }

    @Test
    @DisplayName("Test null grid throws exception")
    void testNullGrid() {
        assertThrows(NullPointerException.class,
                () -> finder.findConnectedGroups(null),
                "Should throw NullPointerException for null grid");
    }

    @Test
    @DisplayName("Test grid with null row throws exception")
    void testGridWithNullRow() {
        int[][] image = {
                { 1, 1, 1 },
                null,
                { 1, 1, 1 }
        };

        assertThrows(NullPointerException.class,
                () -> finder.findConnectedGroups(image),
                "Should throw NullPointerException for grid with null row");
    }

    @Test
    @DisplayName("Test grid with invalid values throws exception")
    void testGridWithInvalidValues() {
        int[][] image = {
                { 1, 1, 1 },
                { 1, 2, 1 }, // Contains a 2, which is invalid
                { 1, 1, 1 }
        };

        assertThrows(IllegalArgumentException.class,
                () -> finder.findConnectedGroups(image),
                "Should throw IllegalArgumentException for grid with invalid values");
    }

    @Test
    @DisplayName("Test complex grid with multiple groups")
    void testComplexGrid() {
        int[][] image = {
                { 1, 1, 0, 0, 1, 1 },
                { 1, 1, 0, 0, 1, 1 },
                { 0, 0, 1, 1, 0, 0 },
                { 0, 0, 1, 1, 0, 0 },
                { 1, 0, 0, 0, 0, 1 }
        };

        List<Group> actual = finder.findConnectedGroups(image);
        System.out.println(actual);
        assertEquals(5, actual.size(), "Should find exactly 5 groups");

        // First group should be the 4-pixel group at top-left
        assertEquals(4, actual.get(0).size(), "First group should have 4 pixels");
        assertEquals(0, actual.get(0).centroid().x(), "First group centroid X should be 0");
        assertEquals(0, actual.get(0).centroid().y(), "First group centroid Y should be 0");

        // Second group should be the 4-pixel group at top-right
        assertEquals(4, actual.get(1).size(), "Second group should have 4 pixels");
        assertEquals(4, actual.get(1).centroid().x(), "Second group centroid X should be 4");
        assertEquals(0, actual.get(1).centroid().y(), "Second group centroid Y should be 0");

        // Third group should be the 4-pixel group in the middle
        assertEquals(4, actual.get(2).size(), "Third group should have 4 pixels");
        assertEquals(2, actual.get(2).centroid().x(), "Third group centroid X should be 2");
        assertEquals(2, actual.get(2).centroid().y(), "Third group centroid Y should be 2");
    }

    @Test
    @DisplayName("Test very large grid with single pixel groups")
    void testVeryLargeGridWithSinglePixelGroups() {
        // Create a 10x10 grid with alternating 1s and 0s (checkerboard pattern)
        int[][] image = new int[10][10];
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                image[i][j] = (i + j) % 2;
            }
        }

        List<Group> actual = finder.findConnectedGroups(image);

        // In a checkerboard pattern, each '1' is isolated and forms its own group
        assertEquals(50, actual.size(), "Should find 50 single-pixel groups");

        // All groups should have size 1
        for (Group group : actual) {
            assertEquals(1, group.size(), "Each group should have exactly 1 pixel");
        }

        // First group should be in the bottom-right corner
        assertEquals(9, actual.get(0).centroid().x(), "First group should have highest X");
        assertEquals(9, actual.get(0).centroid().y(), "First group should have highest Y");
    }

    @Test
    @DisplayName("Test grid with single row")
    void testSingleRowGrid() {
        int[][] image = { { 1, 0, 1, 1, 0, 1 } };

        List<Group> actual = finder.findConnectedGroups(image);

        assertEquals(3, actual.size(), "Should find 3 groups");

        // Check the 2-pixel group
        assertEquals(2, actual.get(0).size(), "First group should have 2 pixels");
        assertEquals(2, actual.get(0).centroid().x(), "First group centroid X should be 2");
        assertEquals(0, actual.get(0).centroid().y(), "First group centroid Y should be 0");

        // Check single-pixel groups
        assertEquals(1, actual.get(1).size(), "Second group should have 1 pixel");
        assertEquals(1, actual.get(2).size(), "Third group should have 1 pixel");
    }

    @Test
    @DisplayName("Test grid with single column")
    void testSingleColumnGrid() {
        int[][] image = { { 1 }, { 0 }, { 1 }, { 1 }, { 0 }, { 1 } };

        List<Group> actual = finder.findConnectedGroups(image);

        assertEquals(3, actual.size(), "Should find 3 groups");

        // Check the 2-pixel group
        assertEquals(2, actual.get(0).size(), "First group should have 2 pixels");
        assertEquals(0, actual.get(0).centroid().x(), "First group centroid X should be 0");
        assertEquals(2, actual.get(0).centroid().y(), "First group centroid Y should be 2");

        // Check single-pixel groups
        assertEquals(1, actual.get(1).size(), "Second group should have 1 pixel");
        assertEquals(1, actual.get(2).size(), "Third group should have 1 pixel");
    }

    @Test
    @DisplayName("Test grid with spiral pattern")
    void testSpiralPatternGrid() {
        int[][] image = {
                { 1, 1, 1, 1, 1 },
                { 0, 0, 0, 0, 1 },
                { 1, 1, 1, 0, 1 },
                { 1, 0, 0, 0, 1 },
                { 1, 1, 1, 1, 1 }
        };

        List<Group> actual = finder.findConnectedGroups(image);

        // Should find 2 groups: one large spiral and one smaller island
        assertEquals(2, actual.size(), "Should find 2 groups");

        // Check the spiral group (outer perimeter)
        Group spiralGroup = actual.get(0);
        assertEquals(16, spiralGroup.size(), "Spiral group should have 16 pixels");

        // Check the inner island
        Group innerGroup = actual.get(1);
        assertEquals(3, innerGroup.size(), "Inner group should have 3 pixels");
        assertEquals(1, innerGroup.centroid().x(), "Inner group centroid X should be 1");
        assertEquals(2, innerGroup.centroid().y(), "Inner group centroid Y should be 2");
    }

    @Test
    @DisplayName("Test grid with centroid calculation edge case")
    void testCentroidCalculationEdgeCase() {
        // Create a grid with a group that has an unusual shape to test centroid
        // calculation
        int[][] image = {
                { 0, 0, 0, 0, 0, 0, 0 },
                { 0, 1, 0, 0, 0, 1, 0 },
                { 0, 0, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 0, 0, 0, 0 },
                { 0, 1, 0, 0, 0, 1, 0 },
                { 0, 0, 0, 0, 0, 0, 0 }
        };

        List<Group> actual = finder.findConnectedGroups(image);

        // Should find 4 single-pixel groups at the corners
        assertEquals(4, actual.size(), "Should find 4 groups");

        // All groups should have equal size
        for (Group group : actual) {
            assertEquals(1, group.size(), "Each group should have 1 pixel");
        }

        // Check sorting: First should be bottom-right
        assertEquals(5, actual.get(0).centroid().x(), "First group should be at bottom-right (X)");
        assertEquals(4, actual.get(0).centroid().y(), "First group should be at bottom-right (Y)");

        // Last should be top-left
        assertEquals(1, actual.get(3).centroid().x(), "Last group should be at top-left (X)");
        assertEquals(1, actual.get(3).centroid().y(), "Last group should be at top-left (Y)");
    }

    @Test
    @DisplayName("Test 1x1 grid with zero")
    void testSinglePixelZeroGrid() {
        int[][] image = { { 0 } };

        List<Group> actual = finder.findConnectedGroups(image);

        assertTrue(actual.isEmpty(), "Should find no groups in a 1x1 grid with 0");
    }

    @Test
    @DisplayName("Test grid with extreme centroid coordinates")
    void testExtremeCentroidCoordinates() {
        // Create a grid with two 1s at extreme positions
        int[][] image = new int[100][100];
        image[0][0] = 1;
        image[99][99] = 1;

        List<Group> actual = finder.findConnectedGroups(image);

        assertEquals(2, actual.size(), "Should find 2 groups");

        // First group should be at bottom-right extreme
        assertEquals(99, actual.get(0).centroid().x(), "First group should be at extreme bottom-right (X)");
        assertEquals(99, actual.get(0).centroid().y(), "First group should be at extreme bottom-right (Y)");

        // Second group should be at top-left extreme
        assertEquals(0, actual.get(1).centroid().x(), "Second group should be at extreme top-left (X)");
        assertEquals(0, actual.get(1).centroid().y(), "Second group should be at extreme top-left (Y)");
    }

    @Test
    @DisplayName("Test grid with alternating L-shaped groups")
    void testAlternatingLShapedGroups() {
        int[][] image = {
                { 1, 0, 1, 0, 1 },
                { 1, 0, 1, 0, 0 },
                { 1, 1, 1, 0, 1 },
                { 0, 0, 0, 0, 1 },
                { 1, 1, 1, 1, 1 }
        };

        List<Group> actual = finder.findConnectedGroups(image);

        // Should have 3 groups: bottom row, right column, and top-left L
        assertEquals(3, actual.size(), "Should find 3 groups");

        // First group should be the bottom row (7 pixels)
        assertEquals(7, actual.get(0).size(), "First group should have 7 pixels");
        assertEquals(2, actual.get(0).centroid().x(), "First group centroid X should be 2");
        assertEquals(4, actual.get(0).centroid().y(), "First group centroid Y should be 4");

        // Second group should be the top-left L shape (5 pixels)
        assertEquals(5, actual.get(1).size(), "Second group should have 5 pixels");

        // Third group should be the right column segment (3 pixels)
        assertEquals(3, actual.get(2).size(), "Third group should have 3 pixels");
        assertEquals(4, actual.get(2).centroid().x(), "Third group centroid X should be 4");
    }
}