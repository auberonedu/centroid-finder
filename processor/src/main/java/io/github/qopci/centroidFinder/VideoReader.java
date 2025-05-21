package io.github.qopci.centroidFinder;

import org.bytedeco.javacv.*;
import java.awt.image.BufferedImage;

public class VideoReader implements AutoCloseable {
    private final FFmpegFrameGrabber grabber;
    private final Java2DFrameConverter converter = new Java2DFrameConverter();
    private double frameRate;
    private int frameNumber = 0;

    // Constructor here: this is the video grabber, where it starts
    public VideoReader(String path) throws Exception {
        grabber = new FFmpegFrameGrabber(path); // Use JavaCV to open the video file
        grabber.start(); 
        frameRate = grabber.getFrameRate(); 
    }

    // Read the next frame from vid, then return the bufferedImage
    public BufferedImage nextFrame() throws Exception {
        Frame frame = grabber.grabImage(); 
        frameNumber++;
    
        if (frame != null) {
            return converter.convert(frame); 
        } else {
            return null;
        }
    }

    // Check whether the current frame should processed as new SECOND
    public boolean shouldProcessThisFrame() {
        return (int)(frameNumber / frameRate) != (int)((frameNumber - 1) / frameRate);
    }   
    
    // Lets other classes access the actual frame rate of the video
    public double getFrameRate() {
        return grabber.getFrameRate();
    }

    // Close the video grabber 
    public void close() throws Exception {
        grabber.close();
    }
}
