package io.github.alstondsouza1.centroidFinder;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class CommandLineParserTest {

    @Test
    public void testValidArguments() {
        String[] args = {"video.mp4", "output.csv", "FF0000", "50"};
        CommandLineParser parser = new CommandLineParser(args);

        assertEquals("video.mp4", parser.getInputPath());
        assertEquals("output.csv", parser.getOutputCsv());
        assertEquals(0xFF0000, parser.getTargetColor());
        assertEquals(50, parser.getThreshold());
    }

    @Test
    public void testInvalidArgumentCountThrowsException() {
        String[] args = {"video.mp4", "output.csv", "FF0000"};
        assertThrows(IllegalArgumentException.class, () -> {
            new CommandLineParser(args);
        });
    }

    @Test
    public void testInvalidColorFormatThrowsException() {
        String[] args = {"video.mp4", "output.csv", "ZZZZZZ", "50"};
        assertThrows(NumberFormatException.class, () -> {
            new CommandLineParser(args);
        });
    }

    @Test
    public void testInvalidThresholdThrowsException() {
        String[] args = {"video.mp4", "output.csv", "FF0000", "abc"};
        assertThrows(NumberFormatException.class, () -> {
            new CommandLineParser(args);
        });
    }
    /*@Test
    public void testInvalidVideoFile() {
        String[] args = {"video.mp", "output.csv", "FF0000", "50"};
        assertThrows(Exception.class, () -> {
            new CommandLineParser(args);
        });
    }*/
}