package com.ds.dynamic.md;

import java.util.Arrays;

public class LongestParlindromicSubStr {
    public static void main(String[] args) {
        System.out.println(new SolutionV2().longestPalindrome("ababababababa").equals("ababababababa"));

        System.out.println(new SolutionV2().longestPalindrome("babad").equals("bab"));
        System.out.println(new SolutionV2().longestPalindrome("abababab").equals("abababa"));
        System.out.println(new SolutionV2().longestPalindrome("bacabab").equals("bacab"));

        System.out.println(new SolutionV2().longestPalindrome("a").equals("a"));
        System.out.println(new SolutionV2().longestPalindrome("aa").equals("aa"));
        System.out.println(new SolutionV2().longestPalindrome("aaa").equals("aaa"));
        System.out.println(new SolutionV2().longestPalindrome("aaaa").equals("aaaa"));
         System.out.println(new SolutionV2().longestPalindrome("axxbxxyyxxbxza").equals("xbxxyyxxbx"));
        System.out.println(new SolutionV2().longestPalindrome("pqsxyyxxrzzrxyxsqp").equals("xrzzrx"));


    }
       //submitted
    static class SolutionV2 {
        public String longestPalindrome(String s) {
            if(s.length() <=1) return  s;
            String max = String.valueOf(s.charAt(0));
            for (int i = 1; i < s.length(); i++) {
                //try to fold from current postion
                int subsequenceStart = getPosition(i -1,i+1,s);
                int lengthOfSubstring = (i-subsequenceStart) *2+1;
                max = lengthOfSubstring > max.length() ? s.substring(subsequenceStart,subsequenceStart+lengthOfSubstring) : max;
                if(s.charAt(i) == s.charAt(i-1)) {
                    subsequenceStart = getPosition(i-2,i+1,s);
                    lengthOfSubstring = (i-subsequenceStart-1) *2+2;
                    max = lengthOfSubstring > max.length() ? s.substring(subsequenceStart,subsequenceStart+lengthOfSubstring) : max;
                }
            }
            return max;
        }

        public int getPosition(int start,int end,String input) {   //abcdbaab, baaab
            while(start >= 0 && end < input.length() && input.charAt(start) == input.charAt(end)) {
                start --;
                end++;
            }
            return start+1;
        }
    }
}
