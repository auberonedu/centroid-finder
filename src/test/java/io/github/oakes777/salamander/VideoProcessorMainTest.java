package io.github.oakes777.salamander;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

public class VideoProcessorMainTest {

    @TempDir
    Path tempDir;

    @Test
    public void testFullRunWithMockedInput() throws Exception {
        // Prepare test video
        String testVideoPath = "sampleInput/shorty.mp4"; // keep it small for fast testing
        String outputCsv = tempDir.resolve("output.csv").toString();

        // Simulated user input
        String input = String.join("\n",
                "0000FF", // target color (blue)
                "50", // threshold
                "yes", // accept preview
                outputCsv, // CSV output filename
                "5" // frame interval
        );

        InputStream originalIn = System.in;
        PrintStream originalOut = System.out;

        try {
            System.setIn(new ByteArrayInputStream(input.getBytes()));
            System.setOut(new PrintStream(OutputStream.nullOutputStream())); // silence output

            VideoProcessorMain.main(new String[] { testVideoPath });

            // Confirm CSV was created
            File outputFile = new File(outputCsv);
            assertTrue(outputFile.exists(), "Output CSV should be created.");
        } finally {
            System.setIn(originalIn);
            System.setOut(originalOut);
        }
    }

    @Test
    public void testMissingArgsPrintsUsageMessage() {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;

        try {
            System.setOut(new PrintStream(output));
            VideoProcessorMain.main(new String[] {}); // No arguments
            String result = output.toString();
            assertTrue(result.contains("Usage:"), "Should print usage instructions.");
        } finally {
            System.setOut(originalOut);
        }
    }
}
