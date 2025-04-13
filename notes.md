When ImageSummaryApp starts, user enters:
1. **input image** *(astring path)*
2. **target color** *(int in hex form)*
3. **threshold** *(int, default to 0)*

**binarizer** takes in a distance finder, the target color, and the threshhold, then uses these on the input image to create a new binary image (output).

**groupFinder** uses binarizer and a DFS binary group finder to locate all the areas of connected pixels in the binarized image.

**groups** contains a list of groups of connects areas, which **writer** then adds to a csv (output).