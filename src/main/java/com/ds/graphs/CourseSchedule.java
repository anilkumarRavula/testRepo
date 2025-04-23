package com.ds.graphs;

import java.util.*;

/**
 * There are a total of numCourses courses you have to take, labeled from 0 to numCourses - 1. You are given an array prerequisites where prerequisites[i] = [ai, bi] indicates that you must take course bi first if you want to take course ai.
 *
 * For example, the pair [0, 1], indicates that to take course 0 you have to first take course 1.
 * Return the ordering of courses you should take to finish all courses. If there are many valid answers, return any of them. If it is impossible to finish all courses, return an empty array.
 *
 *
 *
 * Example 1:
 *
 * Input: numCourses = 2, prerequisites = [[1,0]]
 * Output: [0,1]
 * Explanation: There are a total of 2 courses to take. To take course 1 you should have finished course 0. So the correct course order is [0,1].
 * Example 2:
 *
 * Input: numCourses = 4, prerequisites = [[1,0],[2,0],[3,1],[3,2]]
 * Output: [0,2,1,3]
 * Explanation: There are a total of 4 courses to take. To take course 3 you should have finished both courses 1 and 2. Both courses 1 and 2 should be taken after you finished course 0.
 * So one correct course order is [0,1,2,3]. Another correct ordering is [0,2,1,3].
 * Example 3:
 *
 * Input: numCourses = 1, prerequisites = []
 * Output: [0]
 */
public class CourseSchedule {

    public static void main(String[] args) {
        System.out.println(Arrays.toString(new Solution().findOrder(4,new int[][]{new int[]{1,0},
                new int[]{2,0},
                new int[]{3,1},
                new int[]{3,2}})));
        /**
         * 0-> 1,2
         * 3-> 1,2
         * 2->4
         * 1->5
         * 5->4
         */
        System.out.println(Arrays.toString(new Solution_Cycle().findOrder(6,new int[][]{new int[]{0,1},
                new int[]{0,2},
                new int[]{3,1},
                new int[]{3,2},
                new int[]{2,4},
                new int[]{1,5},
                new int[]{5,4}
              //  new int[]{2,3}

        })));

        System.out.println(Arrays.toString(new Solution_Cycle().findOrder(2,new int[][]{new int[]{0,1},new int[]{1,0}})));

    }
    static class Solution_Cycle {

        public int[] findOrder(int numCourses, int[][] prerequisites) {
            List<Integer> courseOrder = new ArrayList<>();
            boolean[] compltedOnes = new boolean[numCourses];
            boolean[] visiting = new boolean[numCourses];

            Map<Integer, List<Integer>> dependeciesLIst = new HashMap<>();

            for (int[] dependency: prerequisites) {
                dependeciesLIst.putIfAbsent(dependency[0],new ArrayList<>());
                dependeciesLIst.get(dependency[0]).add(dependency[1]);
            }
            try {
                for (int courseCode = 0; courseCode < numCourses ; courseCode++) {
                    addCoursesInOrder(compltedOnes,visiting,courseOrder,dependeciesLIst,courseCode);
                }
            } catch (Exception exception) {
                return new int[]{};
            }

            return courseOrder.stream().mapToInt(i->i).toArray();
        }

        private void addCoursesInOrder(boolean[] compltedOnes,boolean[] visiting,List<Integer> courses, Map<Integer, List<Integer>> dependeciesLIst,
                                                int courseCode) {
            //return if it is already visited;



            if(visiting[courseCode]) throw new RuntimeException("cycling") ;
            if(compltedOnes[courseCode]) return ;
            List<Integer> prerequesits = dependeciesLIst.getOrDefault(courseCode,Collections.emptyList());

            visiting[courseCode] = true;
            compltedOnes[courseCode] = true;

            //get all dependents for the course
            prerequesits.forEach(preRequesite->{
                addCoursesInOrder(compltedOnes,visiting,courses,dependeciesLIst,preRequesite);
            });
            //
            visiting[courseCode] = false;
            courses.add(courseCode);


        }
    }
    static class Solution_NonCycle {

        public int[] findOrder(int numCourses, int[][] prerequisites) {
            List<Integer> courseOrder = new ArrayList<>();
            boolean[] compltedOnes = new boolean[numCourses];

            Map<Integer, List<Integer>> dependeciesLIst = new HashMap<>();

            for (int[] dependency: prerequisites) {
                dependeciesLIst.putIfAbsent(dependency[0],new ArrayList<>());
                dependeciesLIst.get(dependency[0]).add(dependency[1]);
            }

            for (int courseCode = 0; courseCode < numCourses ; courseCode++) {
                courseOrder.addAll(addCoursesInOrder(compltedOnes,dependeciesLIst,courseCode));
            }

            return courseOrder.stream().mapToInt(i->i).toArray();
        }

        private List<Integer> addCoursesInOrder(boolean[] compltedOnes, Map<Integer, List<Integer>> dependeciesLIst,
                                                int courseCode) {
            //return if it is already visited;
            List<Integer> courses = new ArrayList<>();

            if(compltedOnes[courseCode]) return courses;

            if(!dependeciesLIst.containsKey(courseCode)) {
                 courses.add(courseCode);
                compltedOnes[courseCode] = true;
                return courses;
            }
            compltedOnes[courseCode] = true;
            //get all dependents for the course
            dependeciesLIst.get(courseCode).forEach(preRequesite->{
                courses.addAll(addCoursesInOrder(compltedOnes,dependeciesLIst,preRequesite));

            });
            //

            courses.add(courseCode);

            return courses;

        }
    }

    static class Solution {

        public int[] findOrder(int numCourses, int[][] prerequisites) {
            Map<Integer, List<Integer>> adjList = new HashMap<>();
            int[] inDegree = new int[numCourses];

            // Build the graph
            for (int[] pair : prerequisites) {
                int course = pair[0], prereq = pair[1];
                adjList.computeIfAbsent(prereq, k -> new ArrayList<>()).add(course);
                inDegree[course]++;
            }

            // Start with courses that have no prerequisites
            Queue<Integer> queue = new ArrayDeque<>();
            for (int i = 0; i < numCourses; i++) {
                if (inDegree[i] == 0) queue.offer(i);
            }

            int[] courseOrder = new int[numCourses];
            int index = 0;

            while (!queue.isEmpty()) {
                int course = queue.poll();
                courseOrder[index++] = course;

                for (int next : adjList.getOrDefault(course, Collections.emptyList())) {
                    if (--inDegree[next] == 0) {
                        queue.offer(next);
                    }
                }
            }

            return index == numCourses ? courseOrder : new int[0]; // cycle detected
        }
    }

}
