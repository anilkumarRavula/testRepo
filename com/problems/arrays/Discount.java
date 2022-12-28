/**

 A shopkeeper has a sale to complete and has arranged the items being sold in an array. Starting from the left, the shopkeeper rings up each item at its full price less the price of the first lower or equal priced item to its right. If there is no item to the right that costs less than or equal to the current item's price, the current item is sold at full price.



 For example, assume there are items priced [2, 3, 1, 2, 4, 2].

 The items 0 and 1 are each discounted by 1 unit, the first equal or lower price to the right.
 Item 2, priced 1 unit sells at full price because there are no equal or lower priced items to the right.
 The next item, item 3 at 2 units, is discounted 2 units, as would the item 4 at 4 units.
 The final item 5 at 2 units must be purchased at full price because there are no lower prices to the right.



 The total cost is 1 + 2 + 1 + 0 + 2 + 2 = 8 units. The full price items are at indices [2, 5] using 0 based indexing.



 Function Description

 Complete the function finalPrice in the editor below. The function must print the total cost of all items on the first line. On the second line, print a space-separated list of integers representing the indices of the items purchased at full price, in ascending index order.



 finalPrice has the following parameter(s):

 prices[prices[0],...prices[n-1]]:  an array of item prices



 Constraints

 1 ≤ n ≤ 105
 1 ≤ prices[i] ≤ 106, where 0 ≤ i < n


 Input Format for Custom Testing

 Input from stdin will be processed as follows and passed to the function.



 The first line contains an integer n, the size of the array prices.

 Each of the next n lines contains an integer prices[i] where 0 ≤ i < n.


 Sample Case 0

 Sample Input 0

 6
 5
 1
 3
 4
 6
 2



 Sample Output 0

 14
 1 5



 Explanation 0

 The prices of the items are given by the array:

 index     0  1  2  3  4  5
 prices = [5, 1, 3, 4, 6, 2]

 We can find the discount on each item:

 prices[0] = 5, first lower or equal price to the right = prices[1] = 1, discounted price = 5 - 1 = 4
 prices[1] = 1, lowest to the right = prices[5] = 2, no lower price to the right so no discount, price = 1
 prices[2] = 3, first lower or equal = prices[5] = 2, discounted price = 3 - 2 = 1
 prices[3] = 4, first lower or equal = prices[5] = 2, discounted price = 4 - 2 = 2
 prices[4] = 6, first lower or equal = prices[5] = 2, discounted price = 6 - 2 = 4
 prices[5] = 2, no lower or equal to the right, so no discount, price = 2

 The final discounted price is 4 + 1 + 1 + 2 + 4 + 2 = 14. There is no discount for item[1] or item[5].

 */
