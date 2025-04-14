import interfaces.BinaryGroupFinder;

import java.util.ArrayList;
import java.util.List;

import records.Coordinate;
import records.Group;

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
        // Loop through 2D array (image)
            // catch if is == 1
                // Helper method that returns the list of coordinates, change the 1's to *'s

                // Group group1 = new Group(helperMethod1(getArea), helperMethod(getCentroid))
                // add the group to that list of groups

        // Collections.sort(lists)
                    
        
        return null;
    }

    // getArea
    public static int getArea(List<Coordinate> coords){
        return coords.size();
    }
    
    // getCentroid
    public static Coordinate getCentroid(List<Coordinate> coords){
        // total x
        // total y

        // loop through coords
            // add to x
            // add to y
        
        // average x = sum of X / coords.size()

        // average y = sum of Y / coords.size()

        // return new Coordinate(average x, average y)
    }

    // getCoordinates
    public static
}
