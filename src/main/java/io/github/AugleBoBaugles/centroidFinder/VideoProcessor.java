package io.github.AugleBoBaugles.centroidFinder;

import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.PrintWriter;

public class VideoProcessor {
    public BufferedImage inputImage; // TODO - Make this video
    public int targetColor;
    public int threshhold;
    public PrintWriter csv;
    public int framesPerSec = 30; // TODO: Maybe remove this?
    public int secondIncrement = 1; // TODO: Possible customizable feature later

    public VideoProcessor(BufferedImage inputImage, int targetColor, int threshhold) {
        this.inputImage = inputImage;
        this.targetColor = targetColor;
        this.threshhold = threshhold;
    }

    
    // Loop through whole video, at each fps increment, run frameToData on frame
    public static void extractFrames(String videoPath, String outputDir) throws Exception {
        
        FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(videoPath);
        grabber.start();

        Java2DFrameConverter converter = new Java2DFrameConverter();

        // TODO: convert frames to seconds
        int frameNumber = 0;
        Frame frame;

        while ((frame = grabber.grabImage()) != null) {
            BufferedImage bufferedImage = converter.convert(frame);
            if (bufferedImage != null) {
                File outputFile = new File(outputDir, String.format("frame_%05d.png", frameNumber++));
                ImageIO.write(bufferedImage, "png", outputFile);

                // TODO: seconds = frameNumber / fps
            }
        }

        grabber.stop();
        grabber.release();
        System.out.println("Done: Extracted " + frameNumber + " frames.");
    }



    public static void main(String[] args) {
        try {
            String videoPath = "sampleInput/sample_video_1.mp4"; // adjust as needed
            String outputDir = "sampleOutput"; // ensure this directory exists or create it
            new File(outputDir).mkdirs();
            extractFrames(videoPath, outputDir);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // TODO: frameToData method takes in framePath and passes that into an
    // LargestCentroid with color and threshold, add returned data to csv
    public void frameToData(String framePath, int seconds){
        LargestCentroid currentCentroid = new LargestCentroid(framePath, targetColor, threshhold, seconds);
        LargestCentroidRecord record = currentCentroid.findLargestCentroid();
        // TODO: add record data to csv file
        
        // extract timestamp from file

        // get seconds, x, and y from record

        // write to csv
    }

}
