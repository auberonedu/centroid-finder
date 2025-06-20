package io.github.alstondsouza1.centroidFinder;

import org.jcodec.api.FrameGrab;
import org.jcodec.api.JCodecException;
import org.jcodec.common.io.NIOUtils;
import org.jcodec.common.io.SeekableByteChannel;
import org.jcodec.common.model.Picture;
import org.jcodec.scale.AWTUtil;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

// reads frames from a video file usng jcodec
public class VideoReader {
    private FrameGrab grab;
    private int frameRate;

    // initializes the video reader with a file path
    public VideoReader(String inputPath) throws IOException, JCodecException {
        SeekableByteChannel channel = NIOUtils.readableChannel(new File(inputPath));

        // estimate the frame rate from the video file
        this.grab = FrameGrab.createFrameGrab(channel);
        this.frameRate = grab.getVideoTrack().getMeta().getTotalFrames() / (int) grab.getVideoTrack().getMeta().getTotalDuration();
    }

    // returns next frame as a BufferedImage
    public BufferedImage getNextFrame() throws IOException {
        Picture picture = grab.getNativeFrame();
        if (picture == null) return null;
        return AWTUtil.toBufferedImage(picture);
    }

    // returns the frame rate of the video
    public int getFrameRate() {
        return frameRate;
    }
}