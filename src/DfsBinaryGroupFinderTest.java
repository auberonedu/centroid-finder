

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

    // get area
        // size is zero
        // size is 1
        // size is ridiculously huge
        // coords is empty
        // coords is null

        @Test
        public void testGetArea_1() {
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

    // get centroid
        // odds divided correctly (sum5, sum7) / 2 = (2, 3)
        // evens divided correctly (sum4, sum6) / 2 = (2, 3)
        // ? totals of 0 handled
    
}
