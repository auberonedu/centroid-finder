package io.github.qopci.centroidFinder;

import java.io.PrintWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class CsvWriter {
    private final PrintWriter writer;

    //constructor here: open the output file and write the headerline
    public CsvWriter(String path) throws IOException {
        this.writer = new PrintWriter(new FileWriter(path)); 
        this.writer.println("x,y"); //this writes the CSV header
    }
    //write the coordinate pair to CSV file
    public void write(Coordinate c) {
        writer.printf("%d,%d%n", c.x(), c.y()); // Write x and y values as a CSV line
    }

    //close the file when done writing
    public void close() {
        writer.close(); // Ensure file is properly closed
    }

}
