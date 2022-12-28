package com.ds.graphs;

import java.util.*;
import java.util.stream.Collectors;

public class Graph {

    // Each node maps to a list of all his neighbors
    private HashMap<Node, LinkedList<Node>> adjacencyMap;
    private boolean directed;

    public Graph(boolean directed) {
        this.directed = directed;
        adjacencyMap = new HashMap<>();
    }

    public void addEdge(Node source, Node destination) {
        // Bidirectional mapping if unidirectional
        adjacencyMap.putIfAbsent(source,new LinkedList<>());
        adjacencyMap.putIfAbsent(destination,new LinkedList<>());
        adjacencyMap.get(source).add(destination);
        adjacencyMap.get(destination).add(source);
    }
    public void printEdges() {
        adjacencyMap.entrySet().stream().forEach(entry-> {
            System.out.print(entry.getKey());System.out.print("-->");
            System.out.print(entry.getValue().stream().map(Node::getName).collect(Collectors.joining(",")));
            System.out.println();
        });
    }

    public boolean hasEdge(Node source, Node destination) {
        return adjacencyMap.containsKey(source)  && adjacencyMap.get(source).contains(destination);
    }

    public LinkedList<Node> getAdjasentNodes(Node node) {
        return adjacencyMap.get(node);
    }

    public static class Node {
        int n;
        String name;

        Node(int n, String name){
            this.n = n;
            this.name = name;
        }

        public int getN() {
            return n;
        }

        public void setN(int n) {
            this.n = n;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Node node = (Node) o;
            return n == node.n && Objects.equals(name, node.name);
        }

        @Override
        public int hashCode() {
            return Objects.hash(n, name);
        }

        @Override
        public String toString() {
            return "Node{" +
                    "n=" + n +
                    ", name='" + name + '\'' +
                    '}';
        }
    }

}

