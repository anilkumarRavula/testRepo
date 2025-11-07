package java8;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Input: arr[] = {"abc", "car", "ada", "racecar", "cool"}
 * Output: "ada", "racecar"
 * Explanation: These two are the only palindrome strings in the given array
 *
 *
 *
 *
 *
 * Input: arr[] = {"def", "ab"}
 * Output: -1
 * Explanation: No palindrome string is present in the given array.
 */
public class FindPallendromicStrings {
    public static void main(String[] args) {
        String[] strings= new String[] {"abc", "car", "ada","a", "racecar", "cool"};
        System.out.println(Arrays.stream(strings).filter(FindPallendromicStrings::isPallendrome).toList());
    }
    static boolean isPallendrome(String s) {
        return IntStream.range(0,s.length()/2).allMatch(index->s.charAt(index) == s.charAt(s.length()-index-1));
    }

}
