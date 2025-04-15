

import static org.junit.Assert.*;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;
import records.Coordinate;
import records.Group;

public class DfsBinaryGroupFinderTest {



    // find Connected Groups
        // image null, NullPointerException
        // image has null row, NullPointerException
        // image row length = 0, IllegalArgumentException
        // image col length = 0; IllegalArgumentException
        // List is SORTED

        @Test
        public void testFindConnectedGroups_oneGroups() {
            // Create image
            int[][] image = new int[][]{
                {0, 1},
                {0, 1},
            };
    
            // Create List of Group(s)
            List<Group> groupsList = new ArrayList<>();
            // Add Group with size and Coordinate centroid
            groupsList.add(new Group(2, new Coordinate(0, 1)));

            // Create instance of DFS
            DfsBinaryGroupFinder test = new DfsBinaryGroupFinder();
            // Save 
            List<Group> actual = test.findConnectedGroups(image);
            System.out.println(actual);

            assertEquals(groupsList, actual);
        }


    @Test
    public void testConnectedGroups_fourGroups() {
        int[][] image = new int[][]{
            {0, 1, 0, 1, 1},
            {0, 1, 0, 0, 1},
            {0, 1, 0, 1, 0},
            {0, 1, 1, 0, 0},
            {0, 1, 0, 1, 1},
        };

        // Create List of Group(s)
        List<Group> groupsList = new ArrayList<>();
        // Add Group with size and Coordinate centroid
        groupsList.add(new Group(6, new Coordinate(2, 1))); 
        groupsList.add(new Group(3, new Coordinate(0, 3)));
        groupsList.add(new Group(2, new Coordinate(4, 3)));
        groupsList.add(new Group(1, new Coordinate(2, 3)));

        // Create instance of DFS
        DfsBinaryGroupFinder test = new DfsBinaryGroupFinder();
        // Save 
        List<Group> actual = test.findConnectedGroups(image);
        System.out.println(actual);

        assertEquals(groupsList, actual);

    }

    // get coordinates

        @Test
        public void testGetCoordinates_threeCoordinatesOneGroupStartTop() {
            // Create image
            int[][] image = new int[][]{
                {0, 1, 0},
                {0, 1, 0},
                {0, 1, 0}
            };

            // Set start coordinates
            int row = 0;
            int col = 1;

            // Create empty array list
            List<Coordinate> coords = new ArrayList<>();
            DfsBinaryGroupFinder.getCoordinates(image, row, col, coords);

            int result = coords.size();
            System.out.println(coords);

            assertEquals(3, result);
        }

        @Test
        public void testGetCoordinates_threeCoordinatesOneGroupStartMiddle() {
            // Create image
            int[][] image = new int[][]{
                {0, 1, 0},
                {0, 1, 0},
                {0, 1, 0}
            };

            // Set start coordinates
            int row = 1;
            int col = 1;

            // Create empty array list
            List<Coordinate> coords = new ArrayList<>();
            DfsBinaryGroupFinder.getCoordinates(image, row, col, coords);

            int result = coords.size();
            System.out.println(coords);

            assertEquals(3, result);
        }

        @Test
        public void testGetCoordinates_threeCoordinatesOneGroupStartBottom() {
            // Create image
            int[][] image = new int[][]{
                {0, 1, 0},
                {0, 1, 0},
                {0, 1, 0}
            };

            // Set start coordinates
            int row = 2;
            int col = 1;

            // Create empty array list
            List<Coordinate> coords = new ArrayList<>();
            DfsBinaryGroupFinder.getCoordinates(image, row, col, coords);

            int result = coords.size();
            System.out.println(coords);

            assertEquals(3, result);
        }

        @Test
        public void testGetCoordinates_twoCoordinatesMultipleGroups() {
            // Create image
            int[][] image = new int[][]{
                {0, 1, 0, 1, 1},
                {0, 1, 0, 0, 1},
                {0, 1, 0, 1, 0},
                {0, 1, 1, 0, 0},
                {0, 1, 0, 1, 1},
            };

            // Set start coordinates
            int row = 4;
            int col = 4;

            // Create empty array list
            List<Coordinate> coords = new ArrayList<>();
            DfsBinaryGroupFinder.getCoordinates(image, row, col, coords);

            int result = coords.size();
            System.out.println(coords);

            assertEquals(2, result);
        }

        @Test
        public void testGetCoordinates_oneCoordinateMultipleGroups() {
            // Create image
            int[][] image = new int[][]{
                {0, 1, 0, 1, 1},
                {0, 1, 0, 0, 1},
                {0, 1, 0, 1, 0},
                {0, 1, 1, 0, 0},
                {0, 1, 0, 1, 1},
            };

            // Set start coordinates
            int row = 2;
            int col = 3;

            // Create empty array list
            List<Coordinate> coords = new ArrayList<>();
            DfsBinaryGroupFinder.getCoordinates(image, row, col, coords);

            int result = coords.size();

            assertEquals(1, result);
        }


        @Test
        public void testGetCoordinates_rowOutOfBoundsSmall() {
            // Create image
            int[][] image = new int[][]{
                {0, 1, 0, 1, 1},
                {0, 1, 0, 0, 1},
                {0, 1, 0, 1, 0},
                {0, 1, 1, 0, 0},
                {0, 1, 0, 1, 1},
            };

            // Set start coordinates
            int row = -1;
            int col = 0;

            // Create empty array list
            List<Coordinate> coords = new ArrayList<>();
            DfsBinaryGroupFinder.getCoordinates(image, row, col, coords);

            int result = coords.size();

            assertEquals(0, result);
        }

        @Test
        public void testGetCoordinates_rowOutOfBoundsBig() {
            // Create image
            int[][] image = new int[][]{
                {0, 1, 0, 1, 1},
                {0, 1, 0, 0, 1},
                {0, 1, 0, 1, 0},
                {0, 1, 1, 0, 0},
                {0, 1, 0, 1, 1},
            };

            // Set start coordinates
            int row = 5;
            int col = 0;

            // Create empty array list
            List<Coordinate> coords = new ArrayList<>();
            DfsBinaryGroupFinder.getCoordinates(image, row, col, coords);

            int result = coords.size();

            assertEquals(0, result);
        }

    // get area

        @Test
        public void testGetArea_zeroCoordinates() {
            // Create a new List of Coordinates with 5 coordinates
            List<Coordinate> coords = new ArrayList<>();
            int area = DfsBinaryGroupFinder.getArea(coords);

            assertEquals(0, area);
        }

        @Test
        public void testGetArea_oneCoordinate() {
            // Create a new List of Coordinates with 5 coordinates
            List<Coordinate> coords = new ArrayList<>();
            int area = DfsBinaryGroupFinder.getArea(coords);
            coords.add(new Coordinate(0, 0));

            assertEquals(1, area);
        }

        @Test
        public void testGetArea_fiveCoordinates() {
            // Create a new List of Coordinates with 5 coordinates
            List<Coordinate> coords = new ArrayList<>();
            coords.add(new Coordinate(0, 0));
            coords.add(new Coordinate(1, 1));
            coords.add(new Coordinate(2, 2));
            coords.add(new Coordinate(3, 3));
            coords.add(new Coordinate(4, 4));

            int area = DfsBinaryGroupFinder.getArea(coords);

            assertEquals(5, area);
        }

        @Test
        public void testGetArea_nullList() {
            // Create a new List of Coordinates with 5 coordinates
            List<Coordinate> coords = null;

            assertThrows(NullPointerException.class, () -> DfsBinaryGroupFinder.getArea(coords));
        }

        @Test
        public void testGetArea_fourHundredAndFourCoordinates() {
            // Create a new List of Coordinates with 5 coordinates
            List<Coordinate> coords = new ArrayList<>();
            for (int i = 0; i < 404; i++) {
                coords.add(new Coordinate(i, 10));
            }

            int area = DfsBinaryGroupFinder.getArea(coords);

            assertEquals(404, area);
        }

    // get centroid
        // odds divided correctly (sum5, sum7) / 2 = (2, 3)
        // evens divided correctly (sum4, sum6) / 2 = (2, 3)
        // ? totals of 0 handled
    
}
