package io.github.brittLiban.centroidfinder;

import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class VideoAnalyzer {

    public List<BufferedImage> processVideo(VideoCapture video, int frameInterval) {
        List<BufferedImage> frames = new ArrayList<>();
        Mat frame = new Mat();
        int frameCount = 0;

        while (video.read(frame)) {
            if (frameCount % frameInterval == 0) {
                BufferedImage img = OpenCVUtils.convertMatToBufferedImage(frame);
                if (img != null) {
                    frames.add(img);
                }
            }
            frameCount++;
        }

        System.out.println("Video split into " + frames.size() + " frames (every " + frameInterval + "th frame).");
        return frames;
    }
}
