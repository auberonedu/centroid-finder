package io.github.qopci.centroidFinder;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CommandLineParserTest {

    @Test
    void parsesValidHexColorWithoutPrefix() {
        String[] args = {
            "video.mp4",
            "output.csv",
            "0xFF0000",
            "30"
        };
        CommandLineParser parser = new CommandLineParser(args);

        assertEquals("video.mp4", parser.inputPath);
        assertEquals("output.csv", parser.outputCsv);
        assertEquals(0xFF0000, parser.targetColor);
        assertEquals(30, parser.threshold);
    }

    @Test
    void parsesValidHexColorWith0xPrefix() {
        String[] args = {"video.mp4", "output.csv", "0x00ff00", "40"};
        CommandLineParser parser = new CommandLineParser(args);
        assertEquals(0x00FF00, parser.targetColor);
        assertEquals(40, parser.threshold);
    }

    @Test
    void throwsExceptionWhenArgumentsAreMissing() {
        String[] args = {"video.mp4", "output.csv", "ff0000"};
        Exception exception = assertThrows(IllegalArgumentException.class, () -> new CommandLineParser(args));
        assertTrue(exception.getMessage().contains("Usage"));
    }

    @Test
    void throwsExceptionOnInvalidHexColor() {
        String[] args = {"video.mp4", "output.csv", "notAHex", "20"};
        Exception exception = assertThrows(IllegalArgumentException.class, () -> new CommandLineParser(args));
        assertTrue(exception.getMessage().contains("Invalid hex color format"));
    }

    @Test
    void throwsExceptionOnNonIntegerThreshold() {
        String[] args = {"video.mp4", "output.csv", "ff0000", "notAnInt"};
        assertThrows(IllegalArgumentException.class, () -> new CommandLineParser(args));
    }

    @Test
    void parsesThresholdBoundaryZero() {
        String[] args = {"video.mp4", "output.csv", "000000", "0"};
        CommandLineParser parser = new CommandLineParser(args);
        assertEquals(0, parser.threshold);
    }

    @Test
    void parsesThresholdLargeValue() {
        String[] args = {"video.mp4", "output.csv", "abcdef", "999"};
        CommandLineParser parser = new CommandLineParser(args);
        assertEquals(999, parser.threshold);
    }

    @Test
    void testNegativeThresholdThrowsException() {
        String[] args = {"video.mp4", "output.csv", "000000", "-1"};
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            new CommandLineParser(args);
        });
        assertEquals("Threshold must be non-negative", ex.getMessage());
    }

    @Test
    void acceptsShortHexColor() {
        String[] args = {"video.mp4", "output.csv", "abc", "12"};
        CommandLineParser parser = new CommandLineParser(args);
        assertEquals(0xABC, parser.targetColor);
    }
}
