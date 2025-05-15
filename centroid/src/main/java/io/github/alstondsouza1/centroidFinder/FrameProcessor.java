package io.github.alstondsouza1.centroidFinder;

import java.awt.image.BufferedImage;
import java.util.List;

// processes a single video to find the largest centroid
public class FrameProcessor {
    private final ImageGroupFinder finder;
    
    // constructor
    public FrameProcessor(ImageGroupFinder finder) {
        this.finder = finder;
    }

    // returns cen
    public Coordinate getLargestCentroid(BufferedImage frame) {
        List<Group> groups = finder.findConnectedGroups(frame);
        if (groups == null || groups.isEmpty()) return new Coordinate(-1, -1);
        return groups.get(0).centroid(); // largest group
    }
}