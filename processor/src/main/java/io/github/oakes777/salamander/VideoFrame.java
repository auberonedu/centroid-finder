package io.github.oakes777.salamander;

import java.awt.image.BufferedImage;

/**
 * Simple wrapper to hold a video frame and its timestamp (in seconds).
 */
public record VideoFrame(BufferedImage image, double timestampSeconds) {}
