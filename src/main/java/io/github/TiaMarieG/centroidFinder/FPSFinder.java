package io.github.TiaMarieG.centroidFinder;

import java.nio.file.Paths;

import org.bytedeco.javacv.FFmpegFrameGrabber;

public class FPSFinder {

    /*
     * Exports the framerate per second of an mp4 file.
     * Prints the complete stack trace if there is an error.
     * 
     * @param videoPath String value of the path of the given video
     * 
     */
    public double FPSReader(String videoPath) {
        double fps = 0.0;
        // Using getFrameRate to return teh videos frames per second
        try (FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(videoPath)) {
            grabber.start();
            fps = grabber.getFrameRate();
            grabber.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fps;
    }

    /*
     * Exports the total frames a mp4 file has.
     * Prints the complete stack trace if there is an error.
     * 
     * @param videoPath String value of the path of the given video
     * 
     */
    public int totalFramesOfVideo(String videoPath) {
        int totalFrames = 0;

        // Using getLengthInFrames to access the amount of frames in the file
        try (FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(videoPath)) {
            grabber.start();
            totalFrames = grabber.getLengthInFrames();
            grabber.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return totalFrames;
    }

    public static void main(String[] args) {
        String videoPath = Paths.get("videos", "nyan-cat.mp4").toString();

        try (FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(videoPath)) {
            grabber.start();

            System.out.println("Frame Rate: " + grabber.getFrameRate());
            System.out.println("Total Frames: " + grabber.getLengthInFrames());
            System.out.println("Duration (ms): " + grabber.getLengthInTime() / 1000);
            System.out.println("Format: " + grabber.getFormat());
            System.out.println("Video Codec: " + grabber.getVideoCodecName());
            System.out.println("Audio Channels: " + grabber.getAudioChannels());
            System.out.println("Sample Rate: " + grabber.getSampleRate());

            grabber.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    

}
