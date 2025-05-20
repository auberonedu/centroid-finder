package io.github.alstondsouza1.centroidFinder;

import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

// Used AI to generate and fixed the errors for CsvWriterTest
public class CsvWriterTest {

    @Test
    public void testWriteCentroidToFile() throws IOException {
        String path = "test_output.csv";
        CsvWriter writer = new CsvWriter(path);

        writer.writeCentroid(1.23, new Coordinate(10, 20));
        writer.writeCentroid(2.00, new Coordinate(5, 5));
        writer.close();

        List<String> lines = Files.readAllLines(Paths.get(path));

        assertEquals("time,x,y", lines.get(0));
        assertEquals("1.23,10,20", lines.get(1));
        assertEquals("2.00,5,5", lines.get(2));

        // cleanup
        Files.deleteIfExists(Paths.get(path));
    }
}