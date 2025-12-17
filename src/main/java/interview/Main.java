package interview;

/**
 *Wal
 * Given 2D grid of size 4x4, where each cell represents the cost to travel from it,  find the minimum cost to move from top left cell to bottom right cell.
 * Constraints - 4 direction movement is allowed. Left, right, up , down
 * Output - print the path
 *
 *      {   0
 *     0    {9, 4, 9, 9},
 *         {6, 7, 6, 4},
 *         {8, 3, 3, 11},
 *         {7, 4, 9, 10}
 *      }
 *
 *  //dfs
 *  //backtrack approach  wh
 *
 *
 */

import java.util.LinkedList;
import java.util.Queue;

/**
 * Given a square grid of size N, each cell of which contains an integer cost that represents a cost to traverse through that cell, we need to find a path from the top left cell to the bottom right cell by which the total cost incurred is minimum.
 * From the cell (i,j) we can go (i,j-1), (i, j+1), (i-1, j), (i+1, j).
 *
 * Examples :
 *
 * Input: grid = {{9,4,9,9},{6,7,6,4},{8,3,3,7},{7,4,9,10}}
 * Output: 43
 * Explanation: The grid is-
 * 9 4 9 9
 * 6 7 6 4
 * 8 3 3 7
 * 7 4 9 10
 * The minimum cost is-
 * 9 + 4 + 7 + 3 + 3 + 7 + 10 = 43.
 * Input: grid = {{4,4},{3,7}}
 * Output: 14
 * Explanation: The grid is-
 * 4 4
 * 3 7
 * The minimum cost is- 4 + 3 + 7 = 14.
 * Expected Time Complexity: O(n2*log(n))
 * Expected Auxiliary Space: O(n2)
 *  Constraints:
 * 1 ≤ n ≤ 500
 * 1 ≤ cost of cells ≤ 500
 */

public class Main {
    static int[][] travelCOsts = new int[][]{new int[]{9, 4, 9, 9},
            new int[]{6, 7, 6, 4},
            new int[]{8, 3, 3, 11},
            new int[]{7, 4, 9, 10}};
    static int destinationRow = 3;
    static int destinationPostion = 3;
    static int[][] vistedPaths = new int[4][4];


    public static void main(String[] args) {
        System.out.println("Hello, World!");
    }

    class Solution {

        //Function to return the minimum cost to react at bottom
        //right cell from top left cell.
        public int minimumCostPath(int[][] grid) {
            // Code here
           // boolean[][] visited = new boolean[grid.length][grid[0].length];
            int[][] costGrid = new int [grid.length][grid[0].length];
            Queue<int[]> linkedList = new LinkedList<>();
            linkedList.add(new int[]{0,0});
            costGrid[0][0] = grid[0][1];
            while(!linkedList.isEmpty()) {
                int[] index = linkedList.poll();

                int inputCOst = grid[index[0]][index[1]];

                int currentCOst = costGrid[index[0]][index[1]];


            }
            return 0;
        }


    }
}
