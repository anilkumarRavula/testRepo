package com.ds.linkedList;

public class DetectCycleInLikedList {
    private static void print(LinkedListNode head) {
        while(head != null) {
            System.out.println(head);
            head = head.next();
        }
    }

    static boolean hasCycle(LinkedListNode root) {
        LinkedListNode forward = root;
        LinkedListNode prev = root;
        int loops = 1;
        LinkedListNode current = root;
        while(current != null && forward != null) {
            System.out.println(current);
            prev = current;
            current = current.next();
            forward = forward.next() != null ? ((forward.next().next()) != null ? forward.next().next() : forward.next()) : null;
            System.out.println(loops++);
           // System.out.println("forward"+forward);
            if(current == forward) break;
        }
        if(current == null || forward == null) return true;
        System.out.println("cycle found");
        System.out.println(forward);

        if(root == forward) {
            prev.next(null);
        } else{
            current = root;
            while (current.next() != null) {
                LinkedListNode next = current.next();

                if(next == forward) {
                    // previous in sync
                    if(prev == current) {
                        forward.next(null);
                        break;
                    } else {
                        prev.next(null);
                        break;
                    }
                }
                current =next;
            }
        }
        return true;
    }

    public static void main(String[] args) {
        LinkedListNode head = new LinkedListNode(1);

        LinkedListNode circular = new LinkedListNode(2,new LinkedListNode(3,new LinkedListNode(4)));
        circular.next().next().next(new LinkedListNode(5,new LinkedListNode(6,head)));
        head.next(circular);
        hasCycle(head);
        System.out.println("After cycle detection");
        print(head);
    }

}
