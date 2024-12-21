package com.ds.linkedList;

/**
 * Given the head of a singly linked list and two integers left and right where left <= right, reverse the nodes of the list from position left to position right, and return the reversed list.
 *
 *
 *
 * Example 1:
 *
 *
 * Input: head = [1,2,3,4,5], left = 2, right = 4
 * Output: [1,4,3,2,5]
 * Example 2:
 *
 * Input: head = [5], left = 1, right = 1
 * Output: [5]
 */
public class ReverseLinkedListSpecifiedIntervals {
    public static void main(String[] args) {
        reverseBetween(ListNode.creteNodes(1,2,3,4,5),3,4).print();
    }

    public static ListNode reverseBetween(ListNode head, int left, int right) {
        if(head == null) return  head;
        int counter = 1;
        ListNode current = head;
        ListNode prev = null;
        ListNode start = null;
        while(current != null) {
            ListNode next = current.next;
            if(counter == left) {
                start = prev;
            } else if(counter > left && counter <= right) {
                current.next = prev;
            }
            if(counter == right) {
                if(start != null) {
                    ListNode leftNode = start.next;
                    start.next = current;
                    leftNode.next = next;
                } else {
                    head.next = next;
                    head = current;
                }

            }
            prev = current;
            current = next;
            counter++;
        }

        return head;
    }
}
