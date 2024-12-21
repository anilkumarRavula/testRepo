package com.ds.linkedList;

public class CycleInLinkedList {
    public static void main(String[] args) {
        ListNode<Integer> a = new ListNode<>(1);
        System.out.println(hasCycle(a));

        a.next = new ListNode(2);
        System.out.println(hasCycle(a));
        a.next.next = a;
        System.out.println(hasCycle(a));

    }
    public static  boolean hasCycle(ListNode head) {
        ListNode next = next(head);
        ListNode current =  head;
        ListNode prev = head; //creatin cycle wantedly
        while(next != null && next != head ) {
           ListNode nextNode = current.next;
            current.next = prev;
            prev = current;
            current = nextNode;
            next = next(next);
        }
        if(next == null) return  false;
        else {
            return true;
        }

    }

    public static  boolean hasCycle1(ListNode head) {
        ListNode next = next(head);
        while((next != null && next.next != null && head != next )) {
            head = head.next;
            next = next(next);
        }
        if(next == null) return  false;
        else {
            return true;
        }

    }
    private static ListNode next(ListNode next) {
        return next.next != null ? next.next.next : next.next;
    }

}
