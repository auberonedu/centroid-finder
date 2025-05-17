package io.github.qopci.centroidFinder;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

// AI Used to generate tests for CommandLineParserTest.java
class CommandLineParserTest {

    @Test
    void parsesValidNamedColorsCorrectly() {
        String[] args = {
            "video.mp4",
            "output.csv",
            "RED",
            "30"
        };
        CommandLineParser parser = new CommandLineParser(args);

        assertEquals("video.mp4", parser.inputPath);
        assertEquals("output.csv", parser.outputCsv);
        assertEquals(0xFF0000, parser.targetColor);
        assertEquals(30, parser.threshold);
    }

    @Test
    void throwsExceptionWhenArgumentsAreMissing() {
        String[] args = {"video.mp4", "output.csv", "RED"};
        Exception exception = assertThrows(IllegalArgumentException.class, () -> new CommandLineParser(args));
        assertTrue(exception.getMessage().contains("Usage"));
    }

    @Test
    void throwsExceptionOnInvalidColorName() {
        String[] args = {"video.mp4", "output.csv", "BLUE", "20"};  // BLUE not in COLOR_MAP
        Exception exception = assertThrows(IllegalArgumentException.class, () -> new CommandLineParser(args));
        assertTrue(exception.getMessage().contains("Invalid color name"));
    }

    @Test
    void throwsExceptionOnNonIntegerThreshold() {
        String[] args = {"video.mp4", "output.csv", "RED", "notAnInt"};
        assertThrows(IllegalArgumentException.class, () -> new CommandLineParser(args));
    }

    @Test
    void acceptsLowercaseColorNames() {
        String[] args = {"video.mp4", "output.csv", "pink", "10"};
        CommandLineParser parser = new CommandLineParser(args);
        assertEquals(0xFFC0CB, parser.targetColor);
    }

    @Test
    void trimsWhitespaceInColorNames() {
        String[] args = {"video.mp4", "output.csv", "  white  ", "15"};
        CommandLineParser parser = new CommandLineParser(args);
        assertEquals(0xFFFFFF, parser.targetColor);
    }

    @Test
    void parsesThresholdBoundaryZero() {
        String[] args = {"video.mp4", "output.csv", "BLACK", "0"};
        CommandLineParser parser = new CommandLineParser(args);
        assertEquals(0, parser.threshold);
    }

    @Test
    void parsesThresholdLargeValue() {
        String[] args = {"video.mp4", "output.csv", "RED", "999"};
        CommandLineParser parser = new CommandLineParser(args);
        assertEquals(999, parser.threshold);
    }
}
