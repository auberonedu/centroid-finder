import static org.junit.Assert.assertEquals;

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

    // test null subarray

    // test empty
    
}
