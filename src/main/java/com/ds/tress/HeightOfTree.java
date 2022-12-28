package src.com.ds.tress;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Stack;

public class HeightOfTree {
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
    public static void main(String[] args) {
        Node n = new Node(1,new Node(2,new Node(4,new Node(11,new Node(15),null),new Node(12)),new Node(5)),
                new Node(3,new Node(6,new Node(8),new Node(9)),new Node(7)));
        System.out.println(heightOfTree(n));
    }
    public static int heightOfTree(Node root) {
        int height = 0;
        Deque<Node> deque = new ArrayDeque<>();
        deque.addFirst(root);
        while(!deque.isEmpty()) {

            int nodesCount = deque.size();
            height++;

           while(nodesCount > 0) {
               Node current = deque.poll();
               if(current.getLeft() != null) deque.addLast(current.getLeft());
               if(current.getRight() != null) deque.addLast(current.getRight());
               nodesCount--;
           }

        }
       return height-1;
    }
}
