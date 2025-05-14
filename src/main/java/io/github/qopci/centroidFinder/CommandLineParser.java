package io.github.qopci.centroidFinder;

public class CommandLineParser {
    public final String inputPath;
    public final String outputCsv;
    public final int targetColor;
    public final int threshold;

    // Constructor that parses the arguments
    public CommandLineParser(String[] args) {
        if (args.length < 4) {
            throw new IllegalArgumentException("Usage: java VideoProcessorApp <video_path> <csv_output> <R,G,B> <threshold>");
        }

        // Assign the values of the arguments to their respective fields
        this.inputPath = args[0];
        this.outputCsv = args[1];
        this.targetColor = parseColor(args[2]); 
        this.threshold = Integer.parseInt(args[3]);  
    }

    private int parseColor(String colorString) {
        String[] rgb = colorString.split(",");
        
        if (rgb.length != 3) throw new IllegalArgumentException("Color must be in R,G,B format");
        
        // Parse the individual RGB components as integers and combine them into one color integer
        int r = Integer.parseInt(rgb[0].trim());
        int g = Integer.parseInt(rgb[1].trim());
        int b = Integer.parseInt(rgb[2].trim());
        
        // Return the combined color 
        return (r << 16) | (g << 8) | b;
    }
}
