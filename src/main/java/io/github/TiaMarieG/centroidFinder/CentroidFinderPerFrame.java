package io.github.TiaMarieG.centroidFinder;

import org.bytedeco.javacv.*;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 * Processes a video to extract the largest centroid from every Nth frame.
 * Uses provided ImageBinarizer and BinaryGroupFinder from the original project.
 */
public class CentroidFinderPerFrame {

    private final ImageBinarizer binarizer;
    private final BinaryGroupFinder groupFinder;

    /**
     * Constructs the frame processor using any compatible binarizer and group finder.
     *
     * @param binarizer the image binarizer (e.g., DistanceImageBinarizer)
     * @param groupFinder the group detection logic (e.g., DfsBinaryGroupFinder)
     */
    public CentroidFinderPerFrame(ImageBinarizer binarizer, BinaryGroupFinder groupFinder) {
        this.binarizer = binarizer;
        this.groupFinder = groupFinder;
    }

    /**
     * Processes a video file and extracts centroid coordinates from every Nth frame.
     *
     * @param videoPath  path to the video file (e.g., "videos/sample.mp4")
     * @param frameStep  how often to sample frames (e.g., 30 = every 30th frame)
     * @return a list of DataPoint objects containing frame number and centroid coordinates
     * @throws FrameGrabber.Exception if there's a problem reading the video
     */
    public List<DataPoint> processVideo(String videoPath, int frameStep) throws FrameGrabber.Exception {
        List<DataPoint> results = new ArrayList<>();

        try (
            FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(videoPath);
            Java2DFrameConverter converter = new Java2DFrameConverter()
        ) {
            grabber.start();
            int frameNumber = 0;

            for (Frame frame; (frame = grabber.grabImage()) != null; frameNumber++) {
                if (frameNumber % frameStep != 0) continue;

                BufferedImage image = converter.convert(frame);
                if (image != null) {
                    int[][] binaryImage = binarizer.toBinaryArray(image);
                    List<Group> groups = groupFinder.findConnectedGroups(binaryImage);

                    Group largest = LargestCentroidPerFrameSelector.selectLargest(groups);
                    if (largest != null) {
                        Coordinate c = largest.centroid();
                        results.add(new DataPoint(frameNumber, c.x(), c.y()));
                    }
                }
            }

            grabber.stop();
        }

        return results;
    }

    /**
     * Simple record structure to hold centroid data per frame.
     */
    public record DataPoint(int frame, int x, int y) {}
}
