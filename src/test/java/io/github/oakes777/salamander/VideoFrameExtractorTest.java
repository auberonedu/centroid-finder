package io.github.oakes777.salamander;

import java.io.IOException;
import java.util.Iterator;
import java.util.NoSuchElementException;

import org.jcodec.api.JCodecException;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

public class VideoFrameExtractorTest {

    private static final String SHORT_VIDEO_PATH = "sampleInput/shorty.mp4";

    @Test
    public void testConstructorLoadsVideo() throws IOException, JCodecException {
        try (VideoFrameExtractor extractor = new VideoFrameExtractor(SHORT_VIDEO_PATH)) {
            assertNotNull(extractor);
        }
    }

    @Test
    public void testIteratorReturnsFrames() throws IOException, JCodecException {
        try (VideoFrameExtractor extractor = new VideoFrameExtractor(SHORT_VIDEO_PATH)) {
            Iterator<VideoFrame> it = extractor.iterator();
            assertTrue(it.hasNext(), "Expected iterator to have at least one frame");
            VideoFrame frame = it.next();
            assertNotNull(frame);
            assertNotNull(frame.image());
        }
    }

    @Test
    public void testFrameHasValidTimestamp() throws IOException, JCodecException {
        try (VideoFrameExtractor extractor = new VideoFrameExtractor(SHORT_VIDEO_PATH)) {
            Iterator<VideoFrame> it = extractor.iterator();
            if (it.hasNext()) {
                VideoFrame frame = it.next();
                assertTrue(frame.timestampSeconds() >= 0, "Timestamp should be non-negative");
            }
        }
    }

    @Test
    public void testNoSuchElementThrownWhenDone() throws IOException, JCodecException {
        try (VideoFrameExtractor extractor = new VideoFrameExtractor(SHORT_VIDEO_PATH)) {
            Iterator<VideoFrame> it = extractor.iterator();
            while (it.hasNext()) {
                it.next(); // exhaust all frames
            }
            assertThrows(NoSuchElementException.class, it::next);
        }
    }
}
