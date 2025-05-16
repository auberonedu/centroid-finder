package io.github.jameson789.app;

import org.bytedeco.javacv.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class VideoFrameProcessor {
    private final String videoPath;
    private final CentroidTracker tracker;

    public VideoFrameProcessor(String videoPath, String hexTargetColor, int threshold) {
        this.videoPath = videoPath;

        int targetColor = Integer.parseInt(hexTargetColor, 16);
        ColorDistanceFinder distanceFinder = new EuclideanColorDistance();
        ImageBinarizer binarizer = new DistanceImageBinarizer(distanceFinder, targetColor, threshold);
        ImageGroupFinder groupFinder = new BinarizingImageGroupFinder(binarizer, new DfsBinaryGroupFinder());

        this.tracker = new CentroidTracker(groupFinder);
    }

    public void process() throws Exception {
        FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(videoPath);
        grabber.start();

        OpenCVFrameConverter.ToIplImage converter = new OpenCVFrameConverter.ToIplImage();
        Java2DFrameConverter javaConverter = new Java2DFrameConverter();

        List<String> rows = new ArrayList<>();
        double frameRate = grabber.getFrameRate();
        int frameNumber = 0;

        Frame frame;
        while ((frame = grabber.grabImage()) != null) {
            double timestamp = frameNumber / frameRate;
            BufferedImage img = javaConverter.convert(frame);
            int[] centroid = tracker.findLargestCentroid(img);
            rows.add(String.format("%.2f,%d,%d", timestamp, centroid[0], centroid[1]));
            frameNumber++;
        }

        grabber.stop();
        CSVWriter.write("video_groups.csv", rows);
        System.out.println("Tracking complete: video_groups.csv");
    }
}
