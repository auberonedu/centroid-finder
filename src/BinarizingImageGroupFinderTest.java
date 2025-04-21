import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class BinarizingImageGroupFinderTest {

    // TODO: ADD TESTS

    @Test
    public void test_test() {

        // Define test colors
        int targetColor = 0x00000000;
        int threshold = 150;

        // Create a color distance finder
        ColorDistanceFinder colorDistance = new EuclideanColorDistance();
        // CREATE A BINARIZER (input: color distance finder, int color, int threshold)
        DistanceImageBinarizer binarizer = new DistanceImageBinarizer(colorDistance, targetColor, threshold);
        // Create group finder
        DfsBinaryGroupFinder finder = new DfsBinaryGroupFinder();

        // CREATE AN IMAGE GROUP FINDER (input: image binarizer, binary group finder)
        BinarizingImageGroupFinder testImageGroupFinder = new BinarizingImageGroupFinder(binarizer, finder);

        // Create a 2 x 2 buffered image
        BufferedImage bufferedImage = new BufferedImage(2, 2, BufferedImage.TYPE_INT_RGB);
        bufferedImage.setRGB(0, 0, 0x000000);
        bufferedImage.setRGB(0, 1, 0xFFFFFF);
        bufferedImage.setRGB(1, 0, 0xFFFFFF);
        bufferedImage.setRGB(1, 1, 0x000000);

        List<Group> actual = testImageGroupFinder.findConnectedGroups(bufferedImage);
        List<Group> expected = new ArrayList<>();
        expected.add(new Group(1, new Coordinate(0, 0)));
        expected.add(new Group(1, new Coordinate(1, 1)));

        assertEquals(expected.size(), actual.size());

        
    }
    
}
