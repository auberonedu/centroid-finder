import org.junit.Test;

import interfaces.ColorDistanceFinder;

public class BinarizingImageGroupFinderTest {

    @Test
    public void test_test() {

        // Define test colors
        // int colorA = 0x000000;
        // int colorB = 0xFFFFFF;

    
        // Create a color distance finder
        // ColorDistanceFinder colorDistance = new EuclideanColorDistance(0x000000, 0xFFFFFF);
        // CREATE A BINARIZER (input: color distance finder, int color, int threshold)
        DistanceImageBinarizer binarizer = new DistanceImageBinarizer(null, 0, 0);
        // Create group finder
        DfsBinaryGroupFinder finder = new DfsBinaryGroupFinder();

        // CREATE AN IMAGE GROUP FINDER (input: image binarizer, binary group finder)
        BinarizingImageGroupFinder testImageGroupFinder = new BinarizingImageGroupFinder(null, finder);


        /*    public BinarizingImageGroupFinder(ImageBinarizer binarizer, BinaryGroupFinder groupFinder) {
        this.binarizer = binarizer;
        this.groupFinder = groupFinder;
    } */

    }
    
}
