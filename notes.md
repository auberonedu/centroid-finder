ImageSummaryApp.java notes 
--------------------------------------------------------------------------------------------------------------------------

- Need 3 parameters: input_image, hex_target_color, threshold

- Starting position of image -> args[0]

- Position color -> args[1]

- Threshold starts at 0
(MUST PASS IN *INTEGERS* ONLY)

BinarizingImageGroupFinder.java notes 
--------------------------------------------------------------------------------------------------------------------------

- Finds connected white pixels groups in an image 

- ImageBinarizer turns a colored image into black and white 

- BinaryGroupFinder searches for groups that are touched vertically/horizontally 

- Returns the sorted list of connected groups in a Descending order 

BinaryGroupFinder.java notes 
--------------------------------------------------------------------------------------------------------------------------

- Purpose of this method is to find groups of connected 1 in 2D image array

- Pixels are only horizontal or vertical 

- If any arrays are null, then it will throw a *NullPointerException*

- If any are invalid will throw an *IllegalArgumentException*

- Output of this method, returns a list of group finders in decending order 

ColorDistanceFinder.java notes 
--------------------------------------------------------------------------------------------------------------------------

- Interface which compares two colors and their differences

- Colors are represented as 24-bit RGB integers, using the format -> 0xRRGGBB

- RR = Red (8 bits), GG = Green (8 bits), BB = Blue (8 bits)

Coordinate.java notes 
--------------------------------------------------------------------------------------------------------------------------

- Holds the location in an image/array

- Y increases downwards, X increases to the right

- *EXAMPLE*: row: 3, column: 4 -> x:4, y:3

DfsBinaryGroupFinder.java notes 
--------------------------------------------------------------------------------------------------------------------------

- The purpose of this is to find and return a sorted list of groups of connected 1s in a binary 2d image 

- Uses DFS to explore all the connected 1s only horizontally and vertically, no diagonals

- For the centroid for each group have to use integer division

- Return all groups in order by size, by y, by x, all sorted

DistanceImageBinarizer.java notes 
--------------------------------------------------------------------------------------------------------------------------

- Purpose of this file is to convert a colored image to binary imange using only 0s or 1s based on color distance

- Constructor has three fields, distanceFInder, targetColor, threshold

- toBinaryArray method, converts an image to 2D array pf 0s and 1s

- toBufferedImage method, then convert it back to a buffered IMage
