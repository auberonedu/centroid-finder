package io.github.oakes777.salamander;
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
     * @throws IAE if either input is not a valid 24-bit RGB color
     */
    @Override
    public double distance(int colorA, int colorB) {
        //edge cases - IAE for negative input, input > 24 bits;
        if (colorA < 0 || colorA > 0xFFFFFF || colorB < 0 || colorB > 0xFFFFFF) {
            throw new IllegalArgumentException("Input color value must be in 32-bit RGB format! (0x000000 to 0xFFFFFF)");
        }

        // RGB color component refinement
        // we  use bit-shifting & bit-masking to 'clarify'
        //colorA
        int rA = (colorA >> 16) & 0xff;
        int gA = (colorA >> 8) & 0xff;
        int bA = colorA & 0xff;
        //colorB
        int rB = (colorB >> 16) & 0xff;
        int gB = (colorB >> 8) & 0xff;
        int bB = colorB & 0xff;
        //calculate difference between color components 'a' minus 'b'
        //honestly this just makes the euclidean formula code POP
        int dr = rA - rB;
        int dg = gA - gB;
        int db = bA - bB;
        //return the result of the Euclidean 3D spatial geometry distance formula applied to the two colors
        return Math.sqrt(dr*dr + dg*dg + db*db);
    }
}
