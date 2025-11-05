package com.ds.bst;

/**
 There is an integer array nums sorted in ascending order (with distinct values).

 Prior to being passed to your function, nums is possibly rotated at an unknown pivot index k (1 <= k < nums.length) such that the resulting array is [nums[k], nums[k+1], ..., nums[n-1], nums[0], nums[1], ..., nums[k-1]] (0-indexed). For example, [0,1,2,4,5,6,7] might be rotated at pivot index 3 and become [4,5,6,7,0,1,2].

 Given the array nums after the possible rotation and an integer target, return the index of target if it is in nums, or -1 if it is not in nums.

 You must write an algorithm with O(log n) runtime complexity.



 Example 1:

 Input: nums = [4,5,6,7,0,1,2], target = 0
 Output: 4
 Example 2:

 Input: nums = [4,5,6,7,0,1,2], target = 3
 Output: -1
 Example 3:

 Input: nums = [1], target = 0
 Output: -1
 */
public class SearchInRotatedArray {
    public static void main(String[] args) {
        System.out.println(new Solution().search(new int[]{4,5,6,7,0,1,2},0));
        System.out.println(new Solution().search(new int[]{4,5,6,7,0,1,2},2));
        System.out.println(new Solution().search(new int[]{4,5,6,7,0,1,2},4));
        System.out.println(new Solution().search(new int[]{4,5,6,7,8,9,2},2));
        System.out.println(new Solution().search(new int[]{11,5,6,7,8,9,10},6));

        //System.out.println(new Solution().search(new int[]{4,5,6,7,0,1,2},9));

    }
    static class Solution {
        public int search(int[] nums, int target) {
            int start = 0;
            int end = nums.length-1;
            while(start <= end) {

                int mid = (start+end)/2;

                //System.out.println(mid);
                if(nums[mid] == target) {
                    return mid;
                }
                if(nums[start] == target) {
                    return start;
                }
                if(nums[end] == target) {
                    return end;
                }

                if(nums[mid] > nums[start]) { // LEFT ARRAY IS SORTED
                    if(target <= nums[mid] &&  target >= nums[start]) {
                        end = mid-1;
                    } else {
                        start = mid+1;
                    }
                } else  if(target >= nums[mid] &&  target <= nums[end]) { //IN BETWEEN RIGHT ARRAY
                    start = mid+1;
                } else {                 // IN LEFT
                    end = mid-1;
                }
                //System.out.println(start+"--"+end);
           }

            return -1;
        }
        }


}
