package com.ds.arrays.twopinter;

public class MaxContainer {

    public static void main(String[] args) {
        System.out.println(maxArea(new int[] {1,1}));
        System.out.println(maxArea(new int[] {2,2,100,100}));
        System.out.println(maxArea(new int[] {1,6,8,2,5,4,8,3,7}));
        System.out.println(maxArea(new int[] {1,8,6,2,5,4,8,3,7}));
        System.out.println(maxArea(new int[] {1,50,6,2,5,4,10,50,7}));
        System.out.println(maxArea(new int[] {100,50,2,5,4,10,7,8}));


    }
    public static int maxArea(int[] height) {
        int left = 0;
        int right = height.length-1;
        int maxWith = 0;
        while (left < right) {
             int width = (right-left) * (Math.min(height[left], height[right]));
             if(maxWith < width) {
                 maxWith = width;
             } else {
                 if (height[left] <= height[right]) {
                     left ++;
                 } else  {
                     right --;
                 }
             }

        }
        return maxWith;
    }

}
