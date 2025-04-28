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

    private static final int[][] moves = {
        { -1, 0 }, // UP
        { 1, 0 }, // DOWN
        { 0, -1 }, // LEFT
        { 0, 1 } // RIGHT
    };
    @Override
    public List<Group> findConnectedGroups(int[][] image) {
        if (image == null) throw new NullPointerException("The image can't be null.");
        

        for (int[] row : image) {
            if (row == null)throw new NullPointerException("Rows can't be null.");
            
        }
        int height = image.length;
        int width = image[0].length; 
        boolean[][] visited = new boolean[height][width];
        List<Group> groups = new ArrayList<>();


        for(int r = 0; r < height; r++) {
            for(int c = 0; c < width; c++) {
                if(image[r][c] == 1 && !visited[r][c]){
                List<Coordinate> pixelGroup = new ArrayList<>();

                findConnectedGroupsDFS(image, visited, r, c, pixelGroup);

                int size = pixelGroup.size();

                int totalX = 0;
                int totalY = 0;
                
                for (Coordinate pixel : pixelGroup) {
                    
                    totalX += pixel.x();
                    totalY += pixel.y();

                    }    
                    
                int centroidX = totalX / size;
                int centroidY = totalY / size;

                groups.add(new Group(size, new Coordinate(centroidX, centroidY)));
                }
            }
        }
        //to return in decending order 
        Collections.sort(groups, Collections.reverseOrder());
        return groups;
    }

    private void findConnectedGroupsDFS(int[][] image, boolean[][] visited, int row, int col, List<Coordinate> pixelGroup){
        
        int height = image.length;
        int width = image[0].length; 
        
        if(row < 0 || row >= height || col < 0 || col >= width || image[row][col] == 0 || visited[row][col]) return;
        
        visited[row][col] = true;

        pixelGroup.add(new Coordinate(col, row));

        for (int[] direction : moves){
            int newRow = row + direction[0];
            int newCol = col + direction[1];
            findConnectedGroupsDFS(image, visited, newRow, newCol, pixelGroup);
        }
    }
}    
