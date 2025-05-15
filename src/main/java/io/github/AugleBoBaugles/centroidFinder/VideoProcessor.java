package io.github.AugleBoBaugles.centroidFinder;

import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

public class VideoProcessor {

    public static void extractFrames(String videoPath, String outputDir) throws Exception {
        FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(videoPath);
        grabber.start();

        Java2DFrameConverter converter = new Java2DFrameConverter();

        int frameNumber = 0;
        Frame frame;

        while ((frame = grabber.grabImage()) != null) {
            BufferedImage bufferedImage = converter.convert(frame);
            if (bufferedImage != null) {
                File outputFile = new File(outputDir, String.format("frame_%05d.png", frameNumber++));
                ImageIO.write(bufferedImage, "png", outputFile);
            }
        }

        grabber.stop();
        grabber.release();
        System.out.println("Done: Extracted " + frameNumber + " frames.");
    }

    public static void main(String[] args) {
        try {
            String videoPath = "sampleInput/sample_video_1.mp4"; // adjust as needed
            String outputDir = "sampleOutput"; // ensure this directory exists or create it
            new File(outputDir).mkdirs();
            extractFrames(videoPath, outputDir);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
}
