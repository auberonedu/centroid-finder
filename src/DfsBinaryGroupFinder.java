import java.util.ArrayList;
import java.util.List;

public class DfsBinaryGroupFinder implements BinaryGroupFinder {
    private final int[][] directions = new int[][] {
            { -1, 0 }, // up
            { 1, 0 }, // down
            { 0, -1 }, // left
            { 0, 1 } // right
    };

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
        if (image == null) throw new NullPointerException("Null array or subarray");

        for(int[] row : image) {
            if(row == null) throw new NullPointerException("Null array or subarray");
        }
        List<Group> groups = new ArrayList<>();
        boolean[][] visited = new boolean[image.length][image[0].length];
        for (int r = 0; r < image.length; r++) {
            for (int c = 0; c < image[0].length; c++) {
                if (image[r][c] == 1 && !visited[r][c]) {
                    List<int[]> pixelatedGroup = new ArrayList<>();
                    findConnectedGroups(image, new int[] { r, c }, visited, pixelatedGroup);
                    Group group = new Group(pixelatedGroup.size(), null);
                    groups.add(group);
                }
            }
        }

        return groups;
    }

    private void findConnectedGroups(int[][] image, int[] location, boolean[][] visited, List<int[]> pixelatedGroup) {
        int curR = location[0];
        int curC = location[1];

        if (curR < 0 || curR >= image.length || curC < 0 || curC >= image[0].length)
            throw new IllegalArgumentException("Invalid x or y");

        if (visited[curR][curC] || image[curR][curC] == 0)
            return;

        visited[curR][curC] = true;

        pixelatedGroup.add(location);

        for (int[] direction : directions) {
            int newR = curR + direction[0];
            int newC = curC + direction[1];

            if (newR >= 0 && newR < image.length && newC >= 0 && newC < image[0].length) {
                findConnectedGroups(image, new int[] { newR, newC}, visited, pixelatedGroup);
            }

        }
    }
}
