package com.ds.linkedList;

/**
 * You are given the heads of two sorted linked lists list1 and list2.
 *
 * Merge the two lists into one sorted list. The list should be made by splicing together the nodes of the first two lists.
 *
 * Return the head of the merged linked list.
 *
 *
 *
 * Example 1:
 *
 *
 * Input: list1 = [1,2,4], list2 = [1,3,4]
 * Output: [1,1,2,3,4,4]
 * Example 2:
 *
 * Input: list1 = [], list2 = []
 * Output: []
 * Example 3:
 *
 * Input: list1 = [], list2 = [0]
 * Output: [0]
 */
public class MergeSortedLinkedLists {
    public static void main(String[] args) {
        ListNode<Integer> list1 = new ListNode<>(1);
        list1.next = new ListNode(2);
        list1.next.next = new ListNode(4);
        ListNode<Integer> list2 = new ListNode<>(0);
        list2.next = new ListNode(3);
        list2.next.next = new ListNode(4);
        list2.next.next.next = new ListNode(5);
        list2.next.next.next.next = new ListNode(6);

        ListNode elements = mergeTwoLists(list1,list2);
        //ListNode elements = mergeTwoLists(null,new ListNode(3));

        print(elements);
        ListNode elements1 = mergeTwoLists(null,new ListNode(3));
        print(elements1);
        ListNode<Integer> list24 = new ListNode<>(1);
        list24.next = new ListNode(2);
        list24.next.next = new ListNode(3);
        list24.next.next.next = new ListNode(4);

        ListNode elements2 = mergeTwoLists(new ListNode(5), list24);
            print(elements2);

    }

    private static void print(ListNode elements) {
        while(elements != null) {
            System.out.print(elements.val);
            System.out.print(" ");

            elements = elements.next;
        }
        System.out.println();
    }
    public static ListNode mergeTwoLists(ListNode list11, ListNode list2) {
        ListNode head = null;

        while(list11 != null && list2 != null) {

            ListNode leftStartNode = list11;
            ListNode rightStartNode = list2;

            ListNode prev = null;

            while (list11 != null && list11.val <= list2.val) { // identify point for list one
                if(head == null)
                    head = leftStartNode;
                prev = list11;
                list11 = list11.next;
            }
            if(prev != null) {
                prev.next = list2;
            }
            prev = null;
            while (list11 != null && list2 != null && list11.val >= list2.val) { // identify point for list one
                if(head == null)
                    head = rightStartNode;
                prev = list2;
                list2 = list2.next;
            }
            if(prev != null) {
                prev.next = list11;
            }

        }

        return head == null ? (list11 == null ? list2 :  list11) : head;
    }

}
