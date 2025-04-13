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

