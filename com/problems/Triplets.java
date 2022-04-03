package com.problems;

import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

/*
* You are given an array and you need to find number of tripets of indices  such that the elements at those indices are in geometric progression for a given common ratio  and .
Example
There are  and  at indices  and . Return .
Function Description
Complete the countTriplets function in the editor below.
countTriplets has the following parameter(s):
int arr[n]: an array of integers
int r: the common ratio
Returns
int: the number of triplets
Input Format
The first line contains two space-separated integers  and , the size of  and the common ratio.
The next line contains  space-seperated integers .
Constraints
Sample Input 0

4 2
1 2 2 4
Sample Output 0
2
Explanation 0
There are  triplets in satisfying our criteria, whose indices are  and
Sample Input 1
6 3
1 3 9 9 27 81
Sample Output 1
6
Explanation 1
The triplets satisfying are index , , , ,  and .
Sample Input 2
5 5
1 5 5 25 125
Sample Output 2
4
Explanation 2
The triplets satisfying are index , , , .
*
 */
public class Triplets {

    static long countTriplets(List<Long> arr, long r) {
        Map<Long, Long> numberCounts = new HashMap<>();
        Map<String, Long> combinationCounts = new HashMap<>();
        long totalCount = 0;
        for (Long number : arr) {
            //update map with current count
            numberCounts.computeIfPresent(number, (key, value) -> value + 1l);
            numberCounts.putIfAbsent(number, 1l);
            // check for element in geometric progression by dividing r
            if (r == 1) continue;
            long firstRation = number / r;
            if(number%r != 0) continue;
            if (numberCounts.containsKey(firstRation)) {
                String firstElmntKey = getKey(number, firstRation);
                combinationCounts.computeIfPresent(firstElmntKey,
                        (key, value) -> combinationCounts.get(key) + numberCounts.get(firstRation));
                combinationCounts.putIfAbsent(firstElmntKey, numberCounts.get(firstRation));

                // check for second element in geometric progression by dividing r
                long secondRation = firstRation / r;
                String tripletCombo = getKey(firstRation, secondRation);
                if (numberCounts.containsKey(secondRation) && combinationCounts.containsKey(tripletCombo)) {
                    String secondTermComboKey = getKey(number, secondRation);
                    // add current count and new count if present for previous set
                    combinationCounts.computeIfPresent(secondTermComboKey,
                            (key, value) -> combinationCounts.get(key) + combinationCounts.get(tripletCombo));
                    combinationCounts.putIfAbsent(secondTermComboKey, combinationCounts.get(tripletCombo));
                    totalCount += combinationCounts.get(tripletCombo);
                }
            }
            System.out.println(combinationCounts);
            //System.out.println(numberCounts);

        }
        if (r == 1) {
            totalCount = numberCounts.values().stream().map(l -> (l*(l-1)*(l-2)/6)).reduce(0l, Long::sum);
        }
        return totalCount;
    }

    private static String getKey(long number1, long number2) {
        return number1 + "," + number2;
    }

    public static void main(String[] args) throws IOException {
     BufferedReader bufferedReader = new BufferedReader(new FileReader("D:\\anil\\codetestcases\\trippletsinput06.txt"));
        List<Long> arr = Stream.of(bufferedReader.readLine().split(" "))
                .map(Long::parseLong)
                .collect(toList());
        System.out.println(countTriplets(arr, 3l));
/*
        System.out.println(countTriplets(
                Arrays.asList(1l,17l,80l,68l,5l,5l,58l,17l,38l,81l,26l,44l,38l,6l,12l,11l,37l,67l,70l,16l,19l,35l,71l,16l,32l,45l,7l,39l,2l,14l,16l,78l,82l,5l,18l)
                , 3l));
*/

    }
}