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
    @Override
    public List<Group> findConnectedGroups(int[][] image) {
        
        // Saving rows/columns to variables
        int rows = image.length;
        int cols = image[0].length;
        
        // Check for a null or empty array
        if (image == null || image[0] == null || rows == 0 || cols == 0) {
            throw new NullPointerException("Provided array is null or empty");
        }

        // Check for a non-rectangular/invalid array - This is based on the
        // testNonRectangularGrid() that AI created
        for (int i = 1; i < rows; i++) {
            if (image[i].length != cols) {
                throw new IllegalArgumentException("Provided array is not rectangular");
            }
        }

        // Check for a non-binary array - This is based on the
        // testGridWithInvalidValues() that AI created
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < image[i].length; j++) {
                if (image[i][j] != 0 && image[i][j] != 1) {
                    throw new IllegalArgumentException("Provided array contains non-binary values");
                }
            }
        }

        // Creating a boolean array to track visited cells
        boolean[][] visited = new boolean[rows][cols];

        // List to store the groups found since this method needs to return a list
        List<Group> groups = new ArrayList<>();

        // Looping through the image to find groups
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (image[r][c] == 1 && !visited[r][c]) {
                    Group group = findPixelGroups(image, visited, r, c);
                    groups.add(group);
                }
            }
        }

        // Sorting the groups in descending order
        groups.sort(Collections.reverseOrder());
        
        return groups;
    }

    // Helper method to find connected groups
    public Group findPixelGroups(int[][] image, boolean[][] visited, int row, int col) {

        // List to store group pixel coordinates
        List<int[]> pixelCoordinates = new ArrayList<>();

        // DFS/Recursion to find all connected pixels
        pixelGroupTraversal(image, visited, row, col, pixelCoordinates);

        int groupSize = pixelCoordinates.size();

        // Initiating variables to be used to calculate sum of x/y coordinates
        int xCoordSum = 0;
        int yCoordSum = 0;

        for (int[] pixelCoord : pixelCoordinates) {
            xCoordSum += pixelCoord[1];
            yCoordSum += pixelCoord[0];
        }

        // Math to find the centroid
        int xCentroid = xCoordSum / groupSize;
        int yCentroid = yCoordSum / groupSize;

        // Create a new group and return it
        return new Group(groupSize, new Coordinate(xCentroid, yCentroid));
    }

    // Helper method for movement through connected pixels
    private static void pixelGroupTraversal(int[][] image, boolean[][] visited, int row, int col, List<int[]> pixelGroups) {
        int[][] directions = new int[][] 
        {
                { -1, 0 },
                { 1, 0 },
                { 0, -1 },
                { 0, 1 }
        };

        visited[row][col] = true;
        pixelGroups.add(new int[]{row, col});

        for (int[] direction : directions) {
            int newRow = row + direction[0];
            int newCol = col + direction[1];
            
            // If the cell row is within bounds
            if (newRow >= 0 && newRow < image.length &&
            // If the cell col is within bounds
            newCol >= 0 && newCol < image[0].length &&
            // If the cell contains a 1
            image[newRow][newCol] == 1 && !visited[newRow][newCol])
            {
                pixelGroupTraversal(image, visited, newRow, newCol, pixelGroups);
            }
        }
    }
}
