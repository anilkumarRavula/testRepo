package src.com.ds.tress;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;

public class Traversal {

    public static void main(String[] args) {
        System.out.println(null == null);
        Node n = new Node(4,new Node(2,new Node(1),new Node(3)),
                new Node(6,new Node(5,new Node(4),null),new Node(7)));

        preOrder(n); System.out.println("     preOrder <->");
        preOrderTraditional(n);
        inOrder(n); System.out.println("     inOrder <->");
        inOrderTraditionalIterator(n);inOrderWithNewNode(n);
        postOrder(n); System.out.println("     postOrder <->");
        postOrderIterator(n);

    }

    public static void preOrder(Node root) {
        if(root == null) return;
        System.out.print(root.getData()+"->");
        preOrder(root.getLeft());
        preOrder(root.getRight());
    }
    public static void preOrderTraditional(Node root) {
        System.out.println("preOrderTraditional ->");

        Deque<Node> deque = new ArrayDeque<Node>() ;
        deque.addLast(root);
        while(!deque.isEmpty()){
            Node current = deque.poll();
            System.out.print(current.getData()+"->");
            if(current.getRight() != null) deque.addFirst(current.getRight());
            if(current.getLeft() != null)  deque.addFirst(current.getLeft());
        }
        System.out.println("      preOrderTraditional <-");

    }

    static void inOrder(Node root) {
        if(root == null) return;
        inOrder(root.getLeft());
        System.out.print(root.getData()+"->");
        inOrder(root.getRight());

    }
    static void inOrderTraditionalIterator(Node root) {
        System.out.println("inOrderTraditionalIterator ->");
        Deque<Node> deque = new ArrayDeque<Node>() ;
        //deque.addLast(root);
        Node current = root;

        while(!deque.isEmpty() || current != null){
           if(current != null) {
               deque.push(current);
               current = current.getLeft();
           } else{
               current = deque.poll();
               System.out.print(current.getData()+"->");
               current = current.getRight();
           }
        }
        System.out.println("      inOrderTraditionalIterator <-");

    }
    static void inOrderTraditionalWithMap(Node root) {
        System.out.println("inOrderTraditional ->");
        HashSet<Integer> rootNodes = new HashSet<>();
        Deque<Node> deque = new ArrayDeque<Node>() ;
        deque.addLast(root);
        while(!deque.isEmpty()){
            Node current = deque.poll();
            if(!rootNodes.contains(current.getData())) {
                if(current.getRight() != null) deque.addFirst(current.getRight());
                 deque.addFirst(current);
                if(current.getLeft() != null)  deque.addFirst(current.getLeft());
                rootNodes.add((Integer) current.getData());
            } else {
                System.out.print(current.getData()+"->");
            }
        }
        System.out.println("      inOrderTraditional <-");

    }
    static void inOrderWithNewNode(Node root) {
        System.out.println("inOrderWithNewNode ->");
        HashSet<Integer> rootNodes = new HashSet<>();
        Deque<Node> deque = new ArrayDeque<Node>() ;
        deque.addLast(root);
        while(!deque.isEmpty()){
            Node current = deque.poll();
                if(current.getRight() != null) deque.addFirst(current.getRight());
                if(current.getRight() != null || current.getLeft() != null) {
                    deque.addFirst(new Node(current.getData()));
                } else {
                    System.out.print(current.getData() + "->");
                }
                if(current.getLeft() != null)  deque.addFirst(current.getLeft());

        }
        System.out.println("      inOrderWithNewNode <-");

    }
    static void postOrder(Node root) {
        if (root == null) return;
        postOrder(root.getLeft());
        postOrder(root.getRight());
        System.out.print(root.getData() + "->");
    }
    static void postOrderIterator(Node root) {
        System.out.println("postOrderIterator ->");
        Deque<Node> deque = new ArrayDeque<Node>() ;
        //deque.addLast(root);
        Node current = root;
        Deque<Node> nodesInQueue = new ArrayDeque<Node>() ;
        while(!deque.isEmpty() || current != null){
            if(current != null) {
                deque.addFirst(current);
                current = current.getLeft();
            } else{
                while(!nodesInQueue.isEmpty() && deque.peek().equals(nodesInQueue.peek())) {
                    System.out.print(deque.poll().getData()+"->");
                    nodesInQueue.poll();
                }
                if(deque.isEmpty()) return;
                nodesInQueue.addFirst(deque.peek());

                current = deque.peek().getRight();
            }
        }
        System.out.println("      postOrderIterator <-");

    }

    }
