package io.github.jameson789.app;

import java.awt.image.BufferedImage;
import java.util.List;

public class ImageProcessor {
    private final ImageBinarizer binarizer;
    private final ImageGroupFinder groupFinder;

    public ImageProcessor(int targetColor, int threshold) {
        ColorDistanceFinder distanceFinder = new EuclideanColorDistance();
        this.binarizer = new DistanceImageBinarizer(distanceFinder, targetColor, threshold);
        this.groupFinder = new BinarizingImageGroupFinder(binarizer, new DfsBinaryGroupFinder());
    }

    public CentroidResult processImage(BufferedImage image) {
        List<Group> groups = groupFinder.findConnectedGroups(image);
        Group largest = groups.stream()
                              .max((a, b) -> Integer.compare(a.size(), b.size()))
                              .orElse(null);
        System.out.println("Groups found: " + groups.size());
        return (largest != null) ? new CentroidResult(largest.centroid().x(), largest.centroid().y()) : null;
    } 

    public BufferedImage getBinarizedImage(BufferedImage image) {
        return binarizer.toBufferedImage(binarizer.toBinaryArray(image));
    }

}
