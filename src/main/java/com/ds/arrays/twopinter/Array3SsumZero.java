package com.ds.arrays.twopinter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Given an integer array nums, return all the triplets [nums[i], nums[j], nums[k]] such that i != j, i != k, and j != k, and nums[i] + nums[j] + nums[k] == 0.
 *
 * Notice that the solution set must not contain duplicate triplets.
 *
 *
 *
 * Example 1:
 *
 * Input: nums = [-1,0,1,2,-1,-4]
 * Output: [[-1,-1,2],[-1,0,1]]
 * Explanation:
 * nums[0] + nums[1] + nums[2] = (-1) + 0 + 1 = 0.
 * nums[1] + nums[2] + nums[4] = 0 + 1 + (-1) = 0.
 * nums[0] + nums[3] + nums[4] = (-1) + 2 + (-1) = 0.
 * The distinct triplets are [-1,0,1] and [-1,-1,2].
 * Notice that the order of the output and the order of the triplets does not matter.
 */
public class Array3SsumZero {
    public static void main(String[] args) {
        System.out.println(threeSum(new int[] {-4,-2,1,-5,-4,-4,4,-2,0,4,0,-2,3,1,-5,0}));
        System.out.println(threeSum(new int[] {-4,-2,-2,-2,0,1,2,2,2,3,3,4,4,6,6}));
       System.out.println(threeSum(new int[] {-1,0,1,2,-1,-4}));
        System.out.println(threeSum(new int[] {-1,0,1,2,-1,-4,-2,-3,3,0,4}));


    }

    public static List<List<Integer>> threeSum(int[] nums) {
        Arrays.sort(nums);
        System.out.println(Arrays.toString(nums));
        List<List<Integer>> triplets = new ArrayList<>();

        for (int i = 0; i < nums.length-2 ; i++) {
            int current = nums[i];
            if (i>0 && current == nums[i-1]) {
                continue;
            }
            int target = -current;
            int left = i+1;
            int right = nums.length-1;

            while(left < right) {
                int sum = nums[left]+nums[right];
                if(sum == target
                        && (nums[left] != nums[left-1]
                        || ( right == nums.length - 1 || nums[right] != nums[right + 1]) )
                ) {

                    triplets.add(Arrays.asList(nums[i],nums[left],nums[right]));
                    left++;
                    right --;
                } else if ( sum < target) {
                    left++;
                } else {
                    right --;
                }
            }
        }
        return  triplets;
    }
}
