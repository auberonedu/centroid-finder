import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;


/**
 * AI Used - Unit tests for DfsBinaryGroupFinder implementation.
 */
public class DfsBinaryGroupFinderTest {

    private DfsBinaryGroupFinder finder;

    @BeforeEach
    public void setUp() {
        finder = new DfsBinaryGroupFinder();
    }

    @Test
    public void testLargeConnectedGroup() {
        // All 1s form one large group
        int[][] image = {
            {1, 1, 1},
            {1, 1, 1},
            {1, 1, 1}
        };
        
        List<Group> groups = finder.findConnectedGroups(image);
        assertEquals(1, groups.size());
        assertEquals(9, groups.get(0).size());
        assertEquals(new Coordinate(1, 1), groups.get(0).centroid());
    }

    @Test
    public void testVerticalLineGroup() {
        // Vertical line of 1s
        int[][] image = {
            {0, 1, 0},
            {0, 1, 0},
            {0, 1, 0}
        };
    
        List<Group> groups = finder.findConnectedGroups(image);
        assertEquals(1, groups.size());
        assertEquals(3, groups.get(0).size());
        assertEquals(new Coordinate(1, 1), groups.get(0).centroid());
    }

    @Test
    public void testHorizontalLineGroup() {
        // Horizontal line of 1s
        int[][] image = {
            {0, 0, 0},
            {1, 1, 1},
            {0, 0, 0}
        };
    
        List<Group> groups = finder.findConnectedGroups(image);
        assertEquals(1, groups.size());
        assertEquals(3, groups.get(0).size());
        assertEquals(new Coordinate(1, 1), groups.get(0).centroid());
    }
    

    @Test
    public void testNoGroups() {
        // All 0s, no groups
        int[][] image = {
            {0, 0},
            {0, 0}
        };
    
        List<Group> groups = finder.findConnectedGroups(image);
        assertTrue(groups.isEmpty());
    }

    @Test
    public void testMixedGroupSizes() {
        // Two groups of different sizes - one large and one small
        int[][] image = {
            {1, 1, 0, 1},
            {1, 0, 0, 0},
            {1, 1, 1, 0}
        };
    
        List<Group> groups = finder.findConnectedGroups(image);
        assertEquals(2, groups.size());
    
        Group largest = groups.get(0);
        Group smallest = groups.get(1);
    
        assertEquals(6, largest.size());
        assertEquals(1, smallest.size());
    }

    @Test
    public void testNullImageThrowsException() {
        // Null image should throw an exception
        assertThrows(NullPointerException.class, () -> {
            finder.findConnectedGroups(null);
        });
    }

    @Test
    public void testJaggedArrayThrowsException() {
        // Jagged array (non-rectangular) should throw an exception
        int[][] image = {
            {1, 0},
            null,
            {1, 1}
        };
    
        assertThrows(IllegalArgumentException.class, () -> {
            finder.findConnectedGroups(image);
        });
    }

    @Test
    public void testEmptyArrayThrowsException() {
        // Empty array should throw an exception
        int[][] image = new int[0][0];
    
        assertThrows(IllegalArgumentException.class, () -> {
            finder.findConnectedGroups(image);
        });
    }
}