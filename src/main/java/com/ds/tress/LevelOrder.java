package com.ds.tress;

import java.util.ArrayDeque;
import java.util.Queue;

/**
 *   4
 *   /
 *   2
 */
public class LevelOrder {

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
       -- 1 2 3 4 5 6 7 11 12 8 9 15

         */

        Node n = new Node(1,new Node(2,new Node(4,new Node(11,new Node(15),null),new Node(12)),new Node(5)),
                new Node(3,new Node(6,new Node(8),new Node(9)),new Node(7)));
new LevelOrderSolution().levelOrder(n);
    }

    static class LevelOrderSolution {
        public void levelOrder(Node root) {
            Queue<Node> nodes = new ArrayDeque<>();
            nodes.add(root);
            while(!nodes.isEmpty()) {
                Node parent =  nodes.poll();
                System.out.print(parent.getData()+" ");
                if(parent.getLeft() != null) {
                    nodes.add(parent.getLeft());
                }
                if(parent.getRight() != null) {
                    nodes.add(parent.getRight());
                }
            }
        }
    }
}
