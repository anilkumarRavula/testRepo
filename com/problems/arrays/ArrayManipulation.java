package com.problems.arrays;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Starting with a 1-indexed array of zeros and a list of operations, for each operation add a value to each the array element between two given indices, inclusive. Once all operations have been performed, return the maximum value in the array.
 *
 * Example
 *
 *
 * Queries are interpreted as follows:
 *
 *     a b k
 *     1 5 3
 *     4 8 7
 *     6 9 1
 * Add the values of  between the indices  and  inclusive:
 *
 * index->	 1 2 3  4  5 6 7 8 9 10
 * 	[0,0,0, 0, 0,0,0,0,0, 0]
 * 	[3,3,3, 3, 3,0,0,0,0, 0]
 * 	[3,3,3,10,10,7,7,7,0, 0]
 * 	[3,3,3,10,10,8,8,8,1, 0]
 * The largest value is  after all operations are performed.
 *
 * Function Description
 *
 * Complete the function arrayManipulation in the editor below.
 *
 * arrayManipulation has the following parameters:
 *
 * int n - the number of elements in the array
 * int queries[q][3] - a two dimensional array of queries where each queries[i] contains three integers, a, b, and k.
 * Returns
 *
 * int - the maximum value in the resultant array
 * Input Format
 *
 * The first line contains two space-separated integers  and , the size of the array and the number of operations.
 * Each of the next  lines contains three space-separated integers ,  and , the left index, right index and summand.
 *
 * Constraints
 *
 * Sample Input
 *
 * 5 3
 * 1 2 100
 * 2 5 100
 * 3 4 100
 * Sample Output
 *
 * 200
 * Explanation
 *
 * After the first update the list is 100 100 0 0 0.
 * After the second update list is 100 200 100 100 100.
 * After the third update list is 100 200 200 200 100.
 *
 * The maximum value is .
 */
public class ArrayManipulation {

    public static long arrayManipulation(int n, List<List<Integer>> queries) {
        System.out.println();
        // Write your code here
        long max = 0;
        long sum = 0;
        //Map<Long,Long>
        List<Integer>[] rangePointsForOperations = new List [n];
        for (int i = 0; i < queries.size(); i++) {
            addToRanges(rangePointsForOperations,queries.get(i).get(0)-1,i);
            addToRanges(rangePointsForOperations,queries.get(i).get(1)-1,i);
        }
        //calculateSum
        HashSet<Integer> operationWindow = new HashSet<>();
        for (List<Integer> allranges : rangePointsForOperations) {
            int completedRangeSum = 0;
            if(allranges != null)  {
                for (Integer operation: allranges) {
                    if(operationWindow.contains(operation)) {
                        completedRangeSum += queries.get(operation).get(2);
                        operationWindow.remove(operation);
                    } else{
                        sum += queries.get(operation).get(2);
                        operationWindow.add(operation);
                    }
                }
                if(sum > max) {
                    max = sum;
                }
                sum = sum - completedRangeSum;
            }
        }
        System.out.println(Arrays.toString(rangePointsForOperations));
        return max;
    }
    private static void addToRanges(List<Integer>[] rangePointsForOperations, int rangeValue, int operationIndex ) {
        Optional.ofNullable(rangePointsForOperations[rangeValue])
                .ifPresentOrElse(val-> val.add(operationIndex),()-> rangePointsForOperations[rangeValue] = new ArrayList<>(Arrays.asList(operationIndex)));
    }
    public static void main(String[] args) {
        //long max = arrayManipulation(10,List.of(List.of(1,2,100),List.of(1,2,100),List.of(1,2,100))); ,List.of(4l,8l,7l)
        long max = arrayManipulation(10,List.of(List.<Integer>of(1,5,100),List.<Integer>of(4,8,7),List.<Integer>of(6,9,1)));
        System.out.println(max);
        assert max == 10 : "max not matching";
    }
}
