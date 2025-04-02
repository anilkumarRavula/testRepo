package com.ds.graphs;

/**
 * Given an m x n 2D binary grid grid which represents a map of '1's (land) and '0's (water), return the number of islands.
 *
 * An island is surrounded by water and is formed by connecting adjacent lands horizontally or vertically. You may assume all four edges of the grid are all surrounded by water.
 *
 *
 *
 * Example 1:
 *
 * Input: grid = [
 *   ["1","1","1","1","0"],
 *   ["1","1","0","1","0"],
 *   ["1","1","0","0","0"],
 *   ["0","0","0","0","0"]
 * ]
 * Output: 1
 * Example 2:
 *
 * Input: grid = [
 *   ["1","1","0","0","0"],
 *   ["1","1","0","0","0"],
 *   ["0","0","1","0","0"],
 *   ["0","0","0","1","1"]
 * ]
 * Output: 3
 */
public class NoOfIslands {
    public static void main(String[] args) {
        char[][] island = new char[][] {{'1','1','1','1','0'}
                ,{'1','1','0','1','0'}
                ,{'1','1','0','0','0'},
                {'0','0',0,'0','0'}};
        System.out.println(new Solution().numIslands(island));
        System.out.println(new Solution().numIslands(new char[][] {{'1','1','0','0','0'}
                ,{'1','1','0','1','0'}
                ,{'0','0','1','0','0'},
                {'0','0',0,'1','1'}}));
        System.out.println(new Solution().numIslands(new char[][] {{'1','0','1','1','1'}
                                                                  ,{'1','0','1','0','1'}
                                                                  ,{'1','1','1','0','1'}}));

    }

   static class Solution {
        public int numIslands(char[][] grid) {
            boolean[][] visited = new boolean[grid.length][grid[0].length];
            int noOfIslands = 0;

            for (int i = 0; i <grid.length ; i++) {
                for (int j = 0; j <grid[0].length ; j++) {
                    if(!visited[i][j]) {
                        if(grid[i][j] == '1') {
                            noOfIslands++;
                            connectAdjusentLand(i,j,visited,grid);
                        } else {
                            visited[i][j] = true;
                        }
                    }
                }
            }
            return noOfIslands;
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
