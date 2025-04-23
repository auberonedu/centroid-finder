import org.junit.jupiter.api.Test;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BinarizingImageGroupFinderTest {

    private static class DummyBinarizer implements ImageBinarizer {
        private final int[][] binary;

        public DummyBinarizer(int[][] binary) {
            this.binary = binary;
        }

        @Override
        public int[][] toBinaryArray(BufferedImage image) {
            return binary;
        }

        @Override
        public BufferedImage toBufferedImage(int[][] image) {
            return null; // not needed for these tests
        }
    }

    private static class DummyGroupFinder implements BinaryGroupFinder {
        private final List<Group> groups;

        public DummyGroupFinder(List<Group> groups) {
            this.groups = groups;
        }

        @Override
        public List<Group> findConnectedGroups(int[][] image) {
            return groups;
        }
    }

    @Test
    public void testFindConnectedGroups_SingleGroup() {
        int[][] binary = new int[][] {
            {0, 1},
            {1, 1}
        };

        // Manually create the expected group
        Coordinate centroid = new Coordinate(1, 0); // (1+0+1)/3 = 0, (1+1+0)/3 = 0.66 => (0,0) due to int division
        Group group = new Group(3, centroid);

        ImageBinarizer binarizer = new DummyBinarizer(binary);
        BinaryGroupFinder groupFinder = new DummyGroupFinder(List.of(group));
        ImageGroupFinder finder = new BinarizingImageGroupFinder(binarizer, groupFinder);

        List<Group> result = finder.findConnectedGroups(new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB));

        assertEquals(1, result.size());
        assertEquals(group, result.get(0));
    }

    @Test
    public void testFindConnectedGroups_MultipleGroupsSorted() {
        int[][] binary = new int[][] {
            {1, 0},
            {1, 1},
            {0, 1},
        };

        Group largeGroup = new Group(4, new Coordinate(1, 1));
        Group smallGroup = new Group(1, new Coordinate(0, 0));

        List<Group> unsortedGroups = Arrays.asList(smallGroup, largeGroup);

        ImageBinarizer binarizer = new DummyBinarizer(binary);
        BinaryGroupFinder groupFinder = new DummyGroupFinder(unsortedGroups);
        ImageGroupFinder finder = new BinarizingImageGroupFinder(binarizer, groupFinder);

        List<Group> result = finder.findConnectedGroups(new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB));

        assertEquals(2, result.size());
        assertEquals(smallGroup, result.get(0));
        assertEquals(largeGroup, result.get(1));
    }

    @Test
    public void testFindConnectedGroups_EmptyBinaryImage() {
        int[][] binary = new int[][] {{0, 0}, {0, 0}};

        ImageBinarizer binarizer = new DummyBinarizer(binary);
        BinaryGroupFinder groupFinder = new DummyGroupFinder(new ArrayList<>());
        ImageGroupFinder finder = new BinarizingImageGroupFinder(binarizer, groupFinder);

        List<Group> result = finder.findConnectedGroups(new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB));

        assertTrue(result.isEmpty());
    }

    @Test
    public void testFindConnectedGroups_NullBinaryArrayThrows() {
        ImageBinarizer binarizer = new DummyBinarizer(null);

        BinaryGroupFinder groupFinder = new BinaryGroupFinder() {
            @Override
            public List<Group> findConnectedGroups(int[][] image) {
                throw new NullPointerException("Binary array is null");
            }
        };

        ImageGroupFinder finder = new BinarizingImageGroupFinder(binarizer, groupFinder);

        assertThrows(NullPointerException.class, () -> finder.findConnectedGroups(new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB)));
    }
}