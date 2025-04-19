import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

import org.junit.Assert;
import org.junit.Test;

public class EuclideanColorDistanceTest {
    EuclideanColorDistance dist = new EuclideanColorDistance();
    
    @Test
    public void noColor() {
        assertEquals(dist.distance(0, 0), 0.0);
    }
    @Test
    public void sameColor() {
        //One color
        //R
        assertEquals(dist.distance(0x000000FF, 0x000000FF), 0.0);
        //G
        assertEquals(dist.distance(0x0000FF00, 0x0000FF00), 0.0);
        //B
        assertEquals(dist.distance(0x00FF0000, 0x00FF0000), 0.0);

        //Two colors
        //RG
        assertEquals(dist.distance(0x0000FFFF, 0x0000FFFF), 0.0);
        //GB
        assertEquals(dist.distance(0x00FFFF00, 0x00FFFF00), 0.0);
        //BR
        assertEquals(dist.distance(0x00FF00FF, 0x00FF00FF), 0.0);

        //All three
        assertEquals(dist.distance(0x00FFFFFF, 0x00FFFFFF), 0.0);
    }
    @Test
    public void constantColorPlusLinear() {
        int constantColor = 0x000000FF; //Red
        final int GREEN_SHIFT = 8;
        final int BLUE_SHIFT = 16;

        double[] test = new double[256];
        for(int i = 0; i < test.length; i++) {
            test[i] = (double)i;
        }
        double[] res = new double[256];

        for(int i = 0; i < 256; i++) {
            res[i] = dist.distance(constantColor, constantColor+(i << GREEN_SHIFT)); //Const red, linear green
        }
        Assert.assertTrue(Arrays.equals(test, res));

        constantColor = constantColor << GREEN_SHIFT; //Green

        for(int i = 0; i < 256; i++) {
            res[i] = dist.distance(constantColor, constantColor+i); //Const green, linear red
        }

        Assert.assertTrue(Arrays.equals(test, res));

        constantColor = constantColor << GREEN_SHIFT; //Blue (Shifted by eight, not actually green).

        for(int i = 0; i < 256; i++) {
            res[i] = dist.distance(constantColor, constantColor+i); //Const blue, linear red
        }

        Assert.assertTrue(Arrays.equals(test, res));


    }

}
