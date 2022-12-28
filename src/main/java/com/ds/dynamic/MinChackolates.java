package com.ds.dynamic;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/*
* Alice is a kindergarten teacher. She wants to give some candies to the children in her class.  All the children sit
* in a line and each of them has a rating score according to his or her performance in the class.
*  Alice wants to give at least 1 candy to each child. If two children sit next to each other, then the one with the higher rating must get more candies.
*  Alice wants to minimize the total number of candies she must buy.

Example


She gives the students candy in the following minimal amounts: . She must buy a minimum of 10 candies.
 */
public class MinChackolates {

    public static long candies(List<Integer> arr) {
        // Write your code here
        System.out.println("input--->"+arr);
        long[] data = new long[arr.size()];
        data[0]  = 1;
        for (int i = 1; i < arr.size() ; i++) {
            long prevoiusCount = data[i-1];
            if(arr.get(i) < arr.get(i-1)) {
                if(prevoiusCount == 1)  {
                    data[i] = prevoiusCount;
                    data[i-1] = prevoiusCount+1;
                } else {
                    data[i] = 1;
                }
            } else if(arr.get(i) > arr.get(i-1)) {
                data[i] = prevoiusCount+1;
            } else {
                data[i] = 1;
            }
        }
        System.out.println(Arrays.toString(data));
        long minChackolates = data[data.length-1];
        for (int i = data.length-2; i >=0 ; i--) {

            long prev = arr.get(i +1);
            long preValue = data[i +1];

            long current = arr.get(i);
            long currentVal = data[i];
            //case 1
            if(current > prev) {
                if(currentVal <= preValue) data[i] = preValue+1;
            } else if(current < prev) {
                if(currentVal == preValue) {
                    System.out.println("something worng");
                    data[i] = preValue-1;
                } // it might make imbalance balance

            } else {
                //previous value is higher no issues
               // System.out.println("no change");
            }
            minChackolates += data[i];
        }
        //System.out.println(Arrays.toString(data));

        //System.out.println("");
        return minChackolates;
    }

    public static void main(String[] args) {
        List<Integer> arr = Arrays.asList(1,2,3,3,2,1);
        //Collections.shuffle(arr);
        System.out.println(candies(arr));
    }
}
