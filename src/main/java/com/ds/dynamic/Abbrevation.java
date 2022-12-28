package com.ds.dynamic;

import java.sql.Struct;
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

       /* System.out.println(abbreviationAllCombinations("dlDDLgHVgGIRadc"
                ,"DLHVGIR",new HashMap<>()));

        System.out.println(count);*/
        String a = "XbxxobxBobbbxooXobXxxBOXoOboxxbobXOoBbxbXooXBboxooOxxXbboxoOxlobbObbXoXXbbXobbbXoxbxXBxoobooxbxoxoxOxxOxbxbxXobbbbBbxoxoooxooobXxbooBbOXxXxbxqobbbboXxoXXbbbxObXXxobOXXOxoOoxoXOXBxOxBoxbobxoBxbobobXooOxxOBXbxxXbooxbxooOxoxoobxxBOxxbbbxBxzXxbBxOobBObooofbbBXXOxxoxxbXBbOboxxooBbxOoboXoooXBbBbooOoBbbObxobxbBBoOxoxobBoOXXobObxobxOObobbbxxoboxoXxbXoxxxxbbobbXoXooBXXxboxbobxxxXboxOoOoxBoboOXboBoobXobxXdxObbbBxbxBbOOXbxooXboxboonxxxXOBbbXXoobooxbbxboxoOxBBbxBOxoobXbbxxbXXObxBbxBXBxoxOxoBbxBobOXbboxooBxbooXbXbooBbbxXboxXbxXoxbboxOXOooXbobooXXoxobbxoOxOoBbxxoBboboxoOBBxoboBoOboxbbxxbbbObXbboXbObOjXOXBxbxXobbbboBxBoOooxbxxOooxxbxxobbobxbbXoOobbBXoObxobXxoobxBxBbxoobXxoxObboxobobooxOoooBBbbbxxXoxbXxoXooxOBxboobxooxXOxobXoXmObxxXObooXXXboOXxbXxObxxbbObObxbxxbxxBXxBxoxOooaxooxXBXoXOxoOXxbBoBXxXooboXboOooxoxOxXxbxoboOObbBoXxbboxxooBBbooxXBbBoxBOobbboobobooxoxOxoXOXXboxXOboBxoboOooxbxBxobooXOoxOOObbxbobxxoxbOBoBxboxoobbbxoooxBxoobBbobBbooOBbxoboooookxXoobbbbBbOoxOBOobXObXBxoXoboxobbXBXBBoxBxoxooOxobxo";
        String b = "XBOBBOBOXOXBOXOOBOXOXOBBXOXBXOXXBBOBXOXXXOBBXBOOOXXOXBBBXOXOOOXXOBBOXXOBBXXXOXXXOXXOOXOXBOBBBXBBXOOXOBXXOOOOBXBOXXBXBXXXBXOBBOBBXXOXXBOBBXOXXBBOOOBBBOXBBBOXXBXXOBOBXOOOXXXXXXOBXXBOXXOOOOBOOOXBBOOBXOXXOBBBBOOXXOOXXXOBBXXOXBBOXOXBBBOXOBXXXBXXOBBXBOOBBBOXBBBOXBXBOBBXXXOXBOOXOOXBOXXOOOOBBBBOOBBOBOOBOBXBBOXBOBOXOXBXOBBOBBOXBOOXXBBBBBXOBXOBXXXBBBXOOBOOOXOOBBBXXOXXOXOBOXOBXXOXOOXXXOXXOBOOXBBXBOXBXXOXOXBOBXXOOXOOOXXBOOBBXXXBBOXBBXBOBBOOBOOXOXXBXOBOOOXBOXOOXOOOBBOBBOOOBBBBBOOBOXBBBOBOBXOXBXOBXBXBXBBBXOOO";
        System.out.println(canMakeString(a,b,0,0,new HashMap<>()));
        System.out.println(count);
    }
    private static boolean isCharactersSame(String a, String b, int apointer, int bpointer) {
        return b.charAt(bpointer) == a.charAt(apointer);
    }
    private static boolean canCaptilizeAndMakeEquals(String a, String b, int apointer, int bpointer,Map<String,Boolean> result) {
        count++;

        return Character.isUpperCase(b.charAt(bpointer))
                && (b.charAt(bpointer) == Character.toUpperCase(a.charAt(apointer))
                && canMakeString(a, b, apointer + 1, bpointer + 1,result));
    }

    public static  boolean canMakeString(String a, String b,int aPointer, int bPointer,Map<String,Boolean> result) {
        count++;
       // System.out.println(a.substring(aPointer)+"-->"+b.substring(bPointer));
      //  System.out.println(aPointer+"-->"+bPointer+"-->"+same);

        if(aPointer >= a.length()) {
            if(bPointer < b.length() ) {
                return false;
            }
        }

        if(bPointer >= b.length()) {
            for (int i = aPointer; i < a.length(); i++) {
                if(Character.isUpperCase(a.charAt(i))) {
                    return  false;
                }
            }
            return true;
        }
        if(result.containsKey(aPointer+"-->"+bPointer)) return result.get(aPointer+"-->"+bPointer);
        boolean same = true;
        //28-12-22
        if(a.equals(b)) return true;

       same =  same &&   isCharactersSame(a,b,aPointer,bPointer) ?
               canMakeString(a,b,aPointer+1,bPointer+1,result)
               :
               ( (!Character.isUpperCase(a.charAt(aPointer)) && canMakeString(a,b,aPointer+1,bPointer,result)) || canCaptilizeAndMakeEquals(a,b,aPointer,bPointer,result)) ;

       // System.out.println(aPointer+"-->"+bPointer+"-->"+same);
        result.put(aPointer+"-->"+bPointer,same);
        return same;
    }



}
