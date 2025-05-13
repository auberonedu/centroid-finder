We want to implement the centroid algorithm first to locate biggest centroid, which should be the first in the index from the algorithm we wrote because it was sorted largest to smallest.

Similar to our VideoExperiment.java, while the while loop is running we want to run the getX() and getY() every frame and store that information in the CSV. 

Potentially turn every frame into .png and then run the algorithm to grab x and y coordinates from those pngs for every frame.

Consider using ImageGroupFinder.java interface, try to convert frame to BufferedImage rather than png so then we can process in RAM without writing and saving to disk with pngs. 

So now we will work on figuring out converting from a singular frame to a BufferedImage while processing video and have it write x and y coordinates of largest centroid to csv then proceed to next frame and repeat the same thing until end of the video.

Later on when testing where salamander is location wise, potentially use metadata from the video resolution to determine the boundaries and which of the three areas the salamander is in. May include math to separate 3 boundaries.