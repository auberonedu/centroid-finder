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
    @Override
    public List<Group> findConnectedGroups(int[][] image) {
        if (image == null){
            throw new NullPointerException();
        }
        // create shallow copy of image array to keep track of visited pixels without editing original array
        // nested for loop to find start of each group
        // if image[r] is null, throw null pointer
        // if image[r][c] is not a 1 or a 0, throw invalid input
        // when group is found, perform dfs with that x, y as starting point
        // dfs will need to fill an array with the size of the group and a sum of the group's x and y coordinates
        // a new Group record will be created and added to the List using this info^
        // sort list and return it
        return null;
    }

    private void dfs(int[][] image, int y, int x, int[] groupInfo){
        // if y is out of bound or x is out of bounds return
        // if image[y][x] = 0 return

        // create 2d array of direction options

        // groupInfo[0] += 1
        // groupInfo[1] += x
        // groupInfo[2] += y

        // set current pixel to 0 
        // for each direction, perform recursion

    }
    
}
