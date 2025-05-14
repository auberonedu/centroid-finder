package io.github.qopci.centroidFinder;

import org.bytedeco.javacv.*;
import java.awt.image.BufferedImage;

public class VideoReader {
    private final FFmpegFrameGrabber grabber;
    private final Java2DFrameConverter converter = new Java2DFrameConverter();
    private double frameRate;
    private int frameNumber = 0;

    //Constructor here:
    //Read the next frame from vid, then return the bufferedImage
    //then check whether the current frame should processed as new SECOND
    //Close the video grabber 

}
