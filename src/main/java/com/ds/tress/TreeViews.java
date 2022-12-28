package com.ds.tress;

import java.util.*;

public class TreeViews {
    public static void main(String[] args) {
/*
        Node n = new Node(4,new Node(2,new Node(1),new Node(3)),
                new Node(6,new Node(5,new Node(4),null),new Node(7)));
*/
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
        rightView(n);
        maxDepth = 0;
        System.out.println();

        Node n1 = new Node(1,new Node(2,new Node(4,new Node(11,new Node(15),null),new Node(12)),new Node(5)),
                new Node(3,new Node(6,new Node(8),new Node(9,new Node(16,new Node(17),null),null)),new Node(7)));

        leftView(n1,0);
        rightViewIteratorMorrisonTravel(n);
        rightViewIteratorMorrisonTravel(n1);
        topViewWithIterator(n);
        bottomViewWithIteratorSingleWhileLoop(n);
    }
    public static int maxDepth = 0;
    public static void rightView(Node n){
        rightView(n,0,0);
        System.out.println();
    }
    public static int rightView(Node n, int depth, int maxDepth) {
        if(n == null) return maxDepth;
        depth++;
        if(depth > maxDepth) {
            n.process();
            maxDepth = depth;
        }
        maxDepth =  rightView(n.getRight(),depth,maxDepth);
        maxDepth =  rightView(n.getLeft(),depth,maxDepth);
    return maxDepth;
    }
    public static int leftView(Node n, int depth) {
        if(n == null) return depth;
        depth++;
        if(depth > maxDepth) {
            n.process();
            maxDepth = depth;
        }
        depth =  leftView(n.getLeft(),depth);
        depth =  leftView(n.getRight(),depth);
        depth--;
        return depth;
    }
    public static void rightViewIteratorMorrisonTravel(Node root) {
        System.out.println("      rightViewIteratorMorrisonTravel <-");

        int maxDepth = 0;
        int depth = 0;
        Node current =root;
        while(current != null) {
            //get the right
            if(current.getRight() != null) {
                //find left most of this
                Node leftMost = current.getRight();
                int nodesBetween = 0;
                while(leftMost.getLeft()!= null && !leftMost.getLeft().equals(current)) {
                    leftMost = leftMost.getLeft();
                    nodesBetween++;
                }
                if(leftMost.getLeft() == null) {
                    leftMost.setLeft(current);
                    depth++;
                    if(depth > maxDepth) {
                        current.process();
                        maxDepth = depth;
                    }
                    current = current.getRight();
                }else if(leftMost.getLeft().equals(current)) {
                    current = current.getLeft();
                    leftMost.setLeft(null);
                    depth = depth - (nodesBetween+1);
                }
            } else {
                depth++;
                if(depth > maxDepth) {
                    current.process();
                    maxDepth = depth;
                }
                current = current.getLeft();
            }

        }

        System.out.println("      rightViewIteratorMorrisonTravel <-");

    }

    public static void topViewWithIterator(Node n) {
        System.out.println("      topViewWithIterator ->");

        Deque<Node> nodes = new ArrayDeque<>();
        Map<Node,Integer> nodesCount = new HashMap<>();
        n.process();
        nodes.addFirst(n.getLeft());
        int depth = 1;
        int maxdepth = 0;
        while(!nodes.isEmpty()) {
            Node current = nodes.poll();
            depth = nodesCount.getOrDefault(current,depth);
            if(depth > maxdepth) {
                current.process();
                maxdepth = depth;
            }
            if(current.getRight() != null) {
                nodes.addFirst(current.getRight());
               // depth --;
                nodesCount.put(current.getRight(),depth-1);
            }
            if(current.getLeft() != null) {
                depth++;
                nodes.addFirst(current.getLeft());
            }

        }
        nodes.addFirst(n.getRight());
        depth =1;
        maxdepth = 0;
        while(!nodes.isEmpty()) {
            Node current = nodes.poll();
            depth = nodesCount.getOrDefault(current,depth);
            if(depth > maxdepth) {
                current.process();
                maxdepth = depth;
            }
            if(current.getLeft() != null) {
                nodes.addFirst(current.getLeft());
                // depth --;
                nodesCount.put(current.getLeft(),depth-1);
            }
            if(current.getRight() != null) {
                depth++;
                nodes.addFirst(current.getRight());
            }

        }
        System.out.println("      topViewWithIterator <-");

    }

    /**
     * Yet to finish
     * @param n
     */
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
    public static void bottomViewWithIteratorSingleWhileLoop(Node n) {
        System.out.println(" Wrong results     topViewWithIteratorSingleWhileLoop ->");

        Deque<Node> nodes = new ArrayDeque<>();
        HashMap<Integer,Node> nodeParentLevel = new HashMap<>();
        HashMap<Node,Integer> nodeevelCount = new HashMap<>();

        nodes.addFirst(n);
        int depth = 0;
        Node leftSubtree = n.getLeft();
        while(!nodes.isEmpty()) {
            Node current = nodes.poll();
            //reset count
            if(current.equals(leftSubtree)) {
                depth = -1;
            }
            depth = nodeevelCount.getOrDefault(current,0);
            nodeParentLevel.put(depth,current);
            if(current.getLeft() != null) {
                nodes.addFirst(current.getLeft());
                 //depth --;
                nodeevelCount.put(current.getLeft(),depth-1);
            }
            if(current.getRight() != null) {
                // depth++;
                nodeevelCount.put(current.getRight(),depth+1);
                nodes.addFirst(current.getRight());
            }

        }
        System.out.println(nodeParentLevel);
        System.out.println("      topViewWithIteratorSingleWhileLoop <-");

    }



}
