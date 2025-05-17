package io.github.AugleBoBaugles.centroidFinder;

import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class VideoProcessor {
    private String videoPath; 
    private int targetColor;
    private int threshhold;
    private PrintWriter csv;
    private int framesPerSec = 30; // TODO: Maybe remove this?
    private int secondIncrement = 1; // TODO: Possible customizable feature later

    public VideoProcessor(String videoPath, int targetColor, int threshhold) {
        this.videoPath = videoPath;
        this.targetColor = targetColor;
        this.threshhold = threshhold;
    }

    public void extractFrames() {
        // TODO: Figure out what this is actually doing
        try {
            // making a directory to hold the output files
            String outputDir = "sampleOutput"; // ensure this directory exists or create it
            new File(outputDir).mkdirs();

            // TODO: Make sure this puts it into sampleOutput directory
            String csvFile = "sampleOutput/largestCentroids.csv";  // Path to the new CSV file
            new File(csvFile);
            
            extractFrames(videoPath, outputDir);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Loop through whole video, at each fps increment, run frameToData on frame
    private void extractFrames(String videoPath, String outputDir) throws Exception {
        
        FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(videoPath);
        grabber.start(); //Start reading in the video

        Java2DFrameConverter converter = new Java2DFrameConverter();

        int frameNumber = 0;  // Counter for total frames processed 
        Frame frame; // Holds the current frame being used

        while ((frame = grabber.grabImage()) != null) {
            // Add logic to check
            if (frameNumber % framesPerSec == 0) { // check if the frame is at a whole second
                BufferedImage bufferedImage = converter.convert(frame);
                if (bufferedImage != null) {
                    // File outputFile = new File(outputDir, String.format("frame_%05d.png", frameNumber));
                    // ImageIO.write(bufferedImage, "png", outputFile);

                    // run logic to get data
                    frameToData(bufferedImage, (frameNumber/framesPerSec));  
                }
            }
            frameNumber++;  // Move to the next frame
        }

        grabber.stop(); // Stop reading the video file
        grabber.release(); 
    }

    // frameToData method takes in framePath and passes that into a
    // LargestCentroid with color and threshold, add returned data to csv
    public void frameToData(BufferedImage frame, int seconds){
        
        LargestCentroid currentCentroid = new LargestCentroid(frame, targetColor, threshhold, seconds);
        LargestCentroidRecord currentLargestCentroidRecord = currentCentroid.findLargestCentroid();

        // add record data to csv file

        // TODO: PrintWriter needs to create the CSV file BEFORE we loop through and add data
        try (PrintWriter writer = new PrintWriter("largestCentroids.csv")) {
        } catch (Exception e) {
            System.err.println("Error creating largestCentroids.csv");
            e.printStackTrace();
        }
    }

}
