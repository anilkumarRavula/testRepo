package com.ds.subarray;

/**
 * Given a circular integer array nums of length n, return the maximum possible sum of a non-empty subarray of nums.
 *
 * A circular array means the end of the array connects to the beginning of the array. Formally, the next element of nums[i] is nums[(i + 1) % n] and the previous element of nums[i] is nums[(i - 1 + n) % n].
 *
 * A subarray may only include each element of the fixed buffer nums at most once. Formally, for a subarray nums[i], nums[i + 1], ..., nums[j], there does not exist i <= k1, k2 <= j with k1 % n == k2 % n.
 *
 *
 *
 * Example 1:
 *
 * Input: nums = [1,-2,3,-2]
 * Output: 3
 * Explanation: Subarray [3] has maximum sum 3.
 * Example 2:
 *
 * Input: nums = [5,-3,5]
 * Output: 10
 * Explanation: Subarray [5,5] has maximum sum 5 + 5 = 10.
 * Example 3:
 *
 * Input: nums = [-3,-2,-3]
 * Output: -2
 * Explanation: Subarray [-2] has maximum sum -2.
 */
public class MaxSubArrayCircular {
    public static void main(String[] args) {
/*
        System.out.println(new Solution().maxSubarraySum(new int[] {1,2,-3,-1,5}));
        System.out.println(new Solution().maxSubarraySum(new int[] {-2,1,-3,4,-1,2,1,-5,4}));
        System.out.println(new Solution().maxSubarraySum(new int[] {5,4,-1,7,8}));
        System.out.println(new Solution().maxSubarraySum(new int[] {1}));
        System.out.println(new Solution().maxSubarraySum(new int[] {0,2,-2}));
*/
        System.out.println(new Solution().maxSubarraySum(new int[] {1,-2,3,-2}));
        System.out.println(new Solution().maxSubarraySum(new int[] {5,-3,5}));
        System.out.println(new Solution().maxSubarraySum(new int[] {-3,-2,-3}));
        System.out.println(new Solution().maxSubarraySum(new int[] {1}));


        //System.out.println(new Solution2().maxSubarraySumCircular(new int[] {1,2,-1,-1,2,5}));
        //System.out.println(new Solution2().maxSubarraySumCircular(new int[] {-2,3,-3,4,-1,2,1,-5,4}));
        //
       // System.out.println(new Solution2().maxSubarraySumCircular(new int[] {5,4,-1,7,8}));
        //System.out.println(new Solution2().maxSubarraySumCircular(new int[] {1}));
       // System.out.println(new Solution2().maxSubarraySumCircular(new int[] {0,2,-2}));


    }
   static class Solution {
        public int maxSubarraySum(int[] nums) {
            if(nums.length ==0 ) return 0;
            int maxSum = nums[0];
            //int left = 0;
            //int maxPostion = 0;
            int cummulativeSum = nums[0];

            int[] sums = new int[nums.length];
            sums[0] = nums[0];
            for (int i = 1; i < nums.length ; i++) {
                //storeSums
                sums[i] = sums[i-1]+nums[i];

                int sum = Math.max(cummulativeSum + nums[i],nums[i]);
                if(sum >= maxSum) { //new Max
                    maxSum = sum;
                   // maxPostion = i;
                }

                cummulativeSum = sum;
            }

            System.out.println(maxSum + " ====");

            int oldMaxSum = maxSum;
            cummulativeSum = nums[nums.length-1];
            maxSum =     nums[nums.length-1];
            for (int i = nums.length-2; i >= 0 ; i--) {
                maxSum = Math.max(cummulativeSum,maxSum);
                int sum = Math.max(sums[i] ,sums[i]-nums[i]) + maxSum;
                if(sum >= oldMaxSum) { //new Max
                    oldMaxSum = sum;
                }
                cummulativeSum = cummulativeSum+nums[i];
            }
            return oldMaxSum;
        }
    }

    static class Solution2 {
        public int maxSubarraySumCircular2(int[] nums) {
            if(nums.length ==0 ) return 0;
            int maxSum = nums[0];
            //int right = 0;
            int maxPostion = 0;
            int cummulativeSum = nums[0];
            for (int i = 1; i < nums.length ; i++) {

                int sum = Math.max(cummulativeSum + nums[i],nums[i]);

                if(sum >= maxSum) { //new Max
                    maxSum = sum;
                    maxPostion = i;
                }

                cummulativeSum = sum;

            }

            return  Math.max(0,maxSum);
        }
        public int maxSubarraySumCircular(int[] nums) {
            if(nums.length ==0 ) return 0;
            int maxSum = nums[0];
            //int right = 0;
            int maxPostion = 0;
            int cummulativeSum = nums[0];
            for (int i = 1; i < nums.length ; i++) {

                int sum = Math.max(cummulativeSum + nums[i],nums[i]);

                if(sum >= maxSum) { //new Max
                    maxSum = sum;
                    maxPostion = i;
                }

                cummulativeSum = sum;

            }
            int oldMaxSum = maxSum;
            System.out.println(maxSum + " "+ maxPostion);

            int left = maxPostion-1;
            cummulativeSum = nums[maxPostion];

            while(cummulativeSum != maxSum && left >= 0) {
                cummulativeSum = cummulativeSum+nums[left];
                left--;
            }
            System.out.println("j poistion"+ left);

            int right = maxPostion+1;
            cummulativeSum = maxSum;
            while ( right < (maxPostion+nums.length-(right+1-(left+1)))) {
                int sum = cummulativeSum+nums[right%nums.length];
                if(sum >= maxSum) { //new Max
                    maxSum = sum;
                }
                cummulativeSum = sum;
                right++;
            }

            return  Math.max(oldMaxSum,maxSum);
        }
    }

}
