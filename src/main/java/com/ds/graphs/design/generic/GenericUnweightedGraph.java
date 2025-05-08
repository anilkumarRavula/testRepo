package com.ds.graphs.design.generic;

import java.util.*;

public class GenericUnweightedGraph<T> {
    private final Map<T, List<T>> adjList;
    private final boolean isDirected;

    public GenericUnweightedGraph(boolean isDirected) {
        this.adjList = new HashMap<>();
        this.isDirected = isDirected;
    }

    public void addVertex(T vertex) {
        adjList.putIfAbsent(vertex, new ArrayList<>());
    }

    public void addEdge(T src, T dest) {
        addVertex(src);
        addVertex(dest);
        adjList.get(src).add(dest);
        if (!isDirected) {
            adjList.get(dest).add(src);
        }
    }

    public List<T> getNeighbors(T vertex) {
        return adjList.getOrDefault(vertex, new ArrayList<>());
    }

    public void printGraph() {
        for (T vertex : adjList.keySet()) {
            System.out.print(vertex + " -> ");
            System.out.println(adjList.get(vertex));
        }
    }

    public static void main(String[] args) {
        GenericUnweightedGraph<String> graph = new GenericUnweightedGraph<>(false);

        graph.addEdge("A", "B");
        graph.addEdge("A", "C");
        graph.addEdge("B", "D");
        graph.addEdge("C", "D");

        graph.printGraph();
    }
}
