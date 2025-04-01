package com.ds.sliding.window;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 * Given a string s, find the length of the longest
 * substring
 *  without repeating characters.
 *
 *
 *
 * Example 1:
 *
 * Input: s = "abcabcbb"
 * Output: 3
 * Explanation: The answer is "abc", with the length of 3.
 * Example 2:
 *
 * Input: s = "bbbbb"
 * Output: 1
 * Explanation: The answer is "b", with the length of 1.
 * Example 3:
 *
 * Input: s = "pwwkew"
 * Output: 3
 * Explanation: The answer is "wke", with the length of 3.
 * Notice that the answer must be a substring, "pwke" is a subsequence and not a substring.
 */
public class LongestSubString {

    public static void main(String[] args) {

        System.out.println(new Solution().lengthOfLongestSubstring("abcabcbb"));
        System.out.println(new Solution().lengthOfLongestSubstring("abcadf"));
        System.out.println(new SolutionOriginal().lengthOfLongestSubstring("abcadf"));

    }

    static class Solution {

        public int lengthOfLongestSubstring(String s) {
            Map<Character,Integer> characterPositions = new LinkedHashMap<>(s.length());
            int length = 0;
            for (int i = 0; i <s.length() ; i++) {
                Character current = s.charAt(i);
                if(characterPositions.containsKey(current)) {
                    length = Math.max(length,characterPositions.size());
                    removeKeys(characterPositions, characterPositions.get(current),s,i);
                }
                characterPositions.put(current,i);
            }

            return Math.max(characterPositions.size(),length);
        }
        private static void removeKeys(Map<Character, Integer> characterPositions, int position, String s,int curent) {
            for (int i = curent-characterPositions.size(); i <= position; i++) { //abcadf
                characterPositions.remove(s.charAt(i));
            }
        }
    }
    static class SolutionOriginal {
        public int lengthOfLongestSubstring(String s) {
            int maxLength = 0;
            int left = 0;
            int[] charIndex = new int[128];
            for (int right = 0; right < s.length(); right++) { //abcabcbb
                char currentChar = s.charAt(right);
                left = Math.max(left, charIndex[currentChar]);
                charIndex[currentChar] = right + 1;
                maxLength = Math.max(maxLength, right - left + 1);
            }
            return maxLength;
        }
    }

}
