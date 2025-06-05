package io.github.alstondsouza1.centroidFinder;

public class EuclideanColorDistance implements ColorDistanceFinder {
    final int SHIFT_ONE_BYTE = 8;
    final int SHIFT_TWO_BYTE = 16;

    final int RED_MASK = 0x000000FF;
    final int GREEN_MASK = 0x0000FF00;
    final int BLUE_MASK = 0x00FF0000;
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

        // Extract the red, green, and blue components from each color
        int aR = (colorA & RED_MASK);
        int bR = (colorB & RED_MASK);

        int aG = ((colorA & GREEN_MASK) >> SHIFT_ONE_BYTE);
        int bG = ((colorB & GREEN_MASK) >> SHIFT_ONE_BYTE);

        int aB = ((colorA & BLUE_MASK) >> SHIFT_TWO_BYTE);
        int bB = ((colorB & BLUE_MASK) >> SHIFT_TWO_BYTE);

        int red = bR - aR;
        int green = bG - aG;
        int blue = bB - aB;


        int intRet = (red * red) + (green * green) + (blue * blue);
        double ret = Math.sqrt((double)intRet);

        return ret;
    }
}
