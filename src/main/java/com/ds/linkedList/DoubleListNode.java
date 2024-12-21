package com.ds.linkedList;

public class DoubleListNode<T> {
      public int val;
      public int key;
    public DoubleListNode prev;
    public DoubleListNode next;
    public DoubleListNode(int x, int value) {
        this.key = x;
        this.val = value;
        next = null;
    }
    public static void print(ListNode elements) {
        while(elements != null) {
            System.out.print(elements.val);
            System.out.print(" ");

            elements = elements.next;
        }
        System.out.println();
    }
    public  void print() {
        System.out.print(val);
        DoubleListNode current = next;
        while(current != null) {
            System.out.print(" ");
            System.out.print(current.val);
            current = current.next;
        }
        System.out.println();
    }


}
