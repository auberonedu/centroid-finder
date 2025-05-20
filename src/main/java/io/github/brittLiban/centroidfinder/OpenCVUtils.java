package io.github.brittLiban.centroidfinder;

import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class OpenCVUtils {

    /**
     * Converts an OpenCV Mat object to a BufferedImage using JPEG compression.
     * 
     * @param mat The OpenCV Mat image.
     * @return A BufferedImage or null if conversion fails.
     */
    public static BufferedImage convertMatToBufferedImage(Mat mat) {
        try {
            MatOfByte matOfByte = new MatOfByte();
            Imgcodecs.imencode(".jpg", mat, matOfByte);
            byte[] byteArray = matOfByte.toArray();
            InputStream in = new ByteArrayInputStream(byteArray);
            return ImageIO.read(in);
        } catch (Exception e) {
            System.err.println("Failed to convert Mat to BufferedImage: " + e.getMessage());
            return null;
        }
    }
}