package com.ds.linkedList;

public class LinkedListReverse {
    public static void main(String[] args) {
        LinkedListNode<Integer> linkedList = new LinkedListNode<>(9,
                new LinkedListNode(9,
                        new LinkedListNode(9,new LinkedListNode(9))));
        System.out.println(linkedList);
        LinkedListNode<Integer> current = linkedList;
        current = reverseLinkedList(current);
        LinkedListNode<Integer> prev;
        prev = null;
        int carry = 1;
        while(current != null) {
            System.out.println(current.getValue());
            int newValue = current.getValue()+carry;
            carry =  newValue > 9 ?  1 :  0;
            current.setValue(newValue%10);
            LinkedListNode nextNode =  current.getNext();
            current.setNext(prev);
            prev = current;
            current = nextNode;
        }
        if(carry > 0) {
            prev =  new LinkedListNode(1,prev);
        }
        System.out.println(prev);
    }

    private static LinkedListNode<Integer> reverseLinkedList(LinkedListNode<Integer> current) {
        LinkedListNode<Integer> prev = null;
        while(current != null) {
            System.out.println(current.getValue());
            LinkedListNode nextNode =  current.getNext();
            current.setNext(prev);
            prev = current;
            current = nextNode;
        }
        System.out.println(prev);
        current = prev;
        return current;
    }
}
