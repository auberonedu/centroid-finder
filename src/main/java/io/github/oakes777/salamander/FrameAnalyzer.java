package io.github.oakes777.salamander;

import java.awt.image.BufferedImage;
import java.util.Comparator;
import java.util.List;

/**
 * The FrameAnalyzer class provides a reusable, object-oriented wrapper for analyzing a single BufferedImage using the core components from the original centroid-finder project.
 * 
 *This class modularizes the image-processing pipeline that was previously iplemented in a monolithic fashion in ImageSummaryApp. It reuses and orchestrates the following original components:
 * 
 * EuclideanColorDistance - for computing pixel similarity based on (3D) color distance
 * DistanceImageBinarizer - converts iage to binary mask using target color and threshold
 * DfsBinaryGroupFinder & BinarizingImageGroupFinder - locate all connected white pixel groups
 * 
 * The primary method findLargestGroup(BufferedImage) analyzes a single image frame, locates all white pixel groups, and returns the largest one by pixel count. This is used as part of the video processing pipeline in Project 2, where individual frames from an .mp4 file are analyzed in sequence.
 * 
 * This class does not replace any original components. It augments them in a modular reusable way to support video processing without altering any OG design.
 * 
 * Use Case: Ideal for video frame analysis where each frame must be independently binarized and evaluated for centroid tracking.
 * 
 * parameters: int targetColor, int threshold
 * returns: LARGEST group found in frame, or 'null' if no valid group was detected (ie no white pixels passed threshold).
 * 
 * parameter for findLargestGroup: BufferedImage image.
 * returns: (largest) group.
 * 
 * @author Jonathan, Zach, Stephen
 * @version 2.0
 */

public class FrameAnalyzer {

    // Binarizer used to convert image into binary array (0s and 1s)
    private final ImageBinarizer binarizer;

    // Group finder used to locate all white-pixel-connected groups
    private final ImageGroupFinder groupFinder;

    /**
     * Constructs a FrameAnalyzer using a target color and a binarization threshold.
     * 
     * @param targetColor RGB color to match (in 0xRRGGBB format)
     * @param threshold   Euclidean distance threshold
     */
    public FrameAnalyzer(int targetColor, int threshold) {
        // Use Euclidean color distance to binarize the image
        ColorDistanceFinder distanceFinder = new EuclideanColorDistance();
        this.binarizer = new DistanceImageBinarizer(distanceFinder, targetColor, threshold);

        // Use DFS-based group finder on the binarized image
        this.groupFinder = new BinarizingImageGroupFinder(binarizer, new DfsBinaryGroupFinder());
    }

    /**
     * Processes a BufferedImage to find the largest group of white pixels.
     * 
     * @param image Input image to analyze
     * @return The largest Group found, or null if no groups were detected
     */
    public Group findLargestGroup(BufferedImage image) {
        // Use the group finder to get all white pixel groups in the image
        List<Group> groups = groupFinder.findConnectedGroups(image);

        // Return the group with the largest size, or null if no groups found
        return groups.stream()
                     .max(Comparator.comparingInt(Group::size))
                     .orElse(null);
    }
}
