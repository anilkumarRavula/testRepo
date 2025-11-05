package com.ds;

/**
 * A segment tree is a tree data structure used for efficiently answering range queries on an array. Each node in the tree represents a segment (or interval) of the array. The root node represents the entire array, and each leaf node represents a single element. Internal nodes represent the result of an operation (e.g., sum, min, max) over their children's segments.
 * Here's a basic implementation of a segment tree for sum queries in Java:
 * class SegmentTree {
 *     int[] tree;
 *     int[] arr;
 *     int n;
 *
 *     public SegmentTree(int[] nums) {
 *         n = nums.length;
 *         arr = nums;
 *         tree = new int[4 * n]; // Allocate enough space for the tree
 *         build(0, 0, n - 1);
 *     }
 *
 *     // Build the segment tree recursively
 *     private void build(int node, int start, int end) {
 *         if (start == end) {
 *             tree[node] = arr[start];
 *         } else {
 *             int mid = (start + end) / 2;
 *             build(2 * node + 1, start, mid);
 *             build(2 * node + 2, mid + 1, end);
 *             tree[node] = tree[2 * node + 1] + tree[2 * node + 2];
 *         }
 *     }
 *
 *     // Query the segment tree for the sum in the range [left, right]
 *     public int query(int left, int right) {
 *         return queryUtil(0, 0, n - 1, left, right);
 *     }
 *
 *     private int queryUtil(int node, int start, int end, int left, int right) {
 *         if (right < start || left > end) {
 *             return 0; // Out of range
 *         }
 *         if (left <= start && end <= right) {
 *             return tree[node]; // Fully in range
 *         }
 *         int mid = (start + end) / 2;
 *         return queryUtil(2 * node + 1, start, mid, left, right) +
 *                queryUtil(2 * node + 2, mid + 1, end, left, right);
 *     }
 *
 *     // Update the value at index idx with newValue
 *     public void update(int idx, int newValue) {
 *         updateUtil(0, 0, n - 1, idx, newValue);
 *     }
 *
 *     private void updateUtil(int node, int start, int end, int idx, int newValue) {
 *         if (start == end) {
 *             arr[idx] = newValue;
 *             tree[node] = newValue;
 *             return;
 *         }
 *         int mid = (start + end) / 2;
 *         if (idx <= mid) {
 *             updateUtil(2 * node + 1, start, mid, idx, newValue);
 *         } else {
 *             updateUtil(2 * node + 2, mid + 1, end, idx, newValue);
 *         }
 *         tree[node] = tree[2 * node + 1] + tree[2 * node + 2];
 *     }
 * }
 *
 * This code demonstrates the basic structure and operations of a segment tree. It includes building the tree, querying for a range sum, and updating a value. Segment trees can be adapted for other operations like finding the minimum or maximum in a range by changing the combine logic in the build and queryUtil methods.
 *
 * Generative AI is experimental.
 *
 * [-] https://www.sarthaks.com/3603499/what-is-segment-tree[-] https://medium.com/@YodgorbekKomilo/segment-trees-in-java-a-powerful-data-structure-603e7b2204db
 */
public class SegmentedTree {
}
