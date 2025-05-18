package io.github.TiaMarieG.centroidFinder;

import org.bytedeco.javacv.FFmpegFrameGrabber;

public class FPSFinder {
    public double FPSReader(String videoPath) {
        double fps = 0.0;

        try (
            FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(videoPath);
        ) {
            grabber.start();
            fps = grabber.getFrameRate();
            grabber.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fps;
    }
}
