package com.ds.arrays.twopinter;

import java.util.Arrays;

/**
 * Given a 1-indexed array of integers numbers that is already sorted in non-decreasing order, find two numbers such that they add up to a specific target number. Let these two numbers be numbers[index1] and numbers[index2] where 1 <= index1 < index2 <= numbers.length.
 *
 * Return the indices of the two numbers, index1 and index2, added by one as an integer array [index1, index2] of length 2.
 *
 * The tests are generated such that there is exactly one solution. You may not use the same element twice.
 *
 * Your solution must use only constant extra space.
 *
 *
 *
 * Example 1:
 *
 * Input: numbers = [2,7,11,15], target = 9
 * Output: [1,2]
 * Explanation: The sum of 2 and 7 is 9. Therefore, index1 = 1, index2 = 2. We return [1, 2].
 * Example 2:
 *
 * Input: numbers = [2,3,4], target = 6
 * Output: [1,3]
 * Explanation: The sum of 2 and 4 is 6. Therefore index1 = 1, index2 = 3. We return [1, 3].
 * Example 3:
 *
 * Input: numbers = [-1,0], target = -1
 * Output: [1,2]
 * Explanation: The sum of -1 and 0 is -1. Therefore index1 = 1, index2 = 2. We return [1, 2].
 *
 *
 * Constraints:
 *
 * 2 <= numbers.length <= 3 * 104
 * -1000 <= numbers[i] <= 1000
 * numbers is sorted in non-decreasing order.
 * -1000 <= target <= 1000
 * The tests are generated such that there is exactly one solution.
 */
public class TargetSumArraySored {
    public static void main(String[] args) {
        System.out.println(Arrays.toString(new Solution().twoSum(new int[] {-3,3,4,90},0)));
        System.out.println(Arrays.toString(new Solution().twoSum(new int[] {-2,-1,-1,0},-3)));
        System.out.println(Arrays.toString(new Solution().twoSum(new int[] {-1,-1,0},-1)));
        System.out.println(Arrays.toString(new Solution().twoSum(new int[] {-4,-1,0,2,3,4},0)));
        System.out.println(Arrays.toString(new Solution().twoSum(new int[] {-4,-1,0,1,1,2},-2)));


    }
    //v2
    public static  int[] twoSum(int[] numbers, int target) {
        //iterate through number
        int index = 0;

        int sumRequired = 0;

        return numbers;
    }

   static class Solution {
        public int[] twoSum(int[] numbers, int target) {
            int left = 0;
            int right = numbers.length-1;

            while(left < right) {
                if(numbers[left] + numbers[right] < target) {
                    left ++;
                } else if(numbers[left] + numbers[right] > target) {
                    right--;
                } else {
                    return new int[]{left+1,right+1};
                }
            }

            return numbers;          }
    }

    class SolutionN2 {
        public int[] twoSum(int[] numbers, int target) {
            int prev = 0;
            while (prev < numbers.length && numbers[prev] <= Math.max(target,0)) {
                int remainingSum = target - numbers[prev];
                int pointer = prev + 1;

                while (pointer < numbers.length && numbers[pointer] <= remainingSum) {
                    int sum = remainingSum - numbers[pointer];
                    if (sum == 0) {
                        return new int[]{prev+1, pointer+1};
                    } else {
                        pointer++;
                    }
                }
                prev++;
            }
            return numbers;          }
    }


}
