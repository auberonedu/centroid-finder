# Jonathan Sule and Zachary Springer are partners for the first half Salamander project!

JS: wave 1, reading through ImageSummaryApp.java and all the other classes/interfaces it calls.
ImageSummaryApp.java: 1. Parses the 3 element command-line input
A. inputImagePath
B. hexTargetColor
C. int threshold 2. Loads input image as a BufferedImage object using ImageIO.read() 3. Converts hex color input into 24 bit RGB integer 4. Uses class EuclideanColorDistance which implements interface ColorDistanceFinder to initialize a distanceFinder; DistanceImageBinarizer is initialized with the interface ColorDistanceFinder, a target RGB color, and a threshold. It uses these to convert the input image into a binary array based on how close each pixel color is to target; 5. The binaryArray is a 2D int array formed by binarizer.toBinaryArray handed inputImage; binaryImage is a BufferedImage formed by binarizer.toBufferedImage handed the recently formed binaryArray 6. The binaryImage is saved in .png format using ImageIO.write(); 7. Connected groups are labeled via CCL (connected component labeling) which will use DFS method; ImageGroupFinder appears to be a polymorphic interface (abstraction!!) which wraps the DfsBinaryGroupFinder with binarization logic (using BinarizingImageGroupFinder) 8. Labeled groups output to CSV file as 3 element rows (size, x, y);

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

---

Java Record -> concise, final, immutable class that automatically generates:

Constructor
getters (but no setters)
equals()
hashCode()
toString()

It can implement interfaces
Cannot extend other classes (however, all records implicitly extend java.lang.Record)
Is immutable by design—fields are final)

---

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

## DfsBinaryGroupFinder.java PseudoCode:

CLASS
Under Class initialize a private static final int[][] CD (cardinal directions) = {{-1,0}, {1,0}, {0, -1}, {0, 1}}; (to use in dfs helper method to make dfs recursion in 4 CDs more elegant code)

---

## MAIN METHOD -> Validates the input, Traverses the full 2D image, Calls the helper method (dfs) whenever it finds a new group, Creates and collects Group objects, Sorts them using Group.compareTo(), Returns the List<Group>

EdgeCases validate input:

1. if image is null:
   throw NullPointerException
2. for each row in image:
   if row is null:
   throw NullPointerException
3. if not all rows are the same length:
   throw IllegalArgumentException

---

Initializing state

1. height = number of rows in image (remember x=col and y=row!!)
2. width = number of columns in image[0]
3. visited = 2D boolean array [height][width], initialized to false
4. groups = empty list to store Group objects

---

Iterate through entire 2D int matrix starting top left, left to right row by row

1.  for y from 0 to height - 1: (remember y-axis (vertical) = rows)
    for x from 0 to width - 1: (x-axis (horizontal) so = columns)
    if image[y][x] == 1 and not visited[y][x]: (edge cases)
    pixelsInGroup = empty list of Coordinates
    call dfs(image, visited, x, y, pixelsInGroup) (this is our dfs recursion step)

            size = number of pixels in pixelsInGroup
            sumX = sum of x-coordinates in pixelsInGroup
            sumY = sum of y-coordinates in pixelsInGroup
            centroidX = sumX / size  (INTEGER DIVISION)
            centroidY = sumY / size
            create Group with size and Coordinate(centroidX, centroidY)
            add Group to groups

---

return a List<Group> (list of groups :^)
here's how it works:
sort groups by Group.compareTo(): - largest size first - if sizes equal, use descending x - if x equal, use descending y
return groups

---

## HELPER METHOD -> Does not return anything, Its sole job is to explore a single group of connected 1s, It adds coordinates to a passed-in List<Coordinate> (or similar structure), The calling method then uses that list to build the Group object

## DFS helper method! (input: 2D int matrix 'image', 2D boolean matrix 'visited', int row (y coord), int col(x coord), List<Coordinate> list of coordinates called 'pixelsInGroup'){}

Edge cases: we don't want to move off the 2D matrix so UP y-1, DOWN y+1, LEFT x-1, RIGHT x+1
We don't want to loop so if visited has already been there return;
We don't want to move into cell containing == 0 because that's not part of our island!!

---

Once we're sure cell is a new '1' we add to visited, add the coord to pixelsInGroup:
visited[y][x] = true
add Coordinate(x, y) to pixelsInGroup
(for more elegant code) initialize row as y coord and col as x coord

Recurse to 4-connected neighbors (no diagonals!)
for each loop (int[] direction : directions) {}
dfs(image, visited, row + direction[0], col + direction[1], pixelsInGroup);

---

## DONE SON!! YIPPEEE! (THIS WAS JS/ZS spending 90 minutes in Friday lab!!)

Sunday noon meeting at Wild Wheat in Kent JS/ZS
We are slowly processing the pseudocode we worked on Friday in lab into actual code. We've run into a question about how to use the Coordinate java record and did some W3 and google investigation:

Java Record innate behaviors

1. When we define a record eg. record Coordinate(int x, int y), we’re creating a special kind of class used for storing data.
2. Java automatically creates two private final fields: one called x and one called y.
3. Java automatically generates a public constructor: so we can say new Coordinate(3, 5) to make a new coordinate with those values.
4. Java automatically generates two accessor methods named exactly after the fields: x() and y(). These are like built-in getters:
   -> c.x() returns the value of x
   -> c.y() returns the value of y
   We don't need to write these methods - Java does it for us based on the record’s parameters.
   So even though we don’t see public int x() written anywhere, we’re still able to call c.x() inside our code. It’s part of the record’s automatic behavior.
   Records also come with useful methods like equals(), hashCode(), and toString()—so they work well in lists, maps, debugging, and comparison operations.
   The key idea is that records are meant to be simple, immutable data containers with less code and automatic structure.

---

re compareTo() method:
-> Integer.compare(a, b) returns -1 if a < b, 0 if a == b, and 1 if a > b;
-> It naturally sorts from smallest to largest
-> We are tasked to return the CSV sorted from largest to smallest so we must reverse the order in Integer.compare() to sort in descending order (largest size, x, y first)

---

Wave 3 - we have looked over the javadoc for EuclideanColorDistance class; neither of us were initially aware of using a 3D x, y, z graph to represent a RGB color, so we have just studied this together. Here is our answer:

3D Color Space via XYZ 3D Graph:
RGB color space means we are describing colors using 3 numbers: red, green, blue; we imagine these as x, y, z axis on a 3D color 'graph'; now we can use the Euclidean 3D spatial geometry distance formula to tell us the 'distance' between two different colors (ie two different points on this 3D graph).

## we are assuming that we use this so we can set the threshold somewhere in between the line connecting these two 'dots' on this 3D xyz graph?

Euclidean distance formula 3D geometry->
Distance between 2 points on xyz/3D graph:
SquareRoot of ((r1-r2)^2 + (g1-g2)^2 + (b1-b2)^2)
The larger the distance, the more different the colors are.

---

## Unit testing for wave3 - we have discussed our preferences for unit tests with AI and have asked AI to write the Unit test code for wave 3

for wave2 we reverse engineered the class code from the unit tests AI generated for us; however, wave2 class was highly similar to our Explorer class assignment and the interview questions Zach and I recently tested each other on, so this made wave 2 easier in a sense.
For wave3 we have a helper class with a distance method so we had to study (google and w3) about the 24 bit rgb color syntax, hex rgb color syntax and usage inside Java code, the concept of using an x.y.z graph (ie 3D space) to describe two different colors as if they were separated by metaphysical space then using the 3D geometry distance formula (Euclidean) to 'measure the distance' between the 2 'points' of color on the 3D graph,
That was super complex and took us a few hours to get through.
We used AI again to generate this time 15 tests (although some are identical so we will be cleaning those up and the end result will likely be less than 15);
Instead of reverse engineering wave3 class code from the unit tests, this time we will attempt to write the pseudocode based on the javadoc above the unfinished wave3 class code already provided

---

wave3 EuclideanColorDistance.java javadoc info:

1. This class implements ColorDistanceFinder interface
2. Class public method: 'double distance(int colorA, int colorB)'
3. Both parameters represent respective colors in 24-bit RGB format
4. Each color component is an 8-bit value (0-255)
5. The method pretends each color is a point in 3D space then ...
6. Uses the Euclidean 3d spatial geometry distance formula to calculate the two colors' 'distance' from each other
7. sprt((r1-r2)^2 + (g1-g2)^2 + (b1-b2)^2)
8. The method returns a double representing how visually different the two colors are

---

## Week2 in sdev334 our professor Auberon showed us how to think in bits and bytes, and delved into 'bit shifts' and 'masking' so we assume we will use these concepts and resulting code to process the 24-bit color input into its individual R-G-B components

Pseudocode:
[public class EuclideanColorDistance implements ColorDistanceFinder {]
[@Override
public double distance(int colorA, int colorB) {]
//RGB color component refinement
//rgb format: 0xRRGGBB
//each color component takes up 8 bits
//we can use bit-shifting & bit-masking to 'clarify'
//individual color components

    //Red component from left-most 8 bits:
    rA = (colorA shifted right 16 bits) and 0xFF
    //Green from middle 8 bits
    gA = (colorA shifted right 8 bits) and 0xFF
    //Blue from right-most 8 bits (no shift required)
    bA = colorA and 0xFF

    rB = (colorB shifted right 16 bits) and 0xFF
    gB = (colorB shifted right 8 bits) and 0xFF
    bB = colorB and 0xFF

    //calculate 'distance' using Euclidean formula
    //initialize subtraction difference for elegant code
    dr = rA-rB
    dg = gA-gB
    db = bA-bB
    //return result of Euclidean formula
    Return Math.sqrt(dr*dr + dg*dg + db*db)

---

Integers in Java: this was learned from google searching because we didn't understand why we would need to use (& 0xff) for the red component since we had already right shifted the structure by 16 bits effectively removing the blue then green components.
But here's the explanation if I'm understanding this correctly:
On paper we could omit the (& 0xff) since we only have left the red component; but in JAVA, Java uses a 32-bit memory allocation for an Integer. But the left most bit is not part of the actual integer itself, it's reserved by Java to indicate if the Integer is + or -; if + the left most bit will be a 0, if the integer is negative the left most bit will be a 1;
So that raised the question, so then in Java an Integer can only be up to 2^31, NOT 2^32, and that is correct; In Java, the largest int we can deal with is 2^31 = 2.147~ billion!
So THAT'S why we need to 'mask' all three components after right shifting (for red and green; we don't need to right shit for blue)!!

---

wave4 DistanceImageBinarizer:
This class takes a colored image and converts into binary (black & white) image considering how close each pixel is to target color.
Two main jobs: 1. toBinaryArray(BufferedImage image) loops through the pixels checking their distance from target color then output 2D array of 1s/0s;
(1='white'=close to target color; 0=black=far from target) 2. toBufferedImage(int[][] image) takes binary array and converts to java BufferedImage

---

## DistanceImageBinarizer class acts as a liaison between the input image, the binary logic used in DfsBinaryGroupFinder, and outputs a BufferedImage format of the 2D int array we've received from DFS method

## Wave4 class allows us to go back and forth between real image data and binary data

MOCKS

1. Simulate behavior of real objects
2. Created using libraries like Mockito
3. We define what they return and verify method calls
4. Great for testing interactions with external services (eg APIs, databases)
   EX:
   WeatherApiClient mockClient = Mockito.mock(WeatherApiClient.class);
   Mockito.when(mockClient.fetchWeather()).thenReturn("Sunny");

---

FAKES

1. Real implenentations made for testing
2. Simpler, lightweight versions of real classes
3. Can store data in memory (like using a HashMap instead of a real DB)
   EX: public class FakeRepository implements RealRepository{}

## Unit Tests - we will ask AI to write these:

wave4 pseudocode (DistanceImageBinarizer)
