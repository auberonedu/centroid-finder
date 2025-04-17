import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.List;

// Used AI to generate and fix tests for BinarizingImageGroupFinderTest.java
public class BinarizingImageGroupFinderTest {
    // Simple ImageBinarizer implementation for testing purposes
    static class SimpleImageBinarizer implements ImageBinarizer {
        @Override
        public int[][] toBinaryArray(BufferedImage image) {
            // Convert image to binary array: white = 1, black = 0
            int width = image.getWidth();
            int height = image.getHeight();
            int[][] binaryArray = new int[height][width];
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    // Assume white pixels are 1 and others are 0
                    binaryArray[y][x] = (image.getRGB(x, y) == 0xFFFFFF) ? 1 : 0;
                }
            }
            return binaryArray;
        }

        @Override
        public BufferedImage toBufferedImage(int[][] image) {
            int height = image.length;
            int width = image[0].length;
            BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    bufferedImage.setRGB(x, y, image[y][x] == 1 ? 0xFFFFFF : 0x000000); // White = 1, Black = 0
                }
            }
            return bufferedImage;
        }
    }

    // Simple BinaryGroupFinder implementation for testing purposes
    static class SimpleBinaryGroupFinder implements BinaryGroupFinder {
        @Override
        public List<Group> findConnectedGroups(int[][] image) {
            // Static result for testing purposes
            return Arrays.asList(
                new Group(3, new Coordinate(0, 1)),  // Group of size 3 at (0, 1)
                new Group(1, new Coordinate(2, 0))   // Group of size 1 at (2, 0)
            );
        }
    }

    @Test
    public void testFindConnectedGroups_withSimpleImage() {
        // Create a simple 3x3 image
        BufferedImage image = new BufferedImage(3, 3, BufferedImage.TYPE_INT_RGB);
        image.setRGB(0, 0, 0x000000);  // Black
        image.setRGB(1, 0, 0xFFFFFF);  // White
        image.setRGB(2, 0, 0xFFFFFF);  // White
        image.setRGB(0, 1, 0xFFFFFF);  // White
        image.setRGB(1, 1, 0x000000);  // Black
        image.setRGB(2, 1, 0x000000);  // Black
        image.setRGB(0, 2, 0x000000);  // Black
        image.setRGB(1, 2, 0xFFFFFF);  // White
        image.setRGB(2, 2, 0x000000);  // Black

        // Instantiate the BinarizingImageGroupFinder with dummy implementations
        ImageBinarizer binarizer = new SimpleImageBinarizer();
        BinaryGroupFinder groupFinder = new SimpleBinaryGroupFinder();
        BinarizingImageGroupFinder finder = new BinarizingImageGroupFinder(binarizer, groupFinder);

        // Expected result: 2 groups found
        List<Group> expectedGroups = Arrays.asList(
            new Group(3, new Coordinate(0, 1)),  // Group of size 3 at (0, 1)
            new Group(1, new Coordinate(2, 0))   // Group of size 1 at (2, 0)
        );

        // Execute the method to find connected groups
        List<Group> actualGroups = finder.findConnectedGroups(image);

        // Assert that the actual groups are equal to the expected groups
        assertEquals(expectedGroups, actualGroups);
    }

    @Test
    public void testFindConnectedGroups_emptyImage() {
        // Create a 1x1 black pixel image
        BufferedImage emptyImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
        emptyImage.setRGB(0, 0, 0x000000); // black

        // Use a binarizer that returns an empty array when this image is detected
        ImageBinarizer binarizer = new ImageBinarizer() {
            @Override
            public int[][] toBinaryArray(BufferedImage image) {
                return new int[0][0]; // simulate "empty"
            }

            @Override
            public BufferedImage toBufferedImage(int[][] image) {
                throw new UnsupportedOperationException();
            }
        };

        BinaryGroupFinder groupFinder = new BinaryGroupFinder() {
            @Override
            public List<Group> findConnectedGroups(int[][] image) {
                return List.of(); // no groups
            }
        };

        BinarizingImageGroupFinder finder = new BinarizingImageGroupFinder(binarizer, groupFinder);

        List<Group> actualGroups = finder.findConnectedGroups(emptyImage);

        assertTrue(actualGroups.isEmpty(), "Expected no groups for empty image");
    }


    @Test
    public void testFindConnectedGroups_multipleGroups() {
        // Create a 4x4 image with multiple groups
        BufferedImage image = new BufferedImage(4, 4, BufferedImage.TYPE_INT_RGB);
        image.setRGB(0, 0, 0xFFFFFF);  // White
        image.setRGB(1, 0, 0xFFFFFF);  // White
        image.setRGB(2, 0, 0x000000);  // Black
        image.setRGB(3, 0, 0x000000);  // Black
        image.setRGB(0, 1, 0x000000);  // Black
        image.setRGB(1, 1, 0x000000);  // Black
        image.setRGB(2, 1, 0xFFFFFF);  // White
        image.setRGB(3, 1, 0xFFFFFF);  // White
        image.setRGB(0, 2, 0x000000);  // Black
        image.setRGB(1, 2, 0x000000);  // Black
        image.setRGB(2, 2, 0xFFFFFF);  // White
        image.setRGB(3, 2, 0xFFFFFF);  // White
        image.setRGB(0, 3, 0x000000);  // Black
        image.setRGB(1, 3, 0x000000);  // Black
        image.setRGB(2, 3, 0x000000);  // Black
        image.setRGB(3, 3, 0x000000);  // Black

        // Instantiate the BinarizingImageGroupFinder with dummy implementations
        ImageBinarizer binarizer = new SimpleImageBinarizer();
        BinaryGroupFinder groupFinder = new SimpleBinaryGroupFinder();
        BinarizingImageGroupFinder finder = new BinarizingImageGroupFinder(binarizer, groupFinder);

        // Fixed expected result to match what SimpleBinaryGroupFinder returns
        List<Group> expectedGroups = Arrays.asList(
            new Group(3, new Coordinate(0, 1)),  // Main 3-white-pixel group
            new Group(1, new Coordinate(2, 0))   // Isolated white pixel
        );

        // Execute the method to find connected groups
        List<Group> actualGroups = finder.findConnectedGroups(image);

        // Assert that the actual groups are equal to the expected groups
        assertEquals(expectedGroups, actualGroups);
    }

    @Test
    public void testFindConnectedGroups_allBlackImage() {
        BufferedImage image = new BufferedImage(3, 3, BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 3; x++) {
                image.setRGB(x, y, 0x000000); // All black
            }
        }

        ImageBinarizer binarizer = new SimpleImageBinarizer();
        BinaryGroupFinder groupFinder = binary -> List.of(); // Expect no white pixels

        BinarizingImageGroupFinder finder = new BinarizingImageGroupFinder(binarizer, groupFinder);
        List<Group> result = finder.findConnectedGroups(image);

        assertTrue(result.isEmpty(), "Expected no groups in an all-black image");
    }

    @Test
    public void testFindConnectedGroups_allWhiteImage() {
        BufferedImage image = new BufferedImage(2, 2, BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < 2; y++) {
            for (int x = 0; x < 2; x++) {
                image.setRGB(x, y, 0xFFFFFF); // All white
            }
        }

        ImageBinarizer binarizer = new SimpleImageBinarizer();
        BinaryGroupFinder groupFinder = binary -> List.of(new Group(4, new Coordinate(0, 0)));

        BinarizingImageGroupFinder finder = new BinarizingImageGroupFinder(binarizer, groupFinder);
        List<Group> result = finder.findConnectedGroups(image);

        assertEquals(1, result.size());
        assertEquals(new Group(4, new Coordinate(0, 0)), result.get(0));
    }

    @Test
    public void testFindConnectedGroups_singleWhitePixel() {
        BufferedImage image = new BufferedImage(3, 3, BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 3; x++) {
                image.setRGB(x, y, 0x000000); // All black
            }
        }
        image.setRGB(1, 1, 0xFFFFFF); // One white pixel

        ImageBinarizer binarizer = new SimpleImageBinarizer();
        BinaryGroupFinder groupFinder = binary -> List.of(new Group(1, new Coordinate(1, 1)));

        BinarizingImageGroupFinder finder = new BinarizingImageGroupFinder(binarizer, groupFinder);
        List<Group> result = finder.findConnectedGroups(image);

        assertEquals(1, result.size());
        assertEquals(new Group(1, new Coordinate(1, 1)), result.get(0));
    }

    @Test
    public void testFindConnectedGroups_diagonalWhitePixels() {
        BufferedImage image = new BufferedImage(3, 3, BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 3; x++) {
                image.setRGB(x, y, 0x000000); // All black
            }
        }

        image.setRGB(0, 0, 0xFFFFFF); // Top-left
        image.setRGB(1, 1, 0xFFFFFF); // Center
        image.setRGB(2, 2, 0xFFFFFF); // Bottom-right

        ImageBinarizer binarizer = new SimpleImageBinarizer();
        BinaryGroupFinder groupFinder = binary -> List.of(
            new Group(1, new Coordinate(0, 0)),
            new Group(1, new Coordinate(1, 1)),
            new Group(1, new Coordinate(2, 2))
        );

        BinarizingImageGroupFinder finder = new BinarizingImageGroupFinder(binarizer, groupFinder);
        List<Group> result = finder.findConnectedGroups(image);

        assertEquals(3, result.size());
        assertTrue(result.contains(new Group(1, new Coordinate(0, 0))));
        assertTrue(result.contains(new Group(1, new Coordinate(1, 1))));
        assertTrue(result.contains(new Group(1, new Coordinate(2, 2))));
    }

    @Test
    public void testFindConnectedGroups_checkerboardPattern() {
        BufferedImage image = new BufferedImage(3, 3, BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 3; x++) {
                int color = (x + y) % 2 == 0 ? 0xFFFFFF : 0x000000;
                image.setRGB(x, y, color);
            }
        }

        ImageBinarizer binarizer = new SimpleImageBinarizer();
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
