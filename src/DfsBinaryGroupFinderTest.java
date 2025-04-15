import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class DfsBinaryGroupFinderTest {

    @Test
    public void findConnectedGroups_nullArray(){
        BinaryGroupFinder finder = new DfsBinaryGroupFinder();
        int[][] image = null;

        Exception exception = assertThrows(NullPointerException.class, () -> {
            finder.findConnectedGroups(image);
        });
    }

    @Test
    public void findConnectedGroups_nullSubArray(){
        BinaryGroupFinder finder = new DfsBinaryGroupFinder();
        int[][] image = {
            null,
            {0, 0, 0}
        };

        Exception exception = assertThrows(NullPointerException.class, () -> {
            finder.findConnectedGroups(image);
        });
    }

    @Test
    public void findConnectedGroups_invalidArray(){
        BinaryGroupFinder finder = new DfsBinaryGroupFinder();
        int[][] image = {
            {2, 3, 3, 3, 3},
            {3, 2, 2, 3, 3},
            {2, 3, 2, 3, 2},
            {2, 2, 3, 3, 2}
        };

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            finder.findConnectedGroups(image);
        });
    }

    

    @Test
    public void findConnectedGroups_oneGroup(){
        BinaryGroupFinder finder = new DfsBinaryGroupFinder();

        List<Group> expected = Arrays.asList(new Group(8, new Coordinate(3, 1)));
        int[][] image = {
            {0, 0, 0, 1, 0, 0, 0},
            {0, 0, 1, 1, 1, 0, 0},
            {0, 0, 0, 1, 1, 0, 0},
            {0, 0, 0, 1, 1, 0, 0},
            {0, 0, 0, 0, 0, 0, 0}
        };

        List<Group> actual = finder.findConnectedGroups(image);
        assertEquals(expected, actual);
    }

    @Test
    public void findConnectedGroups_varyingGroupSizes(){
        BinaryGroupFinder finder = new DfsBinaryGroupFinder();

        List<Group> expected = Arrays.asList(new Group(6, new Coordinate(2, 5)),
        new Group(5, new Coordinate(3, 2)), new Group(3, new Coordinate(5, 6)),
        new Group(2, new Coordinate(2, 0)));
        int[][] image = {
            {0, 0, 1, 0, 0, 0, 0},
            {0, 0, 1, 0, 1, 0, 0},
            {0, 0, 0, 1, 1, 0, 0},
            {0, 0, 0, 1, 1, 0, 0},
            {0, 0, 0, 0, 0, 0, 0},
            {0, 1, 1, 1, 1, 0, 0},
            {0, 0, 1, 1, 0, 0, 1},
            {0, 0, 0, 0, 0, 1, 1}
        };

        List<Group> actual = finder.findConnectedGroups(image);
        assertEquals(expected, actual);
    }
}
