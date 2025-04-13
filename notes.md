ImageSummaryApp.java Notes
------------------------------------------

- this runs everything

- we need 3 argument from the user like image file, hex color, threhold number

what it does:

- loads the image

- checks each pixel, if its close to the color then it makes white or else black

- save the binarized image

- finds group of pixels (white)

- writes them into the csv file as in like x, y and size


BinarizingImageGroupFinder.java
------------------------------------------

- Has 2 steps
    - coverts the image to binary array (using ImageBinarizer)
    - finds white pixel groups (using BinaryGroupFinder)

- returns the list of group objects


BinaryGroupFinder.java 
------------------------------------------

- Finds all connected white pixels groups in the image

- pixels are connected like up/right/down/left (no diagonal)

- each group has bunch of 1s touching each other

- calculates the sizes like how many pixels and also the centroid like the average xy and y of position pixels


ColorDistanceFinder.java notes
------------------------------------------

- Interface that show how to compare the 2 RGS colors

- Each color is in 0xRRGGBB format (24-bit hex)

- RR = red, GG = green, BB = blue


Coordinate.java notes
------------------------------------------

- records that stores as x and y coordinate

- represents pixels locations in the image

- example like (row: 4, col: 7) = x:7, y:4

    X moves right
    Y moves down


DfsBinaryGroupFinder.java
------------------------------------------

- loops around every pixels

- used DFS to explore the connected 1s only up/down/left/right only

- tracks how many pixels = size, sum of the positions (x and y) to find out the centroid or to calculate it

- makes a group with size and centroid
