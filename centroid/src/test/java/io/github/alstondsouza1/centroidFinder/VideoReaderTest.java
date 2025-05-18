package io.github.alstondsouza1.centroidFinder;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import org.jcodec.api.JCodecException;

public class VideoReaderTest {

    private final String VALID_VIDEO_PATH = "sample.mp4"; 

    @Test
    public void testFileExists() {
        File file = new File(VALID_VIDEO_PATH);
        assertTrue(file.exists(), "sample.mp4 must exist in root directory for this test.");
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