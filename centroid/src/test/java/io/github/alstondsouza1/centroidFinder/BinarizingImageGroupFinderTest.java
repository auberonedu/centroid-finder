package io.github.alstondsouza1.centroidFinder;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.ArrayList;


// Used AI to generate and fixed the errors for BinarizingImageGroupFinderTest

public class BinarizingImageGroupFinderTest {

    class TestBinarizer implements ImageBinarizer {
        @Override
        public int[][] toBinaryArray(BufferedImage image) {
            int height = image.getHeight();
            int width = image.getWidth();
            int[][] binary = new int[height][width];
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    binary[y][x] = (image.getRGB(x, y) == 0xFFFFFF) ? 1 : 0;
                }
            }
            return binary;
        }

        @Override
        public BufferedImage toBufferedImage(int[][] image) {
            return null; 
        }
    }


    class FixedGroupFinder implements BinaryGroupFinder {
        @Override
        public List<Group> findConnectedGroups(int[][] binary) {
            List<int[]> pixels = new ArrayList<>();
            pixels.add(new int[]{5, 5});
            pixels.add(new int[]{5, 6});
            pixels.add(new int[]{6, 5});
            pixels.add(new int[]{6, 6});
            return List.of(new Group(4, centroidGen(pixels, 4)));
        }
    }

    private Coordinate centroidGen(List<int[]> pixels, int size) {
        int sumX = 0;
        int sumY = 0;
        for (int[] pixel : pixels) {
            sumX += pixel[1]; // x = column
            sumY += pixel[0]; // y = row
        }
        return new Coordinate(sumX / size, sumY / size);
    }

    @Test
    public void testFindConnectedGroups_withMockedBinarizerAndGroupFinder() {
        BufferedImage image = new BufferedImage(16, 16, BufferedImage.TYPE_INT_RGB);

        // Add a 2x2 white block at (5,5)
        image.setRGB(5, 5, 0xFFFFFF);
        image.setRGB(5, 6, 0xFFFFFF);
        image.setRGB(6, 5, 0xFFFFFF);
        image.setRGB(6, 6, 0xFFFFFF);

        ImageBinarizer binarizer = new TestBinarizer();
        BinaryGroupFinder groupFinder = new FixedGroupFinder();
        BinarizingImageGroupFinder finder = new BinarizingImageGroupFinder(binarizer, groupFinder);

        List<Group> result = finder.findConnectedGroups(image);

        assertEquals(1, result.size());
        assertEquals(4, result.get(0).size());
        assertEquals(new Coordinate(5, 5), result.get(0).centroid());
    }

    @Test
    public void testFindConnectedGroups_returnsNullIfBinaryArrayIsNull() {
        ImageBinarizer nullBinarizer = new ImageBinarizer() {
            public int[][] toBinaryArray(BufferedImage image) {
                return null;
            }

            public BufferedImage toBufferedImage(int[][] image) {
                return null;
            }
        };

        BinaryGroupFinder dummyGroupFinder = binary -> List.of(); 
        BinarizingImageGroupFinder finder = new BinarizingImageGroupFinder(nullBinarizer, dummyGroupFinder);

        BufferedImage image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
        List<Group> result = finder.findConnectedGroups(image);

        assertNull(result, "Should return null if binary array is null");
    }

    @Test
    public void testFindConnectedGroups_allBlackImage_returnsEmptyList() {
        BufferedImage image = new BufferedImage(2, 2, BufferedImage.TYPE_INT_RGB);
        image.setRGB(0, 0, 0x000000);
        image.setRGB(0, 1, 0x000000);
        image.setRGB(1, 0, 0x000000);
        image.setRGB(1, 1, 0x000000);

        ImageBinarizer binarizer = new TestBinarizer();
        BinaryGroupFinder groupFinder = binary -> List.of();
        BinarizingImageGroupFinder finder = new BinarizingImageGroupFinder(binarizer, groupFinder);

        List<Group> result = finder.findConnectedGroups(image);
        assertTrue(result.isEmpty(), "Should return empty list for all-black image");
    }

    @Test
    public void testFindConnectedGroups_singleWhitePixel_returnsOneGroup() {
        BufferedImage image = new BufferedImage(2, 2, BufferedImage.TYPE_INT_RGB);
        image.setRGB(0, 0, 0x000000);
        image.setRGB(0, 1, 0x000000);
        image.setRGB(1, 0, 0xFFFFFF);
        image.setRGB(1, 1, 0x000000);

        ImageBinarizer binarizer = new TestBinarizer();
        BinaryGroupFinder groupFinder = binary -> List.of(new Group(1, new Coordinate(1, 0)));
        BinarizingImageGroupFinder finder = new BinarizingImageGroupFinder(binarizer, groupFinder);

        List<Group> result = finder.findConnectedGroups(image);
        assertEquals(1, result.size());
        assertEquals(new Group(1, new Coordinate(1, 0)), result.get(0));
    }

    @Test
    public void testFindConnectedGroups_checkerboardPattern_returnsCorrectGroups() {
        BufferedImage image = new BufferedImage(3, 3, BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 3; x++) {
                int color = (x + y) % 2 == 0 ? 0xFFFFFF : 0x000000;
                image.setRGB(x, y, color);
            }
        }

        ImageBinarizer binarizer = new TestBinarizer();
        BinaryGroupFinder groupFinder = binary -> List.of(
            new Group(1, new Coordinate(0, 0)),
            new Group(1, new Coordinate(2, 0)),
            new Group(1, new Coordinate(1, 1)),
            new Group(1, new Coordinate(0, 2)),
            new Group(1, new Coordinate(2, 2))
        );
        BinarizingImageGroupFinder finder = new BinarizingImageGroupFinder(binarizer, groupFinder);

        List<Group> result = finder.findConnectedGroups(image);
        assertEquals(5, result.size());
    }

    
}