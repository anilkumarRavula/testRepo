package com.ds.dynamic;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Given a string s and a dictionary of strings wordDict, return true if s can be segmented into a space-separated sequence of one or more dictionary words.
 *
 * Note that the same word in the dictionary may be reused multiple times in the segmentation.
 *
 *
 *
 * Example 1:
 *
 * Input: s = "leetcode", wordDict = ["leet","code"]
 * Output: true
 * Explanation: Return true because "leetcode" can be segmented as "leet code".
 * Example 2:
 *
 * Input: s = "applepenapple", wordDict = ["apple","pen"]
 * Output: true
 * Explanation: Return true because "applepenapple" can be segmented as "apple pen apple".
 * Note that you are allowed to reuse a dictionary word.
 * Example 3:
 *
 * Input: s = "catsandog", wordDict = ["cats","dog","sand","and","cat"]
 * Output: false
 */
public class WordBreak {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("Anil".substring(0));
        System.out.println(new SolutionV3().wordBreak("aaaaaaa",List.of("aaaa","aaa")));
        System.out.println(new SolutionV3().wordBreak("catsandog",
                List.of("cats","dog","sand","and","cat")));

        System.out.println(new SolutionV3().wordBreak("catsandogcat",
                List.of("cats","dog","sand","and","cat","an")));
        System.out.println(new SolutionV3().wordBreak("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaab",
                List.of("a","aa","aaa","aaaa","aaaaa","aaaaaa","aaaaaaa","aaaaaaaa","aaaaaaaaa","aaaaaaaaaa")));


    }
    /**fanal solution mine*/
    public static class SolutionV4 {

        public boolean wordBreak(String s, List<String> wordDict) {

            HashSet<String> dictionary = new HashSet<>(wordDict);
            boolean[] postionArray = new boolean[s.length()];
            return canItBreak(s,0, dictionary,postionArray);
        }

        private boolean canItBreak(String s, int index, HashSet<String> dictionary,boolean[] postionArray) {

            if(s.isEmpty() || index >= s.length()) return true;
            StringBuilder current = new StringBuilder();
            if(postionArray[index]) return false;
            int j = index;
            while(j < s.length()) {
                current.append(s.charAt(j));
                if(dictionary.contains(current.toString())) {
                    if(canItBreak(s,j+1,dictionary,postionArray)) {
                        return true;
                    }
                }
                j++;
            }
            //mark index
            postionArray[index] = true;
            return false;
        }

    }
    public static class SolutionV3 {

        public boolean wordBreak(String s, List<String> wordDict) throws InterruptedException {

            HashSet<String> dictionary = new HashSet<>(wordDict);
            int[] postionArray = new int[s.length()];
            return canItBreak(s,0, dictionary,postionArray);
        }

        private boolean canItBreak(String s, int index, HashSet<String> dictionary,int[] postionArray) {
            System.out.println("--"+s.substring(index)+"---"+index);
            if(s.isEmpty() || index >= s.length()) return true;
            StringBuilder current = new StringBuilder();
            int j = index;
            while(j < s.length()) {
                current.append(s.charAt(j));
                if(dictionary.contains(current.toString()) && (j == s.length()-1  || (j <  s.length()-1 && postionArray[j+1] != 1))) {
                   if(canItBreak(s,j+1,dictionary,postionArray)) {
                       return true;
                   }
                }
                j++;
            }
            //mark index
            postionArray[index] = 1;
            return false;
        }

    }
    public static class SolutionV2 {
        public boolean wordBreak(String s, List<String> wordDict) throws InterruptedException {


            HashSet<String> dictionary = new HashSet<>(wordDict);

            if(dictionary.contains(s)) return  true;

            int[] postions = new int[s.length()];
            List<String> words = new ArrayList<>();

            int subStringLegth = 0;
            int poisition = 0;
            StringBuilder currentString = new StringBuilder();

            while((poisition < s.length() || !words.isEmpty()) && subStringLegth < s.length() ) {

                //not found full word there are partial matches
                if(poisition >= s.length()) {
                    currentString.setLength(0);
                    currentString.append(words.remove(words.size()-1));
                    subStringLegth = subStringLegth - currentString.length();
                    poisition = subStringLegth+currentString.length();
                    System.out.println(currentString+"---"+poisition);
                    //Thread.sleep(2000);

                }

                currentString.append(s.charAt(poisition));

                //when found a word in dictory  add that word to list and look for rest of th eword
                if(dictionary.contains(currentString.toString()) && postions[Math.min(poisition+1,s.length()-1)] != 1) {
                    if(poisition == (s.length()-1)) return true;
                    if(dictionary.contains(s.substring(poisition+1))) {
                        return true;
                    } else {
                        words.add(currentString.toString());
                        subStringLegth+=currentString.length();
                        currentString.setLength(0);
                        postions[poisition+1] = 1; //mark for this postion we can't process any string
                    }
                }
                poisition++;
                System.out.println(words);
            }
            System.out.println(subStringLegth);
            return subStringLegth == s.length();
        }
    }

   public static class Solution {
        public boolean wordBreak(String s, List<String> wordDict) throws InterruptedException {


            HashSet<String> dictionary = new HashSet<>(wordDict);

            if(dictionary.contains(s)) return  true;

            int[] postions = new int[s.length()];
            List<String> words = new ArrayList<>();

            int subStringLegth = 0;
            int poisition = 0;
            String currentString = "";
            while((poisition < s.length() || !words.isEmpty()) && subStringLegth < s.length() ) {

                //not found full word there are partial matches
                if(poisition >= s.length()) {
                    currentString = words.remove(words.size()-1);
                    subStringLegth = subStringLegth - currentString.length();
                    poisition = subStringLegth+currentString.length();
                    System.out.println(currentString+"---"+poisition);
                    //Thread.sleep(2000);

                }

                currentString+=s.charAt(poisition);

                //when found a word in dictory  add that word to list and look for rest of th eword
                if(dictionary.contains(currentString) && postions[Math.min(poisition+1,s.length()-1)] != 1) {
                    if(poisition == (s.length()-1)) return true;
                    if(dictionary.contains(s.substring(poisition+1))) {
                        return true;
                    } else {
                        words.add(currentString);
                        subStringLegth+=currentString.length();
                        currentString="";
                        postions[poisition+1] = 1; //mark for this postion we can't process any string
                    }
                }
                poisition++;
                System.out.println(words);
            }
            System.out.println(subStringLegth);
            return subStringLegth == s.length();
        }
    }
    class LeeSolution {
        public boolean wordBreak(String s, List<String> wordDict) {
            boolean[] dp = new boolean[s.length() + 1];
            dp[0] = true;
            for(int i = 1; i <= s.length(); i++) {
                for(String cur : wordDict) {
                    int start = i - cur.length();
                    if(start >= 0 && dp[start] && s.substring(start, i).equals(cur)){
                        dp[i] = true;
                        break;
                    }
                }
            }
            return dp[s.length()];
        }
    }
}
