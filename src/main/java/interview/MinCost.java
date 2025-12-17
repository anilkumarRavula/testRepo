package interview;

/**
 *Wal
 * Given 2D grid of size 4x4, where each cell represents the cost to travel from it,  find the minimum cost to move from top left cell to bottom right cell.
 * Constraints - 4 direction movement is allowed. Left, right, up , down
 * Output - print the path
 *
 *      {   0
 *     0    {9, 4, 9, 9},
 *         {6, 7, 6, 4},
 *         {8, 3, 3, 11},
 *         {7, 4, 9, 10}
 *      }
 *
 *  //dfs
 *  //backtrack approach  wh
 *
 *
 */

import java.util.*;

/**
 * Given a square grid of size N, each cell of which contains an integer cost that represents a cost to traverse through that cell, we need to find a path from the top left cell to the bottom right cell by which the total cost incurred is minimum.
 * From the cell (i,j) we can go (i,j-1), (i, j+1), (i-1, j), (i+1, j).
 *
 * Examples :
 *
 * Input: grid = {{9,4,9,9},{6,7,6,4},{8,3,3,7},{7,4,9,10}}
 * Output: 43
 * Explanation: The grid is-
 * 9 4 9 9
 * 6 7 6 4
 * 8 3 3 7
 * 7 4 9 10
 * The minimum cost is-
 * 9 + 4 + 7 + 3 + 3 + 7 + 10 = 43.
 * Input: grid = {{4,4},{3,7}}
 * Output: 14
 * Explanation: The grid is-
 * 4 4
 * 3 7
 * The minimum cost is- 4 + 3 + 7 = 14.
 * Expected Time Complexity: O(n2*log(n))
 * Expected Auxiliary Space: O(n2)
 *  Constraints:
 * 1 ≤ n ≤ 500
 * 1 ≤ cost of cells ≤ 500
 */

public class MinCost {
    static int[][] travelCOsts = new int[][]{new int[]
                     {9, 4, 9, 9},
            new int[]{6, 7, 6, 4},
            new int[]{8, 3, 3, 11},
            new int[]{7, 4, 9, 10}};
    static int[]sourceROw = {0,0};
    static int[] dest = {3,3};

    static int[][] directions  = {
            {-1,0}, //top
            {0,1}, //right
            {1,0},//down}
            {0,-1}//left
    };

    public static void main(String[] args) {
        System.out.println("Hello, World!");
        System.out.println(Arrays.compare(new int[] {3,3},dest));
        System.out.println(new Solution().minimumCostPath(travelCOsts,sourceROw,dest ));
    }

    static class Solution {

        //Function to return the minimum cost to react at bottom
        //right cell from top left cell.
        public int minimumCostPath(int[][] grid, int[] source, int destination[]) {

            int[][] costGrid = new int [grid.length][grid[0].length]; //maintain cost at each position
            Map<String,int[]>  parents = new HashMap<>();//maintains paths
            Queue<int[]> elements =
                    new PriorityQueue<>((pos1,pos2) -> ((Integer) getCost(costGrid, pos1[0], pos1[1]))
                            .compareTo(getCost(costGrid, pos2[0], pos2[1])));
            elements.add(source);

            costGrid[source[0]][source[1]] = getCost(grid, 0, 0);
            parents.put(source[0]+","+source[1], null);
            while(!elements.isEmpty()) {

                int[] index = elements.poll();
                for (int[] direction :directions) {

                    int minCostAtLastPos = getCost(costGrid, index[0], index[1]);
                    int row =  Math.max(0,Math.min(index[0]+direction[0],grid.length-1));
                    int col = Math.max(0,Math.min(index[1]+direction[1],grid[0].length-1));

                    int calculatedCost = minCostAtLastPos+getCost(grid, row, col);
                    int inputCOst = getCost(costGrid, row, col) ;

                    if(inputCOst <= 0 || inputCOst >  calculatedCost ) {
                        costGrid[row][col] = calculatedCost;
                        parents.put(row+","+col, index);
                        elements.add(new int[] {row,col});

                    }

                    if(Arrays.compare(new int[] {row,col},destination) == 0) {
                       int minCOst  = costGrid[row][col];
                       String position = row+","+col;
                        LinkedList<Integer> path = new LinkedList<>();
                        path.addFirst(grid[row][col]);
                       while(parents.get(position) != null) {
                           int parentRow = parents.get(position)[0];
                           int parentCol = parents.get(position)[1];

                           path.addFirst(grid[parentRow][parentCol]);
                           position =parentRow+","+parentCol;
                        }
                        Arrays.stream(costGrid).forEach(cost-> System.out.println(Arrays.toString(cost)));

                        System.out.println("------->");

                        System.out.println(path);

                        return minCOst;
                    }


                }

            }


            return -1;
        }

        private int getCost(int[][] costGrid, int index, int index1) {
            return costGrid[index][index1];
        }


    }
}
