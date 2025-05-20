package io.github.jameson789.app;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

public class ImageSummaryAppTest {

    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();

    @BeforeEach
    void setUp() {

        System.setErr(new PrintStream(errContent));

        // Clean up any existing output files
        new File("frame_centroids.csv").delete();
        deleteDirectory(new File("binarized_frames"));
    }

    @AfterEach
    void tearDown() {
        // Clean up output files after tests
        new File("frame_centroids.csv").delete();
        deleteDirectory(new File("binarized_frames"));
    }

    private void deleteDirectory(File directory) {
        if (directory.exists()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        deleteDirectory(file);
                    } else {
                        file.delete();
                    }
                }
            }
            directory.delete();
        }
    }

    @Test
    void testInvalidArguments() {
        String[] args = { "ensantina.mp4", "FF0000" };
        assertThrows(IllegalArgumentException.class, () -> ImageSummaryApp.main(args));
    }

    @Test
    void testInvalidHexColor() {
        String[] args = { "ensantina.mp4", "GGGGGG", "100" };

        assertDoesNotThrow(() -> ImageSummaryApp.main(args));
        assertFalse(new File("frame_centroids.csv").exists());
        assertTrue(errContent.toString().contains("Error parsing color or threshold"));
    }

    @Test
    void testInvalidColorFormat() {
        String[] args = { "ensantina.mp4", "ZZZZZZ", "30" };

        assertDoesNotThrow(() -> ImageSummaryApp.main(args));
        assertTrue(errContent.toString().contains("Error parsing color or threshold"));
    }
}