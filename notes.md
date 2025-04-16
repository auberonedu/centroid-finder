## App Behavior
When ImageSummaryApp starts, user enters:
1. **input image** *(astring path)*
2. **target color** *(int in hex form)*
3. **threshold** *(int, default to 0)*

**binarizer** takes in a distance finder, the target color, and the threshhold, then uses these on the input image to create a new binary image (output).

**groupFinder** uses binarizer and a DFS binary group finder to locate all the areas of connected pixels in the binarized image.

**groups** contains a list of groups of connects areas, which **writer** then adds to a csv (output).

## App Structure
### Main App
- **Image Summary App**
    - Take image path, target hex color, and integer threshhold, return binarized image and a CSV of the size and centroid of all connected groups.
### Libraries
- [**Buffered Image**](https://docs.oracle.com/javase/8/docs/api/java/awt/image/BufferedImage.html)
- [**File**](https://docs.oracle.com/javase/8/docs/api/java/io/File.html)
- [**Print Writer**](https://docs.oracle.com/javase/8/docs/api/java/io/PrintWriter.html)
- **List**
- [**Image IO**](https://docs.oracle.com/javase/8/docs/api/javax/imageio/ImageIO.html)

### Interfaces
- **Binary Group Finder**
    - Find connected groups of 1s in an integer array representing a binary image.
- **Color Distance Finder**
    - Compute distance between two colors.
- **Image Binarizer**
    - Convert from RGB image to binary image in the form of a 2D array of integers of 1 and 0.
- **Image Group Finder**
    - Finds connected groups in an image.
### Classes
- **Binarizing Image Group Finder**
    - Implements *Image Group Finder*.
    - Uses *Image Binarizer* to convert RGB image into a 2D array, and *Binary Group Finder* to locate connected groups of pixels.
- **DFS Binary Group Finder**
    - Implements *Binary Group Finder*.
    - Find connected pixel groups of 1s in an integer array representing a binary image.
- **Distance Image Binarizer**
    - Implements *Image Binarizer*.
    - Use color distance to determine whether each pixel should be black or white in the binary image.
- **Euclidean Color Distance**
    - Implements *Color Distance Finder*.
    - Applies Euclidean distance formula to difference of colors.
        - sqrt((r1 - r2)^2 + (g1 - g2)^2 + (b1 - b2)^2)
### Records
- **Coordinate**
    - Contains two ints representing an x and y coordinate.
- **Group**
    - Represents a group of contiguous pixels in an image, with a calculated centroid that is located at the average of pixel coordinates in each dimension.


## References
### EuclideanColorDistanceTest
#### Links 
- (Google Color Picker: https://g.co/kgs/DG41Bvm)
- (Rapid Tables: https://www.rapidtables.com/web/color/RGB_Color.html)

### DistanceImageBinarizer
#### Links
- Java RGB Color Channels: Extracting from BufferedImage (https://www.youtube.com/watch?v=agW5FJSbjCM)
- Java BufferedImage Class (https://www.tutorialspoint.com/java_dip/java_buffered_image.htm)