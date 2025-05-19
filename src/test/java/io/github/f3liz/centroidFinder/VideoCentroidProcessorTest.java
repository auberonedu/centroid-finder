package io.github.f3liz.centroidFinder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.awt.image.BufferedImage;
import org.bytedeco.javacv.Frame;

import org.bytedeco.javacv.Java2DFrameConverter;
import org.junit.jupiter.api.Test;

public class VideoCentroidProcessorTest {
    @Test
public void testCentroidFromSingleColoredRegion() {
    // Arrange
    int width = 100;
    int height = 100;
    BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

    // Draw a red square from (10,10) to (19,19)
    for (int x = 10; x < 20; x++) {
        for (int y = 10; y < 20; y++) {
            image.setRGB(x, y, 0xFF0000); // Red
        }
    }

    Frame frame = new Java2DFrameConverter().convert(image);
    FrameToBufferedImageConverter converter = new FrameToBufferedImageConverter();
    BufferedImage converted = converter.convert(frame);

    ColorDistanceFinder distanceFinder = new EuclideanColorDistance();
    ImageBinarizer binarizer = new DistanceImageBinarizer(distanceFinder, 0xFF0000, 50);
    ImageGroupFinder groupFinder = new BinarizingImageGroupFinder(binarizer, new DfsBinaryGroupFinder());

    // Act
    List<Group> groups = groupFinder.findConnectedGroups(converted);

    // Assert
    assertFalse(groups.isEmpty());
    Group largest = groups.get(0);
    assertEquals(14, largest.centroid().x(), "Expected x of centroid");
    assertEquals(14, largest.centroid().y(), "Expected y of centroid");
}

@Test
public void testNoMatchingColorProducesNegativeCoordinates() {
    BufferedImage image = new BufferedImage(50, 50, BufferedImage.TYPE_INT_RGB);

    // Fill with blue only
    for (int x = 0; x < 50; x++) {
        for (int y = 0; y < 50; y++) {
            image.setRGB(x, y, 0x0000FF);
        }
    }

    Frame frame = new Java2DFrameConverter().convert(image);
    FrameToBufferedImageConverter converter = new FrameToBufferedImageConverter();
    BufferedImage converted = converter.convert(frame);

    ColorDistanceFinder distanceFinder = new EuclideanColorDistance();
    ImageBinarizer binarizer = new DistanceImageBinarizer(distanceFinder, 0xFF0000, 50); // red
    ImageGroupFinder groupFinder = new BinarizingImageGroupFinder(binarizer, new DfsBinaryGroupFinder());

    List<Group> groups = groupFinder.findConnectedGroups(converted);

    assertTrue(groups.isEmpty());
}


}