package com.ds.linkedList;

import java.util.HashMap;
import java.util.Map;

/**
 * A linked list of length n is given such that each node contains an additional random pointer, which could point to any node in the list, or null.
 *
 * Construct a deep copy of the list. The deep copy should consist of exactly n brand new nodes, where each new node has its value set to the value of its corresponding original node. Both the next and random pointer of the new nodes should point to new nodes in the copied list such that the pointers in the original list and copied list represent the same list state. None of the pointers in the new list should point to nodes in the original list.
 *
 * For example, if there are two nodes X and Y in the original list, where X.random --> Y, then for the corresponding two nodes x and y in the copied list, x.random --> y.
 *
 * Return the head of the copied linked list.
 *
 * The linked list is represented in the input/output as a list of n nodes. Each node is represented as a pair of [val, random_index] where:
 *
 * val: an integer representing Node.val
 * random_index: the index of the node (range from 0 to n-1) that the random pointer points to, or null if it does not point to any node.
 * Your code will only be given the head of the original linked list.
 *
 *
 *
 * Example 1:
 *
 *
 * Input: head = [[7,null],[13,0],[11,4],[10,2],[1,0]]
 * Output: [[7,null],[13,0],[11,4],[10,2],[1,0]]
 * Example 2:
 *
 *
 * Input: head = [[1,1],[2,1]]
 * Output: [[1,1],[2,1]]
 * Example 3:
 *
 *
 *
 * Input: head = [[3,null],[3,0],[3,null]]
 * Output: [[3,null],[3,0],[3,null]]
 */
public class CopyLinkedLIstWithRandomePointer {
    public static void main(String[] args) {
        copyRandomListv2(Node.creteNodes(new int[]{7, -1
        },new int[]{13,0},new int[]{11,4},new int[]{10,2},new int[]{1,0})).print();
    }
    public static Node copyRandomList(Node head) {
        head.print();
        //System.out.println( getLength(head));
        int length = getLength(head);
        int[] nodesValues = new int[length];
        int[] randomValues = new int[length];
        Node[] clonedNodes = new Node[length+1];
        //set nodes values with index
        Node current = head;
        int counter = 0;
        while(current != null) {
            nodesValues[counter] = current.val;
            clonedNodes[counter] = new Node(current.val);
            current.val = counter;
            current = current.next;
            counter++;
        }
        //setvalues with random
         current = head;
         counter = 0;
        while(current != null) {
            randomValues[counter] = current.random == null ? -1 : current.random.val;
            current = current.next;
            counter++;
        }

        current = head;
        counter = 0;
        while(current != null) {
            Node clonedNode = clonedNodes[counter];
            if(current.random != null)
             clonedNode.random = clonedNodes[randomValues[counter]];
            clonedNode.next = clonedNodes[counter+1];
            current = current.next;
            counter++;
        }

        return clonedNodes[0];
    }
    public static Node copyRandomListv2(Node head) {
        head.print();
        //System.out.println( getLength(head));
        Map<Node,Node> cloenedNodes  = new HashMap<>();
        //set nodes values with index
        Node current = head;
        int counter = 0;
        while(current != null) {
            cloenedNodes.put(current,new Node(current.val));
            current = current.next;
            counter++;
        }
        current = head;
        while(current != null) {
            Node clonedNode = cloenedNodes.get(current);
            if(current.random != null)
                clonedNode.random = cloenedNodes.get(current.random) ;
            clonedNode.next = cloenedNodes.get(current.next);
            current = current.next;
        }

        return cloenedNodes.get(head);
    }
    public static int getLength(Node head) {
        int length  = 1;
        Node next = head.next;
        if(next == null) return length ;
        if(next.next == null) return 2;

        Node fast = next.next;
        length = 3;
        int point = 1;
        while(fast != null) {
            System.out.println("->"+point++);
            if(fast.next == null) return length ;
            if(fast.next.next == null) return length+1;
            length = length + 2;
            fast = fast.next.next;
        }
        //System.out.println(length);
        return length;
    }


}
class Node {
    int val;
    Node next;
    Node random;

    public Node(int val) {
        this.val = val;
        this.next = null;
        this.random = null;
    }
    public static Node creteNodes (int[]... numbers) {
        Node node = new Node(numbers[0][0]);
        Node prev = node;
        Node[] nodes = new Node[numbers.length];
        nodes[0] = node;
        for (int i = 1; i < numbers.length ; i++) {
            prev.next = new Node(numbers[i][0]);
            prev = prev.next;
            nodes[i] = prev;
        }
        for (int i = 0; i < numbers.length ; i++) {
            prev = nodes[i];
            if(numbers[i][1] >= 0)
            prev.random = nodes[numbers[i][1]];
        }
        return node;
    }
    public int getLength() {
        int length  = 1;
        if(next == null) return length ;
        if(next.next == null) return 2;

        Node fast = next.next;
        length = 3;
        int point = 1;
        while(fast != null) {
            System.out.println("->"+point++);
            if(fast.next == null) return length ;
            if(fast.next.next == null) return length+1;
            length = length + 2;
            fast = fast.next.next;
        }
        System.out.println(length);
        return length;
    }
    public  void print() {
        System.out.print("[" + val+ ","+ (random != null ? random.val : null)+"]");
        Node current = next;
        while(current != null) {
            System.out.print(" ");
            System.out.print("[" + current.val+ ","+ (current.random != null ? current.random.val : null)+"]");
            current = current.next;
        }
        System.out.println();
    }
}

