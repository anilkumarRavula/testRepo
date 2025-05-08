package com.ds.string;

import java.util.Objects;

/**
 * Given two strings s and goal, return true if and only if s can become goal after some number of shifts on s.
 *
 * A shift on s consists of moving the leftmost character of s to the rightmost position.
 *
 * For example, if s = "abcde", then it will be "bcdea" after one shift.
 *
 *
 * Example 1:
 *
 * Input: s = "abcde", goal = "cdeab"
 * Output: true
 * Example 2:
 *
 * Input: s = "abcde", goal = "abced"
 * Output: false
 */
public class MatchRotatedString {

    public static final char A = 'a';

    public static void main(String[] args) {
        String a = "anilakjdak";
        int index = a.indexOf(A,1);
        while(index != -1) {
            System.out.println(index);
            System.out.println(a.substring(0,index) +"-->"+ a.substring(index));
            index = a.indexOf(A,index+1);

        }

    }
   static class Solution {
        public boolean rotateString(String s, String goal) {
            char[] goalsCHaracters = goal.toCharArray();

            if(Objects.equals(s,goal)) return  true;
            if(s == null || goal == null || goal.length() != s.length()) return  false;
            int index = s.indexOf(goalsCHaracters[0]);
            while(index != -1) {

            }

            return false;
        }
        private boolean matchSubString(int goalStart,int goalEnd, int start, int end) {

            return false;
        }
    }
}
