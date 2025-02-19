package com.ds.linkedList;

import java.util.HashMap;
import java.util.Map;

/**
 * LRUCache lRUCache = new LRUCache(2);
 * lRUCache.put(1, 1); // cache is {1=1} 0->1
 * lRUCache.put(2, 2); // cache is {1=1, 2=2} 1-2
 * lRUCache.get(1);    // return 1   2-1
 * lRUCache.put(3, 3); // LRU key was 2, evicts key 2, cache is {1=1, 3=3}   1 3
 * lRUCache.get(2);    // returns -1 (not found)
 * lRUCache.put(4, 4); // LRU key was 1, evicts key 1, cache is {4=4, 3=3} 4-3
 * lRUCache.get(1);    // return -1 (not found)
 * lRUCache.get(3);    // return 3
 * lRUCache.get(4);    // return 4 3-4
 *
 * 1   t
 *
 *  2 t
 *
 * 3 t
 *
 * 4 t
 *
 * 5 t
 *
 * 3(?)
 */
public class LRUCacheV2 {
    Map<Integer,DoubleListNode> cache;
    DoubleListNode dummyNode = new DoubleListNode(-1, -1);
    DoubleListNode head = null;
    DoubleListNode tail = null;
    int capacity = 0;
    int size = 0;
    public LRUCacheV2(int capacity) {
        cache = new HashMap<>(capacity);
        this.capacity = capacity;
    }

    public int get(int key) {
        DoubleListNode value = cache.getOrDefault(key,dummyNode);
        //adjust cache order
        if(value != dummyNode)
            moveNodeToTail(value);
        System.out.println(value.val);
        return value.val;
    }

    public void put(int key, int value) {
        DoubleListNode newNode = cache.getOrDefault(key,new DoubleListNode(key,value));

        updateCacheIfFull(key);
        //remove and adjust order
        if(cache.containsKey(key)) {
            cache.get(key).val = value;
            size--;
        }
        cache.put(key,newNode);
        init(cache.get(key));
        size++;
        moveNodeToTail(newNode);
    }

    private void init(DoubleListNode node) {
        if(size != 0) return;
        head = node;
        tail = node;
        head.next = tail;
        tail.prev = head;
    }

    private void updateCacheIfFull(int key) {
        if (size < capacity || cache.containsKey(key)) {
            return;
        }
        cache.remove(head.key);
        System.out.println("--"+head.key);
        size --;
        head = head.next;
        head.prev = null;
    }

    private void moveNodeToTail(DoubleListNode node) {   // 2-2
        if(node == tail) return;
        DoubleListNode previousNode = node.prev;      //h 2 -<1 3 4 t
        addToTail(node);
        if(head == node){                           //if it is head node
            head = head.next;
            head.prev = null;
        } else if(previousNode != null){ //new node       // existing node
            previousNode.next = node.next;
            previousNode.prev = previousNode.next;
        }
    }
    private void addToTail(DoubleListNode node) {
        tail.next = node;
        node.prev = tail;
        tail = tail.next;
    }
    public static void main(String[] args) {
        LRUCacheV2 lRUCache = new LRUCacheV2(10);
        lRUCache.put(10,13);
        lRUCache.put(3,17);
        lRUCache.put(6,11);
        lRUCache.put(10,5);
        lRUCache.put(9,10);
        lRUCache.get(13);
        lRUCache.put(2,19);
        lRUCache.get(2);
        lRUCache.get(3);
        lRUCache.put(5,25);
        lRUCache.get(8);
        lRUCache.put(9,22);
        lRUCache.put(5,5);
        lRUCache.put(1,30);
        lRUCache.get(11);
        lRUCache.put(9,12);
        lRUCache.get(7);
        lRUCache.get(5);
        lRUCache.get(8);
        lRUCache.get(9);
        lRUCache.put(4,30);
        lRUCache.put(9,3);
        lRUCache.get(9);
        lRUCache.get(10);
        lRUCache.get(10);
        lRUCache.put(6,14);
        lRUCache.put(3,1);
        lRUCache.get(3);
        lRUCache.put(10,11);
        lRUCache.get(8);
        lRUCache.put(2,14);
        lRUCache.get(1);
        lRUCache.get(5);
        lRUCache.get(4);
        lRUCache.put(11,4);
        lRUCache.put(12,24);
        lRUCache.put(5,18);
        lRUCache.get(13);
        lRUCache.put(7,23);
        lRUCache.get(8);
        lRUCache.get(12);
        lRUCache.put(3,27);
        lRUCache.put(2,12);
        lRUCache.get(5);
        lRUCache.put(2,9);
        lRUCache.put(13,4);
        lRUCache.put(8,18);
        lRUCache.put(1,7);
        lRUCache.get(6);
        lRUCache.put(9,29);

    }

}
