package com.ds.dynamic;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

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
        int[] coins = {186, 419, 83, 408};
        int amount = 6249;

        GptCoinChangeSolution.Result ans = GptCoinChangeSolution.coinChangeWithCombo(coins, amount);
        System.out.println("Min coins: " + ans.minCoins);
        if (ans.minCoins != -1) {
            System.out.println("One optimal combination (coin -> count):");
            for (Map.Entry<Integer,Integer> e : ans.counts.entrySet()) {
                System.out.println(e.getKey() + " -> " + e.getValue());
            }
        } else {
            System.out.println("No solution");
        }

       // System.out.println(new BadSolution().coinChange(new int[] {411,412,413,414,415,416,417,418,419,420,421,422},9864));
      // System.out.println(new Solution().coinChange(new int[] {186,419,83,408},6249));
       // System.out.println(new Solution().coinChange(new int[] {10,2,4,11},155));

      //  System.out.println(new Solution().coinChange(new int[] {5,4,3,2},11));

       // System.out.println(new Solution().coinChange(new int[] {2},3));

       // System.out.println(new Solution().coinChange(new int[] {1},0));

    }
    static class BadSolution {
        public int coinChange(int[] coins, int amount) {
            if(amount == 0 ) return 0;
            Arrays.sort(coins);
            //System.out.println("===============");
           // int[] minCoins = new int[coins.length];
           // Arrays.fill(minCoins,Integer.MAX_VALUE);
           System.out.println(Arrays.toString(coins));
            System.out.println("===============");
            int min = coinChange(coins,amount,0,coins.length,null);
           // System.out.println("output :"+min);
            return  min;
        }
        public int coinChange(int[] coins, int amount,int fromIndex,int toIndex,int[] minCoins) {

            if(amount == 0 ) return 0;

            int index = Arrays.binarySearch(coins,fromIndex,toIndex,amount);

            if(index == 0 && amount % coins[index] !=0) {
                return -1;
            }

            //insertion point in range
            if(index < 0) {
                index = -(index+1) -1;
                if(index < 0) return -1; // not in range
            }


            int finalMinCoins = Integer.MAX_VALUE;

            for(int indexPos = index; indexPos>=0 ; indexPos--) { //min for each index

                int coinsAdded = amount / coins[indexPos];

                int minForEachCoin = Integer.MAX_VALUE; // min for each choice of index
                if(amount % coins[indexPos] ==0) {
                    minForEachCoin = Math.min(minForEachCoin,coinsAdded);
                   // System.out.println(coins[indexPos]+"--minForEachCoin(I)--->"+coinsAdded);
                } else  if(indexPos != 0) {
                    while(coinsAdded > 0 && coinsAdded < finalMinCoins) {

                       System.out.println(coins[indexPos]+"--coins--->"+coinsAdded+"-->"+amount+"-- left :"+(amount-(coins[indexPos]*coinsAdded)));

                        int minCoinsFOrRemainingAMount = coinChange(coins,amount-(coins[indexPos]*coinsAdded),0,indexPos,minCoins);

                        if(minCoinsFOrRemainingAMount > -1) {
                            minForEachCoin = Math.min(minForEachCoin,minCoinsFOrRemainingAMount+coinsAdded);
                         System.out.println(coins[indexPos]+"--coins required for --->"+amount+ "--- IS : "+(minCoinsFOrRemainingAMount+coinsAdded));
                        }

                        coinsAdded--;

                    }
                }
                finalMinCoins = Math.min(minForEachCoin,finalMinCoins);
             System.out.println(coins[index]+"==============="+finalMinCoins);

            }

           System.out.println(coins[index]+"======FINAL========="+finalMinCoins);

            return finalMinCoins == Integer.MAX_VALUE ? -1 : finalMinCoins;
        }











        public int coinChange1(int[] coins, int amount,int fromIndex,int toIndex,int[] minCoins) {

            if(toIndex == 0) return 0;

            int index = Arrays.binarySearch(coins,fromIndex,toIndex,amount);
            //insertion point in range
            if(index < 0 ) {
                index = -(index+1) -1;
                if(index <0) return -1; // not in range
            }
            int finalMinCoins = Integer.MAX_VALUE;
            for(int indexPos = index; indexPos>=0 ; indexPos--) {
                int minForEachCoin = 0;

                int coinsAdded = amount/coins[index];

                while(coinsAdded > 0) {
                    System.out.println(coins[indexPos]+"--coins--->"+coinsAdded+"-->"+(amount-(coins[indexPos]*coinsAdded)));

                    int downStreamIndex = coinChange(coins,amount-(coins[indexPos]*coinsAdded),0,indexPos,minCoins);
                    if(downStreamIndex > 0) {
                        System.out.println(coins[indexPos] +" downSteam "+ coins[downStreamIndex]);
                        minCoins[indexPos] = Math.min(coinsAdded + minCoins[downStreamIndex], minCoins[indexPos]);
                        minCoins[downStreamIndex] = Integer.MAX_VALUE;
                    } else {
                        minCoins[indexPos] = coinsAdded;
                    }
                    System.out.println(Arrays.toString(minCoins));

                    coinsAdded--;

                }
                System.out.println(coins[indexPos] + "--final-->"+Arrays.toString(minCoins));


            }

            return index;
        }
    }



     class GptCoinChangeSolution {

        static class Result {
            int minCoins;
            Map<Integer, Integer> counts = new LinkedHashMap<>();
        }

        public static Result coinChangeWithCombo(int[] coins, int amount) {
            final int INF = amount + 1;

            // dp[i] = min coins to make sum i
            int[] dp = new int[amount + 1];
            Arrays.fill(dp, INF);
            dp[0] = 0;

            // prev[i] = coin used last to reach i (for path reconstruction)
            int[] prev = new int[amount + 1];
            Arrays.fill(prev, -1);

            // (Optional) keep coins in stable order for predictable output
            // (LinkedHashMap below will preserve this order in the result)
            int[] coinsCopy = Arrays.copyOf(coins, coins.length);
            // Not required to sort, but sorting can be helpful for readability:
            // Arrays.sort(coinsCopy);

            for (int coin : coinsCopy) {
                for (int i = coin; i <= amount; i++) {
                    if (dp[i - coin] + 1 < dp[i]) {
                        dp[i] = dp[i - coin] + 1;
                        prev[i] = coin;
                    }
                }
            }

            Result res = new Result();
            if (dp[amount] >= INF) {
                res.minCoins = -1; // impossible
                return res;
            }

            res.minCoins = dp[amount];

            // Reconstruct one optimal combination
            int curr = amount;
            // Use LinkedHashMap to preserve coin display order
            for (int c : coinsCopy) res.counts.put(c, 0);
            while (curr > 0) {
                int coin = prev[curr];
                if (coin == -1) break; // safety
                res.counts.put(coin, res.counts.get(coin) + 1);
                curr -= coin;
            }

            // Remove coins with zero count for cleaner output
            res.counts.entrySet().removeIf(e -> e.getValue() == 0);
            return res;
        }

        public static void main(String[] args) {
            int[] coins = {186, 419, 83, 408};
            int amount = 6249;

            Result ans = coinChangeWithCombo(coins, amount);
            System.out.println("Min coins: " + ans.minCoins);
            if (ans.minCoins != -1) {
                System.out.println("One optimal combination (coin -> count):");
                for (Map.Entry<Integer,Integer> e : ans.counts.entrySet()) {
                    System.out.println(e.getKey() + " -> " + e.getValue());
                }
            } else {
                System.out.println("No solution");
            }
        }
    }

}
