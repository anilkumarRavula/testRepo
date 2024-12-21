package com.ds.arrays;
/**
 * Given an array of integers citations where citations[i] is the number of citations a researcher received for their ith paper, return the researcher's h-index.
 *
 * According to the definition of h-index on Wikipedia: The h-index is defined as the maximum value of h such that the given researcher has published at least h papers that have each been cited at least h times.
 *
 *
 *
 * Example 1:
 *
 * Input: citations = [3,0,6,1,5]
 * Output: 3
 * Explanation: [3,0,6,1,5] means the researcher has 5 papers in total and each of them had received 3, 0, 6, 1, 5 citations respectively.
 * Since the researcher has 3 papers with at least 3 citations each and the remaining two with no more than 3 citations each, their h-index is 3.
 * Example 2:
 *
 * Input: citations = [1,3,1]
 * Output: 1
 *
 *
 * Constraints:
 *
 * n == citations.length
 * 1 <= n <= 5000
 * 0 <= citations[i] <= 1000

 */
public class Hindex {
    public static void main(String[] args) {
       System.out.println(hIndex(new int[] {1,1,3,6,7,10,7,1,8,5,9,1,4,4,3}));
       System.out.println(hIndex(new int[] {11,15}));
        System.out.println(hIndex(new int[] {1,7,9,4}));
       System.out.println(hIndex(new int[] {1,6,2,4,1,3,1,4,1,4}));

    }

    public static int hIndex(int[] citations) {
        int index = 0;
        int largerElements = 0;
        for (int i = 0; i < citations.length; i++) {

            if(citations[i] > index) {
                largerElements++;
            }
            if(largerElements > index) {
                index++;
                largerElements = 0;
            }
        }
        int maxHindex = (index)*(index+1)/2+largerElements;
        System.out.println(index+"---"+maxHindex);
        int count = 0;
        int hindex = maxHindex;

        for (int i = 0; i < citations.length; i++) {

            if(citations[i] > index) {
                hindex = Math.min(citations[i],hindex);
                count++;
            }
        }
        System.out.println(hindex+"---"+count);
        if(count < hindex) {
            hindex = Math.max(index,count);
        }

        return hindex;
    }


}
