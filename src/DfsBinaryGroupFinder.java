import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

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

        List<Group> groupList = new ArrayList<>();

        if (image == null) {
            throw new NullPointerException("Image array cannot be null");
        }

        if (image.length == 0 || image[0].length == 0) {
            throw new IllegalArgumentException("Image array cannot be empty");
        }

        for (int[] row : image) {
            if (row == null) {
                throw new IllegalArgumentException("Image array cannot contain null subarrays");
            }
        }
        boolean[][] visited = new boolean[image.length][image[0].length];

        for (int i = 0; i < image.length; i++) {
            for (int j = 0; j < image[i].length; j++) {
                if (image[i][j] == 1 && !visited[i][j]) {
                    List<int[]> pixels = findConnectedGroupsHelper(image, new int[]{i, j}, visited);
                    groupList.add(newGroup(pixels));
                }
            }
        }

        groupList.sort(Collections.reverseOrder());
        return groupList;
    }

    // findConnectedGroupsHelper 
    public List<int[]> findConnectedGroupsHelper(int[][] image, int[] current, boolean[][] visited) {
        int row = current[0];
        int col = current[1];

        if (row < 0 || row >= image.length || col < 0 || col >= image[0].length || image[row][col] == 0 || visited[row][col]) {
            return new ArrayList<>();
        }

        visited[row][col] = true;

        List<int[]> connectedPoints = new ArrayList<>();
        connectedPoints.add(new int[]{row, col});

        List<int[]> neighbors = getAdjacentPixels(image, current);

        for (int[] neighbor : neighbors) {
            connectedPoints.addAll(findConnectedGroupsHelper(image, neighbor, visited));
        }

        return connectedPoints;
    }

    // getAdjacentPixels
    public static List<int[]> getAdjacentPixels(int[][] image, int[] current) {
        List<int[]> validMoves = new ArrayList<>();

        int row = current[0];
        int col = current[1];
        
        int[][] directions = new int[][] {
            {-1, 0}, // up
            {1, 0},  // down
            {0, -1}, // left
            {0, 1}   // right
        };

        
        for (int[] direction : directions) {
            int newRow = row + direction[0];
            int newCol = col + direction[1];

            if (newRow >= 0 && newRow < image.length && newCol >= 0 && newCol < image[0].length && image[newRow][newCol] == 1) {
                validMoves.add(new int[]{newRow, newCol});
            }
        }

        return validMoves;
    }

    private Group newGroup(List<int[]> pixels) {
        int size = pixels.size();
        int sumX = 0;
        int sumY = 0;

        for (int[] pixel : pixels) {
            sumX += pixel[1];
            sumY += pixel[0];
        }

        int centroidX = sumX / size;
        int centroidY = sumY / size;

        return new Group(size, new Coordinate(centroidX, centroidY));
    }

}
