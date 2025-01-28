package com.ds.intervals;

import com.ds.arrays.RemoveArrayDeplucates;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * Given an array of intervals where intervals[i] = [starti, endi], merge all overlapping intervals, and return an array of the non-overlapping intervals that cover all the intervals in the input.
 *
 *
 *
 * Example 1:
 *
 * Input: intervals = [[1,3],[2,6],[8,10],[15,18]]
 * Output: [[1,6],[8,10],[15,18]]
 * Explanation: Since intervals [1,3] and [2,6] overlap, merge them into [1,6].
 * Example 2:
 *
 * Input: intervals = [[1,4],[4,5]]
 * Output: [[1,5]]
 * Explanation: Intervals [1,4] and [4,5] are considered overlapping.
 *
 *
 * Constraints:
 *
 * 1 <= intervals.length <= 104
 * intervals[i].length == 2
 * 0 <= starti <= endi <= 104
 */
public class OverLappingIntervals {

    public static void main(String[] args) {
        //[[1,4],[0,4]]
        Arrays.stream(new Solution().merge(new int[][] { new int[]{1,4},new int[]{0,4}})).iterator().forEachRemaining(arr->
                System.out.println(Arrays.toString(arr)));

        Arrays.stream(new Solution().merge(new int[][] { new int[]{1,3},new int[]{2,6},new int[]{8,10},new int[]{15,18}})).iterator().forEachRemaining(arr->
                System.out.println(Arrays.toString(arr)));
        Arrays.stream(new Solution().merge(new int[][] { new int[]{1,4},new int[]{4,5}})).iterator().forEachRemaining(arr->
                System.out.println(Arrays.toString(arr)));

    }


    static class Solution {


        public int[][] merge(int[][] intervals) {
            if(intervals.length == 0) return new int[][]{};
            Arrays.sort(intervals, Comparator.comparingInt(a -> a[0]));
            List<int[]> output = new ArrayList<>();

            int start = intervals[0][0];
            int end = intervals[0][1];
            output.add(new int[] {start,end});
            for (int i = 1; i < intervals.length;i++) {
                if(intervals[i][0] <= end) {
                    end = Math.max(end, intervals[i][1]);
                    output.remove(output.size()-1);
                }  else {
                    start = intervals[i][0];
                    end = intervals[i][1];
                }

                output.add(new int[] {start,end});
            }
            return output.toArray(new int[output.size()][2]);
        }
    }

}
