Image Summary app
- passes in 3 args  <input_image> <hex_target_color> <threshold>

euclideancolordistance is a class that implements the colordistancefinder interface-
ColorDistanceFinder distanceFinder = new EuclideanColorDistance();

DistanceImageBianarizar is a class that takes 3 params, that implments the ImageBianarizer interface 
ImageBinarizer binarizer = new DistanceImageBinarizer(distanceFinder, targetColor, threshold);

use the created distancefinder as the first param in the bianarizer 

- sampleOutput is an image of sample imput with the target of yellow, now in black in white with the target color in white. 