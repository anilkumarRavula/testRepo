package com.ds.graphs;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.*;

/**
 * You are given an array of variable pairs equations and an array of real numbers values, where equations[i] = [Ai, Bi] and values[i] represent the equation Ai / Bi = values[i]. Each Ai or Bi is a string that represents a single variable.
 * <p>
 * You are also given some queries, where queries[j] = [Cj, Dj] represents the jth query where you must find the answer for Cj / Dj = ?.
 * <p>
 * Return the answers to all queries. If a single answer cannot be determined, return -1.0.
 * <p>
 * Note: The input is always valid. You may assume that evaluating the queries will not result in division by zero and that there is no contradiction.
 * <p>
 * Note: The variables that do not occur in the list of equations are undefined, so the answer cannot be determined for them.
 * <p>
 * <p>
 * <p>
 * Example 1:
 * <p>
 * Input: equations = [["a","b"],["b","c"]], values = [2.0,3.0], queries = [["a","c"],["b","a"],["a","e"],["a","a"],["x","x"]]
 * Output: [6.00000,0.50000,-1.00000,1.00000,-1.00000]
 * Explanation:
 * Given: a / b = 2.0, b / c = 3.0
 * queries are: a / c = ?, b / a = ?, a / e = ?, a / a = ?, x / x = ?
 * return: [6.0, 0.5, -1.0, 1.0, -1.0 ]
 * note: x is undefined => -1.0
 * Example 2:
 * <p>
 * Input: equations = [["a","b"],["b","c"],["bc","cd"]], values = [1.5,2.5,5.0], queries = [["a","c"],["c","b"],["bc","cd"],["cd","bc"]]
 * Output: [3.75000,0.40000,5.00000,0.20000]
 * Example 3:
 * <p>
 * Input: equations = [["a","b"]], values = [0.5], queries = [["a","b"],["b","a"],["a","c"],["x","y"]]
 * Output: [0.50000,2.00000,-1.00000,-1.00000]
 * <p>
 * <p>
 * Constraints:
 * <p>
 * 1 <= equations.length <= 20
 * equations[i].length == 2
 * 1 <= Ai.length, Bi.length <= 5
 * values.length == equations.length
 * 0.0 < values[i] <= 20.0
 * 1 <= queries.length <= 20
 * queries[i].length == 2
 * 1 <= Cj.length, Dj.length <= 5
 * Ai, Bi, Cj, Dj consist of lower case English letters and digits.
 */
public class EvaluateDivision {
    static DecimalFormat df = new DecimalFormat("#.####");

    public static void main(String[] args) {
        df.setRoundingMode(RoundingMode.CEILING);

       double[] values =  new Solution().calcEquation(List.of(List.of("a", "b"), List.of("b", "c"),List.of("c", "d"),List.of("e", "f")),
               new double[]{2.0, 3.0,5.0,4.0},
               List.of(List.of("a", "c"),
                       List.of("b", "a"), List.of("a", "d"), List.of("f", "e")));

        System.out.println(Arrays.toString(values));
    }

    static class Solution {

        static Map<String, Integer> indexes = new HashMap<>();
        static List<Map<String, Double>> dataValues = new ArrayList<>();
        static boolean[] visited;

        public double[] calcEquation(List<List<String>> equations, double[] values, List<List<String>> queries) {
            int index = 0;
            buildInputs(equations, values, index);
            double[] results = new double[queries.size()];
            index = 0;
            visited = new boolean[indexes.size()];

            for (List<String> query :
                    queries) {
                results[index++] = getQueryValue(query.get(0), query.get(1));
            }
            return results;
        }

        private void buildInputs(List<List<String>> equations, double[] values, int index) {
            for (List<String> equation : equations) {
                String top = equation.get(0);
                String button = equation.get(1);

                indexes.computeIfAbsent(top, top1 -> {
                    dataValues.add(new HashMap<>());
                    return indexes.size();
                });
                indexes.computeIfAbsent(button, top1 -> {
                    dataValues.add(new HashMap<>());
                    return indexes.size();
                });
                dataValues.get(indexes.get(top)).putIfAbsent(button, values[index]);
                dataValues.get(indexes.get(button)).putIfAbsent(top, Double.valueOf(df.format(1.0000 / values[index])));
                index++;
            }
        }

        private double getQueryValue(String start, String target) {
            //check for start index
            Integer index = indexes.getOrDefault(start,-1);

            //if any one not present in the map return -1 or already in the visted list
            if (index == -1 || !indexes.containsKey(target) || visited[index]) {
                return -1;
            }

            Map<String, Double> values = dataValues.get(index);

            if (values.containsKey(target)) {
                return values.get(target);
            }

            visited[index] = true;
            //one draw back is that it not updatinng all found possible equations proactively
            for (Map.Entry<String, Double> path :
                    values.entrySet()) {
                double queryValue = getQueryValue(path.getKey(), target);
                if (queryValue != -1) {
                    visited[index] = false;
                    return path.getValue() * queryValue;
                }
            }

            visited[index] = false;
            return -1;

        }

    }


    }
