package com.ds.graphs.design.generic;

import java.util.*;

public class GenericAdjMatrix<T> {
    private final Map<T, Integer> vertexIndex;
    private final List<T> indexToVertex;
    private final int[][] matrix;
    private final boolean isDirected;
    private int size;

    public GenericAdjMatrix(int maxVertices, boolean isDirected) {
        this.vertexIndex = new HashMap<>();
        this.indexToVertex = new ArrayList<>();
        this.matrix = new int[maxVertices][maxVertices];
        this.isDirected = isDirected;
        this.size = 0;
    }

    public void addVertex(T vertex) {
        if (!vertexIndex.containsKey(vertex)) {
            vertexIndex.put(vertex, size);
            indexToVertex.add(vertex);
            size++;
        }
    }

    public void addEdge(T src, T dest, int weight) {
        addVertex(src);
        addVertex(dest);
        int i = vertexIndex.get(src);
        int j = vertexIndex.get(dest);
        matrix[i][j] = weight;
        if (!isDirected) {
            matrix[j][i] = weight;
        }
    }

    public void printMatrix() {
        System.out.println("Adjacency Matrix:");
        for (int i = 0; i < size; i++) {
            System.out.print(indexToVertex.get(i) + " -> ");
            System.out.println(Arrays.toString(Arrays.copyOfRange(matrix[i], 0, size)));
        }
    }

    public static void main(String[] args) {
        GenericAdjMatrix<String> graph = new GenericAdjMatrix<>(10, false);

        graph.addEdge("A", "B", 5);
        graph.addEdge("A", "C", 10);
        graph.addEdge("B", "D", 8);
        graph.addEdge("C", "D", 12);

        graph.printMatrix();
    }
}
