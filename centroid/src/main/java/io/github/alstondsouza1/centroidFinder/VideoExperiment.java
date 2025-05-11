package io.github.alstondsouza1.centroidFinder;

import org.jcodec.api.FrameGrab;
import org.jcodec.api.JCodecException;
import org.jcodec.common.io.SeekableByteChannel;
import org.jcodec.common.model.Picture;
import org.jcodec.scale.AWTUtil;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

public class VideoExperiment {
    public static void main(String[] args) {
        File videoFile = new File("sampleInput/sample.mp4");

        try (FileInputStream fis = new FileInputStream(videoFile);
             FileChannel channel = fis.getChannel()) {

            FrameGrab grab = FrameGrab.createFrameGrab((SeekableByteChannel) channel);

            int frameNumber = 0;
            Picture picture;
            while ((picture = grab.getNativeFrame()) != null && frameNumber < 5) {
                BufferedImage bufferedImage = AWTUtil.toBufferedImage(picture);
                File output = new File("frame_" + frameNumber + ".png");
                ImageIO.write(bufferedImage, "png", output);
                System.out.println("Saved frame " + frameNumber);
                frameNumber++;
            }

        } catch (IOException | JCodecException e) {
            System.err.println("Error reading video: " + e.getMessage());
        }
    }
}