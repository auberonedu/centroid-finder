package io.github.AugleBoBaugles.centroidFinder;

import org.junit.jupiter.api.Test;

import io.github.AugleBoBaugles.centroidFinder.BinarizingImageGroupFinder;
import io.github.AugleBoBaugles.centroidFinder.BinaryGroupFinder;
import io.github.AugleBoBaugles.centroidFinder.ColorDistanceFinder;
import io.github.AugleBoBaugles.centroidFinder.Coordinate;
import io.github.AugleBoBaugles.centroidFinder.DfsBinaryGroupFinder;
import io.github.AugleBoBaugles.centroidFinder.DistanceImageBinarizer;
import io.github.AugleBoBaugles.centroidFinder.EuclideanColorDistance;
import io.github.AugleBoBaugles.centroidFinder.Group;
import io.github.AugleBoBaugles.centroidFinder.ImageBinarizer;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class BinarizingImageGroupFinderTest {


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

    // Inner stub class
    private static class StubBinarizer implements ImageBinarizer {
        @Override
        public int[][] toBinaryArray(BufferedImage image) {
            return new int[][] {
                {1, 0},
                {0, 1}
            };
        }

        // dummy method to soothe ImageBinarizer implementation
        @Override
        public BufferedImage toBufferedImage(int[][] binaryArray) {
            // Just return a blank image for testing purposes
            return new BufferedImage(2, 2, BufferedImage.TYPE_INT_RGB);
        }
    }

    // Inner mock class
    private static class MockGroupFinder implements BinaryGroupFinder {
        public int[][] lastInput = null;

        @Override
        public List<Group> findConnectedGroups(int[][] binaryImage) {
            lastInput = binaryImage;
            return List.of(new Group(1, new Coordinate(0, 0)));
        }
    }


    // Simple test 
    @Test
    public void testFindConnectedGroups_usesBinarizerAndGroupFinder() {
        // Arrange
        StubBinarizer binarizer = new StubBinarizer();
        MockGroupFinder groupFinder = new MockGroupFinder();
        BinarizingImageGroupFinder finder = new BinarizingImageGroupFinder(binarizer, groupFinder);

        BufferedImage dummyImage = new BufferedImage(2, 2, BufferedImage.TYPE_INT_RGB);

        // Act
        List<Group> result = finder.findConnectedGroups(dummyImage);

        // Assert
        assertNotNull(groupFinder.lastInput);
        assertEquals(1, result.size());
        assertEquals(1, result.get(0).size());
        assertEquals(new Coordinate(0, 0), result.get(0).centroid());
    }


    // Testing group that is all white (one group)
    
    @Test
    public void testFindConnectedGroups_allWhitePixels_singleGroup() {
        StubBinarizer binarizer = new StubBinarizer() {
            @Override
            public int[][] toBinaryArray(BufferedImage image) {
                return new int[][] {
                    {1, 1},
                    {1, 1}
                };
            }
        };

        BinaryGroupFinder groupFinder = new DfsBinaryGroupFinder();
        BinarizingImageGroupFinder finder = new BinarizingImageGroupFinder(binarizer, groupFinder);
        BufferedImage dummy = new BufferedImage(2, 2, BufferedImage.TYPE_INT_RGB);

        List<Group> result = finder.findConnectedGroups(dummy);

        assertEquals(1, result.size());
        Group group = result.get(0);
        assertEquals(4, group.size());
        assertEquals(new Coordinate(0, 0), group.centroid());  // ((0+0+1+1)/4, (0+1+0+1)/4) = (0, 0)
    }


    // Testing image with no groups 

    @Test
    public void testFindConnectedGroups_allBlackPixels_returnsEmpty() {
        StubBinarizer binarizer = new StubBinarizer() {
            @Override
            public int[][] toBinaryArray(BufferedImage image) {
                return new int[][] {
                    {0, 0},
                    {0, 0}
                };
            }
        };

        BinaryGroupFinder groupFinder = new DfsBinaryGroupFinder();
        BinarizingImageGroupFinder finder = new BinarizingImageGroupFinder(binarizer, groupFinder);
        BufferedImage dummy = new BufferedImage(2, 2, BufferedImage.TYPE_INT_RGB);

        List<Group> result = finder.findConnectedGroups(dummy);

        assertTrue(result.isEmpty());
    }

    @Test
    public void testFindConnectedGroups_multipleGroups_sortedCorrectly() {
        StubBinarizer binarizer = new StubBinarizer() {
            @Override
            public int[][] toBinaryArray(BufferedImage image) {
                return new int[][] {
                    {1, 0, 1, 1, 0},
                    {1, 0, 0, 1, 0},
                    {0, 0, 0, 1, 1},
                    {1, 1, 0, 0, 0},
                    {1, 0, 0, 0, 0}
                };
            }
        };

        BinaryGroupFinder groupFinder = new DfsBinaryGroupFinder();
        BinarizingImageGroupFinder finder = new BinarizingImageGroupFinder(binarizer, groupFinder);
        BufferedImage dummy = new BufferedImage(5, 5, BufferedImage.TYPE_INT_RGB);

        List<Group> result = finder.findConnectedGroups(dummy);

        // There should be 3 distinct groups in the map
        assertEquals(3, result.size());

        // Validate group sizes
        assertEquals(5, result.get(0).size());  // Top-right block of 1s
        assertEquals(3, result.get(1).size());  // Bottom-left block 
        assertEquals(2, result.get(2).size());  // Top-left block

        // Validate centroids
        assertEquals(new Coordinate(1, 3), result.get(0).centroid()); // roughly center of top-right block
        assertEquals(new Coordinate(3, 0), result.get(1).centroid()); // avg of [(0,0), (1,0), (0,2)] -> (0,0)
        assertEquals(new Coordinate(0, 0), result.get(2).centroid()); // avg of [(3,0), (3,1), (4,0)] -> (0,3)

        // Confirm descending sort (by size, then x, then y)
        for (int i = 1; i < result.size(); i++) {
            assertTrue(result.get(i - 1).compareTo(result.get(i)) >= 0);
        }
    }

}
