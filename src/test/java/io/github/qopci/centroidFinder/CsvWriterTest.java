package io.github.qopci.centroidFinder;

import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.file.Files;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

// AI Used to generate tests for CsvWriterTest.java
public class CsvWriterTest {

    @Test
    public void testCsvWriterCreatesFileAndWritesHeader() throws IOException {
        File tempFile = File.createTempFile("test", ".csv");
        tempFile.deleteOnExit();

        try (CsvWriter writer = new CsvWriter(tempFile.getAbsolutePath())) {
            // Just create the writer to test header writing
        }

        List<String> lines = Files.readAllLines(tempFile.toPath());
        assertFalse(lines.isEmpty(), "CSV file should not be empty");
        assertEquals("x,y", lines.get(0), "Header should be 'x,y'");
    }

    @Test
    public void testCsvWriterWritesCoordinatesCorrectly() throws IOException {
        File tempFile = File.createTempFile("test", ".csv");
        tempFile.deleteOnExit();

        try (CsvWriter writer = new CsvWriter(tempFile.getAbsolutePath())) {
            writer.write(new Coordinate(10, 20));
            writer.write(new Coordinate(-5, 0));
        }

        List<String> lines = Files.readAllLines(tempFile.toPath());
        assertEquals(3, lines.size(), "CSV should contain header and two lines");
        assertEquals("10,20", lines.get(1));
        assertEquals("-5,0", lines.get(2));
    }

    @Test
    public void testCsvWriterClosesWithoutException() {
        assertDoesNotThrow(() -> {
            File tempFile = File.createTempFile("test", ".csv");
            tempFile.deleteOnExit();
            try (CsvWriter writer = new CsvWriter(tempFile.getAbsolutePath())) {
                writer.write(new Coordinate(1, 1));
            }
        });
    }

    @Test
    public void testCsvWriterHandlesExtremeValues() throws IOException {
        File tempFile = File.createTempFile("test", ".csv");
        tempFile.deleteOnExit();

        try (CsvWriter writer = new CsvWriter(tempFile.getAbsolutePath())) {
            writer.write(new Coordinate(Integer.MAX_VALUE, Integer.MIN_VALUE));
        }

        List<String> lines = Files.readAllLines(tempFile.toPath());
        assertEquals(String.format("%d,%d", Integer.MAX_VALUE, Integer.MIN_VALUE), lines.get(1));
    }

    @Test
    public void testEmptyFileHasOnlyHeader() throws IOException {
        File tempFile = File.createTempFile("test", ".csv");
        tempFile.deleteOnExit();

        try (CsvWriter writer = new CsvWriter(tempFile.getAbsolutePath())) {
            // no coordinates written
        }

        List<String> lines = Files.readAllLines(tempFile.toPath());
        assertEquals(1, lines.size());
        assertEquals("x,y", lines.get(0));
    }
}
