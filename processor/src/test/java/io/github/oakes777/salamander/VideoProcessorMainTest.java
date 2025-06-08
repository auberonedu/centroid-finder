// package io.github.oakes777.salamander;

// import java.io.ByteArrayInputStream;
// import java.io.ByteArrayOutputStream;
// import java.io.File;
// import java.io.InputStream;
// import java.io.OutputStream;
// import java.io.PrintStream;
// import java.nio.file.Path;

// import static org.junit.jupiter.api.Assertions.assertTrue;
// import org.junit.jupiter.api.Test;
// import org.junit.jupiter.api.io.TempDir;

// public class VideoProcessorMainTest {

//     @TempDir
//     Path tempDir;

//     @Test
//     public void testFullRunWithMockedInput() throws Exception {
//         // Prepare test video
//         String testVideoPath = "sampleInput/shorty.mp4"; // keep it small for fast testing
//         String outputCsv = tempDir.resolve("output.csv").toString();

//         // Simulated user input
//         String input = String.join("\n",
//                 "0000FF", // target color (blue)
//                 "50", // threshold
//                 "yes", // accept preview
//                 outputCsv, // CSV output filename
//                 "5" // frame interval
//         );

//         InputStream originalIn = System.in;
//         PrintStream originalOut = System.out;

//         try {
//             System.setIn(new ByteArrayInputStream(input.getBytes()));
//             System.setOut(new PrintStream(OutputStream.nullOutputStream())); // silence output

//             VideoProcessorMain.main(new String[] { testVideoPath });

//             // Confirm CSV was created
//             File outputFile = new File(outputCsv);
//             assertTrue(outputFile.exists(), "Output CSV should be created.");
//         } finally {
//             System.setIn(originalIn);
//             System.setOut(originalOut);
//         }
//     }

//     @Test
//     public void testMissingArgsPrintsUsageMessage() {
//         ByteArrayOutputStream output = new ByteArrayOutputStream();
//         PrintStream originalOut = System.out;

//         try {
//             System.setOut(new PrintStream(output));
//             VideoProcessorMain.main(new String[] {}); // No arguments
//             String result = output.toString();
//             assertTrue(result.contains("Usage:"), "Should print usage instructions.");
//         } finally {
//             System.setOut(originalOut);
//         }
//     }
// }
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
        assertTrue(output.contains("Usage: java -jar videoprocessor.jar"));
    }

    @Test
    void testMain_withInvalidHexColor() {
        String[] args = {"video.mp4", "ZZZZZZ", "40", "output.csv"};
        VideoProcessorMain.main(args);
        String error = errContent.toString();
        assertTrue(error.contains("Error: Invalid number format"));
    }

    @Test
    void testMain_withMockedVideoProcessing(@TempDir Path tempDir) {
        // This test assumes existence of a sample test file. In practice, you'd mock components.
        File sampleVideo = new File("src/test/resources/sample.mp4");
        if (!sampleVideo.exists()) {
            System.out.println("Skipping real video processing test: sample video not found.");
            return;
        }

        File outputCsv = tempDir.resolve("output.csv").toFile();

        String[] args = {
            sampleVideo.getAbsolutePath(),
            "FF0000",        // red
            "40",            // threshold
            outputCsv.getAbsolutePath(),
            "2"              // frame interval
        };

        VideoProcessorMain.main(args);

        assertTrue(outputCsv.exists(), "Output CSV should exist");
        assertTrue(new File("preview.png").exists(), "Preview image should be saved");
    }
}