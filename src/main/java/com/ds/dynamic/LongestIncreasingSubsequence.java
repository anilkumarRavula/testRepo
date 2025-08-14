package com.ds.dynamic;

import java.util.*;
//=================================================================

/**
 * Example 1:
 *
 * Input: nums = [10,9,2,5,3,7,101,18]
 * Output: 4
 * Explanation: The longest increasing subsequence is [2,3,7,101], therefore the length is 4.
 * Example 2:
 *
 * Input: nums = [0,1,0,3,2,23]
 * Output: 4
 * Example 3:
 *
 * Input: nums = [7,7,7,7,7,7,7]
 * Output: 1
 * The Longest Increasing Subsequence (LIS) is a fundamental problem that shows up in many real-world scenarios:
 *
 * Stock market trends:
 * Find the longest period where stock prices are strictly increasing.
 * Example: “How many consecutive days did stock prices keep trending upwards?”
 *
 * Versioning / dependencies:
 * Suppose you want to find the longest chain of software/library upgrades where each version is strictly newer than the last.
 *
 * Career progression:
 * If each job role has a "seniority" number, LIS finds the longest valid career ladder.
 *
 * Manufacturing / scheduling:
 * Say you have machine tasks with increasing deadlines — LIS finds the maximum tasks that can be completed in sequence.
 *
 * We want to keep the best chance to extend subsequences, and the trick is:
 *
 * Keep the smallest tail value for every subsequence length, and use binary search to update it efficiently.
 */
//=================================================================
 /**
 * Minimum Deletions to Make Sequence Increasing (or Sorted)
 *
 * Given an array, find the minimum number of deletions required to make it strictly increasing.
 *
 * 🔑 Insight: Equivalent to n - LIS(nums).
 *
 * Real-world use: Minimizing changes to clean up corrupted log data.
 *
 */
 //=================================================================

 public class LongestIncreasingSubsequence {

    public static void main(String[] args) {
        System.out.println(new MatrixSolutionv2().lengthOfLIS(new int[] {10,11,12,5,13,4,5,6,11,11}));

        System.out.println(new MatrixSolutionv2().lengthOfLIS(new int[] {0,1,0,3,2,3}));
        System.out.println(new MatrixSolutionv2().lengthOfLIS(new int[] {7,7,7,7,7}));
        System.out.println(new MatrixSolutionv2().lengthOfLIS(new int[] {10,11,12,5,13,15,6,4,5,7,11}));

    }

    /** minimum subsequence solution **/
    static class ArraySolutions {
        public int lengthOfLIS(int[] nums) {

            return 0;
        }
    }



    /** dp solution **/
    static class MatrixSolutionv2 {
        public int lengthOfLIS(int[] nums) {
            int[] maxLengths = new int[nums.length];
            //maxLengths[0] = 1;
            int max = 1;
            for (int i = 1; i < nums.length; i++) {
                for (int j = 0; j < i; j++) {
                    if (nums[i] > nums[j]) {
                        maxLengths[i] = Math.max(maxLengths[i], Math.max(maxLengths[j],  1) + 1);
                    } else {
                        maxLengths[i] = Math.max(maxLengths[i],  1);
                    }
                }
                max = Math.max(maxLengths[i], max);
            }
            System.out.println(Arrays.toString(maxLengths));
            return max;
        }
    }
    /** 2dp**/
    static class MatrixSolution {
        public int lengthOfLIS(int[] nums) {
            int max = 1;
            int[][] postionWiseValues = new int[nums.length][nums.length];
            postionWiseValues[0][0] =1;
            for (int i = 1; i < nums.length; i++) {
                postionWiseValues[i][0] = nums[i] >= nums[0] ? postionWiseValues[0][0] : 0;
                int lastMax = 0;
                for (int j = 1; j < i; j++) {
                    //get j index previous value
                    int calculatedValue = nums[i] >= nums[j] ? postionWiseValues[j][j] : 0;
                    int previousValue   =    postionWiseValues[i][j-1];

                    if(calculatedValue > previousValue) {
                        lastMax = j;
                    } else if(calculatedValue == previousValue) {
                        lastMax = nums[j] > nums[lastMax] ? lastMax : j;
                    }
                    postionWiseValues[i][j] = Math.max(calculatedValue, previousValue);
                    //if it is greater then
                    max = Math.max(max,postionWiseValues[i][j]);
                }

                if(nums[i] > nums[lastMax]) {
                    postionWiseValues[i][i] = postionWiseValues[i][i-1]+1;
                } else {
                    postionWiseValues[i][i] = Math.max(postionWiseValues[i][i-1],1);
                }

                max = Math.max(max,postionWiseValues[i][i]);
            }
            //Arrays.stream(postionWiseValues).forEach(index-> System.out.println(Arrays.toString(index)));
            return max;
        }

    }

    /** treset bad solution**/
    static class BadSolution {
        public int lengthOfLIS(int[] nums) {
            TreeMap<Integer,Integer> positionWiseLongSequences = new TreeMap<>();
            positionWiseLongSequences.put(nums[0],1);
            for (int i = 1; i < nums.length; i++) {
                int current = nums[i];
                findMinPostion(positionWiseLongSequences, current);
                ;
            }
            System.out.println(positionWiseLongSequences);

            return getLongest(positionWiseLongSequences,positionWiseLongSequences.lastEntry()).getValue();
        }

        private static void findMinPostion(TreeMap<Integer, Integer> positionWiseLongSequences, int current) {
            Map.Entry<Integer,Integer> entry = positionWiseLongSequences.lowerEntry(current);
            if(entry != null) {
                   //positionWiseLongSequences.put(current,entry.getValue()+1);
                Map.Entry<Integer, Integer> max = getLongest(positionWiseLongSequences, entry);
                positionWiseLongSequences.put(current,max.getValue()+1);

            } else {
                positionWiseLongSequences.put(current,1);
            }
        }

        private static Map.Entry<Integer, Integer> getLongest(TreeMap<Integer, Integer> positionWiseLongSequences, Map.Entry<Integer, Integer> entry) {
            Map.Entry<Integer,Integer> max = entry;
            while(entry != null) {
                if(entry.getValue() > max.getValue()) {
                    max = entry;
                }
                entry = positionWiseLongSequences.lowerEntry(entry.getKey());
            }
            return max;
        }
    }
}
