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


        //seperate RGB of colorA
        int redA = (colorA >> 16) & 0xff;
        int greenA = (colorA >> 8) & 0xff;
        int blueA = colorA & 0xff;

         //seperate RGB of colorB
        int redB = (colorB >> 16) & 0xff;
        int greenB = (colorB >> 8) & 0xff;
        int blueB = colorB & 0xff;
        //calculate difference of ColorA and ColorB
      
        int distaceRed = redA - redB;
        int distanceGreen = greenA - greenB;
        int distanceBlue = blueA - blueB;

        //return Euclidean color distance sqrt((r1 - r2)^2 + (g1 - g2)^2 + (b1 - b2)^2)
        return Math.sqrt(distaceRed*distaceRed + distanceGreen*distanceGreen + distanceBlue*distanceBlue);

    }
}
