// package main.java.io.github.AugleBoBaugles.centroidFinder;
package io.github.AugleBoBaugles.centroidFinder;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.PrintWriter;
import java.util.List;
import javax.imageio.ImageIO;

/**
 * TODO: Update documentation
 */
public class LargestCentroid {
    public BufferedImage inputImage;
    public int targetColor;
    public int threshold = 0;
    public int seconds;

    // Constructors
    public LargestCentroid(BufferedImage inputImage, int targetColor, int threshold, int seconds) {
        this.inputImage = inputImage;
        this.targetColor = targetColor;
        this.threshold = threshold;
        this.seconds = seconds;
    }

    public LargestCentroid(BufferedImage inputImage, int targetColor, int seconds) {
        this.inputImage = inputImage;
        this.targetColor = targetColor;
        this.seconds = seconds;
    }

    public LargestCentroidRecord findLargestCentroid() {
        // Create the DistanceImageBinarizer with a EuclideanColorDistance instance.
        ColorDistanceFinder distanceFinder = new EuclideanColorDistance();
        ImageBinarizer binarizer = new DistanceImageBinarizer(distanceFinder, targetColor, threshold);
        
        // Binarize the input image.
        int[][] binaryArray = binarizer.toBinaryArray(inputImage);
        
        // Create an ImageGroupFinder using a BinarizingImageGroupFinder with a DFS-based BinaryGroupFinder.
        ImageGroupFinder groupFinder = new BinarizingImageGroupFinder(binarizer, new DfsBinaryGroupFinder());
        
        // Find connected groups in the input image.
        // The BinarizingImageGroupFinder is expected to internally binarize the image,
        // then locate connected groups of white pixels.
        List<Group> groups = groupFinder.findConnectedGroups(inputImage);
        
        //LargestCentroidRecord largest = new LargestCentroidRecord() // Create record
        Group largeGroup = new Group(0, new Coordinate(-1, -1));

        // cycle through Groups and pick largest
        for (Group gr: groups) {
            if (largeGroup.size() <= gr.size()) {
                largeGroup = gr;
            }
        }

        LargestCentroidRecord largestCentroid = new LargestCentroidRecord(seconds, largeGroup.centroid().x(), largeGroup.centroid().y());

        return largestCentroid;

    }
}
