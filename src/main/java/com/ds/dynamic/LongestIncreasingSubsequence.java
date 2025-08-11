package com.ds.dynamic;

import java.util.*;

/**
 * Example 1:
 *
 * Input: nums = [10,9,2,5,3,7,101,18]
 * Output: 4
 * Explanation: The longest increasing subsequence is [2,3,7,101], therefore the length is 4.
 * Example 2:
 *
 * Input: nums = [0,1,0,3,2,23]
 * Output: 4
 * Example 3:
 *
 * Input: nums = [7,7,7,7,7,7,7]
 * Output: 1
 */
public class LongestIncreasingSubsequence {

    public static void main(String[] args) {
        System.out.println(new Solution().lengthOfLIS(new int[] {0,1,0,3,2,23}));
        System.out.println(new Solution().lengthOfLIS(new int[] {7,7,7,7,7}));
        System.out.println(new Solution().lengthOfLIS(new int[] {10,11,12,5,13,15,6,4,5,7,11}));

    }

    static class Solution {
        public int lengthOfLIS(int[] nums) {
            TreeMap<Integer,Integer> positionWiseLongSequences = new TreeMap<>();
            positionWiseLongSequences.put(nums[0],1);
            for (int i = 1; i < nums.length; i++) {
                int current = nums[i];
                findMinPostion(positionWiseLongSequences, current);
                ;
            }
            System.out.println(positionWiseLongSequences);

            return getLongest(positionWiseLongSequences,positionWiseLongSequences.lastEntry()).getValue();
        }

        private static void findMinPostion(TreeMap<Integer, Integer> positionWiseLongSequences, int current) {
            Map.Entry<Integer,Integer> entry = positionWiseLongSequences.lowerEntry(current);
            if(entry != null) {
                   //positionWiseLongSequences.put(current,entry.getValue()+1);
                Map.Entry<Integer, Integer> max = getLongest(positionWiseLongSequences, entry);
                positionWiseLongSequences.put(current,max.getValue()+1);

            } else {
                positionWiseLongSequences.put(current,1);
            }
        }

        private static Map.Entry<Integer, Integer> getLongest(TreeMap<Integer, Integer> positionWiseLongSequences, Map.Entry<Integer, Integer> entry) {
            Map.Entry<Integer,Integer> max = entry;
            while(entry != null) {
                if(entry.getValue() > max.getValue()) {
                    max = entry;
                }
                entry = positionWiseLongSequences.lowerEntry(entry.getKey());
            }
            return max;
        }
    }
}
