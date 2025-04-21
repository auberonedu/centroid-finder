import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;
import java.util.*;
import java.awt.image.BufferedImage;
import org.junit.Test;

public class BinarizingImageGroupFinderTest {
    // set up fakes to make testing simpler
    class FakeBinarizer implements ImageBinarizer{
        private final int[][] predefinedOutput;

        public FakeBinarizer(int[][] predefinedOutput){
            this.predefinedOutput = predefinedOutput;
        }

        @Override
        public int[][] toBinaryArray(BufferedImage image){
            return predefinedOutput;
        }

        @Override
        public BufferedImage toBufferedImage(int[][] image){
            return new BufferedImage(5, 5, BufferedImage.TYPE_INT_RGB);
        }
    }

    class FakeGroupFinder implements BinaryGroupFinder{
        private final List<Group> predefinedGroups;

        public FakeGroupFinder(List<Group> predefinedGroups){
            this.predefinedGroups = predefinedGroups;
        }

        @Override
        public List<Group> findConnectedGroups(int[][] image){
            return predefinedGroups;
        }
    }

    @Test
    public void testReturnsPredefinedGroups(){
        BufferedImage dummyImage = new BufferedImage(2, 2, BufferedImage.TYPE_INT_RGB);
        int[][] fakeBinary = {
            {1, 0}, 
            {1, 0}
        };

        Group group1 = new Group(2, new Coordinate(1, 0));
        List<Group> predefinedGroups = List.of(group1);

        BinarizingImageGroupFinder groupFinder = new BinarizingImageGroupFinder(new FakeBinarizer(fakeBinary), 
        new FakeGroupFinder(predefinedGroups));

        List<Group> result = groupFinder.findConnectedGroups(dummyImage);

        assertEquals(predefinedGroups, result);
    }

    @Test 
    public void testNoWhitePixels(){
        BufferedImage dummyImage = new BufferedImage(3, 3, BufferedImage.TYPE_INT_RGB);
        int[][] fakeBinary = {
            {0, 0, 0},
            {0, 0, 0},
            {0, 0, 0}
        };

        List<Group> emptyGroups = List.of();
        BinarizingImageGroupFinder groupFinder = new BinarizingImageGroupFinder(new FakeBinarizer(fakeBinary), 
        new FakeGroupFinder(emptyGroups));

        List<Group> result = groupFinder.findConnectedGroups(dummyImage);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testMultipleGroupsDescOrder(){
        BufferedImage dummyImage = new BufferedImage(3, 3, BufferedImage.TYPE_INT_RGB);
    }
}
