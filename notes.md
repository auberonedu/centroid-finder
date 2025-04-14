When ImageSummaryApp starts, user enters:
1. **input image** *(astring path)*
2. **target color** *(int in hex form)*
3. **threshold** *(int, default to 0)*

**binarizer** takes in a distance finder, the target color, and the threshhold, then uses these on the input image to create a new binary image (output).

**groupFinder** uses binarizer and a DFS binary group finder to locate all the areas of connected pixels in the binarized image.

**groups** contains a list of groups of connects areas, which **writer** then adds to a csv (output).


Interfaces:
- **Image Group Finder**
- **Binary Group Finder**
    - Find connected groups of 1s in an integer array representing a binary image.
- **Color Distance Finder**
    - Compute distance between two colors.

Classes:
- **Binarizing Image Group Finder**
    - Implements *Image Group Finder*
    - Uses *Image Binarizer* to convert RGB image into a 2D array, and *Binary Group Finder* to locate connected groups of pixels.
- **Euclidean Color Distance**
    - Implements *Color Distance Finder*
    - Applies Euclidean distance formula to difference of colors.
        - sqrt((r1 - r2)^2 + (g1 - g2)^2 + (b1 - b2)^2)
- **DFS Binary Group Finder**
    - Implements *Binary Group Finder*
    - Find connected pixel groups of 1s in an integer array representing a binary image.
-**Distance Image Binarizer**
    

Records:
- **Coordinate**
    - Contains two ints representing an x and y coordinate.
