package io.github.brittLiban.centroidfinder;

import org.junit.jupiter.api.Test;
import org.opencv.videoio.VideoCapture;

import java.awt.image.BufferedImage;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class VideoAnalyzerTest {

    @Test
    public void testProcessVideoLoadsFramesQuickly() {
        VideoCapture video = new VideoCapture(
                "C:\\Users\\liban\\OneDrive\\Documents\\Bac\\SDEV334\\centroid-finder\\ensantina.mp4");
        assertTrue(video.isOpened(), () -> "Video file could not be opened");

        VideoAnalyzer analyzer = new VideoAnalyzer();
        List<BufferedImage> frames = analyzer.processVideo(video, 24); 
        assertFalse(frames.isEmpty(), "No frames were processed");

    }

}
