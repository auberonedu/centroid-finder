package io.github.brittLiban.centroidFinder;


import java.awt.image.BufferedImage;

/**
 * An implementation of the ImageBinarizer interface that uses color distance
 * to determine whether each pixel should be black or white in the binary image.
 * 
 * The binarization is based on the Euclidean distance between a pixel's color
 * and a reference target color.
 * If the distance is less than the threshold, the pixel is considered white
 * (1);
 * otherwise, it is considered black (0).
 * 
 * The color distance is computed using a provided ColorDistanceFinder, which
 * defines how to compare two colors numerically.
 * The targetColor is represented as a 24-bit RGB integer in the form 0xRRGGBB.
 * 
 * // Liban - Some initial thoughts - consider translating the rgb code to the
 * actual number?
 */
public class DistanceImageBinarizer implements ImageBinarizer {
    private final ColorDistanceFinder distanceFinder;
    private final int threshold;
    private final int targetColor;

    /**
     * Constructs a DistanceImageBinarizer using the given ColorDistanceFinder,
     * target color, and threshold.
     * 
     * The distanceFinder is used to compute the Euclidean distance between a
     * pixel's color and the target color.
     * The targetColor is represented as a 24-bit hex RGB integer (0xRRGGBB).
     * The threshold determines the cutoff for binarization: pixels with distances
     * less than
     * the threshold are marked white, and others are marked black.
     *
     * @param distanceFinder an object that computes the distance between two colors
     * @param targetColor    the reference color as a 24-bit hex RGB integer
     *                       (0xRRGGBB)
     * @param threshold      the distance threshold used to decide whether a pixel
     *                       is white or black
     */
    public DistanceImageBinarizer(ColorDistanceFinder distanceFinder, int targetColor, int threshold) {
        this.distanceFinder = distanceFinder;
        this.targetColor = targetColor;
        this.threshold = threshold;
    }

    /**
     * Converts the given BufferedImage into a binary 2D array using color distance
     * and a threshold.
     * Each entry in the returned array is either 0 or 1, representing a black or
     * white pixel.
     * A pixel is white (1) if its Euclidean distance to the target color is less
     * than the threshold.
     *
     * @param image the input RGB BufferedImage
     * @return a 2D binary array where 1 represents white and 0 represents black
     */
    @Override
    public int[][] toBinaryArray(BufferedImage image) {
        if (image == null) {
            throw new NullPointerException("Input image cannot be null");
        }

        // getting the width and height of the image
        int width = image.getWidth();
        int height = image.getHeight();

        // evaluating it for the red
        // this whats in the front over 16 bits/or really shifts everything down.
        // 255 to see what our red is

        // remember
        // FF = 11111111
        // if a number is not 1 it will return 0
        // - essentially remember with hexacode, if a number is for ex in the 16th
        // place,
        // mult that number by 16 and you get that number.
        // when in hexacode each individual digit represetns 4 in binary
        // so when evaluating a hexacode # - do 16^x - x being what ever place it is in.
        // Example: 0x7C = (7 * 16^1) + (C * 16^0)
        // = (7 * 16) + (12 * 1) = 112 + 12 = 124


        // THIS WAS WHAT WE WERE GOING TO HAVE TO DO IF WE ALREADY DIDN"T HAVE THE DISTANCE FORMULA IN THE DISTANCE IMAGE BINARIZER
        
        // int rTarget = (targetColor >> 16) & 0xFF;
        // int gTarget = (targetColor >> 0) & 0xFF;
        // int bTarget = targetColor & 0xFF;

        /*
         * 00000000 - y
         * x
         * (0,9)(2,2)(4,4)
         * 
         * 
         * EACH position in the array is a x - y coordinate that also holds the
         * COLOR for that specific pixel as well.
         * 
         * to get color lets do image.getRGB(x,y)
         */

        int[][] numberizedImage = new int[height][width];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                // this will return the RGB for the current position
                int currentRGB = image.getRGB(x, y) & 0xFFFFFF;

                // taking the current rgb down to its rs
                double distance = distanceFinder.distance(currentRGB, targetColor);

                if (distance <= threshold) {
                    numberizedImage[y][x] = 1;
                } else {
                    numberizedImage[y][x] = 0;
                }

                // do we need to evaluate how close this is to the target?
                // I believe the answer is yes

            }

        }

        // look through image
        // create an array
        // if the current pixel is relevantly close to the wanted number, then mark it
        // as one
        // if not, then its zero

        return numberizedImage;
    }

    /**
     * Converts a binary 2D array into a BufferedImage.
     * Each value should be 0 (black) or 1 (white).
     * Black pixels are encoded as 0x000000 and white pixels as 0xFFFFFF.
     *
     * @param image a 2D array of 0s and 1s representing the binary image
     * @return a BufferedImage where black and white pixels are represented with
     *         standard RGB hex values
     */
    @Override
    public BufferedImage toBufferedImage(int[][] image) {
        // image is getting passed in as a 2d array
        // number of rows and columns in binary array
        int height = image.length;
        int width = image[0].length;

        // create an empty RGB image
        BufferedImage out = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        // iterate over every position in the binary array
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int bit = image[y][x];
                int rgbColor;
                if (bit == 1) {
                    rgbColor = 0xFFFFFF;
                } else {
                    rgbColor = 0x000000;
                }
                out.setRGB(x, y, rgbColor);
            }
        }
        // return the binarized image
        return out;
    }
}
