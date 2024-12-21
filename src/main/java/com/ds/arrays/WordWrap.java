package com.ds.arrays;

import java.util.ArrayList;
import java.util.List;

/**
 * Given an array of strings words and a width maxWidth, format the text such that each line has exactly maxWidth characters and is fully (left and right) justified.
 *
 * You should pack your words in a greedy approach; that is, pack as many words as you can in each line. Pad extra spaces ' ' when necessary so that each line has exactly maxWidth characters.
 *
 * Extra spaces between words should be distributed as evenly as possible. If the number of spaces on a line does not divide evenly between words, the empty slots on the left will be assigned more spaces than the slots on the right.
 *
 * For the last line of text, it should be left-justified, and no extra space is inserted between words.
 *
 * Note:
 *
 * A word is defined as a character sequence consisting of non-space characters only.
 * Each word's length is guaranteed to be greater than 0 and not exceed maxWidth.
 * The input array words contains at least one word.
 */
public class WordWrap {
    public static void main(String[] args) {
       // System.out.println(fullJustify(new String[]{"hello"},10)); // ["This", "is", "an", "example", "of", "text", "justification."]
     // System.out.println(fullJustify(new String[]{"This", "is", "an", "example", "of", "text", "justification."},16));
       // System.out.println(fullJustify(new String[]{"What","must","be","acknowledgment","shall","be"},16));
        System.out.println(fullJustify(new String[]{"everything","else","we","do"},20));

     System.out.println(fullJustify(new String[]{"Science","is","what","we","understand","well","enough","to","explain","to","a","computer.","Art","is","everything","else","we","do"},20));


    }
    public static  List<String> fullJustify(String[] words, int maxWidth) {
        List<String> wrappedText = new ArrayList<>();
        int legthCovered = 0;
        int checkPoint = -1;
        StringBuilder finalText = new StringBuilder();
        for (int pointer = 0; pointer < words.length; pointer++) {
            // check if currntWord can fit to mazWidth
            if (maxWidth < ((legthCovered == 0 ? legthCovered : legthCovered+1) + words[pointer].length())) { //16 < 4  16< 6 16< 9 16 < 11+7* , 16 < 7
                //spaces
                int spacesCount = pointer - (checkPoint+1) -1; //evaluate  3 - (0) -1 = 2    6-(3) -1 2
                int addtionalSpaces = maxWidth - legthCovered; // 16-11 = 5       16- 14 2
                int distribution = spacesCount ==0 ? addtionalSpaces: addtionalSpaces / spacesCount; // 5/2 = 2; 2/2 = 1
                int extraSpaces = addtionalSpaces % Math.max(1,spacesCount);
                //append all from last point
                boolean writeSpaces = false;
                for (int j = checkPoint+1; j < pointer  ; j++) { // 0 ,1 [i,checkpoint)
                    if(writeSpaces) {
                        addSpaces(distribution,finalText); //This__
                        if(j >=  checkPoint+2 && extraSpaces >0) {  //This___
                            addSpaces(1,finalText);
                            extraSpaces --;
                        }
                    } else  {
                        writeSpaces = true;
                    }
                    finalText.append(words[j]); // This,  This____IS___AN
                    if(spacesCount ==0) {
                        addSpaces(distribution,finalText); //This__
                    }
                }
                checkPoint = pointer-1; // 2
                legthCovered = words[pointer].length(); //0
                wrappedText.add(finalText.toString());
                finalText = new StringBuilder();
            } else {
                if(legthCovered > 0) { //append space betweem words  t
                    words[pointer] = " "+words[pointer];            //  " is" " AN"
                }
                legthCovered += words[pointer].length(); //len = 4 7 11   7  10 14
            }
            //if last word
        }
        StringBuilder text = new StringBuilder();
        while(++checkPoint < words.length) {
            text.append(words[checkPoint]);
        }
        int spacesRequired = maxWidth - text.length();
        addSpaces(spacesRequired,text);
        wrappedText.add(text.toString());
        return wrappedText;
    }
    public static  List<String> fullJustifyV2(String[] words, int maxWidth) {
        List<String> wrappedText = new ArrayList<>();
        int legthCovered = 0;
        int checkPoint = -1;
        StringBuilder finalText = new StringBuilder();
        for (int pointer = 0; pointer < words.length; pointer++) {
            // check if currntWord can fit to mazWidth
            if (maxWidth < ((legthCovered == 0 ? legthCovered : legthCovered+1) + words[pointer].length())) { //16 < 4  16< 6 16< 9 16 < 11+7* , 16 < 7
                //spaces
                int spacesCount = pointer - (checkPoint+1) -1; //evaluate  3 - (0) -1 = 2    6-(3) -1 2
                int addtionalSpaces = maxWidth - legthCovered; // 16-11 = 5       16- 14 2
                int distribution = spacesCount ==0 ? addtionalSpaces: addtionalSpaces / spacesCount; // 5/2 = 2; 2/2 = 1
                int extraSpaces = addtionalSpaces % Math.max(1,spacesCount);
                //append all from last point
                boolean writeSpaces = false;
                for (int j = checkPoint+1; j < pointer  ; j++) { // 0 ,1 [i,checkpoint)
                    if(writeSpaces) {
                        addSpaces(distribution,finalText); //This__
                        if(j >=  checkPoint+2 && extraSpaces >0) {  //This___
                            addSpaces(1,finalText);
                            extraSpaces --;
                        }
                    } else  {
                        writeSpaces = true;
                    }
                    finalText.append(words[j]); // This,  This____IS___AN
                    if(spacesCount ==0) {
                        addSpaces(distribution,finalText); //This__
                    }
                }
                checkPoint = pointer-1; // 2
                legthCovered = words[pointer].length(); //0
                wrappedText.add(finalText.toString());
                finalText = new StringBuilder();
            } else {
                if(legthCovered > 0) { //append space betweem words  t
                    words[pointer] = " "+words[pointer];            //  " is" " AN"
                }
                legthCovered += words[pointer].length(); //len = 4 7 11   7  10 14
            }
            //if last word
        }
        StringBuilder text = new StringBuilder();
        while(++checkPoint < words.length) {
            text.append(words[checkPoint]);
        }
        int spacesRequired = maxWidth - text.length();
        addSpaces(spacesRequired,text);
        wrappedText.add(text.toString());
        return wrappedText;
    }
    private static  void addSpaces(int spacesRequired,StringBuilder textStream) {
        while(spacesRequired > 0 ) {
            textStream.append(" ");
            spacesRequired --;
        }
    }

}
