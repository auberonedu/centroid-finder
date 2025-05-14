package io.github.qopci.centroidFinder;

public class CommandLineParser {
    // Variables to store parsed command-line arguments
    public final String inputPath;
    public final String outputCsv;
    public final int targetColor;
    public final int threshold;

    // Constructor goes here...

    private int parseColor(String colorString) {
        String[] rgb = colorString.split(",");
        
        if (rgb.length != 3) throw new IllegalArgumentException("Color must be in R,G,B format");
        
        // Parsing the individual RGB components as integers and combining them into one color integer
        int r = Integer.parseInt(rgb[0].trim());
        int g = Integer.parseInt(rgb[1].trim());
        int b = Integer.parseInt(rgb[2].trim());
        
        // Return the combined color 
        return (r << 16) | (g << 8) | b;
    }
}
