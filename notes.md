ImageSummaryApp.java notes 
--------------------------------------------------------------------------------------------------------------------------

- We need 3 parameters: input_image, hex_target_color, threshold

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

- purpose of this method is to find groups of connected 1

- pixels are only horizontal or vertical 

- if any arrays are null, then it will throw a nullpointerexception 

- if any are invalid will throw an illegalargumentexception 

- output of this method, returns a list of group finders in decending order 