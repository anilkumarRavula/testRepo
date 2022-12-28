package com.ds.tress;

import java.util.*;

public class DistanceBetweenNodes {
    public static void main(String[] args) {
                /*
                1
               / \
            2      3
           / \    / \
         4    5   6     7
        / \      / \
      11    12   8  9
      /
   15
         */

        Node n = new Node(1,new Node(2,new Node(4,new Node(11,new Node(15),null),new Node(12)),new Node(5)),
                new Node(3,new Node(6,new Node(8),new Node(9)),new Node(7)));
        int distamce = findDistanceBetweenNodes(n,new Node(2),new Node(4));
        System.out.println(distamce);
    }

    private static int findDistanceBetweenNodes(Node root, Node node, Node node1) {
        Node n1 = null;
        Node n2 = null;
        Deque<Node> nodes = new ArrayDeque<>();
        Map<Integer,Node> rootNodes = new HashMap<>();
        int distance = 0;
        int matchingCount = 0;
        nodes.push(root);
        rootNodes.put((Integer) root.getData(),null);
        while(matchingCount < 2) {
            int nodesCount = nodes.size();
            if(nodesCount == 0) break;

            while(nodesCount > 0) {
                Node<Integer> current = nodes.pop();
                if(current.getData().equals(node.getData()) || current.getData().equals(node1.getData())) {
                    matchingCount++;
                }
                if(current.getLeft() != null) {
                    rootNodes.put(current.getLeft().getData(),current );
                    nodes.push(current.getLeft());
                }
                if(current.getRight() != null) {
                    rootNodes.put(current.getRight().getData(),current );
                    nodes.push(current.getRight());
                }

                nodesCount--;
            }
        }


        Node parent = node;
        Deque<Node> allAncenstors = new ArrayDeque();
        while(parent != null && !parent.equals(node1)) {
            parent = rootNodes.get(parent.getData());
            if(parent != null)
              allAncenstors.push(parent);
        }
        if(allAncenstors.peek().getData().equals(node1.getData())) return allAncenstors.size();
        //node is not paRENT
        parent = node1;
        Deque<Node> allAncenstors1 = new ArrayDeque();

        while(parent != null && !parent.getData().equals(node.getData())) {
            parent = rootNodes.get(parent.getData());
            if(parent != null)
                allAncenstors1.push(parent);
        }
        if(allAncenstors1.peek().getData().equals(node.getData())) return allAncenstors1.size();
        //node is not paRENT

        while(!allAncenstors.isEmpty() && !allAncenstors1.isEmpty() && allAncenstors.peek().getData().equals(allAncenstors1.peek().getData())) {
            allAncenstors.pop();allAncenstors1.pop();
        }

        return allAncenstors.size()+allAncenstors1.size()+2;
    }
}
