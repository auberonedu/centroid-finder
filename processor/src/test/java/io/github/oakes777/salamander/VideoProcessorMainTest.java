package io.github.oakes777.salamander;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.io.TempDir;

import java.io.*;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class VideoProcessorMainTest {

    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;
    private ByteArrayOutputStream outContent;
    private ByteArrayOutputStream errContent;

    @BeforeEach
    void setUpStreams() {
        outContent = new ByteArrayOutputStream();
        errContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    @AfterEach
    void restoreStreams() {
        System.setOut(originalOut);
        System.setErr(originalErr);
    }

    @Test
    void testMain_withInvalidArguments() {
        String[] args = {}; // not enough arguments
        VideoProcessorMain.main(args);
        String output = outContent.toString();
        assertTrue(output.contains("Usage:"), "Should print usage instructions for missing args");
    }

    @Test
    void testMain_withInvalidRgbColor() {
        String[] args = {
                "video.mp4", // fake path
                "255,red,0", // invalid RGB
                "50",
                "output.csv",
                "0.5s"
        };
        VideoProcessorMain.main(args);
        String error = errContent.toString();
        assertTrue(error.contains("Invalid number format"), "Should catch bad RGB parsing");
    }

    @Test
    void testMain_withMockedVideoProcessing(@TempDir Path tempDir) {
        // This test assumes a real video file exists (optional test)
        File sampleVideo = new File("src/test/resources/sample.mp4");
        if (!sampleVideo.exists()) {
            System.out.println("Skipping video test: sample.mp4 not found.");
            return;
        }

        File outputCsv = tempDir.resolve("output.csv").toFile();

        String[] args = {
                sampleVideo.getAbsolutePath(),
                "255,0,0", // RGB format for red
                "40",
                outputCsv.getAbsolutePath(),
                "0.5s"
        };

        VideoProcessorMain.main(args);

        assertTrue(outputCsv.exists(), "Output CSV should be created");
    }
}
