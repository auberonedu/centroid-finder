import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import java.awt.image.BufferedImage;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class BinarizingImageGroupFinderTest {

    // 🔧 Fakes (used by multiple test cases)
    private static class FakeImageBinarizer implements ImageBinarizer {
        private final int[][] binaryToReturn;

        public FakeImageBinarizer(int[][] binaryToReturn) {
            this.binaryToReturn = binaryToReturn;
        }

        @Override
        public int[][] toBinaryArray(BufferedImage image) {
            return binaryToReturn;
        }
    }

    private static class FakeGroupFinder implements BinaryGroupFinder {
        private final List<Group> groupsToReturn;

        public FakeGroupFinder(List<Group> groupsToReturn) {
            this.groupsToReturn = groupsToReturn;
        }

        @Override
        public List<Group> findGroups(int[][] binary) {
            return groupsToReturn;
        }
    }

    @Test
    void testNullImageThrowsException() {
        ImageBinarizer fakeBinarizer = new FakeImageBinarizer(new int[1][1]);
        BinaryGroupFinder fakeFinder = new FakeGroupFinder(List.of());
        BinarizingImageGroupFinder finder = new BinarizingImageGroupFinder(fakeBinarizer, fakeFinder);
        assertThrows(NullPointerException.class, () -> finder.findConnectedGroups(null));
    }

    @Test
    void testEmptyImageReturnsEmptyGroupList() {
        int[][] binary = new int[0][0];
        ImageBinarizer fakeBinarizer = new FakeImageBinarizer(binary);
        BinaryGroupFinder fakeFinder = new FakeGroupFinder(List.of());
        BinarizingImageGroupFinder finder = new BinarizingImageGroupFinder(fakeBinarizer, fakeFinder);

        List<Group> groups = finder.findConnectedGroups(new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB));
        assertTrue(groups.isEmpty());
    }

    // 🧪 Example test using fakes
    @Test
    void testSingleWhitePixelReturnsSingleGroup() {
        int[][] binary = {
                { 0, 1 },
                { 0, 0 }
        };
        Group g = new Group(1, new Coordinate(1, 0));
        ImageBinarizer fakeBinarizer = new FakeImageBinarizer(binary);
        BinaryGroupFinder fakeFinder = new FakeGroupFinder(List.of(g));

        BinarizingImageGroupFinder finder = new BinarizingImageGroupFinder(fakeBinarizer, fakeFinder);
        List<Group> groups = finder.findConnectedGroups(new BufferedImage(2, 2, BufferedImage.TYPE_INT_RGB));

        assertEquals(1, groups.size());
        assertEquals(g, groups.get(0));
    }

    @Test
    void testAllBlackReturnsNoGroups() {
        int[][] binary = {
                { 0, 0 },
                { 0, 0 }
        };
        ImageBinarizer fakeBinarizer = new FakeImageBinarizer(binary);
        BinaryGroupFinder fakeFinder = new FakeGroupFinder(List.of());
        BinarizingImageGroupFinder finder = new BinarizingImageGroupFinder(fakeBinarizer, fakeFinder);

        List<Group> groups = finder.findConnectedGroups(new BufferedImage(2, 2, BufferedImage.TYPE_INT_RGB));
        assertEquals(0, groups.size());
    }

    @Test
    void testMultipleWhitePixelsConnected() {
        int[][] binary = {
                { 1, 1 },
                { 1, 0 }
        };
        Group g = new Group(3, new Coordinate(0, 0));
        ImageBinarizer fakeBinarizer = new FakeImageBinarizer(binary);
        BinaryGroupFinder fakeFinder = new FakeGroupFinder(List.of(g));

        BinarizingImageGroupFinder finder = new BinarizingImageGroupFinder(fakeBinarizer, fakeFinder);
        List<Group> groups = finder.findConnectedGroups(new BufferedImage(2, 2, BufferedImage.TYPE_INT_RGB));
        assertEquals(1, groups.size());
        assertEquals(g, groups.get(0));
    }

    @Test
    void testGroupFinderReturnsFixedGroups() {
        Group g1 = new Group(5, new Coordinate(1, 1));
        Group g2 = new Group(3, new Coordinate(0, 2));
        List<Group> expected = List.of(g1, g2);

        ImageBinarizer fakeBinarizer = new FakeImageBinarizer(new int[3][3]);
        BinaryGroupFinder fakeFinder = new FakeGroupFinder(expected);

        BinarizingImageGroupFinder finder = new BinarizingImageGroupFinder(fakeBinarizer, fakeFinder);
        List<Group> actual = finder.findConnectedGroups(new BufferedImage(3, 3, BufferedImage.TYPE_INT_RGB));

        assertEquals(expected, actual);
    }

    @Test
    void testBinaryArrayPassedToGroupFinderCorrectly() {
        int[][] expectedBinary = {
                { 1, 0 },
                { 0, 1 }
        };

        AtomicBoolean called = new AtomicBoolean(false);

        BinaryGroupFinder spyFinder = new BinaryGroupFinder() {
            @Override
            public List<Group> findGroups(int[][] binary) {
                called.set(true);
                assertArrayEquals(expectedBinary, binary);
                return List.of();
            }
        };

        ImageBinarizer fakeBinarizer = new FakeImageBinarizer(expectedBinary);
        BinarizingImageGroupFinder finder = new BinarizingImageGroupFinder(fakeBinarizer, spyFinder);
        finder.findConnectedGroups(new BufferedImage(2, 2, BufferedImage.TYPE_INT_RGB));

        assertTrue(called.get());
    }

    @Test
    void testLargeBinaryImageDoesNotCrash() {
        int[][] binary = new int[100][100];
        ImageBinarizer fakeBinarizer = new FakeImageBinarizer(binary);
        BinaryGroupFinder fakeFinder = new FakeGroupFinder(List.of());

        BinarizingImageGroupFinder finder = new BinarizingImageGroupFinder(fakeBinarizer, fakeFinder);
        List<Group> result = finder.findConnectedGroups(new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB));

        assertNotNull(result);
    }

    @Test
    void testMixedBinaryMap() {
        int[][] binary = {
                { 1, 0, 0 },
                { 0, 0, 0 },
                { 0, 0, 1 }
        };

        Group g1 = new Group(1, new Coordinate(0, 0));
        Group g2 = new Group(1, new Coordinate(2, 2));

        ImageBinarizer fakeBinarizer = new FakeImageBinarizer(binary);
        BinaryGroupFinder fakeFinder = new FakeGroupFinder(List.of(g1, g2));
        BinarizingImageGroupFinder finder = new BinarizingImageGroupFinder(fakeBinarizer, fakeFinder);

        List<Group> groups = finder.findConnectedGroups(new BufferedImage(3, 3, BufferedImage.TYPE_INT_RGB));
        assertEquals(2, groups.size());
        assertTrue(groups.containsAll(List.of(g1, g2)));
    }

    @Test
    void testIntegrationWithRealComponents() {
        // 1. Create a small 3x3 image where 3 connected pixels match the target color
        BufferedImage img = new BufferedImage(3, 3, BufferedImage.TYPE_INT_RGB);
        int targetColor = 0x123456;
        img.setRGB(0, 0, targetColor);
        img.setRGB(0, 1, targetColor);
        img.setRGB(1, 1, targetColor); // These 3 form a connected L-shape
        img.setRGB(2, 2, 0xFFFFFF); // A distant white pixel, not matching

        // 2. Use real binarizer with exact match threshold
        ColorDistanceFinder finder = (a, b) -> (a == b ? 0.0 : 1000.0); // Fake Euclidean logic
        DistanceImageBinarizer binarizer = new DistanceImageBinarizer(finder, targetColor, 10);

        // 3. Use real DFS group finder
        DfsBinaryGroupFinder groupFinder = new DfsBinaryGroupFinder();

        // 4. Plug them into the real image group finder
        BinarizingImageGroupFinder fullFinder = new BinarizingImageGroupFinder(binarizer, groupFinder);

        // 5. Run the pipeline
        List<Group> groups = fullFinder.findConnectedGroups(img);

        // 6. Check result
        assertEquals(1, groups.size());
        assertEquals(3, groups.get(0).size()); // 3 connected white pixels

        // Optionally check centroid is reasonable (should be center of L-shape)
        Coordinate c = groups.get(0).centroid();
        assertEquals(0, c.x());
        assertEquals(1, c.y());
    }
}
