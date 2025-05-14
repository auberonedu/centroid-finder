package io.github.qopci.centroidFinder;

import org.bytedeco.javacv.*;
import java.awt.image.BufferedImage;

public class VideoReader {
    private final FFmpegFrameGrabber grabber;
    private final Java2DFrameConverter converter = new Java2DFrameConverter();
    private double frameRate;
    private int frameNumber = 0;

    //Constructor here: this is the video grabber, where it starts
    public VideoReader(String path) throws Exception {
        grabber = new FFmpegFrameGrabber(path); // Use JavaCV to open the video file
        grabber.start(); 
        frameRate = grabber.getFrameRate(); 
    }

    //Read the next frame from vid, then return the bufferedImage
    public BufferedImage nextFrame() throws Exception {
        Frame frame = grabber.grabImage(); 
        frameNumber++;
    
        if (frame != null) {
            return converter.convert(frame); 
        } else {
            return null;
        }
    }

    //then check whether the current frame should processed as new SECOND
    public boolean shouldProcessThisFrame() {
        // Only process one fps — if frameNumber crosses a second boundary
        return (int)((frameNumber - 1) / frameRate) != (int)(frameNumber / frameRate);
    }
    
    //Close the video grabber 
    public void close() throws Exception {
        grabber.close();
    }
}
