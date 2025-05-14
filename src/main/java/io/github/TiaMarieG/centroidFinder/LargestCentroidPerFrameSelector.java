package io.github.TiaMarieG.centroidFinder;

import java.util.List;

/**
 * Utility class for selecting the largest centroid group from a list of groups in a video frame.
 */
public class LargestCentroidPerFrameSelector {

    /**
     * Selects the group with the largest number of white pixels from a list.
     * Returns null if the list is null or empty.
     *
     * @param groups the list of white-pixel groups detected in a frame
     * @return the largest group, or null if none found
     */
    public static Group selectLargest(List<Group> groups) {
        if (groups == null || groups.isEmpty()) return null;

        Group largest = groups.get(0);
        for (Group g : groups) {
            if (g.size() > largest.size()) {
                largest = g;
            }
        }
        return largest;
    }
}
