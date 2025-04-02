package com.ds.graphs;

/**
 * You are given an m x n matrix board containing letters 'X' and 'O', capture regions that are surrounded:
 *
 * Connect: A cell is connected to adjacent cells horizontally or vertically.
 * Region: To form a region connect every 'O' cell.
 * Surround: The region is surrounded with 'X' cells if you can connect the region with 'X' cells and none of the region cells are on the edge of the board.
 * To capture a surrounded region, replace all 'O's with 'X's in-place within the original board. You do not need to return anything.
 *
 *
 *
 * Example 1:
 *
 * Input: board = [["X","X","X","X"],["X","O","O","X"],["X","X","O","X"],["X","O","X","X"]]
 *
 * Output: [["X","X","X","X"],["X","X","X","X"],["X","X","X","X"],["X","O","X","X"]]
 *
 * Explanation:
 *
 *
 * In the above diagram, the bottom region is not captured because it is on the edge of the board and cannot be surrounded.
 *
 * Example 2:
 *
 * Input: board = [["X"]]
 *
 * Output: [["X"]]
 */
public class RegionReplacer {

    public static void main(String[] args) {

    }

  static  class Solution {
        public void solve(char[][] grid) {
            boolean[][] visited = new boolean[grid.length][grid[0].length];
            for (int i = 0; i <grid.length ; i++) {
                for (int j = 0; j <grid[0].length ; j++) {
                    if(!visited[i][j]) {
                        if(grid[i][j] == '1') {
                            connectAdjusentLand(i,j,visited,grid);
                        } else {
                            visited[i][j] = true;
                        }
                    }
                }
            }
        }

      private void connectAdjusentLand(int i, int j, boolean[][] visited,char[][] grid) {


          if( i >= grid.length || j >= grid[0].length || i<0 || j <0 || grid[i][j] == '0' || visited[i][j])
              return;

          visited[i][j] = true;

          //right
          connectAdjusentLand(i,j+1,visited,grid);
          //down
          connectAdjusentLand(i+1,j,visited,grid);
          //left
          connectAdjusentLand(i,j-1,visited,grid);
          // up
          connectAdjusentLand(i-1,j,visited,grid);

      }
    }

}
