package src.com.ds.dynamic;

import java.util.HashMap;
import java.util.Map;
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
    static int  count = 0;
    public static void main(String[] args) {

        System.out.println(abbreviationAllCombinations("dlDDLgHVgGIRadc"
                ,"DLHVGIR",new HashMap<>()));

        System.out.println(count);
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
    public static boolean abbreviationAllCombinations(String a, String b, Map<String,Boolean > context) {
        System.out.println(a+","+b);
        if(b.length() > a.length()) return  false;
        //handling last element
        if(a.length() == 1) return isEqual(a,b,0,0);
        // Write your code here
        if(context.containsKey(b+","+a)) {
            return context.get(b+","+a);
        }
        int i = 0;
        int pointer = 0;
        while (i< b.length() && pointer < a.length() ) { //&& matched.length() <= b.length()
            boolean found = false;
            while (pointer < a.length() && found == false) {
                count++;
                boolean isEqual = isEqual(a, b, i, pointer);
                if(isEqual) {
                    found = true;
                    if(i+1 < a.length())
                    found = found && abbreviationAllCombinations(a.substring(Math.min(pointer+1, a.length()-1)),b.substring(i+1),context);

                    if(found)
                    context.put(b.substring(i)+","+a.substring(pointer),found);
                    if(!found && !Character.isUpperCase(a.charAt(pointer))) {
                        context.put(b.substring(i)+","+a.substring(pointer),false);
                        return false;
                    }

                } else if(Character.isUpperCase(a.charAt(pointer))){
                    context.put(b.substring(i)+","+a.substring(pointer),false);
                    return false;
                }
                pointer++;
            }
            if(!found) return  false;
            i++;
        }
        if(pointer < a.length() && IntStream.range(pointer,a.length()).map(a::charAt).anyMatch((int letter) -> Character.isUpperCase(letter))) {
            return  false;
        }
        System.out.println(context);
        return true;
    }

    private static boolean isEqual(String a, String b, int i, int pointer) {
        boolean isEqual = Character.isUpperCase(b.charAt(i)) ?
                b.charAt(i) == Character.toUpperCase(a.charAt(pointer)) :
                b.charAt(i) == a.charAt(pointer);
        return isEqual;
    }

}
