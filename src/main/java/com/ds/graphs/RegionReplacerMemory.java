package com.ds.graphs;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

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
 * Input: board = [["X","X","X","X"],
 * ["X","O","O","X"],
 * ["X","X","O","X"],
 * ["X","O","X","X"]]
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
public class RegionReplacerMemory {

    public static void main(String[] args) {
        new Solution().solve(new char[][] {{'X','X','X','X'}
                ,{'X','O','O','X'}
                ,{'X','X','O','X'},
                {'X','O','X','X'}});

    }

  static  class Solution {
        public void solve(char[][] grid) {
            Arrays.stream(grid).forEach(row->System.out.println(Arrays.toString(row)));
            Map<String,String> visited = new HashMap<>();
            for (int i = 0; i <grid.length ; i++) {
                for (int j = 0; j <grid[0].length ; j++) {
                    if(grid[i][j] == 'O') {
                        String position= getPosition(i, j);
                        if(visited.containsKey(position)) {
                            updatePostion(i,j,position,grid,visited);
                        } else {
                            visited.put(position,null);
                            connectAdjusentLand(i,j,visited,grid,position);
                        }

                    }
                }
            }
            System.out.println("--");
            Arrays.stream(grid).forEach(row->System.out.println(Arrays.toString(row)));

        }

      private static String getPosition(int i, int j) {
          return i + "," + j;
      }

      private void updatePostion(int i, int j,String position, char[][] grid, Map<String,String> visited) {
          grid[i][j] = visited.getOrDefault(visited.get(position),"O").toCharArray()[0];
      }

      private boolean connectAdjusentLand(int i, int j, Map<String,String> visited,char[][] grid,String parent) {


          String position = getPosition(i,j);

          System.out.println(position + "--"+parent);
          if( i >= grid.length || j >= grid[0].length || i<0 || j <0) {
              return false;
          }


          if(grid[i][j] == 'O' && (j >= grid[0].length-1 || j==0 || i ==0 || i >= grid.length-1)) {
              return false;
          }
          if(grid[i][j] == 'X' || (grid[i][j] == 'O' && !parent.equals(position) && visited.containsKey(position)) ) {
              return true;
          }

          if(visited.get(parent) != null) {
              visited.put(position,parent);
          }

          //right
          boolean result =  connectAdjusentLand(i,j+1,visited,grid,parent) &&
          //down
          connectAdjusentLand(i+1,j,visited,grid,parent) &&
          //left
          connectAdjusentLand(i,j-1,visited,grid,parent) &&
          // up
          connectAdjusentLand(i-1,j,visited,grid,parent);

         if(result && position.equals(parent) && visited.get(parent) == null) {
             grid[i][j] = 'X';
             visited.put(parent,"X");
         }

         return result;
      }
    }

}
