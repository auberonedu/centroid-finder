Vocabulary:
Binarization – A digital image processing technique that transforms a grayscale or color image into a binary image
Euclidean color distance - Euclidean color distance calculates the straight-line distance between two colors

Image Summary App:
- First it looks like this method checks to see if there are less than three arguments written in the command line
- Then it is assigning each of those arguments to a variable, for the threshold variable the program parses the third argument into a integer, using a try-catch method that throws a NumberFormatException if the arg cannot be turned into an integer
- InputImage on line 50 is what loads the image, it reads the image from disk using the path that is provided
- TargetColor converts the color from an RGB format into an integer representing the color
- The image is then binarized, and uses Euclidean color distance to compare each pixel with the target color. The pixel becomes white(1) if it is closer than the threshold, and black if it is not(0).

ImageIO.write(binaryImage, "png", new File("binarized.png"));
 - This line saves the image, and in the process converts the binary arrayy (full of 1's and 0's to account for black/white) back to an image.