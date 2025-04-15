# ImageSummaryApp

1. The main method takes in a `String[]` of *args* to use in the application.
2. An if statement checks whether the length of *args* is less than 3. If so, it prints out an example of how many arguments should be passed into the program. This stops the rest of the program from running if less than 3 arguments are passed into the main method.
3. Three variables are declared. The first called *InputImagePath* stores the input images path as a `String` from `args[0]`. The second called *hexTargetColor* stores the target hex color as a `String` from `args[1]`. The third called *threshold* stores the threshold as an `int` from `args[2]`. The threshold uses a try-catch block to ensure that `args[2]` is an integer before reassigning it from 0. If it's not not an error message is printed to the console and the method returns.
4. A variable of type `BufferedImage` called *inputImage* is created and set to null initially. A try-catch block is then used to validate if the file at *inputImagePath* can be read and assigned to *inputImage*. If the file cannot be read, an error message including *inputImagePath* is printed to the console. The exception is printed using `printStackTrace()` (Built-in java.lang.Throwable method that prints the throwable and its backtrace to the standard error stream), and the method returns.
5. A variable of type `int` called *targetColor* is initially set to 0. A try-catch block is then used to validate if *hexTargetColor* can be parsed to an integer and stored in *targetColor*. If it can't be converted, an error message with an example format is printed to the console, and the method returns.
6. A variable of type `ColorDistanceFinder` called *distanceFinder* creates and stores a new instance of `EuclideanColorDistance`.
7. A variable of type `ImageBinarizer` called *binarizer* creates a new instance of `DistanceImageBinarizer` with the *distanceFinder*, *targetColor*, and *threshold* passed into the instance.


## ColorDistanceFinder
An interface for computing the distance between two different colors. Each color is represented as a 24-bit integer in the format: `0xRRGGBB`, where each color component can be extracted using bit shifting and masking. 

## EuclideanColorDistance
A class (not yet implemented) that implements the *ColorDistanceFinder* interface to compute the distance between two different colors. This is done using the Euclidean distance formula `sqrt((r1 - r2)^2 + (g1 - g2)^2 + (b1 - b2)^2)`, where each variable corresponds to a color integer value represented in a 24-bit integer in the format `0xRRGGBB`. This formula gives a measure of how visually different the two colors are.

## ImageBinarizer
An interface that for converting between RGB images and binary (black and white) images. The binary image will be represented in a 2D array of integers where black will be represented as 0 and white as 1. toBinaryArray will be implemented in DistanceImageBinarizer to convert a BufferedImage given to a binary 2D array. toBufferedImage will do the reverse converting a binary 2D array into a BufferedImage. Both not yet implemented in DistanceImageBinarizer.
## DistanceImageBinarizer
