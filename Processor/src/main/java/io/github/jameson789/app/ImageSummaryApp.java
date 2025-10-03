package io.github.jameson789.app;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;

import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Java2DFrameConverter;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Usage:
 *   java ImageSummaryApp <input_video> <hex_target_color> <threshold> <task_id> [--areas-file <path>]
 *
 * Behavior:
 * - Without --areas-file: CSV rows are "second,x,y".
 * - With --areas-file:    CSV rows are "second,x,y,region" (region may be empty if no match).
 *
 * Notes:
 * - RESULT_PATH env var can override output directory; otherwise "../results" is used.
 * - Regions JSON must be an object of { "name": { "x": int, "y": int, "width": int, "height": int }, ... }.
 */
public class ImageSummaryApp {

    public static void main(String[] args) {
        if (args.length < 4) {
            throw new IllegalArgumentException(
                "Usage: java ImageSummaryApp <input_video> <hex_target_color> <threshold> <task_id> [--areas-file <path>]"
            );
        }

        // Positional args
        final String videoPath = args[0];
        final String hexTargetColor = args[1];
        final String taskId = args[3];

        final int targetColor;
        final int threshold;
        try {
            targetColor = Integer.parseInt(hexTargetColor, 16);
            threshold   = Integer.parseInt(args[2]);
        } catch (Exception e) {
            System.err.println("Error parsing color or threshold.");
            return;
        }

        // Optional flags
        String areasFilePath = null;
        for (int i = 4; i < args.length; i++) {
            String a = args[i];
            if ("--areas-file".equals(a) && i + 1 < args.length) {
                areasFilePath = args[++i];
            } else {
                System.err.println("Unknown or incomplete option: " + a);
            }
        }

        // Load regions if provided
        Map<String, Region> regions = null;
        if (areasFilePath != null && !areasFilePath.isBlank()) {
            try {
                regions = loadRegionsFromJson(areasFilePath);
                System.out.println("Loaded regions from " + areasFilePath + " count=" + regions.size());
            } catch (IOException ex) {
                System.err.println("Failed to load regions JSON: " + ex.getMessage());
                // Continue without regions for robustness
            }
        }
        final boolean writeRegion = (regions != null && !regions.isEmpty());

        // Output file setup
        String resultDir = System.getenv("RESULT_PATH");
        if (resultDir == null || resultDir.isBlank()) {
            resultDir = "../results"; // fallback for local dev
        }

        String baseName = new File(videoPath).getName();
        int dotIndex = baseName.lastIndexOf('.');
        if (dotIndex > 0) baseName = baseName.substring(0, dotIndex);

        String outputFileName = baseName + "_" + taskId + ".csv";
        File outputFile = new File(resultDir, outputFileName);

        try (FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(videoPath);
             PrintWriter writer = new PrintWriter(outputFile)) {

            grabber.start();
            Java2DFrameConverter converter = new Java2DFrameConverter();
            ImageProcessor processor = new ImageProcessor(targetColor, threshold);

            double fps = grabber.getFrameRate();
            double durationInSeconds = grabber.getLengthInTime() / 1_000_000.0;
            System.out.printf("Video duration: %.2f seconds%n", durationInSeconds);

            
            if (writeRegion) writer.println("second,x,y,region"); else writer.println("second,x,y");

            // Process one frame per second
            for (int second = 0; second < (int) durationInSeconds; second++) {
                int frameNumber = (int) (second * fps);
                grabber.setFrameNumber(frameNumber);

                var frame = grabber.grabImage();
                if (frame != null) {
                    BufferedImage image = converter.getBufferedImage(frame);
                    CentroidResult result = processor.processImage(image);

                    if (result != null) {
                        if (writeRegion) {
                            String regionName = findRegionName(regions, result.x(), result.y());
                            writer.printf("%d,%d,%d,%s%n",
                                second, result.x(), result.y(), regionName != null ? regionName : "");
                        } else {
                            writer.printf("%d,%d,%d%n", second, result.x(), result.y());
                        }
                    }
                }
            }

            grabber.stop();
            System.out.println("Processing complete. Output: " + outputFileName);

        } catch (Exception e) {
            System.err.println("Error processing video.");
            e.printStackTrace();
            System.exit(1);
        }
    }

    // -------- helpers --------

    private static Map<String, Region> loadRegionsFromJson(String path) throws IOException {
        byte[] bytes = Files.readAllBytes(Path.of(path));
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(bytes);

        Map<String, Region> out = new LinkedHashMap<>();
        if (root != null && root.isObject()) {
            root.fields().forEachRemaining(entry -> {
                String name = entry.getKey();
                JsonNode n = entry.getValue();
                if (n != null && n.isObject()) {
                    int x = getInt(n, "x");
                    int y = getInt(n, "y");
                    int w = getInt(n, "width");
                    int h = getInt(n, "height");
                    out.put(name, new Region(name, x, y, w, h));
                }
            });
        }
        return out;
    }

    private static int getInt(JsonNode n, String field) {
        JsonNode v = n.get(field);
        if (v == null || !v.isNumber()) {
            throw new IllegalArgumentException("Missing or non-numeric field: " + field);
        }
        return v.asInt();
    }

    private static String findRegionName(Map<String, Region> regions, int x, int y) {
        for (Region r : regions.values()) {
            if (r.contains(x, y)) {
                return r.getName();
            }
        }
        return null; // no match
    }
}
