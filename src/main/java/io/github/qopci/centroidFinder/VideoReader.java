package io.github.qopci.centroidFinder;

import org.bytedeco.javacv.*;
import java.awt.image.BufferedImage;

public class VideoReader {
    private final FFmpegFrameGrabber grabber;
    private final Java2DFrameConverter converter = new Java2DFrameConverter();
    private double frameRate;
    private int frameNumber = 0;
}
