package io.github.brittLiban.centroidFinder;


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
    public double distance(int color1, int color2) {
        //Extract the red, green, and blue components from each color
        int[] rgb1 = rgb(color1);
        int[] rgb2 = rgb(color2);
        
        //compute the difference for each channel
        int deltaRed   = rgb1[0] - rgb2[0];
        int deltaGreen = rgb1[1] - rgb2[1];
        int deltaBlue  = rgb1[2] - rgb2[2];

        
        return Math.sqrt(deltaRed * deltaRed + deltaGreen * deltaGreen + deltaBlue * deltaBlue);
    };


    //this helper method splits a 24‑bit RGB integer into its separate red, green, and blue components.
    private int[] rgb(int color) {
        // extract red, green, blue
        int red = (color >> 16) & 0xFF;
        int green = (color >> 8) & 0xFF;
        int blue = color & 0xFF;
        
         // Return the three channels in an array
        return new int[] {red, green, blue};
    }
    
}
