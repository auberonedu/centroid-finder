package io.github.alstondsouza1.centroidFinder;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import org.jcodec.api.JCodecException;
import org.junit.jupiter.api.Test;

// Used AI to generate and fixed the errors for VideoReaderTest
public class VideoReaderTest {

    private final String VALID_VIDEO_PATH = "sampleInput/ensantina.mp4";

    @Test
    public void testFileExists() {
        File file = new File(VALID_VIDEO_PATH);
        assertTrue(file.exists(), VALID_VIDEO_PATH + " must exist for this test.");
    }

    @Test
    public void testGetFrameRate() throws IOException, JCodecException {
        VideoReader reader = new VideoReader(VALID_VIDEO_PATH);
        int frameRate = reader.getFrameRate();
        assertTrue(frameRate > 0, "Frame rate should be greater than zero.");
    }

    @Test
    public void testReadFirstFrame() throws IOException, JCodecException {
        VideoReader reader = new VideoReader(VALID_VIDEO_PATH);
        BufferedImage frame = reader.getNextFrame();
        assertNotNull(frame, "First frame should not be null.");
    }

    @Test
    public void testReadAllFrames() throws IOException, JCodecException {
        VideoReader reader = new VideoReader(VALID_VIDEO_PATH);
        int count = 0;
        BufferedImage frame;

        while ((frame = reader.getNextFrame()) != null && count < 10) {
            assertNotNull(frame, "Frame should not be null.");
            count++;
        }

        assertTrue(count > 0, "At least one frame should be read.");
    }
}