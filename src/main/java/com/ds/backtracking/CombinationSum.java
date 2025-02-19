package com.ds.backtracking;

import java.util.ArrayList;
import java.util.List;

/**
 * Given an array of distinct integers candidates and a target integer target, return a list of all unique combinations of candidates where the chosen numbers sum to target. You may return the combinations in any order.
 *
 * The same number may be chosen from candidates an unlimited number of times. Two combinations are unique if the
 * frequency
 *  of at least one of the chosen numbers is different.
 *
 * The test cases are generated such that the number of unique combinations that sum up to target is less than 150 combinations for the given input.
 *
 *
 *
 * Example 1:
 *
 * Input: candidates = [2,3,6,7], target = 7
 * Output: [[2,2,3],[7]]
 * Explanation:
 * 2 and 3 are candidates, and 2 + 2 + 3 = 7. Note that 2 can be used multiple times.
 * 7 is a candidate, and 7 = 7.
 * These are the only two combinations.
 * Example 2:
 *
 * Input: candidates = [2,3,5], target = 8
 * Output: [[2,2,2,2],[2,3,3],[3,5]]
 * Example 3:
 *
 * Input: candidates = [2], target = 1
 * Output: []
 */
public class CombinationSum {
    public static void main(String[] args) {
        System.out.println(new Solution().combinationSum(new int[]{2,3,7},12));;
    }
static class Solution {

    public List<List<Integer>> combinationSum(int[] candidates, int target) {

        List<List<Integer>> output = new ArrayList<>();
        combinationSum(output,candidates,target,0,new ArrayList<>(),0);
        return output;

    }

    public void combinationSum(List<List<Integer>> output,int[] candidates, int target,int current, List<Integer> currentList,int sum ) {
        if(current >= candidates.length) return  ;
        //addIfMathcesTarget
        int prev = sum;
        for (int i = current; i < candidates.length; i++) {
            if(candidates[i] > target) {
                continue;
            }
            for (int j = 1; j <= target/candidates[i]; j++) {
                sum = sum+candidates[i];
                if(sum > target) break;
                currentList.add(candidates[i]);
                addCombination(output,currentList,target,sum);
                combinationSum(output,candidates,target,i+1,currentList,sum);
            }

            while (!currentList.isEmpty() && currentList.get(currentList.size()-1) == candidates[i]) {
                currentList.remove(currentList.size()-1);
            }
            sum = prev;
        }

    }

    public void addCombination(List<List<Integer>> output,List<Integer> currentList, int target, int currentsum) {
        if(target == currentsum) {
            output.add(new ArrayList<>(currentList));
        }
    }
}
    static class Solution2 {

        public List<List<Integer>> combinationSum(int[] candidates, int target) {

            List<List<Integer>> output = new ArrayList<>();
            combinationSum(output,new ArrayList<>(),candidates,target,0);
            return output;

        }

        public void combinationSum(List<List<Integer>> output,List<Integer> currentList,int[] candidates, int target,int current ) {


            for(int i = current;i< candidates.length;i++) {
                //aad to list
                currentList.add(candidates[i]);
                //call recursively
                combinationSum(output,currentList,candidates,target-candidates[i],i);
                //removelist
                currentList.remove(currentList.size()-1);
            }

        }


    }

}
