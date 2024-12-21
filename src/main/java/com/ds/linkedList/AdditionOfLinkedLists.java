package com.ds.linkedList;

/**
 * You are given two non-empty linked lists representing two non-negative integers. The digits are stored in reverse order, and each of their nodes contains a single digit. Add the two numbers and return the sum as a linked list.
 *
 * You may assume the two numbers do not contain any leading zero, except the number 0 itself.
 *
 *
 *
 * Example 1:
 *
 *
 * Input: l1 = [2,4,3], l2 = [5,6,4]
 * Output: [7,0,8]
 * Explanation: 342 + 465 = 807.
 */
public class AdditionOfLinkedLists {
    public static void main(String[] args) {
            ListNode<Integer> list1 = new ListNode<>(9);
            list1.next = new ListNode(9);
            list1.next.next = new ListNode(9);
            ListNode<Integer> list2 = new ListNode<>(9);
            list2.next = new ListNode(9);
            list2.next.next = new ListNode(9);
            list2.next.next.next = new ListNode(9);
            list2.next.next.next.next = new ListNode(9);

        addTwoNumbers(list1,list2).print();
        addTwoNumbers(ListNode.creteNodes(9,9,9,9,9,9,9,9),ListNode.creteNodes(9,9,9,9)).print();
        addTwoNumbers(ListNode.creteNodes(2,4,3),ListNode.creteNodes(5,6,4)).print();

        }

    public static ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        int carry = 0;
        ListNode dataSumNode = l1;
        ListNode prev = null;
        while(l1 != null && l2 != null) {
            carry = addAndGetCarry(carry,l1,l1.val+l2.val);
            prev = l1;
            l1 = l1.next;
            l2 = l2.next;
        }

        while(l1 != null) {
            carry = addAndGetCarry(carry, l1, l1.val);
            prev = l1;
            l1 = l1.next;
        }

        while(l2 != null) {
            if(l1 == null) {
                prev.next = l2;
                l1 = prev;
            }
            carry = addAndGetCarry(carry, l2, l2.val);
            prev = l2;
            l2 = l2.next;
        }

        prev.val = prev.val+carry;

        return dataSumNode;
    }
    private static int addAndGetCarry(int carry, ListNode node, int sumData) {
        int currentNodesSum =  carry + sumData;
        node.val = currentNodesSum%10;
        carry = (currentNodesSum)/10;
        return carry;
    }

}
