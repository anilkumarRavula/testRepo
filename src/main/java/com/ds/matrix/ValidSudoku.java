package com.ds.matrix;

import java.util.*;

/**
 * Determine if a 9 x 9 Sudoku board is valid. Only the filled cells need to be validated according to the following rules:
 *
 * Each row must contain the digits 1-9 without repetition.
 * Each column must contain the digits 1-9 without repetition.
 * Each of the any nine 3 x 3 sub-boxes of the grid must contain the digits 1-9 without repetition.
 * Note:
 *
 * A Sudoku board (partially filled) could be valid but is not necessarily solvable.
 * Only the filled cells need to be validated according to the mentioned rules.
 *
 *
 * Example 1:
 *
 *
 * Input: board =
 * [["5","3",".",".","7",".",".",".","."]
 * ,["6",".",".","1","9","5",".",".","."]
 * ,[".","9","8",".",".",".",".","6","."]
 * ,["8",".",".",".","6",".",".",".","3"]
 * ,["4",".",".","8",".","3",".",".","1"]
 * ,["7",".",".",".","2",".",".",".","6"]
 * ,[".","6",".",".",".",".","2","8","."]
 * ,[".",".",".","4","1","9",".",".","5"]
 * ,[".",".",".",".","8",".",".","7","9"]]
 * Output: false
 *
 * [1,0] at postion 1,0 there are 2 8's
 * 6...988false
 * Example 2:
 *
 * Input: board =
 * [["8","3",".",".","7",".",".",".","."]
 * ,["6",".",".","1","9","5",".",".","."]
 * ,[".","9","8",".",".",".",".","6","."]
 * ,["8",".",".",".","6",".",".",".","3"]
 * ,["4",".",".","8",".","3",".",".","1"]
 * ,["7",".",".",".","2",".",".",".","6"]
 * ,[".","6",".",".",".",".","2","8","."]
 * ,[".",".",".","4","1","9",".",".","5"]
 * ,[".",".",".",".","8",".",".","7","9"]]
 * Output: false
 * Explanation: Same as Example 1, except with the 5 in the top left corner being modified to 8. Since there are two 8's in the top left 3x3 sub-box, it is invalid.
 *
 *
 * Constraints:
 *
 * board.length == 9
 * board[i].length == 9
 * board[i][j] is a digit 1-9 or '.'.s
 */
public class ValidSudoku {
    public static void main(String[] args) {

        char[][] board = {
                {'5','3','.','.','7','.','.','.','.'},
                {'6','.','.','1','9','5','.','.','.'},
                {'.','9','2','.','.','.','.','6','.'},
                {'8','.','.','.','6','.','.','.','3'},
                {'4','.','.','8','.','3','.','.','1'},
                {'7','.','.','.','.','.','.','.','6'},
                {'.','6','.','.','.','.','2','8','.'},
                {'.','.','.','4','1','9','.','.','5'},
                {'.','.','.','.','8','.','.','7','9'}
        };
        System.out.println(new Solution().isValidSudoku(board));
    }

    static class Solution {
        public boolean isValidSudoku(char[][] board) {

            return hasUniqueRows(board) &&
            hasUniqueCols(board) &&
            hasUniqueValuesSubMatrix(board);

        }

        private boolean hasUniqueValuesSubMatrix(char[][] board) {
            int startRow = 0;
            int startCol = 0;
            Set<Character> characters = new HashSet<>();
            // matrix 3
            int size = 3;
            //int[][] directions= new int[][] { new int[]{-1,-1},new int[]{+1,-1},new int[]{-1,+1},new int[]{+1,+1} };
            for (int i = startRow; i < board.length-1; i++) {
                System.out.println(characters);
                characters.clear();
                for (int j = startCol; j < board[0].length-1; j++) {
                    characters.clear();
                   // characters.add(board[i][j]);
                    System.out.println("["+i+","+j+"]");
                    for (int row = i; row <i+size ; row++) {
                        for (int col = j; col < j + size; col++) {
                            if (row < 0 || col < 0 || row >= board.length || col >= board[0].length) continue;
                            // System.out.println("["+row+","+col+"]");
                            char current = board[row][col];
                            System.out.print(current);
                            if (current != '.') {
                                if (characters.contains(current)) {
                                    return false;
                                }
                                characters.add(current);
                            }
                        }
                    }
                    System.out.println();
                }
            }

            return true;
        }

        private boolean hasUniqueCols(char[][] board) {
            Set<Character> characters = new HashSet<>();

            for (int i = 0; i < board[0].length; i++) {
                //System.out.println(characters);
                characters.clear();
                for (int j = 0; j < board.length; j++) {
                    if(board[j][i] != '.') {
                        if(characters.contains(board[j][i])) {
                            return false;
                        }
                        characters.add(board[j][i]);
                    }

                }
            }
            System.out.println("--true");
            return true;
        }

        /**
         *
         * [1 1 1]   0,0 --> 0,2
         *
         * [1 1 1]
         * [1 1 1]
         * @param board
         * @return
         */

        private boolean hasUniqueRows(char[][] board) {
            Set<Character> characters = new HashSet<>();
            //int j = 0; j < board.length; j++
            for (int i = 0; i < board.length; i++) {
               // System.out.println(characters);
                characters.clear();
                for (int j = 0; j < board[0].length; j++) {
                    if(board[i][j] != '.') {
                        if(characters.contains(board[i][j])) {
                            return false;
                        }
                        characters.add(board[i][j]);
                    }

                }
            }
            System.out.println("R--true");

            return true;
        }
    }
}
