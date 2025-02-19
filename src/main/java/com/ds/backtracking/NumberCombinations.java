package com.ds.backtracking;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Given two integers n and k, return all possible combinations of k numbers chosen from the range [1, n].
 *
 * You may return the answer in any order.
 *
 *
 *
 * Example 1:
 *
 * Input: n = 4, k = 2
 * Output: [[1,2],[1,3],[1,4],[2,3],[2,4],[3,4]]
 * Explanation: There are 4 choose 2 = 6 total combinations.
 * Note that combinations are unordered, i.e., [1,2] and [2,1] are considered to be the same combination.
 * Example 2:
 *
 * Input: n = 1, k = 1
 * Output: [[1]]
 * Explanation: There is 1 choose 1 = 1 total combination.
 *
 *
 * Constraints:
 *
 * 1 <= n <= 20
 * 1 <= k <= n
 */
public class NumberCombinations {
    static List<List<Integer>> combinations = new ArrayList<>();

    public static void main(String[] args) {
        combine(3,2);
    }
    public static List<List<Integer>> combine(int n, int k) {

        if(!combinations.isEmpty()) combinations.clear();
        formCombinations(n,k,1,1,new ArrayList<>(k)) ;
        System.out.println(combinations);
        return combinations;
    }
    public  static void formCombinations(int n, int k,int start, int current , List<Integer> integers) {
        if(current>k) {
             combinations.add(new ArrayList<>(integers));
             return;
        }
        for (int i = start; i <= n ; i++) { //[1,n]
            integers.add(i);
            formCombinations(n,k,i+1,current+1,integers);
            integers.remove(integers.size()-1);
        }
    }


}
