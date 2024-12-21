package com.ds.linkedList;

import java.util.Objects;
import java.util.Optional;

public class ListNode<T> {
      public int val;
    public ListNode next;
    public ListNode(int x) {
          val = x;
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
        ListNode current = next;
        while(current != null) {
            System.out.print(" ");
            System.out.print(current.val);
            current = current.next;
        }
        System.out.println();
    }
    public static ListNode creteNodes (int... numbers) {
        ListNode node = new ListNode(numbers[0]);
        ListNode prev = node;
        for (int i = 1; i < numbers.length ; i++) {
            prev.next = new ListNode(numbers[i]);
            prev = prev.next;
        }
        return node;
    }
}
