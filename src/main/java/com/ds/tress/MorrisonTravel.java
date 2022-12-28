package com.ds.tress;

import com.sun.jdi.event.ThreadStartEvent;

import static com.ds.tress.Traversal.preOrderTraditional;

public class MorrisonTravel {
    public static void main(String[] args) {
        Node n = new Node(4,new Node(2,new Node(1),new Node(3)),
                new Node(6,new Node(5,new Node(4),null),new Node(7)));
        preOrderTraditional(n);
        preOrder(n);inOrder(n);

    }
    static void preOrder(Node root) {
        System.out.println(Thread.currentThread().getStackTrace()[1].getMethodName());
        System.out.println(" preOrder MorrisonTravel  ->");

        Node current = root;
        while(current != null) {
            //System.out.println(current.getValue());
            if(current.getLeft() != null) {
                //get leftSubtree
                Node rightMost = current.getLeft(); // start from current left is the starting point
                //get right most for the leftsuntree
                while(rightMost.getRight()!=null && !rightMost.getRight().equals(current)) {
                    rightMost = rightMost.getRight();
                }
                if(rightMost.getRight() == null) {
                    //processing this node first time
                    current.process();
                    //set current node to right most
                    rightMost.setRight(current);
                    //start processing left from thsi node
                    current = current.getLeft();

                } else if(rightMost.getRight().equals(current)) { ////when right most is current node
                    // we are at right most node of  parent node let process right node
                    //reset rightnode
                    rightMost.setRight(null);
                    // jump to right node
                    current = current.getRight();
                }
            } else {
                current.process();
                current = current.getRight();
            }
        }

        System.out.println(" preOrder MorrisonTravel  <--");

    }

    static void inOrder(Node root) {
        System.out.println(" inOrder MorrisonTravel  ->");
        Node current = root;
        while(current != null) {
            //System.out.println(current.getValue());
            if(current.getLeft() != null) {
                //get leftSubtree
                Node rightMost = current.getLeft(); // start from current left is the starting point
                //get right most for the leftsuntree
                while(rightMost.getRight()!=null && !rightMost.getRight().equals(current)) {
                    rightMost = rightMost.getRight();
                }
                if(rightMost.getRight() == null) {
                    //processing this node first time
                    //current.process();
                    //set current node to right most
                    rightMost.setRight(current);
                    //start processing left from thsi node
                    current = current.getLeft();

                } else if(rightMost.getRight().equals(current)) { ////when right most is current node
                    // we are at right most node of  parent node let process right node
                    //reset rightnode
                    rightMost.setRight(null);
                    // jump to right node
                    current.process();
                    current = current.getRight();
                }
            } else {
                current.process();
                current = current.getRight();
            }
        }

        System.out.println(" inOrder MorrisonTravel  <--");

    }
}
