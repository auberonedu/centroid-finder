import java.util.ArrayList;
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
        if (image == null) {
            throw new NullPointerException("The image array cannot be null.");
        }

        for (int[] row : image) {
            if (row == null) {
                throw new NullPointerException("The subarrays cannot be null.");
            }
        }
        
        if (image.length == 0 || image[0].length == 0) {
            throw new IllegalArgumentException("The array is Invalid.");
        }

        int[] start = pixelStarterLocation(image);
        boolean[][] visited = new boolean[image.length][image[0].length];
        return findConnectedGroupsHelper(image, start, visited);
    }

    //findConnectedGroupsHelper
    private List<Group> findConnectedGroupsHelper(int[][] image, int[] current, boolean[][] visited) { 
        int row = current[0];
        int col = current[1];

        if (row < 0 || row >= image.length || col < 0 || col >= image[0].length || image[row][col] == 0 || visited[row][col]) {
            return new ArrayList<>();
        }

        // marking the current pixel as visited
        visited[row][col] = true;

        // list that holds the connected pixel groups
        List<int[]> connectedPixels = new ArrayList<>();
        connectedPixels.add(new int[]{row, col});

        List<int[]> moves = possibleDirections(image, current);


    }
    //pixelStarterLocation 
    private static int[] pixelStarterLocation(int[][] image) {
        for (int r = 0; r < image.length; r++) {
            for(int c = 0; c < image[0].length; c++) {
                if (image[r][c] == 0) {
                    return new int[]{r, c};
                }
            }
        }
        throw new IllegalArgumentException("No starting pixel is present.");
    }

    //possibleDirections 
    public static List<int[]> possibleDirections(int[][] image, int[] current) {
        int curR = current[0];
        int curC = current[1];
    
        List<int[]> moves = new ArrayList<>();
    
        // Directions array for up, down, left, and right
        int[][] directions = new int[][]{
            {-1, 0}, 
            {1, 0}, 
            {0, -1}, 
            {0, 1}   
        };

        return 0;
    }
    
}
