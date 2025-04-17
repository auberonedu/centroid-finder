import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.util.Arrays;
import java.util.List;
import java.awt.image.BufferedImage;
import org.junit.Test;

public class BinarizingImageGroupFinderTest {
    // set up fakes to make testing simpler
    class FakeBinarizer implements ImageBinarizer{
        private final int[][] predefinedOutput;

        public FakeBinarizer(int[][] predefinedOutput){
            this.predefinedOutput = predefinedOutput;
        }

        @Override
        public int[][] toBinaryArray(BufferedImage image){
            return predefinedOutput;
        }

        @Override
        public BufferedImage toBufferedImage(int[][] image){
            return new BufferedImage(5, 5, BufferedImage.TYPE_INT_RGB);
        }
    }

    class FakeGroupFinder implements BinaryGroupFinder{
        private final List<Group> predefinedGroups;

        public FakeGroupFinder(List<Group> predefinedGroups){
            this.predefinedGroups = predefinedGroups;
        }

        @Override
        public List<Group> findConnectedGroups(int[][] image){
            return predefinedGroups;
        }
    }
}
