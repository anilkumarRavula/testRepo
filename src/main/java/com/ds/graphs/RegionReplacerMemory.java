package com.ds.graphs;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * You are given an m x n matrix board containing letters 'X' and 'O', capture regions that are surrounded:
 * <p>
 * Connect: A cell is connected to adjacent cells horizontally or vertically.
 * Region: To form a region connect every 'O' cell.
 * Surround: The region is surrounded with 'X' cells if you can connect the region with 'X' cells and none of the region cells are on the edge of the board.
 * To capture a surrounded region, replace all 'O's with 'X's in-place within the original board. You do not need to return anything.
 * <p>
 * <p>
 * <p>
 * Example 1:
 * <p>
 * Input: board = [["X","X","X","X"],
 * ["X","O","O","X"],
 * ["X","X","O","X"],
 * ["X","O","X","X"]]
 * <p>
 * Output: [["X","X","X","X"],["X","X","X","X"],["X","X","X","X"],["X","O","X","X"]]
 * <p>
 * Explanation:
 * <p>
 * <p>
 * In the above diagram, the bottom region is not captured because it is on the edge of the board and cannot be surrounded.
 * <p>
 * Example 2:
 * <p>
 * Input: board = [["X"]]
 * <p>
 * Output: [["X"]]
 */
public class RegionReplacerMemory {

    public static void main(String[] args) {
/*
        new Solution().solve(new char[][]{{'X', 'X', 'X', 'X', 'X'},
                {'X', 'X', 'X', 'O', 'X'}
                , {'X', 'O', 'X', 'O', 'X'}
                , {'X', 'O', 'O', 'O', 'X'},
                {'O', 'X', 'X', 'X', 'X'}});
*/

        new Solution().solve(new char[][]{{'O','X','X','O','X'},{'X','O','O','X','O'},{'X','O','X','O','X'},{'O','X','O','O','O'},{'X','X','O','X','O'}});

    }

    static class Solution {
        public void solve(char[][] grid) {
            Arrays.stream(grid).forEach(row -> System.out.println(Arrays.toString(row)));
            Map<String, String> visited = new HashMap<>();
            for (int i = 0; i < grid.length; i++) {
                for (int j = 0; j < grid[0].length; j++) {
                    if (grid[i][j] == 'O') {
                        String position = getPosition(i, j);
                        if (visited.containsKey(position)) {
                            updatePostion(i, j, position, grid, visited);
                        } else {
                            visited.put(position, null);
                            connectAdjusentLand(i, j, visited, grid, position);
                        }

                    }
                }
            }
            System.out.println("--");
            Arrays.stream(grid).forEach(row -> System.out.println(Arrays.toString(row)));

        }

        private static String getPosition(int i, int j) {
            return i + "," + j;
        }

        private void updatePostion(int i, int j, String position, char[][] grid, Map<String, String> visited) {
            grid[i][j] = visited.getOrDefault(visited.getOrDefault(position, position), "O").toCharArray()[0];
        }

        private boolean connectAdjusentLand(int i, int j, Map<String, String> visited, char[][] grid, String parent) {


            String position = getPosition(i, j);

            System.out.println(position+"---"+parent);
            //boundra check
            if (i >= grid.length || j >= grid[0].length || i < 0 || j < 0) {
                return false;
            }
            //if it is X
            if (grid[i][j] == 'X' || (grid[i][j] == 'O' && visited.get(position) != null && visited.containsKey(position))) {
                return true;
            }

            if (!visited.containsKey(position)) {
                visited.put(position, parent);
            }
            if (position.equals(parent)) {
                visited.put(position, "O");

            }
            //right
            boolean result1 = connectAdjusentLand(i, j + 1, visited, grid, parent);
                    //down
            boolean result2 =        connectAdjusentLand(i + 1, j, visited, grid, parent);
                    //left
            boolean result3 =        connectAdjusentLand(i, j - 1, visited, grid, parent);
                    // up
            boolean result4 =        connectAdjusentLand(i - 1, j, visited, grid, parent);
            boolean result = result1 && result2 && result3 && result4;
            if (result && position.equals(parent)) {
                    grid[i][j] = 'X';
                    visited.put(parent, "X");
            }

            return result;
        }
    }

}
