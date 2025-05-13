package io.github.oakes777.salamander;

import org.jcodec.api.FrameGrab;
import org.jcodec.api.JCodecException;
import org.jcodec.common.io.NIOUtils;
import org.jcodec.common.model.Picture;
import org.jcodec.scale.AWTUtil;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.channels.SeekableByteChannel;


/**
 * Demo: Extracts one frame per second from a video using JCodec.
 * Saves each frame as frame_0.png, frame_1.png, etc.
 */
public class JCodecDemo {
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Usage: java JCodecDemo <video_file>");
            return;
        }

        String videoPath = args[0];
        try {
            SeekableByteChannel ch = NIOUtils.readableChannel(new File(videoPath));
            FrameGrab grab = FrameGrab.createFrameGrab(ch);

            int frameNumber = 0;
            double secondsStep = 1.0;

            while (true) {
                grab.seekToSecondPrecise(frameNumber * secondsStep);
                Picture picture = grab.getNativeFrame();
                if (picture == null) break;

                BufferedImage frame = AWTUtil.toBufferedImage(picture);
                File output = new File("frame_" + frameNumber + ".png");
                ImageIO.write(frame, "png", output);
                System.out.println("Saved frame_" + frameNumber + ".png");

                frameNumber++;
            }

            System.out.println("✅ Done extracting frames.");
        } catch (IOException | JCodecException e) {
            System.err.println("Error processing video: " + videoPath);
            e.printStackTrace();
        }
    }
}
