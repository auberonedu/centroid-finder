import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.Test;

public class DfsBinaryGroupFinderTest {

    @Test
    public void testDfsBinaryGroupFinder_Basic() {
        int[][] image = new int[][]{
            {1, 0, 0, 1},
            {0, 1, 0, 0},
            {0, 0, 0, 0},
            {1, 0, 1, 1}
        };

        DfsBinaryGroupFinder finder = new DfsBinaryGroupFinder();
        List<Group> actual = finder.findConnectedGroups(image);

        assertEquals(5, actual.size());
    }

    // test null
    @Test
    public void testDfsBinaryGroupFinder_NullArray() {
        int[][] image = new int[][]{null};

        DfsBinaryGroupFinder finder = new DfsBinaryGroupFinder();

        Exception exception = assertThrows(NullPointerException.class, () -> {
            finder.findConnectedGroups(image);;
        });
        assertEquals("Null array or subarray", exception.getMessage());
       
    }
    
    // test null subarray
    @Test
    public void testDfsBinaryGroupFinder_NullSubArray() {
        int[][] image = new int[][]{
            {1, 0, 0, 1},
            {0, 1, 0, 0},
            null,
            {1, 0, 1, 1},
            {1, 1, 0, 0}
        };

        DfsBinaryGroupFinder finder = new DfsBinaryGroupFinder();

        Exception exception = assertThrows(NullPointerException.class, () -> {
            finder.findConnectedGroups(image);;
        });
        assertEquals("Null array or subarray", exception.getMessage());
       
    }

    // test empty
    @Test
    public void testDfsBinaryGroupFinder_EmptySize() {
        int[][] image = new int[][]{
            {0, 0, 0, 0},
            {0, 0, 0, 0},
            {0, 0, 0, 0},
            {0, 0, 0, 0}
        };

        DfsBinaryGroupFinder finder = new DfsBinaryGroupFinder();
        List<Group> actual = finder.findConnectedGroups(image);

        assertEquals(0, actual.size());
    }
    
}
