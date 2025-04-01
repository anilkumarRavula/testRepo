package com.ds.arrays;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

/**
 * I want to flatten nested arrays like:
 *
 * [[[1],2],[3]],4] -> [1,2,3,4]
 * manually in java I can't find a clue ! :S
 *
 * I have tried a manual java script guide but it doesn't get a solution
 *
 * public static void main(String[] args) {
 *
 *   Object arr[] = { 1, 2, new Object[] { 4, new int[] { 5, 6 }, 7 }, 10 };
 */
public class FlatternArrays {
    public static void main(String[] args) {

        Object arr[] = { 1, 2, new Object[] { 4, new Object[] { 5, new Object[] {1,2} }, 7 }, 10 };
        List<Integer> list = new ArrayList<>();
        //addToList(list,arr);
        addToList(list,arr);
        System.out.println(list);
       // System.out.println(flatten(arr).collect(Collectors.toList()));

    }
    private static Stream<Object> flatten(Object[] array) {
        return Arrays.stream(array)
                .flatMap(o -> o instanceof Object[] ? flatten((Object[]) o): Stream.of(o));
    }

    public static void addToList(List<Integer> list, Object[] arr) {
        for (Object obj: arr) {
            if(obj instanceof Object[]) {
                addToList(list, (Object[]) obj);
            } else {
                list.add((Integer) obj);
            }
        }

    }
    public static void addToList(List<Integer> list, Object arr) {
            if(arr instanceof Object[]) {
                addToList(list, (Object[]) arr);
            } else {
                list.add((Integer) arr);
            }
    }
   /* public static void addToList(Integer[] list, Object[] arr) {
        for (Object obj: arr) {
            if(obj instanceof Object[]) {
                addToList(list, (Object[]) obj);
            } else {
                list[list.length]= (Integer) obj;
            }
        }

    }*/


}
