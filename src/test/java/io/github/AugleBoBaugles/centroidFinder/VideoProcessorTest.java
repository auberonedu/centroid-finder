package io.github.AugleBoBaugles.centroidFinder;

import org.junit.jupiter.api.Test;
import main.java.io.github.AugleBoBaugles.centroidFinder.VideoProcessor;
import static org.junit.jupiter.api.Assertions.*;
import java.awt.image.BufferedImage;

import java.util.ArrayList;
import java.util.List;

public class VideoProcessorTest {

        // AI Helped create this "spy" to override the frameToData function
        public class TestVideoProcessor extends VideoProcessor {
        public int callCounter = 0;

        public TestVideoProcessor(String path, int targetColor, int threshold) {
            super(path, targetColor, threshold);
        }

        @Override
        public void frameToData(BufferedImage frame, int seconds) {
            callCounter++;
        }
    }

    @Test
    public void testExtractFramesCallsFrameToDataAtOneSecondIntervals() {
        String videoPath = getClass().getResource("sampleInput/sample_video_1.mp4").getPath(); // TODO: Check that this path is accurate
        TestVideoProcessor vp = new TestVideoProcessor(videoPath, 0xFF0000, 30); 

        vp.extractFrames();

        // If video is 3 seconds long, expect calls at 0, 1, 2
        int expected = 4;
        assertEquals(expected, vp.callCounter);
    }
    // extractFrames() pulls out correct frames





    // frameToData() generates expects CSV content
    // NOTE: I OVERRODE THE FUNCTION ABOVE - HOW DO I TEST THIS PROPERLY??
}
