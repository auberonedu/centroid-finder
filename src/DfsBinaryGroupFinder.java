import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class DfsBinaryGroupFinder implements BinaryGroupFinder {
    /**
     * Finds connected pixel groups of 1s in an integer array representing a binary
     * image.
     * 
     * The input is a non-empty rectangular 2D array containing only 1s and 0s.
    //  * NEED TO DO
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

        if (image == null) throw new NullPointerException();
        if (image.length == 0 || image[0] == null || image[0].length == 0) {
            throw new IllegalArgumentException();
        }

        boolean[][] visited = new boolean[image.length][image[0].length];
        List<Group> groups = new ArrayList<>();

        for (int r = 0; r < image.length; r++) {
            for (int c = 0; c < image[0].length; c++) {
                if (image[r][c] == 1 && !visited[r][c]) {
                    // dfs to grab the whole blob
                    List<int[]> blob = findConnectedGroups(image, new int[]{r, c}, visited);
                    int size  = blob.size();
                    int sumX  = 0;
                    int sumY  = 0;
                    // p[0] = row (y), p[1] = col (x)
                    for (int[] p : blob) {
                    sumY += p[0];
                    sumX += p[1];
                    }

                    Coordinate centroid = new Coordinate(sumX / size, sumY / size); 
                    groups.add(new Group(size, centroid));                     
               }
            }
        }


        //is this the part that sorts everything? - Liban
        Collections.sort(groups, Collections.reverseOrder());
 
        return groups;
    }
    // helper to find the four neighbour 1‑pixels of current
    public static List<int[]> connectedOnes(int[][] image, int[] current) {
        int curR = current[0], curC = current[1];
        List<int[]> connected = new ArrayList<>();

        // UP
        int newR = curR - 1, newC = curC;
        if (newR >= 0 && image[newR][newC] == 1) connected.add(new int[]{newR, newC});

        // DOWN
        newR = curR + 1; newC = curC;
        if (newR < image.length && image[newR][newC] == 1) connected.add(new int[]{newR, newC});

        // LEFT
        newR = curR; newC = curC - 1;
        if (newC >= 0 && image[newR][newC] == 1) connected.add(new int[]{newR, newC});

        // RIGHT
        newR = curR; newC = curC + 1;
        if (newC < image[0].length && image[newR][newC] == 1) connected.add(new int[]{newR, newC});

        return connected;
    }
       //depth first search gather one whole blob of 1‑pixels

       // changing this to a bfs using queue
      
       
       public static List<int[]> findConnectedGroups(int[][] image, int[] start, boolean[][] visited) {
           List<int[]> result = new ArrayList<>();
           // a queue in a bfs is a linkedlist like the example, always made like this? 
           Queue<int[]> queue = new LinkedList<>();
       
           int startR = start[0];
           int startC = start[1];
       
           //marking the pixel as already visited so you don't visit it again 
           visited[startR][startC] = true;

           //feeding something into the queue for the first time
           queue.add(new int[]{startR, startC});
       
           while (!queue.isEmpty()) {
            //popping off the the first thing in the queue to get a current -- looks like {x, y}
               int[] current = queue.poll();
               int curR = current[0];
               int curC = current[1];

               //adding the current into the results array to return later. 
               result.add(new int[]{curR, curC}); // snapshot of current
       
               for (int[] neighbor : connectedOnes(image, current)) {
                    // a loop that will check with all the of the neighbors of the current location
                   int r = neighbor[0];
                   int c = neighbor[1];
                   if (!visited[r][c]) {
                       visited[r][c] = true;
                       //if it isn't been visited - make it so it has been first, then add it to the queue.
                       queue.add(new int[]{r, c});
                   }
               }
           }
       
           return result;
       }
       
}