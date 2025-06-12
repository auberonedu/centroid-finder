package io.github.AugleBoBaugles.centroidFinder;

import java.awt.image.BufferedImage;
import java.io.PrintWriter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

public class VideoProcessorTest {

        // AI Helped create this "spy" to override the frameToData function
    public class TestVideoProcessor extends VideoProcessor {
        public int callCounter = 0;

        public TestVideoProcessor(String inputPath, String outputPath, int targetColor, int threshold) {
            super(inputPath, outputPath, targetColor, threshold);
        }

        @Override
        public void frameToData(BufferedImage frame, int seconds, PrintWriter x) {
            callCounter++;
        }
    }

    // @Test
    // public void testExtractFramesCallsFrameToDataAtOneSecondIntervals() {
    //     String videoPath = "sampleInput/sample_video_1.mp4";
    //     String outputPath = "sampleOutput/test1.csv";
    //     TestVideoProcessor vp = new TestVideoProcessor(videoPath, outputPath, 0xFF0000, 30); 

    //     vp.extractFrames();

    //     // result = number of seconds + 1
    //     int expected = 5;
    //     assertEquals(expected, vp.callCounter);
    // }
    // extractFrames() pulls out correct frames


    // Test for writing a valid CSV file and seeing if no centroid was found
    @Test
    public void testFrameToData_writesInvalidCentroid() {
        String videoPath = "sampleInput/sample_video_1.mp4";
        String outputPath = "sampleOutput/test2.csv";
        VideoProcessor vp = new VideoProcessor(videoPath, outputPath, 0xFF9900, 150);

        // Create a dummy image with no matching pixels
        BufferedImage img = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB); // The 10s represent the pixels
        // Iterate through the pixels to fill them with the color black
        for(int i = 0; i < 10; i++) {
            for(int j = 0; j < 10; j++) {
                img.setRGB(i, j, 0x000000);
            }
        }

        // Creating an in-memory character stream that behaves like a StringBuilder, anything written to 'sw' can later be read as a single String.
        java.io.StringWriter sw = new java.io.StringWriter();
        PrintWriter pw = new PrintWriter(sw);

        vp.frameToData(img, 3, pw);
        pw.flush();

        String line = sw.toString().trim();
        assertEquals("3,-1,-1", line, "Should write -1,-1 when no centroid is found");
    }

    // Test for a valid centroid in a 10x10 pixel image.
    @Test
    public void testFrameToData_writesValidCentroid() {
        String videoPath = "sampleInput/sample_video_1.mp4";
        String outputPath = "sampleOutput/test3.csv";
        VideoProcessor vp = new VideoProcessor(videoPath, outputPath, 0xFF9900, 150);

        BufferedImage img = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);
        for(int i = 2; i <= 4; i++) {
            for(int j = 2; j <= 4; j++) {
                img.setRGB(j, i, 0xDD9900); // possible centroid
            }
        }

        java.io.StringWriter sw = new java.io.StringWriter();
        PrintWriter pw = new PrintWriter(sw);

        vp.frameToData(img, 5, pw);
        pw.flush();

        String line = sw.toString().trim();

        assertEquals("5,3,3", line, "Should write the correct centroid coordinates.");
    }
}
