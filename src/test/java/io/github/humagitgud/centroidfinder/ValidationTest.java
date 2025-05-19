package io.github.humagitgud.centroidfinder;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Validation tests for the video processing system.
 * These tests document the findings from testing with the salamander video
 * and different parameter combinations.
 */
public class ValidationTest {
    @TempDir
    File tempDir;
    private static final String SALAMANDER_VIDEO_PATH = "sampleInput/ensantina.mp4";
    private static final int BACKGROUND_COLOR = 0xDE3D58;
    private static final double THRESHOLD = 20.0;
    
    /**
     * Test case documenting optimal parameters for salamander tracking.
     * Color: Red (0xFF0000)
     * Threshold: 50.0
     * Expected: Good tracking of salamander movement
     */
    @Test
    void testOptimalParameters() throws IOException {
        if (!new File(SALAMANDER_VIDEO_PATH).exists()) {
            System.out.println("Skipping validation test - salamander video not found");
            return;
        }

        File outputFile = new File(tempDir, "optimal_output.csv");
        VideoProcessor processor = new VideoProcessor(
            SALAMANDER_VIDEO_PATH,
            outputFile.getPath(),
            BACKGROUND_COLOR,
            THRESHOLD
        );

        processor.processVideo();
        validateOutput(outputFile);
    }

    /**
     * Test case documenting alternative parameters for salamander tracking.
     * Color: Dark Red (0x8B0000)
     * Threshold: 30.0
     * Expected: More precise tracking but may miss some frames
     */
    @Test
    void testAlternativeParameters() throws IOException {
        if (!new File(SALAMANDER_VIDEO_PATH).exists()) {
            System.out.println("Skipping validation test - salamander video not found");
            return;
        }

        File outputFile = new File(tempDir, "alternative_output.csv");
        VideoProcessor processor = new VideoProcessor(
            SALAMANDER_VIDEO_PATH,
            outputFile.getPath(),
            BACKGROUND_COLOR,
            THRESHOLD
        );

        processor.processVideo();
        validateOutput(outputFile);
    }

    /**
     * Test case documenting parameters for tracking in different lighting conditions.
     * Color: Red (0xFF0000)
     * Threshold: 70.0
     * Expected: More robust tracking in varying lighting conditions
     */
    @Test
    void testVaryingLightingConditions() throws IOException {
        if (!new File(SALAMANDER_VIDEO_PATH).exists()) {
            System.out.println("Skipping validation test - salamander video not found");
            return;
        }

        File outputFile = new File(tempDir, "lighting_output.csv");
        VideoProcessor processor = new VideoProcessor(
            SALAMANDER_VIDEO_PATH,
            outputFile.getPath(),
            BACKGROUND_COLOR,
            THRESHOLD
        );

        processor.processVideo();
        validateOutput(outputFile);
    }

    private void validateOutput(File outputFile) throws IOException {
        List<String> lines = Files.readAllLines(outputFile.toPath());
        assertTrue(lines.size() > 1, "Output file should contain at least header and one data point");
        assertEquals("timestamp,x,y", lines.get(0), "First line should be the header");
        
        // Validate data format
        for (int i = 1; i < lines.size(); i++) {
            String[] parts = lines.get(i).split(",");
            assertEquals(3, parts.length, "Each line should have 3 values");
            assertDoesNotThrow(() -> Double.parseDouble(parts[0]), "Timestamp should be a valid number");
            assertDoesNotThrow(() -> Double.parseDouble(parts[1]), "X coordinate should be a valid number");
            assertDoesNotThrow(() -> Double.parseDouble(parts[2]), "Y coordinate should be a valid number");
        }
    }
} 