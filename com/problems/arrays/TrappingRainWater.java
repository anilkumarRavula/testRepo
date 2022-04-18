package com.problems.arrays;

/**
 * Given n non-negative integers representing an elevation map where the width of each bar is 1, compute how much water it can trap after raining.
 * Input: height = [0,1,0,2,1,0,1,3,2,1,2,1]
 * Output: 6
 * <img src="rainwatertrap.png" />
 * Explanation: The above elevation map (black section) is represented by array [0,1,0,2,1,0,1,3,2,1,2,1]. In this case, 6 units of rain water (blue section) are being trapped.
 */
public class TrappingRainWater {
    public static int trap(int[] height) {
        int storedWaterUnits = 0;
        int previousMax = height[0];

        for (int i = 1; i <height.length ; i++) {
            int current = height[i];

            if(current > height[i-1]) {

                //find point where the privious value is equal to current and greter than or equal to previous max
                int unitsCanBefilled = 0;
                int lowestHeight =Math.min(previousMax,current);
                int j = i-1;
                while (j >=0 && height[j] < lowestHeight) {
                    int heightValue = height[j];
                    int unitesAtThisHeight = lowestHeight-heightValue;
                    height[j] = heightValue+unitesAtThisHeight;
                    unitsCanBefilled+=unitesAtThisHeight;
                    j--;
                }
                previousMax = Math.max(previousMax,current);
                storedWaterUnits+=unitsCanBefilled;
            }

        }
        return storedWaterUnits;
    }

    public static void main(String[] args) {
        System.out.println(trap(new int[]{5,4,1,2}));
    }

}
