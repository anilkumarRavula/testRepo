package com.ds.graphs.design;

import java.util.*;

/**
 * Representation	Best For	Space Complexity	Edge Lookup
 * Adjacency List	Sparse Graphs	O(V + E)	O(degree)
 * Adjacency Matrix	Dense Graphs	O(V²)	O(1)
 */
class Edge {
    int dest;
    int weight;

    public Edge(int dest, int weight) {
        this.dest = dest;
        this.weight = weight;
    }

    public String toString() {
        return "(" + dest + ", weight=" + weight + ")";
    }
}

public class WeightedGraphList {
    private final Map<Integer, List<Edge>> adjList;
    private final boolean isDirected;

    public WeightedGraphList(boolean isDirected) {
        this.adjList = new HashMap<>();
        this.isDirected = isDirected;
    }

    public void addEdge(int src, int dest, int weight) {
        adjList.putIfAbsent(src, new ArrayList<>());
        adjList.putIfAbsent(dest, new ArrayList<>());

        adjList.get(src).add(new Edge(dest, weight));
        if (!isDirected) {
            adjList.get(dest).add(new Edge(src, weight));
        }
    }

    public void printGraph() {
        for (int vertex : adjList.keySet()) {
            System.out.print(vertex + " -> ");
            System.out.println(adjList.get(vertex));
        }
    }

    public static void main(String[] args) {
        WeightedGraphList graph = new WeightedGraphList(false);

        graph.addEdge(1, 2, 10);
        graph.addEdge(1, 3, 5);
        graph.addEdge(2, 4, 8);
        graph.addEdge(3, 4, 12);

        graph.printGraph();
    }
}
