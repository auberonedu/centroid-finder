package io.github.oakes777.salamander;
import java.awt.Color;
import java.awt.image.BufferedImage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;

public class FrameAnalyzerTest {

    @Test
    public void testBinarizeOnly() {
        int redColor = 0xFF0000; // Red
        int threshold = 50;

        FrameAnalyzer analyzer = new FrameAnalyzer(redColor, threshold);

        // Create a test image with some red and some non-red pixels
        BufferedImage testImage = new BufferedImage(3, 1, BufferedImage.TYPE_INT_RGB);
        testImage.setRGB(0, 0, 0xFF0000); // exact red
        testImage.setRGB(1, 0, 0x00FF00); // green
        testImage.setRGB(2, 0, 0x0000FF); // blue

        BufferedImage result = analyzer.binarizeOnly(testImage);

        // Only the red pixel should be white (255), others should be black (0)
        assertEquals(Color.WHITE.getRGB(), result.getRGB(0, 0));
        assertEquals(Color.BLACK.getRGB(), result.getRGB(1, 0));
        assertEquals(Color.BLACK.getRGB(), result.getRGB(2, 0));
    }

    @Test
    public void testFindLargestGroupWithMultipleGroups() {
        // Create an image with 3 distinct groups:
        // - Group 1: 2 white pixels
        // - Group 2: 4 white pixels
        // - Group 3: 6 white pixels
        BufferedImage image = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);

        // Fill with black
        for (int y = 0; y < 10; y++)
            for (int x = 0; x < 10; x++)
                image.setRGB(x, y, Color.BLACK.getRGB());

        // Group 1 (2 pixels)
        image.setRGB(0, 0, Color.WHITE.getRGB());
        image.setRGB(1, 0, Color.WHITE.getRGB());

        // Group 2 (4 pixels)
        image.setRGB(3, 3, Color.WHITE.getRGB());
        image.setRGB(4, 3, Color.WHITE.getRGB());
        image.setRGB(4, 4, Color.WHITE.getRGB());
        image.setRGB(3, 4, Color.WHITE.getRGB());

        // Group 3 (6 pixels)
        image.setRGB(7, 7, Color.WHITE.getRGB());
        image.setRGB(8, 7, Color.WHITE.getRGB());
        image.setRGB(9, 7, Color.WHITE.getRGB());
        image.setRGB(9, 8, Color.WHITE.getRGB());
        image.setRGB(8, 8, Color.WHITE.getRGB());
        image.setRGB(7, 8, Color.WHITE.getRGB());

        // Create analyzer with white as target color (hex: FFFFFF)
        FrameAnalyzer analyzer = new FrameAnalyzer(0xFFFFFF, 50);

        // Run the method
        Group largest = analyzer.findLargestGroup(image);

        assertEquals(6, largest.size(), "Largest group should have 6 pixels");
    }

    @Test
    public void testBinarizedLargest() {

        BufferedImage testImage = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);
        
        // Fill pixels with red
        for (int y = 0; y < 10; y++)
            for (int x = 0; x < 10; x++)
                testImage.setRGB(x, y, 0xFF0000);
        
        // Group 1 (1 pixel)
        testImage.setRGB(0, 0, 0x0000FF);  // blue
        testImage.setRGB(1, 0, 0x0000FF);
        // Group 2 (4 connected blue pixels)
        testImage.setRGB(3, 3, 0x0000FF);  // blue
        testImage.setRGB(4, 3, 0x0000FF);  // blue
        testImage.setRGB(4, 4, 0x0000FF);  // blue
        testImage.setRGB(3, 4, 0x0000FF);  
    
        // Group 3 (all green - not blue)
        testImage.setRGB(7, 7, 0x00FF00);
        testImage.setRGB(8, 7, 0x00FF00);
        testImage.setRGB(9, 7, 0x00FF00);
        testImage.setRGB(9, 8, 0x00FF00);
        testImage.setRGB(8, 8, 0x00FF00);
        testImage.setRGB(7, 8, 0x00FF00);
        
        FrameAnalyzer analyzer = new FrameAnalyzer(0x0000FF, 1);
        // Binarize and analyze
        
        Group largest = analyzer.findLargestGroup(testImage);
        assertNotNull(largest, "Expected a largest group but got null. Check if binarization matched blue pixels.");
        assertEquals(4, largest.size(), "Largest group should have 4 white pixels.");

        BufferedImage result = analyzer.binarizeOnly(testImage);
        assertEquals(Color.WHITE.getRGB(), result.getRGB(0, 0), "Blue pixel should be white in binarized image.");
        assertEquals(Color.WHITE.getRGB(), result.getRGB(4, 4), "Blue pixel should be white in binarized image.");
        assertEquals(Color.BLACK.getRGB(), result.getRGB(7, 8), "Green pixel should be black in binarized image.");
    }
}
    

