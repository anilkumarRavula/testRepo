package src.com.ds.graphs;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.Queue;

import static src.com.ds.graphs.Graph.*;

public class GraphTraversal {
    public static void main(String[] args) {
        Graph graph = new Graph(false);
        Node a = new Node(0, "0");
        Node b = new Node(1, "1");
        Node c = new Node(2, "2");
        Node d = new Node(3, "3");
        Node e = new Node(4, "4");
        Node f = new Node(5, "5");
        graph.addEdge(a,d);
        graph.addEdge(a,b);
        graph.addEdge(a,c);
        graph.addEdge(c,d);
        graph.addEdge(e,f);
        graph.addEdge(c,e);

        graph.printEdges();
        bfs(a,graph);
        dfs(a,graph);
    }

    public static void bfs(Node node, Graph graph) {
        Queue<Node> nodes = new ArrayDeque<>();
        nodes.add(node);
        HashSet<Node> visited = new HashSet<>();
        while(!nodes.isEmpty()) {
            Node current = nodes.poll();
            if(!visited.contains(current)) {
                System.out.print(current);System.out.print("->");
                graph.getAdjasentNodes(current).forEach(nodes::add);
                visited.add(current);
            }
        }
        //process for unreachable nodes
        System.out.println();
    }
    public static void dfs(Node node, Graph graph) {
        ArrayDeque<Node> nodes = new ArrayDeque<>();
        nodes.add(node);
        HashSet<Node> visited = new HashSet<>();
        while(!nodes.isEmpty()) {
            Node current = nodes.poll();
            if(!visited.contains(current)) {
                System.out.print(current);System.out.print("->");
                graph.getAdjasentNodes(current).forEach(nodes::addFirst);
                visited.add(current);
            }
        }
        //process for unreachable nodes
        System.out.println();
    }

}
