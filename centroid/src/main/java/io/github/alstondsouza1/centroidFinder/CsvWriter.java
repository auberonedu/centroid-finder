package io.github.alstondsouza1.centroidFinder;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

// writes centroid data to a CSV file
public class CsvWriter {
    private final PrintWriter writer;

    public CsvWriter(String outputCsv) throws IOException {
        this.writer = new PrintWriter(new FileWriter(outputCsv));
        writer.println("time,x,y");
    }

    // write sone line of centroid data with timestamp
    public void writeCentroid(double timeInSeconds, Coordinate c) {
        writer.printf("%.2f,%d,%d\n", timeInSeconds, c.x(), c.y());
    }

    // close the writer
    public void close() {
        writer.close();
    }
}