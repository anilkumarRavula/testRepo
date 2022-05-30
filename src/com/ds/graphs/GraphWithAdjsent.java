package src.com.ds.graphs;

public class GraphWithAdjsent {
    private int numOfNodes;
    private boolean directed;
    private boolean weighted;
    private int[][] matrix;

    /*
     This will allow us to safely add weighted graphs in our class since
     we will be able to check whether an edge exists without relying
     on specific special values (like 0)
    */
    private boolean[][] isSetMatrix;

    public GraphWithAdjsent(int numOfNodes, boolean directed, boolean weighted) {

        this.directed = directed;
        this.weighted = weighted;
        this.numOfNodes = numOfNodes;

        // Simply initializes our adjacency matrix to the appropriate size
        matrix = new int[numOfNodes][numOfNodes];
        isSetMatrix = new boolean[numOfNodes][numOfNodes];
    }

    /*
     Since matrices for directed graphs are symmetrical, we have to add
     [destination][source] at the same time as [source][destination]
    */
    public void addEdge(int source, int destination) {

        int valueToAdd = 1;

        if (weighted) {
            valueToAdd = 0;
        }

        matrix[source][destination] = valueToAdd;
        isSetMatrix[source][destination] = true;

        if (!directed) {
            matrix[destination][source] = valueToAdd;
            isSetMatrix[destination][source] = true;
        }
    }
    public void printMatrix() {
        for (int i = 0; i < numOfNodes; i++) {
            for (int j = 0; j < numOfNodes; j++) {
                // We only want to print the values of those positions that have been marked as set
                if (isSetMatrix[i][j])
                    System.out.format("%8s", String.valueOf(matrix[i][j]));
                else System.out.format("%8s", "/  ");
            }
            System.out.println();
        }
    }
    public void printEdges() {
        for (int i = 0; i < numOfNodes; i++) {
            System.out.print("Node " + i + " is connected to: ");
            for (int j = 0; j < numOfNodes; j++) {
                if (isSetMatrix[i][j]) {
                    System.out.print(j + " ");
                }
            }
            System.out.println();
        }
    }
    public boolean hasEdge(int source, int destination) {
        return isSetMatrix[source][destination];
    }

    public Integer getEdgeValue(int source, int destination) {
        if (!weighted || !isSetMatrix[source][destination])
            return null;
        return matrix[source][destination];
    }

    public static void main(String[] args) {
        GraphWithAdjsent graph = new GraphWithAdjsent(5, false, false);

        graph.addEdge(0, 2);
        graph.addEdge(0, 3);
        graph.addEdge(1, 2);
        graph.addEdge(1, 3); // The default weight is 0 if weighted == true
        graph.addEdge(1, 4);
        graph.addEdge(2, 3);
        graph.addEdge(3, 4);

        System.out.println();
        System.out.println();

        graph.printEdges();

    }


}
