// ImageSummaryApp.java
package io.github.TiaMarieG.centroidFinder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class ImageSummaryApp {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Usage:");
            System.out.println("  java -jar videoprocessor.jar <input_image> <hex_target_color> <threshold>");
            System.out.println("  OR");
            System.out.println(
                    "  java -jar videoprocessor.jar binarize-thumbnail <input_image> <output_image> <r> <g> <b> <threshold>");
            return;
        }

        // Backend-triggered image binarization
        if ("binarize-thumbnail".equals(args[0])) {
            if (args.length != 7) {
                System.err.println(
                        "Usage: java -jar videoprocessor.jar binarize-thumbnail <input_image> <output_image> <r> <g> <b> <threshold>");
                System.exit(1);
            }

            String inputImagePath = args[1];
            String outputImagePath = args[2];
            int r = Integer.parseInt(args[3]);
            int g = Integer.parseInt(args[4]);
            int b = Integer.parseInt(args[5]);
            int threshold = Integer.parseInt(args[6]);

            try {
                BufferedImage inputImage = ImageIO.read(new File(inputImagePath));
                ColorDistanceFinder colorFinder = new EuclideanColorDistance();
                int targetColor = (r << 16) | (g << 8) | b;
                ImageBinarizer binarizer = new DistanceImageBinarizer(colorFinder, targetColor, threshold);
                int[][] binaryArray = binarizer.toBinaryArray(inputImage);
                BufferedImage binaryImage = binarizer.toBufferedImage(binaryArray);
                ImageIO.write(binaryImage, "jpg", new File(outputImagePath));
                System.out.println("✅ Binarized image saved to: " + outputImagePath);
            } catch (Exception e) {
                System.err.println("❌ Error during binarization:");
                e.printStackTrace();
                System.exit(1);
            }
            return;
        }

        // CLI logic for analysis and CSV
        if (args.length < 3) {
            System.out.println("Usage: java -jar videoprocessor.jar <input_image> <hex_target_color> <threshold>");
            return;
        }

        String inputImagePath = args[0];
        String hexTargetColor = args[1];
        int threshold;

        try {
            threshold = Integer.parseInt(args[2]);
        } catch (NumberFormatException e) {
            System.err.println("Threshold must be an integer.");
            return;
        }

        BufferedImage inputImage;
        try {
            inputImage = ImageIO.read(new File(inputImagePath));
        } catch (Exception e) {
            System.err.println("Error loading image: " + inputImagePath);
            e.printStackTrace();
            return;
        }

        int targetColor;
        try {
            targetColor = Integer.parseInt(hexTargetColor, 16);
        } catch (NumberFormatException e) {
            System.err.println("Invalid hex target color. Please provide a color in RRGGBB format.");
            return;
        }

        ColorDistanceFinder distanceFinder = new EuclideanColorDistance();
        ImageBinarizer binarizer = new DistanceImageBinarizer(distanceFinder, targetColor, threshold);

        int[][] binaryArray = binarizer.toBinaryArray(inputImage);
        BufferedImage binaryImage = binarizer.toBufferedImage(binaryArray);

        try {
            File inputFile = new File(inputImagePath);
            String baseName = inputFile.getName().replaceAll("\\.[^.]+$", "");
            File output = new File("thumbnails/" + baseName + "-binarized.png");
            ImageIO.write(binaryImage, "png", output);
            System.out.println("Binarized image saved as " + output.getPath());
        } catch (Exception e) {
            System.err.println("Error saving binarized image.");
            e.printStackTrace();
        }

        ImageGroupFinder groupFinder = new BinarizingImageGroupFinder(binarizer, new DfsBinaryGroupFinder());
        List<Group> groups = groupFinder.findConnectedGroups(inputImage);

        try (java.io.PrintWriter writer = new java.io.PrintWriter("groups.csv")) {
            for (Group group : groups) {
                writer.println(group.toCsvRow());
            }
            System.out.println("Groups summary saved as groups.csv");
        } catch (Exception e) {
            System.err.println("Error writing groups.csv");
            e.printStackTrace();
        }
    }
}
