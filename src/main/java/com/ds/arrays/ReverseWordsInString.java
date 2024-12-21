package com.ds.arrays;

import java.util.Arrays;

/**
 * Given an input string s, reverse the order of the words.
 *
 * A word is defined as a sequence of non-space characters. The words in s will be separated by at least one space.
 *
 * Return a string of the words in reverse order concatenated by a single space.
 *
 * Note that s may contain leading or trailing spaces or multiple spaces between two words. The returned string should only have a single space separating the words. Do not include any extra spaces.
 *
 *
 *
 * Example 1:
 *
 * Input: s = "the sky is blue"
 * Output: "blue is sky the"
 * Example 2:
 *
 * Input: s = "  hello world  "
 * Output: "world hello"
 * Explanation: Your reversed string should not contain leading or trailing spaces.
 * Example 3:
 *
 * Input: s = "a good   example"
 * Output: "example good a"
 * Explanation: You need to reduce multiple spaces between two words to a single space in the reversed string.
 */
public class ReverseWordsInString {
    public static void main(String[] args) {
       // System.out.println(reverseWordsV2("the sky is blue"));
        System.out.println(reverseWordsV2("  hello world  "));

        //System.out.println(reverseWordsV2("a good   example"));
        //System.out.println(reverseWordsV2(" a                                              b    "));


    }
    public static String reverseWordsV2(String s) {
        int start = 0;
        String[] words = s.split("\\s+");
        int end = words.length-1;

        while(start < end) {
            String temp = words[end].trim();
            if(temp.isEmpty()) {
                end --;
            } else if(words[start].trim().isEmpty()){
                start++;
            } else {
                words[end] = words[start].trim();
                words[start] = temp;
                start++;
                end--;
            }
        }

        return Arrays.stream(words).filter(s1 -> !s1.trim().isEmpty()).reduce((s1,s2)-> s1+" "+s2).get();
    }
    public static String reverseWords(String s) {
        int start = 0;
        int end = s.length()-1;
        char[] strings = s.toCharArray();

        reverse(start, end, strings,0);
        boolean wordStarted = false;
        start = 0;
        int skips = 0;
        for (int i = 0; i < strings.length; i++) {
            char letter = strings[i];
            if(letter != ' ') { //started or ended
                if(!wordStarted) {
                    wordStarted = true;
                    start = i;
                }
            } else  if(!wordStarted &&  start-i <= 1) { //continous spaces
                start++;
                skips++;
            }  else { //word ended
                wordStarted = false;
                //swap elements
                reverse(start,i-1,strings,skips);
                skips = 0;
                start = i;
            }

        }
        return strings.toString();

    }

    private static void reverse(int start, int end, char[] strings,int skips) {
        while(start < end) {
            char temp = strings[end];
            strings[end] = strings[start];
            strings[start] = temp;
            start++;
            end--;
        }
    }
}
