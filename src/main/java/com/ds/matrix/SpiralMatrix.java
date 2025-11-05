package com.ds.matrix;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Given an m x n matrix, return all elements of the matrix in spiral order.
 *
 *
 *
 * Example 1:
 *
 *
 * Input: matrix = [
 * [1,2,3],
 * [4,5,6],
 * [7,8,9]]
 * Output: [1,2,3,6,9,8,7,4,5]
 * Example 2:
 *
 *
 * Input: matrix = [[1,2,3,4],[5,6,7,8],[9,10,11,12]]
 * Output: [1,2,3,4,8,12,11,10,9,5,6,7]
 */
public class SpiralMatrix {
    public static void main(String[] args) {
        int[][] oneDArray = {
                {1}, {2}, {3},{13}
        };

        int[][] twoDArray = {
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 9},
                {17, 18, 19}

        };
        int[][] DArray = {
                {1, 2, 3,13},
                {4, 5, 6,16},
                {7, 8, 9,19}
        };
        int[][] Array = {
                {1, 2, 3,13},
                {4, 5, 6,16},
                {7, 8, 9,19},
                {17, 18, 29,19}
        };
        int[][] fiveArray = {
                {1, 2, 3,13,23,26},
                {4, 5, 6,16,26,36},
                {7, 8, 9,19,15,14},
                {17, 18, 29,19,18,19},
                {27, 28, 39,49,58,59}
        };
        int[][] matrix = {
                {2, 3, 4},
                {5, 6, 7},
                {8, 9, 10},
                {11, 12, 13},
               // {14, 15, 16}
        };        System.out.println(new Solution().spiralOrder(matrix));

        System.out.println(new Solution().spiralOrder(oneDArray));

        System.out.println(new Solution().spiralOrder(twoDArray));

        System.out.println(new Solution().spiralOrder(DArray));
        System.out.println(new Solution().spiralOrder(Array));
        System.out.println(new Solution().spiralOrder(fiveArray));

    }
    static class SolutionOptimized {
        public List<Integer> spiralOrder(int[][] matrix) {

            if(matrix == null || matrix.length ==0) return new ArrayList<>();

            List<Integer> output = new ArrayList<>();

            int spirals = Math.round((float)matrix.length/2);
            int colLength = matrix[0].length;

            for(int i = 0; i< spirals && i < colLength-i;i++) {
                //addTop
                int rowLength = matrix.length;

                addElements(matrix,i,i,i+1, colLength -i,output);//1,1,2,3
                //addRight
                addElements(matrix, i+1,colLength-i-1, rowLength-i, colLength-i,output); //2,2

                if(i < colLength/2) {
                    //addBottom
                    addElementsDecrementOrder(matrix, rowLength-i-1,colLength-i-2, Math.max(i,rowLength-i-2), i-1,output); //1 0 1 0
                    //addLeft
                    addElementsDecrementOrder(matrix, rowLength-i-2, i, i,i-1,output);
                }

            }
            return output;
        }
        private void addElements(int[][] matrix, int row, int col, int rowLimit,int colLimit,List<Integer> output) {
            for (int i = row; i< rowLimit;i++) {
                for (int j = col; j< colLimit;j++) {
                    output.add(matrix[i][j]);
                }
            }
        }
        private void addElementsDecrementOrder(int[][] matrix, int row, int col, int rowLimit,int colLimit,List<Integer> output) {
            for (int i = row; i> rowLimit;i--) {
                for (int j = col; j> colLimit;j--) {
                    output.add(matrix[i][j]);
                }
            }
        }
    }
    static class Solution {
        public List<Integer> spiralOrder(int[][] matrix) {
            if(matrix == null || matrix.length ==0) return new ArrayList<>();
            if (matrix[0].length == 1) {
                return IntStream.range(0, matrix.length)
                        .mapToObj(i -> matrix[i][0])
                        .collect(Collectors.toList());
            }
            if (matrix.length == 1) {
                return IntStream.range(0, matrix[0].length)
                        .mapToObj(i -> matrix[0][i])
                        .collect(Collectors.toList());
            }

            List<Integer> output = new ArrayList<>();

            int spirals = Math.round((float)matrix.length/2);
            System.out.println(spirals);
            int colLength = matrix[0].length;

            for(int i = 0; i< spirals && i < colLength-i;i++) {
                //addTop
                System.out.println("=="+i);
                int rowLength = matrix.length;

                addElements(matrix,i,i,i+1, colLength -i,output);//1,1,2,3
                //addRight
                addElements(matrix, i+1,colLength-i-1, rowLength-i, colLength-i,output); //2,2

                if(i < colLength/2) {
                    //addBottom
                    addElementsDecrementOrder(matrix, rowLength-i-1,colLength-i-2, Math.max(i,rowLength-i-2), i-1,output); //1 0 1 0
                    //addLeft
                    addElementsDecrementOrder(matrix, rowLength-i-2, i, i,i-1,output);
                }

            }
            return output;
        }
        private void addElements(int[][] matrix, int row, int col, int rowLimit,int colLimit,List<Integer> output) {
            for (int i = row; i< rowLimit;i++) {
                for (int j = col; j< colLimit;j++) {
                    output.add(matrix[i][j]);
                }
            }
        }
        private void addElementsDecrementOrder(int[][] matrix, int row, int col, int rowLimit,int colLimit,List<Integer> output) {
            for (int i = row; i> rowLimit;i--) {
                for (int j = col; j> colLimit;j--) {
                    output.add(matrix[i][j]);
                }
            }
        }
    }
}
