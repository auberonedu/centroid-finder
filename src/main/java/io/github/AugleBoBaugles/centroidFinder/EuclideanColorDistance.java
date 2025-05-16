// package main.java.io.github.AugleBoBaugles.centroidFinder;
package io.github.AugleBoBaugles.centroidFinder;
public class EuclideanColorDistance implements ColorDistanceFinder {
    /**
     * Returns the euclidean color distance between two hex RGB colors.
     * 
     * Each color is represented as a 24-bit integer in the form 0xRRGGBB, where
     * RR is the red component, GG is the green component, and BB is the blue component,
     * each ranging from 0 to 255.
     * 
     * The Euclidean color distance is calculated by treating each color as a point
     * in 3D space (red, green, blue) and applying the Euclidean distance formula:
     * 
     * sqrt((r1 - r2)^2 + (g1 - g2)^2 + (b1 - b2)^2)
     * 
     * This gives a measure of how visually different the two colors are.
     * 
     * @param colorA the first color as a 24-bit hex RGB integer
     * @param colorB the second color as a 24-bit hex RGB integer
     * @return the Euclidean distance between the two colors
     */
    // Created a final int for the mask
    // 0xf = 4 bits and 0xff = 8 bits
    private static final int mask = 0xff;

    @Override
    public double distance(int colorA, int colorB) {
        // Extract colorA into their respective hex color variants (RGB)
            // int r1, g1, b1
        int r1 = (colorA >> 16) & mask; // 8 bits starting on the left of the hexdecimal
        int g1 = (colorA >> 8) & mask; // The 8 bits in the middle of the hexdecimal
        int b1 = colorA & mask; // The 8 bits starting on the right of the hexdecimal
        
        // Extract colorB into their respective hex color variants (RGB)
            // int r2, g2, b2
        int r2 = (colorB >> 16) & mask;
        int g2 = (colorB >> 8) & mask;
        int b2 = colorB & mask;
        
        // define the distance of each color, then throw into Math.sqrt 
        int distR = r1 - r2;
        int distG = g1 - g2;
        int distB = b1 - b2;

        // returns thr euclidean color distance between two hex RGB colors
            // Math.sqrt((r1 - r2)^2 + (g1 - g2)^2 + (b1 - b2)^2)
        return Math.sqrt(distR * distR + distG * distG + distB * distB);
    }

    /**
     * Returns the aRGB version of a hexadecimal number representing a color.
     * 
     * @param hex a hexadecimal representation of a color
     * @return an array containing alpha, red, green, and blue colors in int form, 0-255
     */
    public static int[] hexToRGB(int hex) {
        // conversion of alpha
            // int alpha = the hex >> 24
        int alpha = (hex >> 24) & mask;
        // conversion of red
            // int red = the hex >> 16
        int red = (hex >> 16) & mask;
        // conversion of green
            // int green = the hex >> 8
        int green = (hex >> 8) & mask;
        // conversion of blue
            // int blue = the hex of mask (0xf)
        int blue = hex & mask;

        
        return new int[]{alpha, red, green, blue};
    }
}
