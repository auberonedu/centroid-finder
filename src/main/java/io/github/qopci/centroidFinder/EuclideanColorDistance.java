package io.github.qopci.centroidFinder;
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
    @Override
    public double distance(int colorA, int colorB) {
        // converting first and second colors to RGB using the helper method
        int[] firstRgbA = RGBconverter(colorA);
        int[] secondRgbB = RGBconverter(colorB);
        
        // calculating the distance between the two colors 
        int redComponent = firstRgbA[0] - secondRgbB[0];
        int greenComponent = firstRgbA[1] - secondRgbB[1];
        int blueComponent = firstRgbA[2] - secondRgbB[2];
        
        return Math.sqrt(redComponent * redComponent + greenComponent * greenComponent + blueComponent * blueComponent);
    }

    public static int[] RGBconverter(int color) {
        // grabbing the RGB colors by shifting and masking
        int red = (color >> 16) & 0xFF;
        int green = (color >> 8) & 0xFF;
        int blue = color & 0xFF;

        return new int[] {red, green, blue};
    }
}
