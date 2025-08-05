package com.ds.utils;

public class SudokuParser {
    public static void main(String[] args) {
        String input = "[[\"5\",\"3\",\".\",\".\",\"7\",\".\",\".\",\".\",\".\"]"
                     + ",[\"6\",\".\",\".\",\"1\",\"9\",\"5\",\".\",\".\",\".\"]"
                     + ",[\".\",\"9\",\"8\",\".\",\".\",\".\",\".\",\"6\",\".\"]"
                     + ",[\"8\",\".\",\".\",\".\",\"6\",\".\",\".\",\".\",\"3\"]"
                     + ",[\"4\",\".\",\".\",\"8\",\".\",\"3\",\".\",\".\",\"1\"]"
                     + ",[\"7\",\".\",\".\",\".\",\"2\",\".\",\".\",\".\",\"6\"]"
                     + ",[\".\",\"6\",\".\",\".\",\".\",\".\",\"2\",\"8\",\".\"]"
                     + ",[\".\",\".\",\".\",\"4\",\"1\",\"9\",\".\",\".\",\"5\"]"
                     + ",[\".\",\".\",\".\",\".\",\"8\",\".\",\".\",\"7\",\"9\"]]";
        
        // Remove brackets and quotes, then split
        input = input.replaceAll("[\\[\\]\"]", "");
        String[] rows = input.split("(?<=\\G([^,]+,){8}[^,]+),?");

        char[][] board = new char[rows.length][];
        for (int i = 0; i < rows.length; i++) {
            String[] cols = rows[i].split(",");
            board[i] = new char[cols.length];
            for (int j = 0; j < cols.length; j++) {
                board[i][j] = cols[j].charAt(0);
            }
        }

        // Print to verify
        for (char[] row : board) {
            System.out.println(java.util.Arrays.toString(row));
        }
    }
}
