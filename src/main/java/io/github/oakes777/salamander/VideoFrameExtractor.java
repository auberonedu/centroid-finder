package io.github.oakes777.salamander;

// Import necessary classes from JCodec and Java standard libraries
import java.awt.image.BufferedImage; // Standard Java class for storing image data
import java.io.File; // Used to access the video file on disk
import java.io.IOException; // Handles input/output-related exceptions
import java.util.Iterator; // Used to support iteration over video frames
import java.util.NoSuchElementException; // Thrown when trying to access non-existent elements in an iterator

import org.jcodec.api.FrameGrab; // JCodec class that allows frame-by-frame access to a video
import org.jcodec.api.JCodecException; // Exception thrown by JCodec-specific operations
import org.jcodec.common.io.NIOUtils; // Utility class for creating file channels (used with FrameGrab)
import org.jcodec.common.io.SeekableByteChannel; // Interface for reading from a file with random access
import org.jcodec.common.model.Picture; // JCodec's raw video frame representation (not yet a BufferedImage)
import org.jcodec.scale.AWTUtil; // Converts JCodec's Picture objects into BufferedImage objects

/**
 * This class extracts frames from a video file one by one.
 * Each frame is converted to a BufferedImage and paired with its timestamp in
 * seconds.
 * The class implements Iterable so you can loop through frames easily using
 * for-each.
 */
public class VideoFrameExtractor implements Iterable<VideoFrame>, AutoCloseable {

    // JCodec object for grabbing video frames
    private final FrameGrab grab;

    // Frame rate calculated from the video metadata
    private final double frameRate;

    // Keeps track of which frame number we're on (used for calculating timestamp)
    private int frameNumber = 0;

    // Store the video channel so we can close it later during cleanup
    private final SeekableByteChannel channel;

    /**
     * Constructor: initializes the FrameGrab object with the input video path.
     * Also calculates the frame rate from the metadata.
     *
     * @param videoPath path to the .mp4 video file
     * @throws IOException if the file cannot be read
     */
    public VideoFrameExtractor(String videoPath) throws IOException, JCodecException {
        // Open a file channel for reading the video file
        this.channel = NIOUtils.readableChannel(new File(videoPath));

        // Create the frame grabber from the channel
        this.grab = FrameGrab.createFrameGrab(channel);

        // Compute frame rate as totalFrames / totalDuration (in seconds)
        this.frameRate = grab.getVideoTrack().getMeta().getTotalFrames()
                / grab.getVideoTrack().getMeta().getTotalDuration();
    }

    /**
     * Returns an iterator that lets us step through each frame of the video.
     * Each frame is returned as a VideoFrame (BufferedImage + timestamp).
     */
    @Override
    public Iterator<VideoFrame> iterator() {
        return new Iterator<>() {

            // This will hold the next frame once we load it
            private Picture nextPic = null;

            // Flag that tells us whether we've attempted to load the next frame already
            private boolean loaded = false;

            // Loads the next frame only if we haven't done it already
            private void loadNext() {
                if (!loaded) {
                    try {
                        nextPic = grab.getNativeFrame(); // Try to grab the next raw frame
                    } catch (IOException e) {
                        nextPic = null; // If error occurs, treat it as end of stream
                    }
                    loaded = true; // Mark that we've attempted to load
                }
            }

            /**
             * Returns true if there is another frame to read, false otherwise.
             */
            @Override
            public boolean hasNext() {
                loadNext(); // Attempt to load next frame
                return nextPic != null; // Return whether a frame is available
            }

            /**
             * Returns the next frame wrapped in a VideoFrame object.
             * Throws NoSuchElementException if no more frames are available.
             */
            @Override
            public VideoFrame next() {
                if (!hasNext()) {
                    throw new NoSuchElementException("No more frames available in the video.");
                }

                // Convert the raw Picture to a standard BufferedImage
                BufferedImage image = AWTUtil.toBufferedImage(nextPic);

                // Calculate the timestamp in seconds for the current frame
                double timestamp = frameNumber++ / frameRate;

                // Clear the loaded flag so we load a new frame next time
                loaded = false;

                // Return the frame and timestamp wrapped in a VideoFrame record
                return new VideoFrame(image, timestamp);
            }
        };
    }

    /**
     * Cleans up the video track when done (needed for file resource cleanup).
     */
    @Override
    public void close() throws Exception {
        channel.close(); // Close the video file properly
    }
}
