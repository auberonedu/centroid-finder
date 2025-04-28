Image Summary app
- passes in 3 args  <input_image> <hex_target_color> <threshold>

DfsBinaryGroupFinder- finds groups of connected pixels of a binary image 


euclideancolordistance is a class that implements the colordistancefinder interface-
ColorDistanceFinder distanceFinder = new EuclideanColorDistance();
finds the difference between two hex RGB integer 

DistanceImageBianarizar is a class that takes 3 params, that implments the ImageBianarize r interface 
ImageBinarizer binarizer = new DistanceImageBinarizer(distanceFinder, targetColor, threshold);
this will turn a color image into black and white image 

BinarizingImageGroupFinder- will call ImageBinarizer and BinaryGroupFinder 

- sampleOutput is an image of sample imput with the target of yellow, now in black in white with the target color in white. 