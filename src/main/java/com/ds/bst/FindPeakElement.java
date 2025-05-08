package com.ds.bst;

/**
 * A peak element is an element that is strictly greater than its neighbors.
 *
 * Given a 0-indexed integer array nums, find a peak element, and return its index. If the array contains multiple peaks, return the index to any of the peaks.
 *
 * You may imagine that nums[-1] = nums[n] = -∞. In other words, an element is always considered to be strictly greater than a neighbor that is outside the array.
 *
 * You must write an algorithm that runs in O(log n) time.
 *
 *
 *
 * Example 1:
 *
 * Input: nums = [1,2,3,1]
 * Output: 2
 * Explanation: 3 is a peak element and your function should return the index number 2.
 * Example 2:
 *  1--> 1
 *  1,2--> 2
 *  [6,5,4,3,2,3,2] --> 6
 * Input: nums = [1,2,1,3,5,6,4]
 * Output: 5
 * Explanation: Your function can return either index number 1 where the peak element is 2, or index number 5 where the peak element is 6.
 */

public class FindPeakElement {
    public static void main(String[] args) {
        System.out.println(new Solution().findPeakElement(new int [] {6,5,4,1,2,1,4,5}));
        System.out.println(new Solution().findPeakElement(new int [] {1,2}));
        System.out.println(new Solution().findPeakElement(new int [] {2,1}));

    }

        static class Solution {
            public int findPeakElement(int[] nums) {
                if(nums.length ==1 ) return  1;
                return findPeakElement(nums,0,nums.length-1,nums.length-1);
            }

            public int findPeakElement(int[] nums,int start, int end,int length) {
                //---
                return length;
            }
        }

    class LSolution {
        public int findPeakElement(int[] arr) {
            int low = 0;
            int high = arr.length-1;
            while(low < high)
            {
                int mid = (low + high)/2;
                if ( arr[mid] <= arr[mid+1] )
                {
                    low = mid + 1;
                }
                else
                {
                    high = mid;
                }
            }
            return high;
        }
    }

}
