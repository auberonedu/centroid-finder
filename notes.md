Vocabulary:
Binarization – A digital image processing technique that transforms a grayscale or color image into a binary image

Image Summary App:
-	First it looks like this method checks to see if there are less than three arguments written in the command line
-	Then it is assigning each of those arguments to a variable, for the threshold variable the program parses the third argument into a integer, using a try-catch method that throws a NumberFormatException if the arg cannot be turned into an integer
-	inputImage on line 50 is what loads the image, it reads the image from disk using the path that is provided
-	targetColor converts the color from an RGB format into an integer representing the color
