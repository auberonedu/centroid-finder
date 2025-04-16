App starts off by checking the 3 args passed into main, making sure all the values are valid and setting them to variables that can be used in the main method.
If this is valid then we instantiate 2 other objects, distanceFinder which takes in two color hex codes, then uses the euclidean formula to calculate how close the colors are.
we also instatiate a binarizer object that converts our image into a 2d binary array, representing the different pixels in the image with 1s being the color we want to find, and 0 being the colors we dont want to find. 
then our binarized image is wrote to a disk called binarized.jpg

then we calculate the groups of colors that we are looking for. We do this by instantiating a new group finder, passing it our binarized and a new DFSBinaryGroupFinder. 
next we calculate our groups of pixels in the image using the findConnectedGroups method in BinarizingImageGroupFinder, this will give us a list<group> objects of our groups that are connected, this is then written to a csv file. 
