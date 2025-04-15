# Notes:

## ImageSummaryApp.java
- Capture CLI args to variables `inputImagePath` (photo.png), `hexTargetColor` (FF0000), and `threshold` (how close color pixels must be to target color) from `java ImageSummaryApp <input_image> <hex_target_color> <threshold>`
- Validate input image, threshold integer, and parse `hexTargetColor` from RGB to 24-bit integer (0xRRGGBB)
- Using EuclideanColorDistance class get distance between target color pixels
- Using DistanceImageBinarizer check if a pixel is within threshold (white) or out of threshold (black)
- Binarize image into a matrix of 1s (white) and 0s (black), then convert into a black and white `BufferedImage`
- Save binarized.png
- Using `DfsBinaryGroupFinder` find connected white pixel groups (4 directional, horiz and vert, no diagonals). Calc size (num of pixels) and centroid (avg x and y using int division)
- Write a CSV file with group size and centroid coordinates (size, x, y)
### Summary
- Finds areas in an image that are visually similar to target color and provides binary image and csv report