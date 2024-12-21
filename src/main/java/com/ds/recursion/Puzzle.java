package com.ds.recursion;

import java.util.*;
import java.util.stream.Collectors;

/**
 * A
 * <p>
 * Crossword grid is provided to you, along with a set of words (or names of places) which need to be filled into the grid. Cells are marked either + or -. Cells marked with a - are to be filled with the word list.
 * <p>
 * The following shows an example crossword from the input
 * grid and the list of words to fit,
 * <p>
 * :
 * <p>
 * Input 	   		Output
 * <p>
 * ++++++++++ 		++++++++++
 * +------+++ 		+POLAND+++
 * +++-++++++ 		+++H++++++
 * +++-++++++ 		+++A++++++
 * +++-----++ 		+++SPAIN++
 * +++-++-+++ 		+++A++N+++
 * ++++++-+++ 		++++++D+++
 * ++++++-+++ 		++++++I+++
 * ++++++-+++ 		++++++A+++
 * ++++++++++ 		++++++++++
 * POLAND;LHASA;SPAIN;INDIA
 */
public class Puzzle {
    public static void main(String[] args) {
        System.out.println(crosswordPuzzle(Arrays.asList("+-----+++"), "POLAND;LHASA;SPAIN;INDIA", new HashSet<>()));
    }

    public static List<String> crosswordPuzzle(List<String> crossword, String words, HashSet<String> compltedWords) {
        // Write your code here

        // iterate though each crossword and find gaps
        String[] wordsSplited = words.split(";");
        Map<Integer, Set<String>> wordsByLength = Arrays.stream(wordsSplited)
                .filter(wrd -> !compltedWords.contains(wrd))
                .collect(Collectors.groupingBy(String::length, Collectors.toSet()));
        if (wordsByLength.isEmpty()) return crossword;
        int bound = crossword.size();
        for (int index = 0; index < bound; index++) {
            String currentRow = crossword.get(index);
            int pointer = 0;
            while (pointer < currentRow.length() ) {
                int startPos = currentRow.substring(pointer).indexOf('-') ;
                    //check for row words
                    int endIndex = startPos > -1 ? startPos+1 : currentRow.length();
                    while (endIndex < currentRow.length() && currentRow.charAt(endIndex) == '-') {
                        endIndex++;
                    }
                    //check for column words
                    int colendIndex = startPos > -1 ? pointer+1 : bound;
                    while (colendIndex < bound && crossword.get(colendIndex).charAt(startPos) == '-') {
                        colendIndex++;
                    }
                    //endIndex-startPos gives word length
                    pointer= endIndex;
            }
            //check for column words


        }
        return crossword;
    }

    private void click() {
        /*for (int index = 0; index < bound; index++) {
            String currentRow = crossword.get(index);
            int startPos = currentRow.indexOf('-');
            if (startPos != -1) {
                int endPos = currentRow.lastIndexOf("-");
                int wordLengthRequired = endPos - startPos;
                System.out.println("s-->"+startPos+" e-->"+endPos);
                if (wordLengthRequired > 0) {
                    //horizontal word required
                    Set<String> matches = findWordsOfLength(compltedWords, wordsByLength, wordLengthRequired+1);
                    if (matches.size() == 1) {
                        compltedWords.add(matches.stream().findAny().get());
                    }
                    for (String match : matches) {
                        String newRow = currentRow.replaceAll(currentRow.substring(startPos, endPos+1), match);
                        crossword.set(index, newRow);
                        List<String> finalLIst = crosswordPuzzle(crossword, words, compltedWords);
                        if (!finalLIst.stream().anyMatch(s -> s.contains("-"))) {
                            crossword = finalLIst;
                        }
                    }
                }

            }
        }*/
    }

    private static Set<String> findWordsOfLength(HashSet<String> completed, Map<Integer, Set<String>> wordsGrouped, int length) {

        return wordsGrouped.get(length).stream().filter(word -> !completed.contains(word)).collect(Collectors.toSet());
    }

}
