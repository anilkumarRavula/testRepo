package src.com.ds.graphs;

import java.util.*;

import static src.com.ds.graphs.WeightedGraph.*;

public class ShortestPathDj {
    public static void main(String[] args) {
        WeightedGraph graphWeighted = new WeightedGraph(true);
        Node zero = new Node(0, "0");
        Node one = new Node(1, "1");
        Node two = new Node(2, "2");
        Node three = new Node(3, "3");
        Node four = new Node(4, "4");
        Node five = new Node(5, "5");
        Node six = new Node(6, "6");

        // Our addEdge method automatically adds Nodes as well.
        // The addNode method is only there for unconnected Nodes,
        // if we wish to add any
        graphWeighted.addEdge(zero, one, 8);
        graphWeighted.addEdge(zero, two, 11);
        graphWeighted.addEdge(one, three, 3);
        graphWeighted.addEdge(one, four, 8);
        graphWeighted.addEdge(one, two, 7);
        graphWeighted.addEdge(two, four, 9);
        graphWeighted.addEdge(three, four, 5);
        graphWeighted.addEdge(three, five, 2);
        graphWeighted.addEdge(four, six, 6);
        graphWeighted.addEdge(five, four, 1);
        graphWeighted.addEdge(five, six, 8);

        graphWeighted.printEdges();
        shortestPathDirectedGraph(zero, six);
    }


    private static void shortestPathInUndirectedGraph(Node source, Node desgtination) {

        Map<String, Float>  weights = new HashMap<>();
        Map<String, String>  parent = new HashMap<>();
        Queue<Node> nodes = new LinkedList<>();
        nodes.add(source);
        parent.put(source.getName(),null);
        weights.put(source.getName(),0.0f);

        HashSet<Node> visitedNodes = new HashSet<>();
        while(!nodes.isEmpty()) {
            Node current = nodes.poll();

            if(visitedNodes.contains(current)) continue;
            System.out.print(current.getName()+"---->");
           current.getAdjNodes().forEach(edge -> {
                nodes.add(edge.getDest());
                float weight = edge.getWeight()+weights.getOrDefault(current.getName(),0.0f);

                if(weights.containsKey(edge.getDest().getName()) ) { //|| parent.getOrDefault(edge.getSource().name,"").equals(edge.getDest().name)
                    if(weights.get(edge.getDest().getName()) > weight) {
                        weights.put(edge.getDest().getName(),weight);
                        parent.put(edge.getDest().getName(),current.getName());
                    }
                } else {
                    weights.put(edge.getDest().getName(),weight);
                    parent.put(edge.getDest().getName(),current.getName());
                }

               weight = edge.getWeight()+weights.getOrDefault(edge.getDest().getName(),0.0f);

               if(weights.containsKey(current.getName()) ) { //|| parent.getOrDefault(edge.getSource().name,"").equals(edge.getDest().name)
                   if(weights.get(current.getName()) > weight) {
                       weights.put(current.getName(),weight);
                       parent.put(current.getName(),edge.getDest().getName());
                   }
               }

            });

            visitedNodes.add(current);
            System.out.println(weights);
            System.out.println(parent);

            System.out.println();
        }
        System.out.println(weights);
        System.out.println(parent);

    }

    /**
     * Go to each nearest node and calucl
     * @param source
     * @param desgtination
     */
    private static void shortestPathDirectedGraph(Node source, Node desgtination) {

        Map<String, Float>  weights = new HashMap<>();
        Map<String, String>  parent = new HashMap<>();
        parent.put(source.getName(),null);
        weights.put(source.getName(),0.0f);
        HashSet<Node> visitedNodes = new HashSet<>();
        PriorityQueue<Edge> nodes = new PriorityQueue<>((e1, e2) -> ((Float) e1.getWeight()).compareTo(e2.getWeight()));
        nodes.add(new Edge(source,null,0.0f));
        Node current = source;
        while(!nodes.isEmpty() && !current.getName().equals(desgtination.getName())) {

            current = nodes.remove().getSource();

            if(current.visited) continue;
            System.out.print(current.getName()+"---->");
            current.getAdjNodes().stream().forEach(edge -> {
                float weight = edge.getWeight()+weights.getOrDefault(edge.getSource().getName(),0.0f);

                if(weights.containsKey(edge.getDest().getName()) ) { //|| parent.getOrDefault(edge.getSource().name,"").equals(edge.getDest().name)
                    if(weights.get(edge.getDest().getName()) > weight) {
                        weights.put(edge.getDest().getName(),weight);
                        parent.put(edge.getDest().getName(),edge.getSource().getName());
                    }
                } else {
                    weights.put(edge.getDest().getName(),weight);
                    parent.put(edge.getDest().getName(),edge.getSource().getName());
                }
                nodes.add(new Edge(edge.getDest(),null,weights.get(edge.getDest().getName())));
            });

            current.setVisited(true);
            System.out.println(weights);
            System.out.println(parent);

            System.out.println();
        }
        System.out.println(weights);
        System.out.println(parent);

    }
}
