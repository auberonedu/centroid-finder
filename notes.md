# Jonathan Sule and Zachary Springer are partners for the first half Salamander project!
JS: wave 1, reading through ImageSummaryApp.java and all the other classes/interfaces it calls.
ImageSummaryApp.java:
    1. Parses the 3 element command-line input
        A. inputImagePath
        B. hexTargetColor
        C. int threshold
    2. Loads input image as a BufferedImage object using ImageIO.read()
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

Wave 2 will implement DfsBinaryGroupFinder, this must be completed by class Wk3 Tuesday;
Wave 3 will implement EuclideanColorDistance
Wave 4 will implement DistanceImageBinarizer
Wave 5 will implement BinarizingImageGroupFinder
Wave 6 will be a validation step using sampleInput with result being compared to the resultant binary image output and the CSV group file both found in sampleOutput folder
Wave 7 we can start to go crazy re enhancements to what should already be a solid working base code project. FUN!!!

JS moving into Wave 2, reading description of DfsBinaryGroupFinder class (this is what we need to implement in Wave 2), Group.java description and code, and considering top 10 unit tests to write first:

DfsBinaryGroupFinder.java:
1. takes as input a 2D int array of 0s(black) and 1s(white)
2. outputs a list of Group objects (List<Group>)
3. Groups are cells connected via 4 cardinal directions only
4. Each component (groups of 1s) it computes size and centroid
5. Returns List<Group> sorted desc order (size, x coord, y coord);
----------------------
Java Record -> concise, final, immutable class that automatically generates:

Constructor
getters (but no setters)
equals()
hashCode()
toString()

It can implement interfaces
Cannot extend other classes (however, all records implicitly extend java.lang.Record)
Is immutable by design—fields are final)
----------------------
Group.java
1. takes in an int size and a Coordinate centroid
2. method compareTo takes in a Group other and compares this group to the other group re size and centroid, then orders them, then another method toCsvRow(a specialized toString method) takes the sorted 3 element strings and puts them into CSV format to put into CSV file

Unit tests to consider for DfsBinaryGroupFinder class:
1. empty pixel group (edge case)
2. single pixel group (simple starting test)
3. diagonal 1s should not connect
4. one 4-connected block
5. sorting logic test (2 groups different size)
6. multiple equal sized groups tests tiebreaking on centroid (if 4 equal sized groups should sort by desc x first, then desc y) -> see test 11 idea!
7. non rectangular data (should throw exception)
8. null input/null row (should throw NPE)
9. checkboard pattern to ensure no false grouping
10. odd shaped group (eg. salamander shape!)
11. the compareTo() method in Group.java record has a 3 tier sorting criteria - if 'island' size are different that will be the sorting criteria used first ie largest to smallest by size, 2nd tier is x coord, 3rd tier is y coord- so we should develop a test (or tests) to make sure this sorting logic is working ie hand different sized islands (that's already covered in test #5), then hand in two same size islands to sort by x coord (covered in 6) but #6 needs two tests, first one same sized islands but different x coord, then same sized islands, same x coord, different y coordinates!

