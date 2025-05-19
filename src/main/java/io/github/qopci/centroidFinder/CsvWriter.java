package io.github.qopci.centroidFinder;

import java.io.PrintWriter;
import java.io.FileWriter;
import java.io.IOException;

public class CsvWriter implements AutoCloseable {
    private final PrintWriter writer;

    // Constructor: open the output file and write the header line with tabs
    public CsvWriter(String path) throws IOException {
        this.writer = new PrintWriter(new FileWriter(path)); 
        this.writer.println("time\tx\ty"); // header with tabs
    }

    // Write the coordinate pair (without time)
    public void write(Coordinate c) {
        writer.printf("%d\t%d%n", c.x(), c.y());
    }

    // Write timestamp in decimal seconds with tabs
    public void write(double timeInSeconds, Coordinate c) {
        writer.printf("%.2f\t%d\t%d%n", timeInSeconds, c.x(), c.y());
    }

    // Write formatted time string with tabs
    public void write(String formattedTime, Coordinate c) {
        writer.printf("%s\t%d\t%d%n", formattedTime, c.x(), c.y());
    }

    // Close the file when done writing
    public void close() {
        writer.close();
    }
}
