package io.github.f3liz.centroidFinder;

import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;

import java.awt.image.BufferedImage;

public class FrameToBufferedImageConverter {
    private final Java2DFrameConverter converter;

    public FrameToBufferedImageConverter() {
        this.converter = new Java2DFrameConverter();
    }

    public BufferedImage convert (Frame frame) {
        return converter.convert(frame);
    }
}
