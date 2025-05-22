package io.github.oakes777.salamander;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.time.Duration;
import java.util.List;

import org.jcodec.api.JCodecException;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

public class VideoCentroidTrackerTest {

    @TempDir
    Path tempDir;

    @Test
    public void testConstructorInitializesAnalyzer() {
        VideoCentroidTracker tracker = new VideoCentroidTracker(0xFF0000, 50);
        assertNotNull(tracker);
    }

    @Test
    public void testTrackCentroidsProcessesEveryNthFrame() throws IOException, JCodecException {
        String csvPath = tempDir.resolve("output_nth.csv").toString();
        VideoCentroidTracker tracker = new VideoCentroidTracker(0xFFFFFF, 10);

        assertTimeoutPreemptively(Duration.ofSeconds(10), () -> {
            tracker.trackCentroids("sampleInput/shorty.mp4", csvPath, 24);
        });

        File out = new File(csvPath);
        assertTrue(out.exists(), "Expected CSV output file to exist");
        assertTrue(out.length() > 0, "Expected CSV file to have content");
    }

    @Test
    public void testOutputFormatIsCorrect() throws IOException, JCodecException {
        String csvPath = tempDir.resolve("test_output.csv").toString();
        VideoCentroidTracker tracker = new VideoCentroidTracker(0xFFFFFF, 10);

        tracker.trackCentroids("sampleInput/shorty.mp4", csvPath, 24);

        List<String> lines = java.nio.file.Files.readAllLines(Path.of(csvPath));
        for (String line : lines) {
            assertTrue(line.matches("^\\d+\\.\\d{3},-?\\d+,-?\\d+$"),
                    "Line did not match expected format: " + line);
        }
    }

    @Test
    public void testGracefulFailureOnBadInput() {
        VideoCentroidTracker tracker = new VideoCentroidTracker(0xFFFFFF, 50);
        assertThrows(IOException.class, () -> {
            tracker.trackCentroids("invalid/path/video.mp4", "output.csv");
        });
    }
}
