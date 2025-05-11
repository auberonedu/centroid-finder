package io.github.AugleBoBaugles.centroidFinder;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.jcodec.api.FrameGrab;
import org.jcodec.api.JCodecException;
import org.jcodec.common.io.NIOUtils;
import org.jcodec.common.model.Picture;
//import org.jcodec.javase.scale.AWTUtil;
import org.jcodec.scale.AWTUtil;
public class VideoConversionApp {
    public static void main(String[] args) throws IOException, JCodecException {
        File file = new File("sampleInput/sample_video_1.mp4");
        FrameGrab grab = FrameGrab.createFrameGrab(NIOUtils.readableChannel(file));
        Picture picture;
        int count = 1;
        while (null != (picture = grab.getNativeFrame())) {
            System.out.println(picture.getWidth() + "x" + picture.getHeight() + " " + picture.getColor());
            BufferedImage bufferedImage = AWTUtil.toBufferedImage(picture);
            ImageIO.write(bufferedImage, "png", new File("sampleOutput/" + Integer.toString(count) + ".png"));
            count++;
        }
    }
}