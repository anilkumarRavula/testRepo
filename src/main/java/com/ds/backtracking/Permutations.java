package com.ds.backtracking;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Given an array nums of distinct integers, return all the possible
 * permutations
 * . You can return the answer in any order.
 *
 *
 *
 * Example 1:
 *
 * Input: nums = [1,2,3]
 * Output: [[1,2,3],[1,3,2],[2,1,3],[2,3,1],[3,1,2],[3,2,1]]
 * Example 2:
 *
 * Input: nums = [0,1]
 * Output: [[0,1],[1,0]]
 * Example 3:
 *
 * Input: nums = [1]
 * Output: [[1]]
 */
public class Permutations {
    static List<List<Integer>> outPut = new ArrayList<>();

    public static void main(String[] args) {
        permute(new int[]{1,2,3});
    }
    public static List<List<Integer>> permute(int[] nums) {
        permute(nums,nums.length,0);
        System.out.println(outPut.size()+"=="+new HashSet<>(outPut).size());
        System.out.println(outPut);
        return outPut;
    }
    public static void permute(int[] nums,int length,int position) {
        if(position >= length-1) { //0 >= 2
            outPut.add(Arrays.stream(nums).boxed().collect(Collectors.toList()));
            return ;
        }
        int[] prev = nums.clone();
        int swaps = position ; //0 //1 //2
        while(swaps < length) { //0<2 1<2
            permute(nums,length,position+1);
            if(swaps < length-1)  {
                nums = prev.clone();
                swap(nums,position,swaps+1);
            }
            swaps++;
        }
       // swap(nums,position,length-1);
    }
    public static void swap(int[] nums, int left, int right) {
        int temp = nums[left];
             nums[left] = nums[right];
            nums[right] = temp;
    }
}
