package com.ds.matrix;

import java.util.HashSet;
import java.util.Set;

/**
 * Determine if a 9 x 9 Sudoku board is valid. Only the filled cells need to be validated according to the following rules:
 *
 * Each row must contain the digits 1-9 without repetition.
 * Each column must contain the digits 1-9 without repetition.
 * Each of the  nine 3 x 3 sub-boxes of the grid must contain the digits 1-9 without repetition.
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
 * Output: true
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
public class ValidSudokuFixedSet {
    public static void main(String[] args) {

        char[][] board = {
                {'5','3','.','.','7','.','.','.','.'},
                {'6','.','.','1','9','5','.','.','.'},
                {'.','9','8','.','.','.','.','6','.'},
                {'8','.','.','.','6','.','.','.','3'},
                {'4','.','.','8','.','3','.','.','1'},
                {'7','.','.','.','2','.','.','.','6'},
                {'.','6','.','.','.','.','2','8','.'},
                {'.','.','.','4','1','9','.','.','5'},
                {'.','.','.','.','8','.','.','7','9'}
        };
        System.out.println(new FatestSolution().isValidSudoku(board));
    }

    static class Solution {
        public boolean isValidSudoku(char[][] board) {

            return hasUniqueRows(board) &&
            hasUniqueCols(board) &&
            hasUniqueValuesSubMatrix(board);

        }

        private boolean hasUniqueValuesSubMatrix(char[][] board) {
            Set<Character> characters = new HashSet<>();
            // matrix 3
            int size = 3;
            for (int i = 0; i < board.length-1; i=i+3) {
                //System.out.println(characters);
                for (int j = 0; j < board[0].length-1; j=j+3) {
                    characters.clear();
                   // System.out.println("["+i+","+j+"]");
                    for (int row = i; row <i+size ; row++) {
                        for (int col = j; col < j + size; col++) {
                            char current = board[row][col];
                           // System.out.print(current);
                            if (current != '.') {
                                if (characters.contains(current)) {
                                    return false;
                                }
                                characters.add(current);
                            }
                        }
                    }
                   // System.out.println();
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
           // System.out.println("--true");
            return true;
        }
        private boolean hasUniqueRows(char[][] board) {
            Set<Character> characters = new HashSet<>();
            for (int i = 0; i < board.length; i++) {
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
            //System.out.println("R--true");

            return true;
        }
    }

    class SolutionSubmitted {
        public boolean isValidSudoku(char[][] board) {
            int row = board.length;
            int col = board[0].length;
            for(int i=0; i<row; i++){
                for(int j=0; j<col; j++){
                    if(board[i][j] != '.'){
                        char temp = board[i][j];
                        board[i][j] = '.';
                        if(!ischeck(board,i,j,temp)){
                            return false;
                        }
                        board[i][j] = temp;
                    }
                }
            }return true;
        }
        public boolean ischeck(char[][] board,int row,int col,char value){
            for(int k=0;k<board.length;k++){
                if(board[k][col] == value ){
                    return false;
                }
            }
            for(int k=0;k<board[0].length;k++){
                if(board[row][k] == value){
                    return false;
                }
            }
            int sr = (row/3)*3;
            int sc = (col/3)*3;
            for(int i=sr;i<sr+3;i++){
                for(int j=sc;j<sc+3;j++){
                    if(board[i][j] == value){
                        return false;
                    }
                }
            }return true;
        }
    }


    static class FatestSolution {
        public boolean isValidSudoku(char[][] board) {

            return hasUniqueRows(board) &&
                    hasUniqueCols(board) &&
                    hasUniqueValuesSubMatrix(board);

        }

        private boolean hasUniqueValuesSubMatrix(char[][] board) {
            // matrix 3
            int size = 3;
            for (int i = 0; i < board.length-1; i=i+3) {
                //System.out.println(characters);
                for (int j = 0; j < board[0].length-1; j=j+3) {
                    int[] intial= new int[10];
                    // System.out.println("["+i+","+j+"]");
                    for (int row = i; row <i+size ; row++) {
                        for (int col = j; col < j + size; col++) {
                            char current = board[row][col];
                            // System.out.print(current);
                            if (numberPresent(board, row, col, intial)) return false;

                        }
                    }
                    // System.out.println();
                }
            }

            return true;
        }

        private boolean hasUniqueCols(char[][] board) {

            for (int i = 0; i < board[0].length; i++) {
                //System.out.println(characters);
                int[] intial= new int[10];
                for (int j = 0; j < board.length; j++) {
                    if (numberPresent(board, j, i, intial)) return false;
                }
            }
            // System.out.println("--true");
            return true;
        }
        private boolean hasUniqueRows(char[][] board) {
            for (int i = 0; i < board.length; i++) {
                int[] intial= new int[10];
                for (int j = 0; j < board[0].length; j++) {
                    if (numberPresent(board, i, j, intial)) return false;

                }
            }
            return true;
        }

        private static boolean numberPresent(char[][] board, int i, int j, int[] intial) {
            System.out.println(58-board[i][j]);

            if(board[i][j] != '.') {
                if(intial[58-board[i][j]] != 0) {
                    return true;
                }
                intial[58-board[i][j]] =1;
            }
            return false;
        }
    }

}
