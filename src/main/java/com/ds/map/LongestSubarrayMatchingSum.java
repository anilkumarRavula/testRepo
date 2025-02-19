package com.ds.map;

/**
 * Confluent
 * Given an array of integers nums and an integer k, return the total number of subarrays whose sum equals to k.
 *
 * A subarray is a contiguous non-empty sequence of elements within an array.
 *
 *
 *
 * Example 1:
 *
 * Input: nums = [1,1,1], k = 2
 * Output: 2
 * Example 2:
 *
 * Input: nums = [1,2,3], k = 3
 * Output: 2
 */
public class LongestSubarrayMatchingSum {

    public static void main(String[] args) {

    }

    static class Solution {
        /**
         * for example
         * 10,5,1,2,7,8
         * sum at each postion
         * 10(o),15(1),16(2),18(3),25(4),33(5)
         * 10-15 not possible
         * 15-15 ==0 possible length currentindex = 2
         * 16-15 =1 . 1 not present in the map so nt
         * 18-15 = 3 not present in map
         * 25-10= 15 (present at map so get he index ie 1 , so length is currenIndex-i=5-0+1=4
         * 33-15= 18 possible but length is 6--3+1=4
         */
        public int subarraySum(int[] nums, int k) {

            // keep sum in map and index
            // at each position check let k1=  sum-k, if k1 present in the map then it means that target possible between two indexes.


            return k;
        }
    }
}
