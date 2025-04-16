public class EuclideanColorDistance implements ColorDistanceFinder {
    /**
     * Returns the euclidean color distance between two hex RGB colors.
     * 
     * Each color is represented as a 24-bit integer in the form 0xRRGGBB, where
     * RR is the red component, GG is the green component, and BB is the blue
     * component,
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
        // RGB color component refinement
        // rgb format: 0xRRGGBB
        // each color component takes up 8 bits
        // we can use bit-shifting & bit-masking to 'clarify'
        // individual color components
        int rA = (colorA >> 16);
        int gA = (colorA >> 8) & 0xff;
        int bA = colorA;
        int rB = (colorB >> 16);
        int gB = (colorB >> 8) & 0xff;
        int bB = colorB;

        int distanceR = rA - rB;
        int distanceG = gA - gB;
        int distanceB = bA - bB;

        return Math.sqrt(distanceR * distanceR + distanceG * distanceG + distanceB * distanceB);
    }
}
