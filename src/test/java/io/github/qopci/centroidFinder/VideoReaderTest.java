package io.github.qopci.centroidFinder;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.awt.image.BufferedImage;
import java.io.File;

// AI Used to generate tests for VideoReaderTest.java
public class VideoReaderTest {
    private static final String TEST_VIDEO_PATH = "src/test/resources/test.mp4";

    @Test
    public void testConstructorInitializesReader() {
        if (!new File(TEST_VIDEO_PATH).exists()) {
            System.out.println("Test video not found. Skipping testConstructorInitializesReader.");
            return;
        }

        try (VideoReader reader = new VideoReader(TEST_VIDEO_PATH)) {
            assertNotNull(reader, "VideoReader should be created");
        } catch (Exception e) {
            fail("Constructor threw an exception: " + e.getMessage());
        }
    }

    @Test
    public void testNextFrameReturnsImage() {
        if (!new File(TEST_VIDEO_PATH).exists()) {
            System.out.println("Test video not found. Skipping testNextFrameReturnsImage.");
            return;
        }

        try (VideoReader reader = new VideoReader(TEST_VIDEO_PATH)) {
            BufferedImage frame = reader.nextFrame();
            assertNotNull(frame, "nextFrame should return a BufferedImage");
        } catch (Exception e) {
            fail("nextFrame threw an exception: " + e.getMessage());
        }
    }

    @Test
    public void testShouldProcessThisFrame() {
        if (!new File(TEST_VIDEO_PATH).exists()) {
            System.out.println("Test video not found. Skipping testShouldProcessThisFrame.");
            return;
        }

        try (VideoReader reader = new VideoReader(TEST_VIDEO_PATH)) {
            boolean found = false;
            for (int i = 0; i < 100; i++) {
                reader.nextFrame();
                if (reader.shouldProcessThisFrame()) {
                    found = true;
                    break;
                }
            }
            assertTrue(found, "At least one frame should be marked for processing");
        } catch (Exception e) {
            fail("Exception during testShouldProcessThisFrame: " + e.getMessage());
        }
    }

    @Test
    public void testCloseDoesNotThrow() {
        if (!new File(TEST_VIDEO_PATH).exists()) {
            System.out.println("Test video not found. Skipping testCloseDoesNotThrow.");
            return;
        }

        try (VideoReader reader = new VideoReader(TEST_VIDEO_PATH)) {
            // Close happens automatically
        } catch (Exception e) {
            fail("close threw an exception: " + e.getMessage());
        }
    }
}
