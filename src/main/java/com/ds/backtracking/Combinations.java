package com.ds.backtracking;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Given a string containing digits from 2-9 inclusive, return all possible letter combinations that the number could represent. Return the answer in any order.
 *
 * A mapping of digits to letters (just like on the telephone buttons) is given below. Note that 1 does not map to any letters.
 *
 *
 *
 *
 * Example 1:
 *
 * Input: digits = "23"
 * Output: ["ad","ae","af","bd","be","bf","cd","ce","cf"]
 * Example 2:
 *
 * Input: digits = ""
 * Output: []
 * Example 3:
 *
 * Input: digits = "2"
 * Output: ["a","b","c"]
 */
public class Combinations {
    public static void main(String[] args) {
        System.out.println();
       // System.out.println(letterCombinations("2"));
       // System.out.println(letterCombinations("7"));
        System.out.println(letterCombinationsRecursive(""));
       /* System.out.println(letterCombinations("2345"));
        System.out.println(letterCombinations("237"));
        System.out.println(letterCombinations2("23"));
        System.out.println(letterCombinations2("2345"));
        System.out.println(letterCombinations2("237"));*/

    }
    public static List<String> letterCombinations(String digits) {
        String[] keyMapping = new String[] {"","","abc","def","ghi","jkl","mno","pqrs","tuv","wxyz"};
        List<String> inputs = new ArrayList<>();
        int totalCombinations = 1;
        for (char digit: digits.toCharArray()) {
            Integer value = digit - '0';
            totalCombinations = totalCombinations * keyMapping[value].length();
            inputs.add(keyMapping[value]);
        }

        List<String> output = new ArrayList<>();

        int combi = 0;
        int rep =1;
        int completedsets = 1 ;
        for (int i = 0; i < inputs.size() ; i++) {
            combi = 0;
            rep = totalCombinations /(inputs.get(i).length()*completedsets);
            completedsets = completedsets * inputs.get(i).length();
            while(combi < totalCombinations) {
                for (char sequence1: inputs.get(i).toCharArray()) {
                    int couunt= 0;
                    while(couunt < rep) {
                        if(i ==0 ) {
                            output.add(String.valueOf(sequence1) );
                        } else {
                            output.set(combi, output.get(combi)+sequence1);
                        }
                        combi++;
                        couunt++;
                    }
                }
            }
        }
        System.out.println(output.size()+"-->"+new HashSet<>(output).size());

        return output;
    }
    public static List<String> letterCombinations2(String digits) {
        String[] keyMapping = new String[] {"","","abc","def","ghi","jkl","mno","pqrs","tuv","wxyz"};
        List<String> inputs = new ArrayList<>();
        for (char digit: digits.toCharArray()) {
            Integer value = digit - '0';
            inputs.add(keyMapping[value]);
        }

        List<String> output = new ArrayList<>();

        for (int i = 0; i < inputs.size() ; i++) {
            List<String> oldOutPut = output;
            output = new ArrayList<>();
            for (char sequence1: inputs.get(i).toCharArray()) {
                    if(i ==0 ) {
                        output.add(String.valueOf(sequence1) );
                    } else {
                        for (int j = 0; j <oldOutPut.size()  ; j++) {
                            output.add(oldOutPut.get(j)+sequence1);
                        }
                    }
            }
        }

        System.out.println(output.size()+"-->"+new HashSet<>(output).size());
        return output;
    }
    public static List<String> letterCombinationsRecursive(String digits) {
        String[] keyMapping = new String[] {"","","abc","def","ghi","jkl","mno","pqrs","tuv","wxyz"};
        List<String> inputs = new ArrayList<>();
        for (char digit: digits.toCharArray()) {
            Integer value = digit - '0';
            inputs.add(keyMapping[value]);
        }


        List<String> output = buildList(inputs,inputs.size()-1);

        System.out.println(output.size()+"-->"+new HashSet<>(output).size());
        return output;
    }

    private static List<String> buildList(List<String> inputs, int position) {
            if(position < 0) return  new ArrayList<>();
        List<String> output = buildList(inputs,position-1);
            if(output.size() == 0) {
                output.addAll(inputs.get(position).chars().mapToObj(var-> String.valueOf((char)var)).collect(Collectors.toList()));
            } else {
                char[] charArray = inputs.get(position).toCharArray();
                int size = output.size();
                for (int i = 0; i < charArray.length; i++) {
                    char sequence1 = charArray[i];
                    for (int j = 0; j < size; j++) {
                        if(i == charArray.length -1) {
                            output.set(j,output.get(j) + sequence1);
                        } else {
                            output.add(output.get(j) + sequence1);
                        }
                    }
                }
            }

        return output;
    }

    public static List<String> letterCombinationsBackTrack(String digits) {
        String[] keyMapping = new String[] {"","","abc","def","ghi","jkl","mno","pqrs","tuv","wxyz"};
        List<String> inputs = new ArrayList<>();
        for (char digit: digits.toCharArray()) {
            Integer value = digit - '0';
            inputs.add(keyMapping[value]);
        }


        List<String> output = buildList(inputs,inputs.size()-1);

        System.out.println(output.size()+"-->"+new HashSet<>(output).size());
        return output;
    }

    private static List<String> backTrack(List<String> inputs, int position) {
        if(position < 0) return  new ArrayList<>();


        return output;
    }

}
