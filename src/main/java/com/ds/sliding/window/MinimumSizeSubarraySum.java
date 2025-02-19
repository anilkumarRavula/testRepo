package com.ds.sliding.window;

/**
 * Given an array of positive integers nums and a positive integer target, return the minimal length of a
 * subarray
 *  whose sum is greater than or equal to target. If there is no such subarray, return 0 instead.
 *
 *
 *
 * Example 1:
 *
 * Input: target = 7, nums = [2,3,1,2,4,3]
 * Output: 2
 * Explanation: The subarray [4,3] has the minimal length under the problem constraint.
 * Example 2:
 *
 * Input: target = 4, nums = [1,4,4]
 * Output: 1
 * Example 3:
 *
 * Input: target = 11, nums = [1,1,1,1,1,1,1,1]
 * Output: 0
 */
public class MinimumSizeSubarraySum {
    public static void main(String[] args) {
        System.out.println(new Solution().minSubArrayLen(7, new int[] {2,3,1,2,4,3}));
        System.out.println(new Solution().minSubArrayLen(1, new int[] {1,4,5}));

    }

    static class Solution {
        public int minSubArrayLen(int target, int[] nums) {
            int start = 0;
            int sum = 0;
            int length = Integer.MAX_VALUE;
            for (int i = 0; i < nums.length; i++) {

                if(target == nums[i]) return 1;

                sum = sum + nums[i];


                if(sum > target) {
                    while(sum > target && start <= i) {
                        sum = sum - nums[start++];
                    }
                }

                if(sum == target) {
                    length = Math.min(length, i-start+1);
                    sum= sum - nums[start++];
                }
            }
            //last one

            return length == Integer.MAX_VALUE ? 0 : length;
        }
    }

}
