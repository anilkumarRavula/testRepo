package com.ds.graphs.design;

import java.util.Arrays;

public class WeightedGraphMatrix {
    private final int[][] adjMatrix;
    private final int numVertices;
    private final boolean isDirected;

    public WeightedGraphMatrix(int numVertices, boolean isDirected) {
        this.numVertices = numVertices;
        this.isDirected = isDirected;
        this.adjMatrix = new int[numVertices][numVertices];

        // Optional: fill with -1 or Integer.MAX_VALUE to denote "no edge"
        for (int i = 0; i < numVertices; i++)
            for (int j = 0; j < numVertices; j++)
                adjMatrix[i][j] = 0;
    }

    public void addEdge(int src, int dest, int weight) {
        adjMatrix[src][dest] = weight;
        if (!isDirected) {
            adjMatrix[dest][src] = weight;
        }
    }

    public void printGraph() {
        System.out.println("Adjacency Matrix:");
        for (int i = 0; i < numVertices; i++) {
            System.out.println(Arrays.toString(adjMatrix[i]));
        }
    }

    public static void main(String[] args) {
        WeightedGraphMatrix graph = new WeightedGraphMatrix(5, false);

        graph.addEdge(1, 2, 10);
        graph.addEdge(1, 3, 5);
        graph.addEdge(2, 4, 8);
        graph.addEdge(3, 4, 12);

        graph.printGraph();
    }
}
