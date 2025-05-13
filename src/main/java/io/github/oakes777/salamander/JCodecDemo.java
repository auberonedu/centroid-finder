package io.github.oakes777.salamander;

import java.io.File;
import java.io.IOException;

import org.jcodec.api.FrameGrab;
import org.jcodec.api.JCodecException;
import org.jcodec.common.io.NIOUtils;
import org.jcodec.common.io.SeekableByteChannel;
import org.jcodec.common.model.Picture;

public class JCodecDemo {
    public static void main(String[] args) throws IOException, JCodecException {
        if (args.length < 1) {
            System.out.println("Usage: java JCodecDemo <video_file>");
            return;
        }

        File videoFile = new File(args[0]);
        SeekableByteChannel channel = null;

        try {
            channel = NIOUtils.readableChannel(videoFile);
            FrameGrab grab = FrameGrab.createFrameGrab(channel);

            int frameNumber = 0;
            Picture picture;
            while ((picture = grab.getNativeFrame()) != null && frameNumber < 5) {
                System.out.println("Frame " + frameNumber +
                        " | Width: " + picture.getWidth() +
                        " | Height: " + picture.getHeight() +
                        " | Format: " + picture.getColor());
                frameNumber++;
            }

        } finally {
            if (channel != null) {
                channel.close();
            }
        }
    }
}
