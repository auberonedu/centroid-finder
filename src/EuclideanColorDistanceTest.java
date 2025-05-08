import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

import org.junit.Assert;
import org.junit.Test;

public class EuclideanColorDistanceTest {
    EuclideanColorDistance dist = new EuclideanColorDistance();
    
    // color masks and shifts
    final int RED = 0x000000FF;
    final int GREEN = 0x0000FF00;
    final int BLUE = 0x00FF0000;

    final int BLUE_SHFT = 16;
    final int GREEN_SHFT = 8;

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

        // test a constant color (red) vs a linear color (green)
        int constantColor = 0x000000FF; //Red
        final int GREEN_SHIFT = 8;
        final int BLUE_SHIFT = 16;

        double[] test = createTestLinear(256);
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

    @Test
    public void twoConstantColorsPlusLinear() {
        int RED_GREEN = 0x0000FFFF;
        int RED_BLUE = 0x00FF00FF;
        int GREEN_BLUE = 0x00FFFF00;

        double[] test = createTestLinear(256);
        double[] cmp = new double[256];

        for(int i  = 0; i < 256; i++) {
            cmp[i] = dist.distance(RED_GREEN, RED_GREEN+(i << BLUE_SHFT));
        }
        Assert.assertTrue(Arrays.equals(cmp, test));

        for(int i  = 0; i < 256; i++) {
            cmp[i] = dist.distance(RED_BLUE, RED_BLUE+(i << GREEN_SHFT));
        }
        Assert.assertTrue(Arrays.equals(cmp, test));

        for(int i  = 0; i < 256; i++) {
            cmp[i] = dist.distance(GREEN_BLUE, GREEN_BLUE+i);
        }
        Assert.assertTrue(Arrays.equals(cmp, test));
    }

    @Test
    public void randomTests() {
        //WolframAlpha helped me find the distance in symbolic form, to compare to.
        assertEquals(dist.distance(coord(126, 127, 238), coord(126, 127, 239)), 1.0);

        assertEquals(dist.distance(coord(56, 78, 23), coord(245, 99, 157)), Math.sqrt(54118));
        assertEquals(dist.distance(coord(77, 99, 127), coord(127, 64, 31)), Math.sqrt(12941));
        assertEquals(dist.distance(coord(255, 41, 37), coord(183, 255, 1)), 2*Math.sqrt(13069));
        assertEquals(dist.distance(coord(1, 1, 1), coord(38, 246, 133)), Math.sqrt(78818));
    }

    private double[] createTestLinear(int size) {
        double[] res = new double[size];

        for(int i = 0; i < size; i++) {
            res[i] = (double) i;
        }

        return res;
    }

    private int coord(int r, int g, int b) {
        return r+(g<<GREEN_SHFT)+(b<<BLUE_SHFT);
    }
}
