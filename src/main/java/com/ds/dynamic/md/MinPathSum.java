package com.ds.dynamic.md;

import java.util.Arrays;

/**
 * Given a m x n grid filled with non-negative numbers, find a path from top left to bottom right, which minimizes the sum of all numbers along its path.
 *
 * Note: You can only move either down or right at any point in time.
 *
 *
 *
 * Example 1:
 *
 *
 * Input: grid = [[1,3,1],[1,5,1],[4,2,1]]
 * Output: 7
 * Explanation: Because the path 1 → 3 → 1 → 1 → 1 minimizes the sum.
 * Example 2:
 *
 * Input: grid = [[1,2,3],[4,5,6]]
 * Output: 12
 */
public class MinPathSum {
    public static void main(String[] args) {
        System.out.println(new OptSolution().minPathSum(new int[][]{{1,3,1}, {1,5,1}, {4,2,1}}));
        System.out.println(new OptSolution().minPathSum(new int[][]{{1,2,3},{4,5,6}}));

    }
   static class Solution {
        public int minPathSum(int[][] grid) {
            int[][] cloned =  new int[grid.length][grid[0].length];
            cloned[0][0] = grid[0][0];
            //calculate values for firstrow
            for(int col = 1; col < grid[0].length;col++) {
                cloned[0][col] = grid[0][col]+cloned[0][col-1];
            }
            //calculate values for col
            for(int row = 1; row < grid.length;row++) {
                cloned[row][0] = grid[row][0]+cloned[row-1][0];
            }
            for (int row = 1; row < grid.length; row ++)  {
                for(int col = 1; col < grid[0].length;col++) {
                    int current = grid[row][col];
                    cloned[row][col] = Math.min(cloned[row-1][col] + current, cloned[row][col-1] +current);
                }
            }
           // Arrays.stream(cloned).forEach(idx-> System.out.println(Arrays.toString(idx)));

            return cloned[grid.length-1][grid[0].length-1];
        }
    }
    static class OptSolution {
        public int minPathSum(int[][] grid) {
            for (int row = 0; row < grid.length; row ++)  {
                for(int col = 0; col < grid[0].length;col++) {
                    if(row == 0 && col == 0) continue;
                        grid[row][col] = Math.min(grid[Math.max(row-1,0)][col] + grid[row][col], grid[row][Math.max(col-1,0)] +grid[row][col]);

                }
            }
            Arrays.stream(grid).forEach(idx-> System.out.println(Arrays.toString(idx)));

            return grid[grid.length-1][grid[0].length-1];
        }
    }

}
