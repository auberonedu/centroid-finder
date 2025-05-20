package io.github.AugleBoBaugles.centroidFinder;

/* 
    Is a record that holds seconds, x, and y of the largest centroid in an image.

    seconds => number of seconds since the beginning of the video
    x => x coordinate of the centroid
    y => y coordinate of the centroid
 */

public record LargestCentroidRecord(int seconds, int x, int y) {
    
    public String toCsvRow() {
        return String.format("%d,%d,%d", this.seconds, this.x, this.y);
    }

}