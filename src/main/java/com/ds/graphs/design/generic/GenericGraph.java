package com.ds.graphs.design.generic;

import java.util.*;

class Edge<T> {
    T dest;
    int weight;

    public Edge(T dest, int weight) {
        this.dest = dest;
        this.weight = weight;
    }

    @Override
    public String toString() {
        return "(" + dest + ", weight=" + weight + ")";
    }
}

public class GenericGraph<T> {
    private final Map<T, List<Edge<T>>> adjList;
    private final boolean isDirected;

    public GenericGraph(boolean isDirected) {
        this.adjList = new HashMap<>();
        this.isDirected = isDirected;
    }

    public void addVertex(T vertex) {
        adjList.putIfAbsent(vertex, new ArrayList<>());
    }

    public void addEdge(T src, T dest, int weight) {
        addVertex(src);
        addVertex(dest);
        adjList.get(src).add(new Edge<>(dest, weight));
        if (!isDirected) {
            adjList.get(dest).add(new Edge<>(src, weight));
        }
    }

    public List<Edge<T>> getNeighbors(T vertex) {
        return adjList.getOrDefault(vertex, new ArrayList<>());
    }

    public void printGraph() {
        for (T vertex : adjList.keySet()) {
            System.out.print(vertex + " -> ");
            System.out.println(adjList.get(vertex));
        }
    }

    public static void main(String[] args) {
        GenericGraph<String> cityGraph = new GenericGraph<>(false);

        cityGraph.addEdge("A", "B", 5);
        cityGraph.addEdge("A", "C", 10);
        cityGraph.addEdge("B", "D", 3);
        cityGraph.addEdge("C", "D", 8);
        cityGraph.addEdge("D", "E", 2);

        cityGraph.printGraph();
    }
}
