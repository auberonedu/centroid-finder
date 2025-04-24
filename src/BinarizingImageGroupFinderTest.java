import java.awt.image.BufferedImage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BinarizingImageGroupFinderTest {
    //Will contain an arbitrary amount of groups of size one, and an arbitrary x/y
    BufferedImage imageOne = new BufferedImage(16, 16, BufferedImage.TYPE_INT_RGB);
    //Will contain an arbitrary amount of groups of size four, and an arbitrary x/y
    BufferedImage imageTwo = new BufferedImage(32, 32, BufferedImage.TYPE_INT_RGB);
    //Will contain an arbitrary amount of groups of an arbitrary size and artibrary x/y.
    BufferedImage imageThree = new BufferedImage(256, 256, BufferedImage.TYPE_INT_RGB);
    //Will contain no groups; size doesn't matter.
    BufferedImage imageNone = new BufferedImage(1024, 1024, BufferedImage.TYPE_INT_RGB);
    //Will contain a single group of an arbitrary size and x/y; here to test against large images.
    BufferedImage imageFive = new BufferedImage(4096, 4096, BufferedImage.TYPE_INT_RGB);

    //TODO: Create arrays of pixels (groups) that will be added to the buffered images, then passed to create groups.



    private Coordinate centroidGen(List<int[]> pixels, int size) {
        int sumX = 0;
        int sumY = 0;

        for(int[] pixel : pixels) {
            sumX += pixel[0];
            sumY += pixel[1];
        }

        int centX = sumX/size;
        int centY = sumY/size;

        return new Coordinate(centX, centY);
    }
    private Group groupGen(int size, Coordinate centroid) {
        //This will look nicer through groupGen(size, centroidGen(pixels, size)), utilizing a more "functional" look.
        return new Group(size, centroid);
    }
}
