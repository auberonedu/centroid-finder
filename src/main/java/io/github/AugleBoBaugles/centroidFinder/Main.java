package io.github.AugleBoBaugles.centroidFinder;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;


/* 
    User types in String args, if there are less than 3 args, get error. 

    arg 1 = path to video
    arg 2 = target color
    arg 3 = threshold
 */
public class Main {
    public static void main(String[] args) {
        if (args.length < 3) {
            System.out.println("Usage: java Main <input_video> <hex_target_color> <threshold>");
            return;
        }
        
        String videoPath = args[0];
        String hexTargetColor = args[1];
        int threshold = 0;

        // Handling that threshold is a valid number.
        try {
            threshold = Integer.parseInt(args[2]);
        } catch (NumberFormatException e) {
            System.err.println("Threshold must be an integer.");
            return;
        }


        // Handling that path is an actual path
        // TODO : remove image path check, replace with video path check??
        // BufferedImage inputImage = null;
        // try {
        //     inputImage = ImageIO.read(new File(inputImagePath));
        // } catch (Exception e) {
        //     System.err.println("Error loading image: " + inputImagePath);
        //     e.printStackTrace();
        //     return;
        // }


        // TODO: check videoPath is a valid path

        // Parse the target color from a hex string (format RRGGBB) into a 24-bit integer (0xRRGGBB)
        int targetColor = 0;

        // Handling that target color is a valid color
        try {
            targetColor = Integer.parseInt(hexTargetColor, 16);
        } catch (NumberFormatException e) {
            System.err.println("Invalid hex target color. Please provide a color in RRGGBB format.");
            return;
        }

        // Instantiate VideoProcessor
        VideoProcessor processor = new VideoProcessor(videoPath, targetColor, threshold); 
        // tell processor to process data
        processor.extractFrames();

        // CREATE CSV, (Check out Group.java for formatting!)N
    }
    
}
