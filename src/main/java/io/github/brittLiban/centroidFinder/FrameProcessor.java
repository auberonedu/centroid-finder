package io.github.brittLiban.centroidFinder;

import java.awt.image.BufferedImage;

public interface FrameProcessor {
    FrameResult processFrame(BufferedImage frame, int frameIndex);
}
