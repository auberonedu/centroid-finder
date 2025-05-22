package io.github.brittLiban.centroidFinder;

public class FrameResult {
    private final int frameIndex;
    private final int x;
    private final int y;

    public FrameResult(int frameIndex, int x, int y) {
        this.frameIndex = frameIndex;
        this.x = x;
        this.y = y;
    }

    public String toCsvRow() {
        return frameIndex + "," + (x >= 0 ? x : "NA") + "," + (y >= 0 ? y : "NA");
    }
}