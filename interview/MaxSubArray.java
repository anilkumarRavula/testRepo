package interview;

import java.util.Arrays;

public class MaxSubArray {

    public static void main(String[] args) {
	// write your code here
        System.out.println(Arrays.toString(getMaxSubString("baaaaaabbbc")));
    }

    public static Object[] getMaxSubString(String input) {
        input = input.toLowerCase();
        //validation on empty
        //map to hold noted calculation

        // pointer for previous and current
        int currentCount = 1;
        //Character current = ;
          Character maxCharacter = input.charAt(0);
          int maxCount  = 1;
        // processing of characters
        for (int i = 0; i < input.length()-1; i++) {
            // note breaking of sequence
            if(input.charAt(i) !=  input.charAt(i+1) )  {
                if(currentCount >= maxCount) {
                    maxCharacter = input.charAt(i);
                    maxCount = currentCount;
                    currentCount = 1;
                }
            } else {
                currentCount++;
            }
        }
        // check wether end is max
        if(currentCount > maxCount) {
            maxCharacter = input.charAt(input.length()-1);
            maxCount = currentCount;

        }

        return new Object[] {maxCharacter,maxCount};
    }

}
