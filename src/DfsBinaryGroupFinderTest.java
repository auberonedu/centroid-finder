

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

    // get coordinates
        // check expected size
        // check expected values
        // ? no 1 in image

        @Test
        public void testCoordinates_threeCoordinatesOneGroupStartTop() {
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

            assertEquals(3, coords.size());
        }

        @Test
        public void testCoordinates_threeCoordinatesOneGroupStartMiddle() {
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

            assertEquals(3, coords.size());
        }

        @Test
        public void testCoordinates_threeCoordinatesOneGroupStartBottom() {
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

            assertEquals(3, coords.size());
        }

        @Test
        public void testCoordinates_twoCoordinatesMultipleGroups() {
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

            assertEquals(2, coords.size());
        }

        @Test
        public void testCoordinates_oneCoordinateMultipleGroups() {
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

            assertEquals(1, coords.size());
        }


        @Test
        public void testCoordinates_rowOutOfBoundsSmall() {
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

            assertEquals(0, coords.size());
        }

        @Test
        public void testCoordinates_rowOutOfBoundsBig() {
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

            assertEquals(0, coords.size());
        }

    // get area
        // size is zero
        // size is 1
        // size is ridiculously huge
        // coords is empty
        // coords is null

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
