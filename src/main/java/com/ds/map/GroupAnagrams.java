package com.ds.map;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Given an array of strings strs, group the anagrams together. You can return the answer in any order.
 *
 *
 *
 * Example 1:
 *
 * Input: strs = ["eat","tea","tan","ate","nat","bat"]
 *
 * Output: [["bat"],["nat","tan"],["ate","eat","tea"]]
 *
 * Explanation:
 *
 * There is no string in strs that can be rearranged to form "bat".
 * The strings "nat" and "tan" are anagrams as they can be rearranged to form each other.
 * The strings "ate", "eat", and "tea" are anagrams as they can be rearranged to form each other.
 * Example 2:
 *
 * Input: strs = [""]
 *
 * Output: [[""]]
 *
 * Example 3:
 *
 * Input: strs = ["a"]
 *
 * Output: [["a"]]
 *
 *
 *
 * Constraints:
 *
 * 1 <= strs.length <= 104
 * 0 <= strs[i].length <= 100
 * strs[i] consists of lowercase English letters.
 */
public class GroupAnagrams {
    public static void main(String[] args) {
        List<List<String>> output =  new Solution().groupAnagrams(new String[] {"eat","tea","tan","ate","nat","bat"});
        System.out.println(output);
    }

    static class Solution {
        public List<List<String>> groupAnagrams(String[] strs) {

            Map<String,List<String>> existingList = new HashMap<>();

            for (String str: strs) {
                char[] sorted = str.toCharArray();
                Arrays.sort(sorted);
                String key = new String(sorted);
                existingList.computeIfAbsent(key,key1->new ArrayList<>());
                existingList.get(key).add(str);
            }
            return existingList.values().stream().toList();
        }
    }
}
