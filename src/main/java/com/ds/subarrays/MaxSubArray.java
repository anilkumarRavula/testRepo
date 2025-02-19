package com.ds.subarrays;

/**
 Given a circular integer array nums of length n, return the maximum possible sum of a non-empty subarray of nums.

 A circular array means the end of the array connects to the beginning of the array.
 Formally, the next element of nums[i] is nums[(i + 1) % n] and the previous element of nums[i] is nums[(i - 1 + n) % n].

 A subarray may only include each element of the fixed buffer nums at most once. Formally, for a subarray nums[i], nums[i + 1], ..., nums[j], there does not exist i <= k1, k2 <= j with k1 % n == k2 % n. */
public class MaxSubArray {
    public static void main(String[] args) {
        System.out.println(String.valueOf(-121));
    }
    static class Solution {
        public int maxSubarraySumCircular(int[] nums) {
            int max = nums[0];
            int sum = nums[0];
            for (int i = 1; i < nums.length; i++) {
                int currentSum = sum + nums[i];
                if(currentSum > max) {
                    max = currentSum;
                } else if(currentSum < nums[i]) {
                    sum = nums[i];
                } else {
                    sum = currentSum;
                }

            }
            if( sum > max) {
                max = sum;
            }

            return max;
        }
    }
}
