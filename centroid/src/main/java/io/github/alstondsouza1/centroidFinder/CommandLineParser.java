package io.github.alstondsouza1.centroidFinder;

public class CommandLineParser {
    private final String inputPath;
    private final String outputCsv;
    private final int targetColor;
    private final int threshold;

    // constructor that takes and validates command line arguments
    public CommandLineParser(String[] args) {
        if (args.length != 4) {
            throw new IllegalArgumentException("Usage: java -jar videoprocessor.jar inputPath outputCsv targetColor threshold");
        }
        this.inputPath = args[0];
        this.outputCsv = args[1];
        this.targetColor = Integer.parseInt(args[2], 16); // parse color from hex
        this.threshold = Integer.parseInt(args[3]); // parse threshold as int
    }

    public String getInputPath() {
        return inputPath;
    }

    public String getOutputCsv() {
        return outputCsv;
    }

    public int getTargetColor() {
        return targetColor;
    }

    public int getThreshold() {
        return threshold;
    }
}