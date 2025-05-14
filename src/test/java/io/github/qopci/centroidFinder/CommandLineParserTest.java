package io.github.qopci.centroidFinder;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

// AI Used to generate tests for CommandLineParserTest.java
class CommandLineParserTest {

    @Test
    void parsesValidArgumentsCorrectly() {
        String[] args = {
            "video.mp4",  // inputPath
            "output.csv", // outputCsv
            "255,100,50", // targetColor
            "30"          // threshold
        };
        CommandLineParser parser = new CommandLineParser(args);

        assertEquals("video.mp4", parser.inputPath);
        assertEquals("output.csv", parser.outputCsv);
        assertEquals((255 << 16) | (100 << 8) | 50, parser.targetColor);
        assertEquals(30, parser.threshold);
    }

    @Test
    void throwsExceptionWhenArgumentsAreMissing() {
        String[] args = {"video.mp4", "output.csv", "255,0,0"};
        Exception exception = assertThrows(IllegalArgumentException.class, () -> new CommandLineParser(args));
        assertTrue(exception.getMessage().contains("Usage"));
    }

    @Test
    void throwsExceptionOnInvalidColorFormat() {
        String[] args = {"video.mp4", "output.csv", "255,0", "20"};
        Exception exception = assertThrows(IllegalArgumentException.class, () -> new CommandLineParser(args));
        assertTrue(exception.getMessage().contains("Color must be in R,G,B format"));
    }

    @Test
    void throwsExceptionOnNonIntegerThreshold() {
        String[] args = {"video.mp4", "output.csv", "255,0,0", "notAnInt"};
        assertThrows(NumberFormatException.class, () -> new CommandLineParser(args));
    }

    @Test
    void parsesColorWithExtraWhitespace() {
        String[] args = {"video.mp4", "output.csv", "  10 , 20 , 30 ", "15"};
        CommandLineParser parser = new CommandLineParser(args);
        assertEquals((10 << 16) | (20 << 8) | 30, parser.targetColor);
    }
}
