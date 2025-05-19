package io.github.qopci.centroidFinder;

import java.io.PrintWriter;
import java.io.FileWriter;
import java.io.IOException;

public class CsvWriter implements AutoCloseable {
    private final PrintWriter writer;

    // Constructor here: open the output file and write the headerline
    public CsvWriter(String path) throws IOException {
        this.writer = new PrintWriter(new FileWriter(path)); 
        this.writer.println("time,x,y"); // Updated header to include time
    }

    // Write the coordinate pair to CSV file
    public void write(Coordinate c) {
        writer.printf("%d,%d%n", c.x(), c.y()); // Original method, still supported
    }

    // New method: write timestamp and coordinate
    public void write(double timeInSeconds, Coordinate c) {
        writer.printf("%.2f,%d,%d%n", timeInSeconds, c.x(), c.y()); // time,x,y formatted
    }

    // Close the file when done writing
    public void close() {
        writer.close(); 
    }
}
