package com.ds.dynamic;

import java.util.Arrays;
import java.util.Collection;

/**
 * You are given an integer array coins representing coins of different denominations and an integer amount representing a total amount of money.
 *
 * Return the fewest number of coins that you need to make up that amount. If that amount of money cannot be made up by any combination of the coins, return -1.
 *
 * You may assume that you have an infinite number of each kind of coin.
 *
 *
 *
 * Example 1:
 *
 * Input: coins = [1,2,5], amount = 11
 * Output: 3
 * Explanation: 11 = 5 + 5 + 1
 * Example 2:
 *
 * Input: coins = [2], amount = 3
 * Output: -1
 * Example 3:
 *
 * Input: coins = [1], amount = 0
 * Output: 0
 */
public class CoingChange {
    public static void main(String[] args) {
        System.out.println(new Solution().coinChange(new int[] {186,419,83,408},6249));
        System.out.println(new Solution().coinChange(new int[] {2},3));

        System.out.println(new Solution().coinChange(new int[] {1},0));

    }
    static class Solution {
        public int coinChange(int[] coins, int amount) {
            if(amount == 0 ) return 0;
            Arrays.sort(coins);
            System.out.println("===============");

            System.out.println(Arrays.toString(coins));
            System.out.println("===============");

            return coinChange(coins,amount,0,coins.length);
        }
        public int coinChange(int[] coins, int amount,int fromIndex,int toIndex) {
            System.out.println(amount+"--->"+fromIndex);
            if(amount == 0 ) return 0;
            int minCoins = -1;
            int index = Arrays.binarySearch(coins,fromIndex,toIndex,amount);
            //insertion point in range
            if(index < 0 ) {
                index = -(index+1) -1;
                if(index <0) return minCoins; // not in range
            }
            int coinsAdded = amount/coins[index];

            while(coinsAdded > 0) {
                System.out.println(coinsAdded+"--coins--->"+coins[index]+"-->"+(amount-(coins[index]*coinsAdded)));

                int additonalCoins = coinChange(coins,amount-(coins[index]*coinsAdded),0,index);
                //System.out.println(index +"add"+ additonalCoins);
                if(additonalCoins > -1) return coinsAdded+additonalCoins;
                coinsAdded--;

            }
            return minCoins;
        }
    }
}
