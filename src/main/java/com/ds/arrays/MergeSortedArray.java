package com.ds.arrays;

import java.util.Arrays;

/**
 * You are given two integer arrays nums1 and nums2, sorted in non-decreasing order, and two integers m and n, representing the number of elements in nums1 and nums2 respectively.
 *
 * Merge nums1 and nums2 into a single array sorted in non-decreasing order.
 *
 * The final sorted array should not be returned by the function, but instead be stored inside the array nums1. To accommodate this, nums1 has a length of m + n, where the first m elements denote the elements that should be merged, and the last n elements are set to 0 and should be ignored. nums2 has a length of n.
 *
 *
 *
 * Example 1:
 *
 * Input: nums1 = [1,2,3,0,0,0], m = 3, nums2 = [2,5,6], n = 3
 * Output: [1,2,2,3,5,6]
 * Explanation: The arrays we are merging are [1,2,3] and [2,5,6].
 * The result of the merge is [1,2,2,3,5,6] with the underlined elements coming from nums1.
 * Example 2:
 *
 * Input: nums1 = [1], m = 1, nums2 = [], n = 0
 * Output: [1]
 * Explanation: The arrays we are merging are [1] and [].
 * The result of the merge is [1].
 * Example 3:
 *
 * Input: nums1 = [0], m = 0, nums2 = [1], n = 1
 * Output: [1]
 * Explanation: The arrays we are merging are [] and [1].
 * The result of the merge is [1].
 * Note that because m = 0, there are no elements in nums1. The 0 is only there to ensure the merge result can fit in nums1.
 */
public class MergeSortedArray {

    public static void main(String[] args) {
        //System.out.println(Arrays.toString(new Solution().merge(new int[] {2,1000,0,0,0,0},2,new int[] {1,3,4,50},4)));
        System.out.println(Arrays.toString(new Solution().mergeSimpliefied(new int[] {1,2,3,0,0,0},3,new int[] {2,5,6},3)));
        System.out.println(Arrays.toString(new Solution().mergeSimpliefied(new int[] {2,1000,0,0,0,0},2,new int[] {1,3,4,50},4)));


    }

   static class Solution {

        public int[] merge1(int[] nums1, int m, int[] nums2, int n) {

            int[] mergedArray = new int[m+n];

            int left = 0;
            int right = 0;

            while(left < m && right < n) {

                if(nums1[left] <= nums2[right]){
                    mergedArray[left+right] = nums1[left];
                    left++;
                }
                if(nums1[left] > nums2[right]){
                    mergedArray[left+right] = nums2[right];
                    right++;
                }
            }

            while(left < m) {
                mergedArray[left+right] = nums1[left];
                left++;
            }

            while(right < n) {
                mergedArray[left+right] = nums2[right];
                right++;
            }

            return mergedArray;
        }

       public int[] merge(int[] nums1, int m, int[] nums2, int n) {
            if(n == 0) return nums1;

           int left = 0;
           int right = 0;
           while(left < m && right < n) {
               if(nums1[left] <= nums2[right]){
                   left++;
               } else {
                   int temp = nums1[left];
                   nums1[left] = nums2[right];
                   int start = right;
                   while(start < n-1 && temp > nums2[start+1]) {
                       nums2[start] = nums2[start+1];
                       start++;
                   }
                   nums2[start] = temp;
               }
           }

           while(left < m) {
               nums1[left+right] = nums1[left];
               left++;
           }

           while(right < n) {
               nums1[left+right] = nums2[right];
               right++;
           }

           return nums1;
       }

       public int[] mergeSimpliefied(int[] nums1, int m, int[] nums2, int n) {
           if(n == 0) return nums1;

           int left = m-1;
           int right = n-1;
           int k = m+n -1;
           while(left >=0 && right >=0) {
               if(nums1[left] <= nums2[right]){
                   nums1[k] = nums2[right];
                   right--;
               } else {
                   nums1[k] = nums1[left];
                   left--;
               }
               k--;
           }

           while(left >= 0) {
               nums1[k--] = nums1[left--];
           }

           while(right >=0) {
               nums1[k--] = nums2[right--];
           }

           return nums1;
       }
       public int[] mergeSimpliefied2(int[] nums1, int m, int[] nums2, int n) {
           if(n == 0) return nums1;

           int left = m-1;
           int right = n-1;
           int k = m+n -1;
           while(left >=0 && right >=0) {
               if(nums1[left] <= nums2[right]){
                   nums1[k] = nums2[right];
                   right--;
               } else {
                   nums1[k] = nums1[left];
                   left--;
               }
               k--;
           }

           while(left >= 0) {
               nums1[k--] = nums1[left--];
           }

           while(right >=0) {
               nums1[k--] = nums2[right--];
           }

           return nums1;
       }
    }
}
