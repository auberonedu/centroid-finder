package main.java;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DfsBinaryGroupFinder implements BinaryGroupFinder {
    /**
     * Finds connected pixel groups of 1s in an integer array representing a binary
     * image.
     * 
     * The input is a non-empty rectangular 2D array containing only 1s and 0s.
     * If the array or any of its subarrays are null, a NullPointerException
     * is thrown. If the array is otherwise invalid, an IllegalArgumentException
     * is thrown.
     *
     * Pixels are considered connected vertically and horizontally, NOT diagonally.
     * The top-left cell of the array (row:0, column:0) is considered to be
     * coordinate
     * (x:0, y:0). Y increases downward and X increases to the right. For example,
     * (row:4, column:7) corresponds to (x:7, y:4).
     *
     * The method returns a list of sorted groups. The group's size is the number
     * of pixels in the group. The centroid of the group
     * is computed as the average of each of the pixel locations across each
     * dimension.
     * For example, the x coordinate of the centroid is the sum of all the x
     * coordinates of the pixels in the group divided by the number of pixels in
     * that group.
     * Similarly, the y coordinate of the centroid is the sum of all the y
     * coordinates of the pixels in the group divided by the number of pixels in
     * that group.
     * The division should be done as INTEGER DIVISION.
     *
     * The groups are sorted in DESCENDING order according to Group's compareTo
     * method
     * (size first, then x, then y). That is, the largest group will be first, the
     * smallest group will be last, and ties will be broken first by descending
     * y value, then descending x value.
     * 
     * @param image a rectangular 2D array containing only 1s and 0s
     * @return the found groups of connected pixels in descending order
     */
    private static final int[][] CD = {
            { -1, 0 }, // UP
            { 1, 0 }, // DOWN
            { 0, -1 }, // LEFT
            { 0, 1 } // RIGHT
    };

    @Override
    public List<Group> findConnectedGroups(int[][] image) {
        //Edge cases - validate input image, rectangular shape
        if(image == null) throw new NullPointerException("Input image is null!");

        int height = image.length;
        //if image has no rows, return empty group list
        if(height == 0) return new ArrayList<>();

        //set 'width' as the first row length to compare to
        //this 2D array is made up of successive rows, so an enhanced
        //for-each loop will iterate through each row in order top 
        //to bottom
        int width = image[0].length;
        for (int[] row : image) {
            if(row == null) throw new NullPointerException("One or more rows are null!");
            if(row.length != width) throw new IllegalArgumentException("Image is not rectangular and must be!");
        }
        //initialize data structures to avoid looping
        //and to track the groups that we find
        boolean[][] visited = new boolean[height][width];
        List<Group> groups = new ArrayList<>();

        //iterate 2D matrix to scan each pixel
        //nested for loops
        for(int row = 0; row < height; row++) {
            for(int col = 0; col < width; col++) {
                //inside-iteration edge case
                //does this current cell contain a 1
                //and has visited not been here yet
                if(image[row][col] == 1 && !visited[row][col]){
                    //if edge cases true place this new
                    //cell coord into a List of
                    //Coordinates (that's a java record)
                    List<Coordinate> pixelsInGroup = new ArrayList<>();
                    //hand in image, visited, row, col, 
                    //pixelsInGroup to DFS helper method
                    dfs(image, visited, row, col, pixelsInGroup);
                    //pIG is a List of Coord that represents a Group
                    //we need to compute the Group size
                    int size = pixelsInGroup.size();
                    //now we can compute centroid
                    int sumX = 0;
                    int sumY = 0;
                    //for each iterate each Coord in pIG
                    for (Coordinate xy : pixelsInGroup) {
                        //x() and y() are Coord Record auto/innate methods
                        sumX += xy.x();
                        sumY += xy.y();
                    }
                    //centroid coordinate will be sumx/size and sumy/size
                    int centroidX = sumX/size;
                    int centroidY = sumY/size;
                    //create new Group object with size, location (centroidX, centroidY) and add to our List of Group which we called groups :^))
                    groups.add(new Group(size, new Coordinate(centroidX, centroidY)));
                }
            }
        }
        //sort groups by 1. size 2. x desc, 3. y desc
        //reverse the order in Integer.compare() to sort in descending order (largest size, x, y first)
        Collections.sort(groups, Collections.reverseOrder());
        //return our List of Group groups
        return groups;
    }

    private void dfs(int[][] image, boolean[][] visited, int row, int col, List<Coordinate> pixelsInGroup) {
        //define again our height and width
        int height = image.length;
        int width = image[0].length;
        //edge cases - don't go off grid UP/DOWN/LEFT/RIGHT
        //don't visit cell == 0 or already visited
        if(row < 0 || row >= height || col < 0 || col >= width || image[row][col] == 0 || visited[row][col]) return;
        //add this cell location to visited and pIG
        visited[row][col] = true;
        //Coordinate is x, y so we must flip row/col
        //to col, row for this nomenclature
        pixelsInGroup.add(new Coordinate(col, row));
        //recurse on dfs helper method all 4 cardinal
        //directions (CD!)
        for (int[] direction : CD) {
            //the fact that the y coord defines the rows
            //and the x coord defines the columns is what
            //has been flipping our minds
            //we define newY by adding direction[0] to ROW
            //we define newX by adding direction[1] to COL
            int newY = row + direction[0];
            int newX = col + direction[1];
            //now ... we recurse!!
            dfs(image, visited, newY, newX, pixelsInGroup);
        }
    }
}
