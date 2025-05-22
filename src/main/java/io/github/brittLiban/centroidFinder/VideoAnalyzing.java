package io.github.brittLiban.centroidFinder;

import java.awt.image.BufferedImage;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;

public class VideoAnalyzing {

    private final FrameProcessor builtProcessor;

    // remember, when this is called in the entry app
    // It could be a LargestBlobProcessor, a MotionDetector, or any other
    // FrameProcessor.
    // the builtProcessor is what ever YOU decide to build to process each frame
    // although this is just a tool - a LIFELESS object. it cant do anything all by
    // itself
    // that is why we introduce another method on top of it telling the behavior we
    // expect
    public VideoAnalyzing(FrameProcessor builtProcessor) {
        this.builtProcessor = builtProcessor;
    }

    public void process(FFmpegFrameGrabber grabber, PrintWriter toCsv) {

        // tracking how long it takes
        long startTime = System.currentTimeMillis();
        int processedFrames = 0;

        // put this in a try catch so that it will close when done to avoid memeory
        // leaks ( free rent in memeory terms )
        // can id if it needs to be closed every if its AutoClosable
        // try catch is good cause it will auto close it for me!
        try (Java2DFrameConverter converter = new Java2DFrameConverter()) {
            // this is a class that will help us convert a raw frame from
            // javaCV into something java can handle.

            Frame frame;
            int frameIndex = 0;

            toCsv.println("frame,x,y");
            // while the grabber.grabImage() != null then do this
            while ((frame = grabber.grabImage()) != null) {
                BufferedImage buffered = converter.convert(frame);

                if (buffered != null) {
                    // here you are intializing the frame to have that logic DONE on it.
                    // builtProcessor IS allowing you access any methods inside frame processor
                    // BUT frameProcessor is also WHERE the method to handle frames are
                    FrameResult result = builtProcessor.processFrame(buffered, frameIndex);
                    processedFrames++;
                    // fortunately we can just literally write the result into the csv
                    toCsv.println(result.toCsvRow());
                }

                if (buffered == null) {
                    toCsv.println(frameIndex + ", -1, -1"); // will record something but NO GOOD ASHOIAH
                }

                frameIndex++;
            }

        } catch (Exception e) {
            System.err.println("Error during video processing:");
            e.printStackTrace();
        }

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        System.out.println("Finished processing " + processedFrames + " frames in " + duration + " ms.");

        // logging performance
        try (PrintWriter perfWriter = new PrintWriter(new FileWriter("performance_log.csv", true))) {
            String username = System.getProperty("user.name");
            String os = System.getProperty("os.name") + " " + System.getProperty("os.version");
            String javaVersion = System.getProperty("java.version");

            perfWriter.println("username, Total frames,processing duration(s), os, jv -v");
            perfWriter.println(username + "," +  processedFrames + "," + duration/1000 + "," + os + "," + javaVersion);
        } catch (IOException e) {
            System.err.println("Could not write performance log.");
            e.printStackTrace();
        }
    }

}
