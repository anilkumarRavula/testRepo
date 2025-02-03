package com.ds.backtracking;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Given n pairs of parentheses, write a function to generate all combinations of well-formed parentheses.
 *
 *
 *
 * Example 1:
 *
 * Input: n = 3
 * Output: ["((()))","(()())","(())()","()(())","()()()"]
 * Example 2:
 *
 * Input: n = 1
 * Output: ["()"]
 */
public class generateParentheses{
    public static void main(String[] args) {
        System.out.println(new Solution().generateParenthesis(3));
    }
    static class Solution {
        public List<String> generateParenthesis(int n) {

            return new ArrayList<>(generateParenthesis(n,1));
        }

        public Set<String> generateParenthesis(int n, int current) {

            if(current == n) return Set.of( "()");
            Set<String> parentheses = generateParenthesis(n,current+1);
            Set<String> outout = new HashSet<>();
            for (String parenthesis : parentheses) {
                outout.add("()"+parenthesis);
                for(int i=0;i<parenthesis.length();i++){
                    if(parenthesis.charAt(i)==')') {
                        outout.add(parenthesis.substring(0,i)+"()"+parenthesis.substring(i));
                    }
                }
            }
            return outout;
        }

    }


}
