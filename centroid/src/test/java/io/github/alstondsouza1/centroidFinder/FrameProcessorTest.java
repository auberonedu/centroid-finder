package io.github.alstondsouza1.centroidFinder;

import org.junit.jupiter.api.Test;

import java.awt.image.BufferedImage;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class FrameProcessorTest {

    class MockImageGroupFinder implements ImageGroupFinder {
        @Override
        public List<Group> findConnectedGroups(BufferedImage image) {
            return List.of(new Group(5, new Coordinate(7, 9)));
        }
    }

    class EmptyImageGroupFinder implements ImageGroupFinder {
        @Override
        public List<Group> findConnectedGroups(BufferedImage image) {
            return List.of();
        }
    }

    @Test
    public void testLargestCentroidReturnedCorrectly() {
        FrameProcessor processor = new FrameProcessor(new MockImageGroupFinder());
        BufferedImage dummy = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);

        Coordinate c = processor.getLargestCentroid(dummy);
        assertEquals(new Coordinate(7, 9), c);
    }

    @Test
    public void testNoGroupsReturnsNegativeOne() {
        FrameProcessor processor = new FrameProcessor(new EmptyImageGroupFinder());
        BufferedImage dummy = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);

        Coordinate c = processor.getLargestCentroid(dummy);
        assertEquals(new Coordinate(-1, -1), c);
    }
}