package src.com.ds.dynamic;

import java.util.stream.IntStream;

/**
 * You can perform the following operations on the string, :
 *
 * Capitalize zero or more of a's lowercase letters.
 * Delete all of the remaining lowercase letters in .
 * Given two strings,  and , determine if it's possible to make  equal to  as described. If so, print YES on a new line. Otherwise, print NO.
 *
 * For example, given  and , in  we can convert  and delete  to match . If  and , matching is not possible because letters may only be capitalized or discarded, not changed.
 *
 * Function Description
 *
 * Complete the function  in the editor below. It must return either  or .
 *
 * abbreviation has the following parameter(s):
 *
 * a: the string to modify
 * b: the string to match
 * Input Format
 *
 * The first line contains a single integer , the number of queries.
 *
 * Each of the next  pairs of lines is as follows:
 * - The first line of each query contains a single string, .
 * - The second line of each query contains a single string, .
 *
 * Constraints
 *
 * String  consists only of uppercase and lowercase English letters, ascii[A-Za-z].
 * String  consists only of uppercase English letters, ascii[A-Z].
 * Output Format
 *
 * For each query, print YES on a new line if it's possible to make string  equal to string . Otherwise, print NO.
 */
public class Abbrevation {
    public static void main(String[] args) {

        System.out.println(abbreviation("DLgHVgGIR"
                ,"DLHVGIR"));
    }
    public static String abbreviation(String a, String b) {
        // Write your code here
        int i = 0;
        int pointer = 0;
        String matched = "";
        while (i< b.length() && pointer < a.length() ) { //&& matched.length() <= b.length()
            boolean found = false;
            while (pointer < a.length() && found == false) {
                boolean isEqual = Character.isUpperCase(b.charAt(i)) ?
                        b.charAt(i) == Character.toUpperCase(a.charAt(pointer)) :
                        b.charAt(i) == a.charAt(pointer);
               if(isEqual) {
                   found = true;
                   matched = matched+b.charAt(i);
               } else if(Character.isUpperCase(a.charAt(pointer))){
                   if(matched.charAt(matched.length()-1) != a.charAt(pointer) )
                         matched = matched+a.charAt(pointer);
               }
               pointer++;
            }
            i++;
        }
        System.out.println(matched);

        System.out.println(pointer);
        //check for equals
        if(!matched.equals(b))  return "NO";
        //check a still has letters
        System.out.println("checking for uppercase");

        System.out.println(a.substring(pointer));
        if(pointer < a.length() && IntStream.range(pointer,a.length()).map(a::charAt).anyMatch((int letter) -> Character.isUpperCase(letter))) {
            return  "NO";
        }
        return "YES";
    }

}
