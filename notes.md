# Jonathan Sule and Zachary Springer are partners for the first half Salamander project!
JS: wave 1, reading through ImageSummaryApp.java and all the other classes/interfaces it calls.
ImageSummaryApp.java:
    1. Parses the 3 element command-line input
        A. inputImagePath
        B. hexTargetColor
        C. int threshold
    2. Loads input image as a BufferedImage using ImageIO.read()
    3. Converts hex color input into 24 bit RGB integer
    4. Uses class EuclideanColorDistance which implements interface ColorDistanceFinder to initialize a distanceFinder; DistanceImageBinarizer is initialized with the interface ColorDistanceFinder, a target RGB color, and a threshold. It uses these to convert the input image into a binary array based on how close each pixel color is to target;
    5. The binaryArray is a 2D int array formed by binarizer.toBinaryArray handed inputImage; binaryImage is a BufferedImage formed by binarizer.toBufferedImage handed the recently formed binaryArray
    6. The binaryImage is saved in .png format using ImageIO.write(); 
    7. Connected groups are labeled via CCL (connected component labeling) which will use DFS method; ImageGroupFinder appears to be a polymorphic interface (abstraction!!) which wraps the DfsBinaryGroupFinder with binarization logic (using BinarizingImageGroupFinder)
    8. Labeled groups output to CSV file as 3 element rows (size, x, y);

At this point I've looked though ImageSummaryApp; From this I can summarize the helper classes/interfaces, here's a summary:
1. ImageSummaryApp is the main controller (class)
2. EuclideanColorDistance calculates RGB color distance (implements interface ColorDistanceFinder)
3. DistanceImageBinarizer converts image to binary array + image (implements interface ImageBinarizer)
4. DfsBinaryGroupFinder uses DFS to find connected pixel groups
5. Group stores info about a group (size, centroid, csv output)
6. BinarizingImageGroupFinder combines binarization and grouping logic (implements interface ImageGroupFinder)
