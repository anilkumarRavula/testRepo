package src.com.ds.dynamic;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

/*

Given an array of integers, find the subset of non-adjacent elements with the maximum sum. Calculate the sum of that subset. It is possible that the maximum sum is , the case when all elements are negative.

Example

The following subsets with more than  element exist. These exclude the empty subset and single element subsets which are also valid.

Subset      Sum
[-2, 3, 5]   6
[-2, 3]      1
[-2, -4]    -6
[-2, 5]      3
[1, -4]     -3
[1, 5]       6
[3, 5]       8
The maximum subset sum is . Note that any individual element is a subset as well.


In this case, it is best to choose no element: return .


 */
public class MaxArraySum {
    static int maxSubsetSum(Integer[] arr) {

        int[] maxArray = new int[arr.length];
        maxArray[0] = arr[0];maxArray[1]=arr[1];maxArray[2]=arr[0]+arr[2] > arr[0] ? ((arr[0]+arr[2]  < arr[2]) ? arr[2] :  arr[0]+arr[2]) : arr[0];
        for (int i = 3; i < arr.length ; i++) {
            int previousMax = Math.max(Math.max(arr[i-2],maxArray[i-2]),Math.max(arr[i-3],maxArray[i-3]));
            int newMax = previousMax+ arr[i];
            maxArray[i] = newMax;
            arr[i] = Math.max(previousMax,arr[i]);
        }
        //find max
        int max = maxArray[2];
        for (int i = 3; i < maxArray.length; i++) {
            if(maxArray[i] > max) {
                max = maxArray[i];
            }
        }

        return max;
    }

    public static void main(String[] args) throws IOException {
        //System.out.println(maxSubsetSum(new Integer[]{8006,-3505,-2450,-2399,-3423,8968,-2026,-3762,3229,6259,2226,8664,6966,364,3835,-1599,6880,8196,5257}));
       BufferedReader bufferedReader = new BufferedReader(new FileReader("D:\\anil\\codetestcases\\maxsubsetArraysum2o.txt"));
        Integer[] inputs  = Stream.of(bufferedReader.readLine().split(" "))
                .map(Integer::parseInt)
                .<Integer>toArray(size-> new Integer[size]);
        System.out.println(maxSubsetSum(inputs));

        System.out.println(call("6416 -3260 9843 -5651 -7919 -4720 -2565 1784 5376 -9605 4049 -3161 -1279 -7674 -7006 1560 -8958 2584 2482 -5842 -2841 -7424 -3440 7361 -2654 -7265 -4253 -9893 -6535 943 -3871 6701"));


    }
    static  int call(String text) {
        Integer[] inputs  = Stream.of(text.split(" "))
                .map(Integer::parseInt)
                .<Integer>toArray(size-> new Integer[size]);
       return maxSubsetSum(inputs);
    }
}
