package io.github.brittLiban.centroidFinder;

import java.awt.image.BufferedImage;
import java.util.List;

public class LargestBlobProcessor implements FrameProcessor {
    private final ImageGroupFinder groupFinder;

    public LargestBlobProcessor(ImageGroupFinder groupFinder) {
        this.groupFinder = groupFinder;
    }

    // This method finds the largest group of white pixels in a binarized image
    @Override
    public FrameResult processFrame(BufferedImage frame, int frameIndex) {
        //the image is made black and white and binarized at this step! 
        List<Group> groups = groupFinder.findConnectedGroups(frame);

        Group largest = null;
        int largestSize = 0;

        //remember this is already going to be a picture that is black and white
        //it just looks for the biggest group of white pixels (1's) and see if its bigger than the next it encounters
        for (Group group : groups) {
            if (group.size() > largestSize) {
                largest = group;
                largestSize = group.size();
            }
        }

        if (largest != null) {
            //remember we made a class that will define a frame as a index (frame #) and its x and y coordinates
            //this is returning that.
            return new FrameResult(frameIndex, largest.centroid().x(), largest.centroid().y());
        } else {
            return new FrameResult(frameIndex, -1, -1);
        }
    }

   
}
