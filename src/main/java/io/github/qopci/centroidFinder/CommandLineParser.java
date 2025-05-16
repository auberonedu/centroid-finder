package io.github.qopci.centroidFinder;

import java.util.Map;

public class CommandLineParser {
    public final String inputPath;
    public final String outputCsv;
    public final int targetColor;
    public final int threshold;

    private static final Map<String, Integer> COLOR_MAP = Map.of(
        "RED", 0xFF0000,
        "PINK", 0xFFC0CB,
        "BLACK", 0x000000,
        "WHITE", 0xFFFFFF
    );

    public CommandLineParser(String[] args) {
        if (args.length < 4) {
            throw new IllegalArgumentException("Usage: java VideoProcessorApp <video_path> <csv_output> <color_name> <threshold>");
        }

        this.inputPath = args[0];
        this.outputCsv = args[1];
        this.targetColor = parseColor(args[2]);
        try {
            this.threshold = Integer.parseInt(args[3]);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Threshold must be an integer", e);
        }
    }

    private int parseColor(String input) {
        input = input.trim().toUpperCase();

        if (!COLOR_MAP.containsKey(input)) {
            throw new IllegalArgumentException("Invalid color name. Supported names: " + COLOR_MAP.keySet());
        }
        return COLOR_MAP.get(input);
    }
}
