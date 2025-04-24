import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.awt.image.BufferedImage;
import java.util.List;

public class BinarizingImageGroupFinderTest {

    private final int MATCH = 0x123456;
    private final int NOMATCH = 0x000000;
    private final int THRESHOLD = 10;
    private final ColorDistanceFinder finder = (a, b) -> (a == b ? 0.0 : 1000.0);
    private final DistanceImageBinarizer binarizer = new DistanceImageBinarizer(finder, MATCH, THRESHOLD);
    private final DfsBinaryGroupFinder groupFinder = new DfsBinaryGroupFinder();

    private final BinarizingImageGroupFinder imageGroupFinder = new BinarizingImageGroupFinder(binarizer, groupFinder);

    @Test
    void testNullImageThrows() {
        assertThrows(NullPointerException.class, () -> imageGroupFinder.findConnectedGroups(null));
    }

    @Test
    void testSingleWhitePixelReturnsOneGroup() {
        BufferedImage img = new BufferedImage(2, 2, BufferedImage.TYPE_INT_RGB);
        img.setRGB(0, 0, MATCH); // only this one matches

        List<Group> groups = imageGroupFinder.findConnectedGroups(img);

        assertEquals(1, groups.size());
        assertEquals(1, groups.get(0).size());
        assertEquals(new Coordinate(0, 0), groups.get(0).centroid());
    }

    @Test
    void testAllBlackReturnsNoGroups() {
        BufferedImage img = new BufferedImage(2, 2, BufferedImage.TYPE_INT_RGB);
        img.setRGB(0, 0, NOMATCH);
        img.setRGB(0, 1, NOMATCH);
        img.setRGB(1, 0, NOMATCH);
        img.setRGB(1, 1, NOMATCH);

        List<Group> groups = imageGroupFinder.findConnectedGroups(img);
        assertTrue(groups.isEmpty());
    }

    @Test
    void testConnectedVerticalGroup() {
        BufferedImage img = new BufferedImage(2, 2, BufferedImage.TYPE_INT_RGB);
        img.setRGB(0, 0, MATCH);
        img.setRGB(0, 1, MATCH);

        List<Group> groups = imageGroupFinder.findConnectedGroups(img);

        assertEquals(1, groups.size());
        assertEquals(2, groups.get(0).size());
        assertEquals(new Coordinate(0, 0), groups.get(0).centroid()); // (0+0)/2, (0+1)/2 = (0,0)
    }

    @Test
    void testTwoDisconnectedGroups() {
        BufferedImage img = new BufferedImage(3, 3, BufferedImage.TYPE_INT_RGB);
        img.setRGB(0, 0, MATCH);
        img.setRGB(2, 2, MATCH); // separate corner

        List<Group> groups = imageGroupFinder.findConnectedGroups(img);

        assertEquals(2, groups.size());
        assertTrue(groups.stream().allMatch(g -> g.size() == 1));
        assertTrue(groups.stream().anyMatch(g -> g.centroid().equals(new Coordinate(0, 0))));
        assertTrue(groups.stream().anyMatch(g -> g.centroid().equals(new Coordinate(2, 2))));
    }

    @Test
    void testLargeSingleGroupLShape() {
        BufferedImage img = new BufferedImage(3, 3, BufferedImage.TYPE_INT_RGB);
        img.setRGB(0, 0, MATCH);
        img.setRGB(0, 1, MATCH);
        img.setRGB(1, 1, MATCH);

        List<Group> groups = imageGroupFinder.findConnectedGroups(img);

        assertEquals(1, groups.size());
        assertEquals(3, groups.get(0).size());
        assertEquals(new Coordinate(0, 0), groups.get(0).centroid()); // (0+0+1)/3 = 0, (0+1+1)/3 = 0
    }

    @Test
    void testCheckerboardPatternOnlyCountsIsolatedPixels() {
        BufferedImage img = new BufferedImage(3, 3, BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 3; x++) {
                if ((x + y) % 2 == 0) {
                    img.setRGB(x, y, MATCH);
                }
            }
        }

        List<Group> groups = imageGroupFinder.findConnectedGroups(img);

        assertEquals(5, groups.size()); // all isolated
        for (Group g : groups) {
            assertEquals(1, g.size());
        }
    }

    @Test
    void testEntireImageAllWhiteReturnsOneBigGroup() {
        int w = 4;
        int h = 4;
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                img.setRGB(x, y, MATCH);
            }
        }

        List<Group> groups = imageGroupFinder.findConnectedGroups(img);

        assertEquals(1, groups.size());
        assertEquals(16, groups.get(0).size());
        assertEquals(new Coordinate(1, 1), groups.get(0).centroid()); // center of 4x4 grid
    }

    @Test
    void testConnectedHorizontalGroup() {
        BufferedImage img = new BufferedImage(3, 1, BufferedImage.TYPE_INT_RGB);
        img.setRGB(0, 0, MATCH);
        img.setRGB(1, 0, MATCH);
        img.setRGB(2, 0, MATCH);

        List<Group> groups = imageGroupFinder.findConnectedGroups(img);

        assertEquals(1, groups.size());
        assertEquals(3, groups.get(0).size());
        assertEquals(new Coordinate(1, 0), groups.get(0).centroid()); // (0+1+2)/3 = 1, y = 0
    }

    @Test
    void testDiagonalPixelsShouldBeSeparateGroups() {
        BufferedImage img = new BufferedImage(2, 2, BufferedImage.TYPE_INT_RGB);
        img.setRGB(0, 0, MATCH);
        img.setRGB(1, 1, MATCH); // diagonally across

        List<Group> groups = imageGroupFinder.findConnectedGroups(img);

        assertEquals(2, groups.size());
        assertTrue(groups.stream().allMatch(g -> g.size() == 1));
    }

    @Test
    void testNonSquareImageMixedGroups() {
        BufferedImage img = new BufferedImage(2, 3, BufferedImage.TYPE_INT_RGB);
        img.setRGB(0, 0, MATCH);
        img.setRGB(0, 1, MATCH);
        img.setRGB(1, 2, MATCH); // isolated

        List<Group> groups = imageGroupFinder.findConnectedGroups(img);

        assertEquals(2, groups.size());
        assertTrue(groups.stream().anyMatch(g -> g.size() == 2));
        assertTrue(groups.stream().anyMatch(g -> g.size() == 1));
}

}
