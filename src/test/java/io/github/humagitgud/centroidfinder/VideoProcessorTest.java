package io.github.humagitgud.centroidfinder;

import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

public class VideoProcessorTest {

    @TempDir
    Path tempDir;

    @Test
    public void testProcessVideo_WritesCSV() throws IOException {
        // Arrange: Path to the real sample video file in the sampleInput folder
        Path inputVideoPath = Path.of("sampleInput/sampleVideo.mp4");
        Path outputCsvPath = Path.of("sampleOutput/output.csv");

        // Ensure the input video exists
        assertTrue(Files.exists(inputVideoPath), "Input video file does not exist: " + inputVideoPath);

        // Set up the grabber with the real video path
        FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(inputVideoPath.toString());
        grabber.start();

        // Create the VideoProcessor with the paths
        VideoProcessor processor = new VideoProcessor(inputVideoPath.toString(), outputCsvPath.toString(), 0xFF0000, 100);

        // Act: Process the video and generate the CSV
        processor.process();

        // Assert: Check that the CSV file is created in the sampleOutput folder
        assertTrue(Files.exists(outputCsvPath), "CSV file should be created.");
        File outputCsv = outputCsvPath.toFile();
        assertTrue(outputCsv.length() > 0, "CSV file should not be empty.");
    }
}
