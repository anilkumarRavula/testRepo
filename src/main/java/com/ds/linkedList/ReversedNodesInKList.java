package com.ds.linkedList;

/**
 * Given the head of a linked list, reverse the nodes of the list k at a time, and return the modified list.
 *
 * k is a positive integer and is less than or equal to the length of the linked list. If the number of nodes is not a multiple of k then left-out nodes, in the end, should remain as it is.
 *
 * You may not alter the values in the list's nodes, only nodes themselves may be changed.
 *
 *
 *
 * Example 1:
 *
 *
 * Input: head = [1,2,3,4,5], k = 2
 * Output: [2,1,4,3,5]
 * Example 2:
 *
 *
 * Input: head = [1,2,3,4,5], k = 3
 * Output: [3,2,1,4,5]
 */
public class ReversedNodesInKList {
    public static void main(String[] args) {
        reverseKGroup3(ListNode.creteNodes(1,2,3,4,5),5).print();
    }

    /**
     *             //keep start to make it as tail
     *
     *             //for every k iterations reverse elements
     *         // if( k itearation done fully connect holding head with current element
     *                 // and connect tail with compled k nodes
     *         // else
     *                 //rollback revesed elements
     *
     *         //head postion --> dummy .next in case of k<= length, else  head
     * @param head
     * @param k
     * @return
     */
    public static ListNode reverseKGroup3(ListNode head, int k) {
        if(k ==1 || head == null) return  head;
        ListNode finalHead = null;
        ListNode tailNode = new ListNode(0);
        tailNode.next = head;      // 0gtl->1    2(c)-> p     3   4  5
        ListNode current = head;           //

        //loop though all elements till last element
            while(current != null) {

                ListNode prev = null;

                int i = 0;
                while (i< k && current != null) {
                    ListNode next = current.next;
                    current.next = prev;
                    prev = current;
                    current = next;
                    i++;
                }                                                     //0->
                 if(i == k) {
                     if(finalHead == null) {
                         finalHead = prev;                                  //null<-1<-2<-3(prev)  null(c)
                     }
                     tailNode.next.next = current; // 1-> 3 3-> 5
                     ListNode newTailNode = tailNode.next;
                     tailNode.next = prev; // 0> 2 3->
                     tailNode = newTailNode ;
                     //prev = groupTailNode;           //gtl ->1->3  gtk->3
                 }
                //
                if(i < k) {
                    current =  prev;                                // 0gtl->1    2(c)-> p     3   4  5(c)
                    prev = null;
                    while ( i > -1  && current != null) {
                        ListNode next = current.next; // 4  3
                        current.next = prev;    //5-> null 4-> 5
                        prev = current;    // prev -> 5 4
                        current = next;   // current 4 3
                        i--;
                    }
                    if(current != null)
                        current.next = prev;
                    //attach ends

                }
                if(finalHead == null) {
                    finalHead = tailNode.next;
                }
            }
        return finalHead;
    }
    public ListNode reverseKGroup(ListNode head, int k) {
        int counter = 1;

        ListNode dummy = new ListNode(0);
        ListNode groupStartNode = dummy;
        ListNode current = head;
        ListNode prev = null;
        groupStartNode.next = head; // keep start element in group start node next
        while(current != null) {
            //reverse nodes
            if(prev != null) {
                prev.next  = current;
            }
            prev = current;
            current = current.next;
            if(counter == k) {
                counter = 1;
                ListNode starNode = groupStartNode.next;
                starNode.next = current.next;//make start node as tail in kgroup
                groupStartNode.next= current; // make last element as start
                groupStartNode = starNode;// point to prev node of k group
            } else {
                counter++;
            }
        }

        return dummy.next;
    }

    public static ListNode reverseKGroup2(ListNode head, int k) {

        ListNode current = head;
        ListNode startNode = head;
        ListNode headKlist = null;
        while(current != null) {
            int i =0;
            ListNode prev = current.next; //lets assume 1(s) 2(p) 3(S) 4() 5
            while ( i < k && current != null) {
                //reverse all elemennts
                ListNode next = current.next;
                current.next = prev;
                prev = current;
                current = next;
                i++;
            }
            startNode.next = current;
            startNode = prev;
            if(headKlist == null) {
                headKlist = prev;
            }
            if(i < k ) {
                // reverse from tail
                ListNode next = prev;
                prev = current;
                current = next;
                while (i >= 0) {
                    next = current.next;
                    current.next = prev;
                    prev = current;
                    current = next;
                    i--;
                }
                if(current == null) {
                    headKlist = prev;
                }
            }
        }


        return current;
    }
}
