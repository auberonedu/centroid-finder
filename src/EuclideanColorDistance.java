import interfaces.ColorDistanceFinder;

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
        // Extract colorA into their respective hex color variants (RGB)
            // int r1, g1, b1
        // Extract colorB into their respective hex color variants (RGB)
            // int r2, g2, b2

        // define the distance of each color, then throw into Math.sqrt 
        
        // returns thr euclidean color distance between two hex RGB colors
            // Math.sqrt((r1 - r2)^2 + (g1 - g2)^2 + (b1 - b2)^2)
        return 0;
    }

    // Helper method to convert a hex int into R, G, B components
    public static int[] hexToRGB(int hex) {
        // conversion of red
            // int red = the hex >> 16
        // conversion of green
            // int green = the hex >> 8
        // conversion of blue
            // int blue = the hex of mask (0xf)

        return new int[]{};
    }
}
