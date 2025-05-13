# Steps / Things to consider:

1. We want to implement the centroid algorithm first to locate biggest centroid, which should be the first in the index from the algorithm we wrote because it was sorted largest to smallest.

2. Similar to our VideoExperiment.java, while the while loop is running we want to run the getX() and getY() every frame and store that information in the CSV. 

3. Potentially turn every frame into .png and then run the algorithm to grab x and y coordinates from those pngs for every frame.

4. Consider using ImageGroupFinder.java interface, try to convert frame to BufferedImage rather than png so then we can process in RAM without writing and saving to disk with pngs. 

5. So now we will work on figuring out converting from a singular frame to a BufferedImage while processing video and have it write x and y coordinates of largest centroid to csv then proceed to next frame and repeat the same thing until end of the video.

6. Later on when testing where salamander is location wise, potentially use metadata from the video resolution to determine the boundaries and which of the three areas the salamander is in. May include math to separate 3 boundaries.

7. JavaCV has a built in method to convert from frame to BufferedImage called Java2DFrameConverter. We will try to implement this into our logic to process each frame.

# Architecture

1. Create a class to store the conversion of a frame to a BufferedImage. 

2. Create a class that will call the other centroid-finder methods that we previously implemented to find connected group and process each frame. (Possibly convert DFSBinaryGroupFinder to iterative approach).


