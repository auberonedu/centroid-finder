package io.github.TiaMarieG.centroidFinder;

import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

public class FPSFinderTest {
    @Test
    public void testFPSReaderWithValidVideo() {
        // Build the relative path to the video
        String videoPath = Paths.get("videos", "nyan-cat.mp4").toString();

        FPSFinder finder = new FPSFinder();
        double fps = finder.FPSReader(videoPath);

        // Verify FPS is correct for a typical video
        assertTrue(fps == 25, "FPS is correct.");
        System.out.println(fps + " Frames Per Second");
    }

    @Test
    public void testFPSReaderWithInvalidPath() {
        String invalidPath = "videos/non_existent_video.mp4";
        FPSFinder finder = new FPSFinder();
        double fps = finder.FPSReader(invalidPath);

        // Should return 0.0 if file is not found or readable
        assertEquals(0.0, fps, "FPS should be 0.0 for an invalid file path.");
    }
}
