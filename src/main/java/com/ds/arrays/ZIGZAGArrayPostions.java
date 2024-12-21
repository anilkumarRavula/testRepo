package com.ds.arrays;

/**
 * The string "PAYPALISHIRING" is written in a zigzag pattern on a given number of rows like this: (you may want to display this pattern in a fixed font for better legibility)
 *
 * P   A   H   N
 * A P L S I I G
 * Y   I   R
 * And then read line by line: "PAHNAPLSIIGYIR"
 *
 * Write the code that will take a string and make this conversion given a number of rows:
 *
 * string convert(string s, int numRows);
 *
 *
 * Example 1:
 *
 * Input: s = "PAYPALISHIRING", numRows = 3
 * Output: "PAHNAPLSIIGYIR"
 * Example 2:
 *
 * Input: s = "PAYPALISHIRING", numRows = 4
 * Output: "PINALSIGYAHRPI"
 * Explanation:
 * P     I    N
 * A   L S  I G
 * Y A   H R
 * P     I
 * Example 3:
 *
 * Input: s = "A", numRows = 1
 * Output: "A"
 */
public class ZIGZAGArrayPostions {
    public static void main(String[] args) {
       // System.out.println(convert("PAYPALISHIRING",3));
        System.out.println(convert("ABCDE",4));

    }
    public static String convert(String s, int numRows) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < numRows ; i++) {
            if(i%(numRows-1) == 0) {
                int start = i;
                while(start < s.length()) {
                    stringBuilder.append(s.charAt(start));
                    start = start + 2*numRows - 2;
                }
            } else {
                int start = i;
                while(start < s.length() ) {
                    stringBuilder.append(s.charAt(start));
                    if(start > i) {
                        if(start + 2* i >= s.length()) break;
                        start = start + 2* i;
                        stringBuilder.append(s.charAt(start));
                    }
                    start = start + 2* (numRows-i) - 2;

                }

            }
           // System.out.println(stringBuilder);
        }
        return stringBuilder.toString();
    }

    /**
     * Analyse this
     * @param s
     * @param numRows
     * @return
     */
    public String convert1(String s, int numRows) {
        if(numRows==1){
            return s;
        }
        StringBuilder str = new StringBuilder();
        int n = s.length();
        int k = 2* (numRows -1);
        for(int i=0;i<numRows;i++){
            int index = i ;
            while(index<n){
                str.append(s.charAt(index));
                if(i!=0 && i!=numRows-1){
                    int k1 = k- (2*i);
                    int k2 = index + k1;
                    if(k2<n){
                        str.append(s.charAt(k2));
                    }
                }
                index = index + k;
            }
        }
        return str.toString();
    }


}
