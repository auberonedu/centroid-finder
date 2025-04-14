public class EuclideanColorDistance implements ColorDistanceFinder {
    final int SHIFT_ONE_BYTE = 8;
    final int SHIFT_TWO_BYTE = 16;
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

        byte aR = (byte)colorA;
        byte bR = (byte)colorB;

        byte aG = (byte) (colorA >> SHIFT_ONE_BYTE);
        byte bG = (byte) (colorB >> SHIFT_ONE_BYTE);

        byte aB = (byte) (colorA >> SHIFT_TWO_BYTE);
        byte bB = (byte) (colorB >> SHIFT_TWO_BYTE);

        short red = (short) bR;
        short green = (short) bG;
        short blue = (short) bB;

        red -= aR;
        green -= aG;
        blue -= aB;

        int intRet = red*red + green*green + blue*blue;

        double ret = Math.sqrt((double)intRet);


        return ret;
    }
}
