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
        int[][] image = {
            {0, 0},
            {0, 0}
        };
    
        List<Group> groups = finder.findConnectedGroups(image);
        assertTrue(groups.isEmpty());
    }

    @Test
    public void testMixedGroupSizes() {
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
        assertThrows(NullPointerException.class, () -> {
            finder.findConnectedGroups(null);
        });
    }

    @Test
    public void testJaggedArrayThrowsException() {
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
        int[][] image = new int[0][0];
    
        assertThrows(IllegalArgumentException.class, () -> {
            finder.findConnectedGroups(image);
        });
    }




}