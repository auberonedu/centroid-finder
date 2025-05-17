// package main.java.io.github.AugleBoBaugles.centroidFinder;
package io.github.AugleBoBaugles.centroidFinder;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.PrintWriter;
import java.util.List;
import javax.imageio.ImageIO;

/**
 * TODO: Update documentation
 * The Image Summary Application. / Largest Centroid
 * 
 * This application takes three command-line arguments:
 * 1. The path to an input image file (for example, "image.png").
 * 2. A target hex color in the format RRGGBB (for example, "FF0000" for red).
 * 3. An integer threshold for binarization.
 * 
 * The application performs the following steps:
 * 
 * 1. Loads the input image.
 * 2. Parses the target color from the hex string into a 24-bit integer.
 * 3. Binarizes the image by comparing each pixel's Euclidean color distance to the target color.
 *    A pixel is marked white (1) if its distance is less than the threshold; otherwise, it is marked black (0).
 * 4. Converts the binary array back to a BufferedImage and writes the binarized image to disk as "binarized.png".
 * 5. Finds connected groups of white pixels in the binary image.
 *    Pixels are connected vertically and horizontally (not diagonally).
 *    For each group, the size (number of pixels) and the centroid (calculated using integer division) are computed.
 * 6. Writes a CSV file named "groups.csv" containing one row per group in the format "size,x,y".
 *    Coordinates follow the convention: (x:0, y:0) is the top-left, with x increasing to the right and y increasing downward.
 * 
 * Usage:
 *   java ImageSummaryApp <input_image> <hex_target_color> <threshold>
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
