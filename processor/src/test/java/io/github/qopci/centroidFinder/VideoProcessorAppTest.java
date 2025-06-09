package io.github.qopci.centroidFinder;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class VideoProcessorAppTest {

    @Test
    public void testFormatTime_Zero() {
        assertEquals("00:00:00", VideoProcessorApp.formatTime(0));
    }

    @Test
    public void testFormatTime_OneSecond() {
        assertEquals("00:00:01", VideoProcessorApp.formatTime(1));
    }

    @Test
    public void testFormatTime_OneMinuteOneSecond() {
        assertEquals("00:01:01", VideoProcessorApp.formatTime(61));
    }

    @Test
    public void testFormatTime_OneHourOneMinuteOneSecond() {
        assertEquals("01:01:01", VideoProcessorApp.formatTime(3661));
    }

    @Test
    public void testFormatTime_RoundedDown() {
        assertEquals("00:01:00", VideoProcessorApp.formatTime(60.999));
    }

    @Test
    public void testFormatTime_Negative() {
        assertEquals("00:00:00", VideoProcessorApp.formatTime(-5));
    }
}
