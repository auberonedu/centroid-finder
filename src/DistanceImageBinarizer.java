import java.awt.image.BufferedImage;

/**
 * An implementation of the ImageBinarizer interface that uses color distance
 * to determine whether each pixel should be black or white in the binary image.
 * 
 * The binarization is based on the Euclidean distance between a pixel's color and a reference target color.
 * If the distance is less than the threshold, the pixel is considered white (1);
 * otherwise, it is considered black (0).
 * 
 * The color distance is computed using a provided ColorDistanceFinder, which defines how to compare two colors numerically.
 * The targetColor is represented as a 24-bit RGB integer in the form 0xRRGGBB.
 */
public class DistanceImageBinarizer implements ImageBinarizer {
    private final ColorDistanceFinder distanceFinder;
    private final int threshold;
    private final int targetColor;

    /**
     * Constructs a DistanceImageBinarizer using the given ColorDistanceFinder,
     * target color, and threshold.
     * 
     * The distanceFinder is used to compute the Euclidean distance between a pixel's color and the target color.
     * The targetColor is represented as a 24-bit hex RGB integer (0xRRGGBB).
     * The threshold determines the cutoff for binarization: pixels with distances less than
     * the threshold are marked white, and others are marked black.
     *
     * @param distanceFinder an object that computes the distance between two colors
     * @param targetColor the reference color as a 24-bit hex RGB integer (0xRRGGBB)
     * @param threshold the distance threshold used to decide whether a pixel is white or black
     */
    public DistanceImageBinarizer(ColorDistanceFinder distanceFinder, int targetColor, int threshold) {
        this.distanceFinder = distanceFinder;
        this.targetColor = targetColor;
        this.threshold = threshold;
    }

    /**
     * Converts the given BufferedImage into a binary 2D array using color distance and a threshold.
     * Each entry in the returned array is either 0 or 1, representing a black or white pixel.
     * A pixel is white (1) if its Euclidean distance to the target color is less than the threshold.
     *
     * @param image the input RGB BufferedImage
     * @return a 2D binary array where 1 represents white and 0 represents black
     */
    @Override
    public int[][] toBinaryArray(BufferedImage image) {
        // declare variables for height and width
        int width = image.getWidth();
        int height = image.getHeight();
        
        int[][] binaryArray = new int[width][height];

        // iterate through the 2d array
        for (int r = 0; r < binaryArray.length; r++){
            for (int c = 0; c < binaryArray[0].length; c++){
                // do some logic
                // get current pixel color
                int currentColor = image.getRGB(r, c);

                // find distance between currentColor and target color (use distanceFinder)
                double distance = distanceFinder.distance(currentColor, targetColor);

                // if distance < threshold -- set to 1 if distance >= threshold -- set to 0
                if (distance < threshold){
                    binaryArray[r][c] = 1; // white
                } else if (distance >= threshold){
                    binaryArray[r][c] = 0; // black
                }

                
            }
        }

            
        
        return binaryArray;
    }

    /**
     * Converts a binary 2D array into a BufferedImage.
     * Each value should be 0 (black) or 1 (white).
     * Black pixels are encoded as 0x000000 and white pixels as 0xFFFFFF.
     *
     * @param image a 2D array of 0s and 1s representing the binary image
     * @return a BufferedImage where black and white pixels are represented with standard RGB hex values
     */
    @Override
    public BufferedImage toBufferedImage(int[][] image) {
        // declare variables for height and width
        int width = image.length;
        int height = image[0].length;
        // declare a BufferedImage object
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        // set a 2d array as same size as image
        // iterate through the 2d array
        for (int r = 0; r < image.length; r++) {
            for (int c = 0; c < image[0].length; c++) {
                // WHITE
                if (image[r][c] == 1) {
                    bufferedImage.setRGB(r, c, 0xFFFFFF);
                } // BLACK
                else if (image[r][c] == 0) {
                    bufferedImage.setRGB(r, c, 0x000000);
                }
            }
        }
        
        return bufferedImage;
    }
}
