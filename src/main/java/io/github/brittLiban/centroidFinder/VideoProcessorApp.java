package io.github.brittLiban.centroidFinder;

import java.io.PrintWriter;

import org.bytedeco.javacv.FFmpegFrameGrabber;

public class VideoProcessorApp {
    public static void main(String[] args) {
        // taking in the command args
        if (args.length < 4) {
            System.out.println("Usage: java VideoProcessorApp inputPath outputCsv targetColor threshold");
            return;
        }

        

        // reading in the args
        String inputVideoPath = args[0]; 
        String outPutCsv = args[1]; 
        String hexTargetColor = args[2]; 
        int threshold = 0;

        try {
            threshold = Integer.parseInt(args[3]);
        } catch (NumberFormatException e) {
            System.err.println("Threshold must be an integer.");
            return;
        }

        // Parse the target color from a hex string (format RRGGBB) into a 24-bit
        // integer (0xRRGGBB)
        int targetColor = 0;
        try {
            targetColor = Integer.parseInt(hexTargetColor, 16);
        } catch (NumberFormatException e) {
            System.out.println("Hex Color Argument Received: '" + hexTargetColor + "'");
            System.err.println("Invalid hex target color. Please provide a color in RRGGBB format.");
            return;
        }

        // Where all the OOP stuff happens, makes no sense ahahhahahahaha

        // me telling javaCV that I want to read each frame of the video 1 by 1
        //getting rid of the pixel formating deprictation message that appeared on my console for each frame...
        org.bytedeco.ffmpeg.global.avutil.av_log_set_level(org.bytedeco.ffmpeg.global.avutil.AV_LOG_QUIET);
        FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(inputVideoPath);

        // setting up a TOOL that will measure HOW CLOSE EACH PIXEL is to the target by
        // using the threshold
        // This is harmless and does nothing on its own. Were just initializing the tool
        // to use later
        ColorDistanceFinder distanceFinder = new EuclideanColorDistance();

        // This is setting up a ANOTHER tool based on our parameters to make a image 1
        // and 0s
        // It is just a tool with some bad itentions, it is harmless on its own and can
        // only be applies to an image or something... (spoiler)
        ImageBinarizer binarizer = new DistanceImageBinarizer(distanceFinder, targetColor, threshold);

        // This takes in the binarizer above, and then it frinds groups of 1's (using
        // the dfsBinaryGroupFinder)
        // and it figures out which pixels are connected and forms blobs!
        ImageGroupFinder groupFinder = new BinarizingImageGroupFinder(binarizer, new DfsBinaryGroupFinder());

        // this then takes one single frame, uses the groupFinder method to find groups
        // and will return
        // the X and Y for the largest centroid PER FRAME
        FrameProcessor processor = new LargestBlobProcessor(groupFinder);

        // me initializing the VideoAnalyzing tool so that we can use its process
        // ability on each frame
        // Here’s the thing that loops through frames and uses all the tools above.
        // so, the processor is what we defined above. It is a collection of all the
        // other componets
        // being actively put together so it can analyze our 1 frame. That is what we
        // are passing in as
        // a parameter here.

        // VidoeAnalyzing is the tool that is going to run through the video and then
        // peel off a
        // single frame from the video

        // the PROCESSOR that we assembled is what is going to be ran ON THAT FRAME

        // this is nice because we can put together many different processors one day
        // that are different than this

        // although why are we make a video and not just analyzing directly with
        // processor?
        VideoAnalyzing video = new VideoAnalyzing(processor);

        try (PrintWriter writer = new PrintWriter(outPutCsv)) {
            grabber.start();
            video.process(grabber, writer); // starts processing frame-by-frame processing
            grabber.stop();
            writer.flush();
        } catch (Exception e) {
            System.err.println("Failed to process video:");
            e.printStackTrace();
        }

    }// end of main
}
