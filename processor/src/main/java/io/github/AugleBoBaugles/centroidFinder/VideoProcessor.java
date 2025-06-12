package io.github.AugleBoBaugles.centroidFinder;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;

import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;

public class VideoProcessor {
    private String videoPath; 
    private String outputPath;
    private int targetColor;
    private int threshold;

    // Constructor
    public VideoProcessor(String videoPath, String outputPath, int targetColor, int threshhold) {
        this.videoPath = videoPath;
        this.outputPath = outputPath;
        this.targetColor = targetColor;
        this.threshold = threshhold;
    }

    // this is working now
    public void extractFrames() {
        try {

            // Get the video title from the path
            File video = new File(videoPath);
            String videoName = video.getName();
            int dotIndex = videoName.lastIndexOf('.');
            videoName = videoName.substring(0, dotIndex);

            // Path to the CSV output file
            String csvFilePath = outputPath + "/" + videoName + ".csv";

            // Create the file and initialize writer (false = overwrite if file exists)
            File csvFile = new File(csvFilePath);
            PrintWriter writer = new PrintWriter(new FileWriter(csvFile, false));

            // Write the CSV header: column names
            writer.println("time,x,y");

            // Process the video frames and write centroid data to CSV
            extractFrames(videoPath, writer);

            // Close the writer after all data is written
            writer.close();
            System.out.println("Finished writing CSV to: " + csvFilePath);
        } catch (Exception e) {
            e.printStackTrace(); // Print any exceptions encountered
        }
    }

    // Loop through whole video, at each fps increment, run frameToData on frame
    private void extractFrames(String videoPath, PrintWriter writer) throws Exception {
        
        FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(videoPath); // Create a video reader
        grabber.start(); //Start reading in the video

        // Converting the Frame to a Buffered Image
        Java2DFrameConverter converter = new Java2DFrameConverter(); 

        int frameNumber = 0;  // Counter for how many frames have been read.
        int secondsProcessed = 0; // How many full seconds we've processed
        double fps = grabber.getFrameRate(); // Get the actual video frame rate
        
        Frame frame; // Hold each video frame as it's read (like a "visited")

        while ((frame = grabber.grabImage()) != null) {
            // Test one frame per second, we divide the frameNumber by the fps, which rounds to the current second
            if ((int)(frameNumber / fps) == secondsProcessed) { // check if the frame is at a whole second
                BufferedImage bufferedImage = converter.convert(frame); // Converting that frame to an image
                if (bufferedImage != null) {
                    // File outputFile = new File(outputDir, String.format("frame_%05d.png", frameNumber));
                    // ImageIO.write(bufferedImage, "png", outputFile);

                    // Process the image to find the centroid, and write the result to the CSV file
                    frameToData(bufferedImage, secondsProcessed, writer);
                    secondsProcessed++;  // Move to the next second of the video file.
                }
            }
            frameNumber++;  // Move to the next frame
        }

        grabber.stop(); // Stop reading the video file
        grabber.release(); // Release the system's resources
    }

    // frameToData method takes in framePath and passes that into a
    // LargestCentroid with color and threshold, add returned data to csv
    public void frameToData(BufferedImage frame, int seconds, PrintWriter writer){
        
        LargestCentroid currentCentroid = new LargestCentroid(frame, targetColor, threshold, seconds); // Create an instance of LargestCentroid to analyze the frame
        LargestCentroidRecord currentLargestCentroidRecord = currentCentroid.findLargestCentroid(); // Get the Result

        // Extract the X and the Y, if no group is found then use -1
        if (currentLargestCentroidRecord == null) {
            System.out.printf("Frame %d: No centroid found%n", seconds);
            writer.printf("%d,%d,%d%n", seconds, -1, -1); // Write one row to CSV: second, x, y
        } else {
            System.out.printf("Frame %d: Centroid at (%d, %d)%n", seconds, currentLargestCentroidRecord.x(), currentLargestCentroidRecord.y());
            writer.println(currentLargestCentroidRecord.toCsvRow()); 
        }
    }

}
