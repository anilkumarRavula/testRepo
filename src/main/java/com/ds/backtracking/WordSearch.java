package com.ds.backtracking;

import java.util.HashSet;
import java.util.Set;
/**
 * Given an m x n grid of characters board and a string word, return true if word exists in the grid.
 *
 * The word can be constructed from letters of sequentially adjacent cells, where adjacent cells are horizontally or vertically neighboring. The same letter cell may not be used more than once.


 */
public class WordSearch {
    public static void main(String[] args) {
        System.out.println(new Solution().exist(new char[][]{{'A','B','C','E'},{'S','F','C','S'},{'A','D','E','E'}},
                "ABCB"));
        System.out.println(new Solution().exist(new char[][]{{'C','A','A'},{'A','A','A'},{'B','C','D'}},
                "AAB"));

        System.out.println(new Solution().exist(new char[][]{{'A','A','A','A','A','A'},{'A','A','A','A','A','A'},{'A','A','A','A','A','A'},{'A','A','A','A','A','A'},{'A','A','A','A','A','A'},{'A','A','A','A','A','A'}},
                "AAAAAAAAAAAAAAB"));
    }


   static class Solution {

        public boolean exist(char[][] board, String word) {
            return exist(board,0,0,word,0,new boolean[board.length][board[0].length],new boolean[board.length][board[0].length]);
        }

       public boolean exist(char[][] board, int row,int col,String word, int position, boolean[][] added, boolean[][]  triedPostions) {
            if(row < 0 || col < 0 || row >= board.length || col >= board[row].length
                    || added[row][col]) {
                return  false;
            }
           if(position ==0 && triedPostions[row][col]){
               return false;
           }

           int newPosition = position;
            if(board[row][col] == word.charAt(position)) {
                newPosition = position+1;
                added[row][col] = true;
            } else if(position > 0) {
                return false;
            }


           if(newPosition >= word.length()) {
               return true;
           }

           boolean result = false;

           if(newPosition > 0) { // found a character
               result =  result ||  exist(board,row,col+1,word,newPosition,added,triedPostions);
               result =  result ||  exist(board,row,col-1,word,newPosition,added,triedPostions);
               result =  result ||  exist(board,row-1,col,word,newPosition,added,triedPostions);
               result =  result ||  exist(board,row+1,col,word,newPosition,added,triedPostions);
          }
           if(!result && newPosition > position)  added[row][col] = false;
           if(!result && position == 0) {
               triedPostions[row][col]=true;
               result = exist(board,row,col+1,word,position,added,triedPostions);
               //downward
               result = result ||  exist(board,row+1,col,word,position,added,triedPostions);

           }
          return result;
       }

    }
}
