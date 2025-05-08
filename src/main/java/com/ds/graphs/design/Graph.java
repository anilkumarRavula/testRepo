package com.ds.graphs.design;

import java.util.*;

public class Graph {
    private final Map<Integer, List<Integer>> adjList;
    private final boolean isDirected;

    public Graph(boolean isDirected) {
        this.adjList = new HashMap<>();
        this.isDirected = isDirected;
    }

    public void addVertex(int vertex) {
        adjList.putIfAbsent(vertex, new ArrayList<>());
    }

    public void addEdge(int src, int dest) {
        addVertex(src);
        addVertex(dest);
        adjList.get(src).add(dest);
        if (!isDirected) {
            adjList.get(dest).add(src);
        }
    }

    public List<Integer> getNeighbors(int vertex) {
        return adjList.getOrDefault(vertex, new ArrayList<>());
    }

    public void printGraph() {
        for (int vertex : adjList.keySet()) {
            System.out.print(vertex + " -> ");
            System.out.println(adjList.get(vertex));
        }
    }

    public static void main(String[] args) {
        Graph graph = new Graph(false); // false for undirected, true for directed

        graph.addEdge(1, 2);
        graph.addEdge(1, 3);
        graph.addEdge(2, 4);
        graph.addEdge(3, 4);

        graph.printGraph();
    }
}
