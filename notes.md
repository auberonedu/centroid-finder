# ImageSummaryApp

1. The main method takes in a `String[]` of *args* to use in the application.
2. An if statement checks whether the length of *args* is less than 3. If so, it prints out an example of how many arguments should be passed into the program. This stops the rest of the program from running if less than 3 arguments are passed into the main method.
3. Three variables are declared. The first called *InputImagePath* stores the input images path as a `String` from `args[0]`. The second called *hexTargetColor* stores the target hex color as a `String` from `args[1]`. The third called *threshold* stores the threshold as an `int` from `args[2]`. The threshold uses a try-catch block to ensure that `args[2]` is an integer before reassigning it from 0. If it's not not an error message is printed to the console and the method returns.
4. A variable of type `BufferedImage` called *inputImage* is created and set to null initially. A try-catch block is then used to validate if the file at *inputImagePath* can be read and assigned to *inputImage*. If the file cannot be read, an error message including *inputImagePath* is printed to the console. The exception is printed using `printStackTrace()` (Built-in java.lang.Throwable method that prints the throwable and its backtrace to the standard error stream), and the method returns.
5. A variable of type `int` called *targetColor* is initially set to 0. A try-catch block is then used to validate if *hexTargetColor* can be parsed to an integer and stored in *targetColor*. If it can't be converted, an error message with an example format is printed to the console, and the method returns.
6. A variable of type `ColorDistanceFinder` called *distanceFinder* creates and stores a new instance of `EuclideanColorDistance`.
7. A variable of type `ImageBinarizer` called *binarizer* creates a new instance of `DistanceImageBinarizer` with the *distanceFinder*, *targetColor*, and *threshold* passed into the instance.
8. A variable of type `int[][]` called *binaryArray* stores the binary array output of *inputImage* using `toBinaryArray` from *binarizer*.
9. A variable of type `ImageBinarizer` called *binaryImage* stores the buffered image of *binaryArray* using `toBufferedImage` from *binarizer*. A try-catch block is then used to validate if *binaryImage* can be written to a png called *binarized.png* using `ImagoIO.write`. If it can't be completed, an error message is printed to the console. The exception is printed using `printStackTrace()`, and the method returns.
10. A variable of type `ImageGroupFInder` called *groupFinder* creates and stores a new instance of `BinarizingImageGroupFinder` with *binarizer* and a new instance of `DfsBinaryGroupFinder` passed in.
11. A variable of type `List<Group>` called *groups* stores the found connected groups of *inputImage* using `findConnectedGroups` from *groupFinder*.
12. A try-catch block is used to write the groups information to a CSV file called `groups.csv`. A try-catch contains a `PrintWriter` called *writer* which is set to write to `groups.csv`. Inside of the try block a for loop of type `Group` loops over each *group* in *groups*. *Writer* then prints to the file using `toCsvRow` and a success statement is printed to the console. If it can't be completed, an error message is printed to the console. The exception is printed using `printStackTrace()`, and the method returns.



## ColorDistanceFinder
An interface for computing the distance between two different colors. Each color is represented as a 24-bit integer in the format: `0xRRGGBB`, where each color component can be extracted using bit shifting and masking. 

## EuclideanColorDistance
A class (not yet implemented) that implements the *ColorDistanceFinder* interface to compute the distance between two different colors. This is done using the Euclidean distance formula `sqrt((r1 - r2)^2 + (g1 - g2)^2 + (b1 - b2)^2)`, where each variable corresponds to a color integer value represented in a 24-bit integer in the format `0xRRGGBB`. This formula gives a measure of how visually different the two colors are. The constructor *distance* takes two parameters with the first being an `int` called *colorA* and the second an `int` called *colorB* to calculate the distance between *colorA* and *colorB*. The distance between *colorA* and *colorB* is then returned as a `double`.

## ImageBinarizer
An interface that for converting between RGB images and binary (black and white) images. The binary image will be represented in a 2D array of integers where black will be represented as 0 and white as 1. toBinaryArray will be implemented in DistanceImageBinarizer to convert a BufferedImage given to a binary 2D array. toBufferedImage will do the reverse converting a binary 2D array into a BufferedImage. Both not yet implemented in DistanceImageBinarizer.

## DistanceImageBinarizer
DistanceImageBinarizer is an implementation of ImageBinarizer interface. There is a DistanceImageBinarizer constructor that takes in three params. The params are ColorDistanceFinder *distanceFinder* (given), int *threshold*, and int *targetColor*. The *distanceFinder* is used to find the Euclidean difference between the pixel's color and target color. *targetColor* is represented as 24-bit hex RGB integers (0xRRGGBB). *threshold* will determine cutoff for binarization with pixels whose distance are less than the threshold being white and others being black. The *toBinaryArray* method will convert a BufferedImage into a binary 2D array and *toBufferedImage* will do the reverse and convert a binary 2D array into a BufferedImage.

## ImageGroupFinder
An interface for finding connected groups in image. Each group is sorted in descending order according to the group's compareTo method. It's method `findConnectedGroups` accepts a parameter of `BufferedImage` and returns a `List<Group>`.

## BinarizingImageGroupFinder
A class (not yet implemented) that implements the *ImageGroupFinder* interface to find connected groups in an image. This is completed by first binarizing a given image and then finding connected groups of white pixels using BinaryGroupFinder. This class uses an *ImageBinarizer* to convert an RGB image into a binary 2D array,  *BinaryGroupsFinder* is then applied to the array to locate the connected groups of white pixels, and the connected groups are then returned in descending order based on the compareTo method in the *Group* record. The constructor accepts two parameters of type `ImageBinarizer` and `BinaryGroupFinder` to construct a `BinarizingImageGroupFinder`. Within the constructor two private fields *binarizer* of type `ImageBinarizer` and *groupFinder* of type `BinaryGroupFinder` are set to the parameters passed into the constructor. A method called *findConnectedGroups* is then used to find the connects groups of white pixels in the image. It accepts a parameter of type `BufferedImage` and returns a type of `List<Group>`.

## DfsBinaryGroupFinder


## BinaryGroupFinder
