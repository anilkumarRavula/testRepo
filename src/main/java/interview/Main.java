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
       // minimumCost(0, 0);
    }

    private int minimumCost(int row, int postion, int currentCost) {
        if (postion > 3 || row > 3) return 0;
        return row;
    }
}

