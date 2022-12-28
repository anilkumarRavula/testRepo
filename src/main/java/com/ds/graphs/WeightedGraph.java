package src.com.ds.graphs;

import java.util.*;
import java.util.stream.Collectors;

public class WeightedGraph {

    // Each node maps to a list of all his neighbors
    private final Set<Node> nodes ;
    private final boolean directed;

    public WeightedGraph(boolean directed) {
        this.directed = directed;
        nodes = new HashSet<>();
    }

    public void addEdge(Node source, Node destination, int weight) {
        // Bidirectional mapping if unidirectional
        nodes.add(source);
        nodes.add(destination);
        source.getAdjNodes().add(new Edge(source,destination,weight));
        if(!directed)
        destination.getAdjNodes().add(new Edge(destination,source,weight));

    }
    public void printEdges() {
        nodes.stream().forEach(entry-> {
            System.out.print(entry);System.out.print("-->");
            System.out.print(entry.getAdjNodes().stream().map(Edge::getDest).map(Node::getName).collect(Collectors.joining(",")));
            System.out.println();
        });
    }

    public void DijkstraShortestPath(Node zero, Node six) {

    }


    public static class Node {
        int n;
        String name;
        boolean visited;
        LinkedList<Edge> adjNodes;

        Node(int n, String name){
            this.n = n;
            this.name = name;
            adjNodes = new LinkedList<>();
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

        public boolean isVisited() {
            return visited;
        }

        public void setVisited(boolean visited) {
            this.visited = visited;
        }

        public LinkedList<Edge> getAdjNodes() {
            return adjNodes;
        }

        public void setAdjNodes(LinkedList<Edge> adjNodes) {
            this.adjNodes = adjNodes;
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

    public static class Edge {

        private final Node source,dest;
        private final float weight;

        public Edge(Node source, Node dest, float weight) {
            this.source = source;
            this.dest = dest;
            this.weight = weight;
        }

        public Node getSource() {
            return source;
        }

        public Node getDest() {
            return dest;
        }

        public float getWeight() {
            return weight;
        }
    }


    }

