package com.ds.arrays;

/**
 *
 */
public class CandyLeastMinimiumRequired {
    public static void main(String[] args) {
        System.out.println(candy(new int[]{1,3,4,5,2}));
        System.out.println(candy(new int[]{1,2,2}));
        System.out.println(candy(new int[]{1,3,4,3,2,1,3}));
        System.out.println(candy(new int[]{2,2,2,2,1}));
        System.out.println("---");
        System.out.println(candy2(new int[]{1,3,4,5,2}));
        System.out.println(candy2(new int[]{1,2,2}));
        System.out.println(candy2(new int[]{1,3,4,3,2,1,3}));
        System.out.println(candy2(new int[]{2,2,2,2,1}));
        System.out.println(candy2(new int[]{1,3,4,3,2,1,3}));

    }
    public static int candy(int[] ratings) {
        int previousHigh = 1;
        int previous = ratings[0];
        int minCandiesRequired = 1;
        for (int i = 1; i < ratings.length ; i++) {

            if(ratings[i] > previous) {
                minCandiesRequired = minCandiesRequired + (previousHigh+1);
                previousHigh+=1;
                previous = ratings[i];
            } else if(ratings[i] == previous) {
                minCandiesRequired += 1;
                previousHigh = 1;
                previous = ratings[i];
            } else {
                int previousPos = i;
                while (i < ratings.length && ratings[i] < previous) {
                    previous = ratings[i];
                    i++;
                }
                //add all low rating to minCandies
                int lowRankingsFromhigh = i - previousPos;
                minCandiesRequired = minCandiesRequired+ (lowRankingsFromhigh * (lowRankingsFromhigh + 1))/2;
               if(previousHigh < lowRankingsFromhigh+1)
                minCandiesRequired += Math.abs(previousHigh - (lowRankingsFromhigh+1));
                //point back to previous postion as for has increnement
                i --;
                previousHigh = 1;
            }
        }
        return minCandiesRequired;
    }
    public static int candy2(int[] ratings) {
        int previous = ratings[0];
        int minCandiesRequired = 0;
        ratings[0] = 1;
        for (int i = 1; i < ratings.length ; i++) {
            if(ratings[i] > previous) {
                previous = ratings[i];
                ratings[i] = Math.max(ratings[i-1],1)+1;
            } else if(ratings[i] == previous) {
                previous = ratings[i];
                ratings[i] = 1;
            } else {
                previous = ratings[i];
                ratings[i] = -1;
            }
        }
        int negatives = 0;
        for (int i = ratings.length-1; i >= 0 ; i--) {
            if(ratings[i] == -1) {
                negatives++;
                ratings[i] = negatives;
            } else {
                ratings[i] = Math.max(ratings[i], negatives+1);
                negatives = 0;
            }
            minCandiesRequired = minCandiesRequired+ ratings[i];
        }
        return minCandiesRequired;
    }
}
