package io.github.qopci.centroidFinder;

public class CommandLineParser {
    public final String inputPath;
    public final String outputCsv;
    public final int targetColor;
    public final int threshold;

    public CommandLineParser(String[] args) {
        if (args.length < 4) {
            throw new IllegalArgumentException("Usage: java -jar videoprocessor.jar <video_path> <csv_output> <target_color> <threshold>");
        }

        this.inputPath = args[0];
        this.outputCsv = args[1];
        this.targetColor = parseColor(args[2]);

        try {
            this.threshold = Integer.parseInt(args[3]);
            if (this.threshold < 0) {
                throw new IllegalArgumentException("Threshold must be non-negative");
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Threshold must be an integer", e);
        }
    }

    private int parseColor(String input) {
        String hex = input.trim().toLowerCase();

        if (hex.startsWith("0x")) {
            hex = hex.substring(2);
        }

        // Validate hex length (only 6 digits for RGB)
        if (!hex.matches("[0-9a-f]{6}")) {
            throw new IllegalArgumentException("Invalid hex color format: " + input);
        }

        return Integer.parseInt(hex, 16);
    }
}
