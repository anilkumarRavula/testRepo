package interview;

/**
 * version -1 ==> max one element
 * [1,1,2] === [1,2,null]
 * [1,1,1,1,22,22] === [1,22]
 *
 * version2 == max 2 eleemnts
 * [1,1,2] === [1,1,2]
 *[1,1,1,1,22,22] === [1,1,22,22]
 *
 */
public class RemoveDuplicates {

    public static void main(String[] args) {
        System.out.println(new Solution().removeDuplicates(new int[] {1,1,2,3,3,4,5,5,5,5},1));
        System.out.println(new Solution().removeDuplicates(new int[] {1,1,2,3,3,4,5,5,5,5},4));

    }

    public static  class Solution {

        public int removeDuplicates(int[] arr, int maxOccurances) {
            int left= 0;
            int duplicateCount = 0;
            for (int i = 1; i <  arr.length; i++) {
                if(arr[left] != arr[i]) {
                    duplicateCount += Math.max(i-left-maxOccurances,0);
                    left = i;
                }
            }
            duplicateCount += Math.max(arr.length-left-maxOccurances,0);

            return arr.length-duplicateCount;
        }
    }

}
