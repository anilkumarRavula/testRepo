package com.ds.arrays;

/**
 * You are given an integer array nums. You are initially positioned at the array's first index, and each element in the array represents your maximum jump length at that position.
 *
 * Return true if you can reach the last index, or false otherwise.
 *
 *
 *
 * Example 1:
 *
 You are given a 0-indexed array of integers nums of length n. You are initially positioned at nums[0].

 Each element nums[i] represents the maximum length of a forward jump from index i. In other words, if you are at nums[i], you can jump to any nums[i + j] where:

 0 <= j <= nums[i] and
 i + j < n
 Return the minimum number of jumps to reach nums[n - 1]. The test cases are generated such that you can reach nums[n - 1].



 Example 1:

 Input: nums = [2,3,1,1,4]
 Output: 2
 Explanation: The minimum number of jumps to reach the last index is 2. Jump 1 step from index 0 to 1, then 3 steps to the last index.
 Example 2:

 Input: nums = [2,3,0,1,4]
 Output: 2
 */
public class JumpingMinimumJumps {
    public static void main(String[] args) {
        System.out.println(minJump(new int[] {3,4,3,2,5,4,3}));
    }
    public static int minJump(int[] nums) {
        int length = nums.length;
        int jumps = 0;
        int maxForward = 0;
        int currentMaxJump = 0;
        //1,1,1,1,,1,1,1
        for (int i = 0; i < length-1 ; i++) {
            //findforwardpostion
            int forward = i+nums[i];  // calculate forward postion from every postion
            //compare forwards with previous best

            if(forward > maxForward) { //if current forwards better than previous then increase
                   maxForward = forward;
            }
            if(maxForward >= length-1) { // when it reaches with in any region break and increment jump
                jumps++;

            } else if(i >= currentMaxJump) {      // jump only once within max forward
                 jumps++;
                currentMaxJump = maxForward;
            }
            if(maxForward >= length-1) {

                   return jumps;
            }
        }
        return 0;
    }
}
