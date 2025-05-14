package io.github.qopci.centroidFinder;

import java.io.PrintWriter;
import java.io.FileWriter;
import java.io.IOException;

public class CsvWriter {
    private final PrintWriter writer;

    // Constructor here: open the output file and write the headerline
    public CsvWriter(String path) throws IOException {
        this.writer = new PrintWriter(new FileWriter(path)); 
        this.writer.println("x,y"); // Writes the CSV header
    }
    
    // Write the coordinate pair to CSV file
    public void write(Coordinate c) {
        writer.printf("%d,%d%n", c.x(), c.y()); // Write x and y values as a CSV line
    }

    // Close the file when done writing
    public void close() {
        writer.close(); 
    }
}
