package io.github.qopci.centroidFinder;

import java.awt.image.BufferedImage;
import java.util.List;

public class FrameProcessor {
    private final ImageBinarizer binarizer;
    private final ImageGroupFinder groupFinder;

    // Initializes processor with target color and threshold
    public FrameProcessor(int targetColor, int threshold) {
        ColorDistanceFinder colorDistance = new EuclideanColorDistance();
        this.binarizer = new DistanceImageBinarizer(colorDistance, targetColor, threshold);
        this.groupFinder = new BinarizingImageGroupFinder(binarizer, new DfsBinaryGroupFinder());
    }

    // Returns centroid of largest color matching group or (-1, -1) if none found
    public Coordinate process(BufferedImage frame) {
        List<Group> groups = groupFinder.findConnectedGroups(frame);
        if (groups.isEmpty()) return new Coordinate(-1, -1);
        
        return groups.get(0).centroid();
    }
}
