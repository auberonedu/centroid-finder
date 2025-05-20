import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.bytedeco.javacv.Frame;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

public class VideoFrameExtractor {
    public static void main(String[] args) {
        String videoPath = "video.mp4";

        try (FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(videoPath)) {
            grabber.start();

            // print data from the video using built in methods
            System.out.println("Frame Rate: " + grabber.getFrameRate());
            System.out.println("Resolution: " + grabber.getImageWidth() + "x" + grabber.getImageHeight());

            
            Frame frame = grabber.grabImage(); 
            if (frame != null) {
                Java2DFrameConverter converter = new Java2DFrameConverter();
                BufferedImage img = converter.convert(frame);

                // save first frame to file
                File output = new File("frame1.jpg");
                ImageIO.write(img, "jpg", output);
                System.out.println("Saved first frame as: " + output.getAbsolutePath());
            }

            grabber.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}