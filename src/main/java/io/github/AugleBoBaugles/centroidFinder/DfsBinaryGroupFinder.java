package io.github.AugleBoBaugles.centroidFinder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DfsBinaryGroupFinder implements BinaryGroupFinder {
   /**
    * Finds connected pixel groups of 1s in an integer array representing a binary image.
    * 
    * The input is a non-empty rectangular 2D array containing only 1s and 0s.
    * If the array or any of its subarrays are null, a NullPointerException
    * is thrown. If the array is otherwise invalid, an IllegalArgumentException
    * is thrown.
    *
    * Pixels are considered connected vertically and horizontally, NOT diagonally.
    * The top-left cell of the array (row:0, column:0) is considered to be coordinate
    * (x:0, y:0). Y increases downward and X increases to the right. For example,
    * (row:4, column:7) corresponds to (x:7, y:4).
    *
    * The method returns a list of sorted groups. The group's size is the number 
    * of pixels in the group. The centroid of the group
    * is computed as the average of each of the pixel locations across each dimension.
    * For example, the x coordinate of the centroid is the sum of all the x
    * coordinates of the pixels in the group divided by the number of pixels in that group.
    * Similarly, the y coordinate of the centroid is the sum of all the y
    * coordinates of the pixels in the group divided by the number of pixels in that group.
    * The division should be done as INTEGER DIVISION.
    *
    * The groups are sorted in DESCENDING order according to Group's compareTo method
    * (size first, then x, then y). That is, the largest group will be first, the 
    * smallest group will be last, and ties will be broken first by descending 
    * y value, then descending x value.
    * 
    * @param image a rectangular 2D array containing only 1s and 0s
    * @return the found groups of connected pixels in descending order
    */

    private static final int[][] directions = {
        {-1, 0}, // UP
        {1,  0}, // DOWN
        {0, -1}, // LEFT
        {0,  1} // RIGHT
    };

    @Override
    public List<Group> findConnectedGroups(int[][] image) {
       // If image sub-arrays is null or at 0 throw a NullPointerException
        if (image == null) throw new NullPointerException("Image not found or empty"); 
        if (image.length == 0 || image[0].length == 0) throw new IllegalArgumentException("Invalid image");
        
        // make List of Groups'
        List<Group> groupsList = new ArrayList<>();

        // Loop through 2D array (image) - for row
        for (int r = 0; r < image.length; r++){
            // if row is null, nullPointerException
            if (image[r] == null) throw new NullPointerException("Row cannot be null.");

            // for col
            for (int c = 0; c < image[0].length; c++){
                // catch if is == 1
                if (image[r][c] == 1){
                    // create groupCoordList -- Helper method that changes the list of coordinates to match the current group, change the 1's to *'s
                    List<Coordinate> groupCoordList = new ArrayList<>();
                    getCoordinates(image, r, c, groupCoordList);

                    // Group group1 = new Group(helperMethod1(getArea), helperMethod(getCentroid))
                    Group currentGroup = new Group(getArea(groupCoordList), getCentroid(groupCoordList));
                    
                    // add the group to that list of groups
                    groupsList.add(currentGroup);
                }
                    
            }
        }

        // sort the list of groups
        Collections.sort(groupsList, Collections.reverseOrder());
                    
        
        return groupsList;
    }

        /**
         * Recurses over connected groups of 1s and adds their coordinates to a list.
         * 
         * @param image a 2d array of 1s and 0s representing a binary image
         * @param row a row coordinate
         * @param col a column coordinate
         * @param groupCoordList a list of the coordinates of each 1 in a contiguous group
         */
        public static void getCoordinates(int[][] image, int row, int col, List<Coordinate> groupCoordList){
            // base case to avoid recursive doom -- is directions valid? is this a 1?
            if (row < 0 || row >= image.length ||
                col < 0 || col >= image[0].length 
                || image[row][col] != 1){
                    return;
            }
    
            // add new Coordinate(row, col) to groupCoordList
            Coordinate newCoord = new Coordinate(row, col);
            groupCoordList.add(newCoord);

            // change the 1 to 2 (stops infinite looping)
            image[row][col] = 2;
    
            // recurse over all directions
            for (var direction : directions){
                int newRow = direction[0] + row;
                int newCol = direction[1] + col;

                getCoordinates(image, newRow, newCol, groupCoordList);
            }
        }

    /**
     * Returns total area of a connected group of coordinates.
     * 
     * @param coords a list of all coordinates in a connected group of 1s
     * @return the total area
     */
    public static int getArea(List<Coordinate> coords){
        return coords.size();
    }
    
    /**
     * Returns a coordinate that is the at the center of a connected groups of 1s.
     * 
     * @param coords a list of all coordinates in a connected group of 1s
     * @return a coordinate where x is the average of all x coordinates in the group and y is the average of all y coordinates in the group.
     */
    public static Coordinate getCentroid(List<Coordinate> coords){
        int totalX = 0; // total x
        int totalY = 0; // total y

        // loop through coords
        for (Coordinate coord : coords) {
            totalX += coord.x(); // add to x
            totalY += coord.y(); // add to y
        }
        
        // average x = sum of X / coords.size()
        int avgX = totalX / coords.size();
        // average y = sum of Y / coords.size()
        int avgY = totalY / coords.size();
        
        // return new Coordinate(average x, average y)
        return new Coordinate(avgX, avgY);
    }


}
