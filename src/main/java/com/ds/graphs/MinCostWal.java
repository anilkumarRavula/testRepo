package com.ds.graphs;

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


public class MinCostWal {
   static  int[][] travelCOsts = new int[][] {new int[] {9, 4, 9, 9},
            new int[] {6, 7, 6, 4},
            new int[] {8, 3, 3, 11},
            new int[] {7, 4, 9, 10}};
    static int destinationRow = 3;
    static int destinationPostion = 3;
    static  boolean[][] vistedPaths = new boolean[4][4] ;


    public static void main(String[] args) {
        System.out.println("Hello, World!");
        minimumCost(0,0,0);
    }

    private static int minimumCost(int row, int postion, int currentCost) {
        if(postion > 3 || postion < 0 || row < 0 || row > 3 || vistedPaths[row][postion]) {
            return 0;
        }
        if(postion == destinationPostion && row == destinationRow) {
            System.out.println("cost"+(currentCost+travelCOsts[row][postion]));
            return travelCOsts[row][postion];
        }
        vistedPaths[row][postion]=true;

        //
        int cost = currentCost + travelCOsts[row][postion];
        //recursively calcaulate cost each for each postion from
        //right

        int cost1 = minimumCost(row,postion+1,cost);
        //left

        int cost2 = minimumCost(row,postion-1,cost);
        //up
        int cost3 = minimumCost(row-1,postion,cost);
        //down
        int cost4 = minimumCost(row+1,postion,cost);

        cost+=Math.min(Math.min(cost1,cost2),Math.min(cost1,cost2));

        return cost;
    }
}