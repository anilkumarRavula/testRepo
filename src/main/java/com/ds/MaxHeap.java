package com.ds;
/**
 * 7,5,1,8,2,4,3,6
 */
public class MaxHeap {

    int[] heap =null;
    int pointer = 0;
    MaxHeap(int size) {
        heap = new int[size];
    }
    public void insert(int value) {
        heap[pointer++] = value;
        //pushup
        pushUp();
    }
    public int top() {
        return heap[0];
    }

    public int remove() {
        int head =  heap[0];
        if(pointer> 0) {
            heap[0] =  heap[--pointer];
        }
        else pointer--;
        pushdown();
        return head;
    }

    private void pushdown() {
        // get left and right compare and swap till finds proper postion
        int position = 0;
        while(position <= pointer) {
            int rightChild = position* 2 + 2; //1
            int leftChild = position* 2 + 1;  // 1
            if(rightChild <= pointer && heap[leftChild] < heap[rightChild]) {
                 swapPostions(position, rightChild);
                 position = rightChild;
            } else if(leftChild <= pointer && heap[leftChild] > heap[position]){ //handle left child
                //swap
                swapPostions(position, leftChild);
                position = leftChild;
            } else { // it's biggeers
                return;
            }
        }
    }

    private void swapPostions(int position, int newHeadPostion) {
        int newHead = heap[newHeadPostion];
        heap[newHeadPostion]  = heap[position];
        heap[position] = newHead;
    }

    public int size() {
        return pointer;
    }

    public void pushUp() {

        int position = pointer -1;
        if(position <= 0) return;
        while(position > 0) {
            int parent = (position-1)/2;
            if( heap[parent] < heap[position]) {
                    swapPostions(parent,position);
            } else { //
               return;
            }
            position = parent;
        }
    }

    public static void main(String[] args) {
         MaxHeap min = new MaxHeap(7);
         min.insert(7);
         min.insert(5);
         min.insert(1);
         assertHeap(min,7,3);
         min.insert(8);

        //min.remove();assertHead(min,7);
        //min.remove();assertHead(min,5);
       // min.remove();assertHead(min,1);
        min.insert(2);
        min.insert(4);
        min.insert(6);
        assertHead(min,8);
        min.remove();assertHead(min,7);
        min.remove();assertHead(min,6);
        min.remove();assertHead(min,5);
        min.remove();assertHead(min,4);
        min.insert(9);assertHead(min,9);
        min.remove();assertHead(min,4);
        min.remove();assertHead(min,2);
    }
    static void assertHeap(MaxHeap min, int head , int size) {
        assertHead(min,head);
        assert min.size() == size : "size not matching";
    }
    static void assertHead(MaxHeap min, int head ) {
        assert min.top() == head : "top not matching";

    }



}
