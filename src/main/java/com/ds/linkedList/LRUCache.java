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
public class LRUCache {
    Map<Integer,Integer> cache;
    ListNode head = null;
    ListNode tail = null;
    int capacity = 0;
    int size = 0;
    public LRUCache(int capacity) {
        cache = new HashMap<>(capacity);
        this.capacity = capacity;
        head = new ListNode(0);
        tail =   head;
        head.next = tail;
    }

    public int get(int key) {
       int value = cache.getOrDefault(key,-1);
        //adjust cache order
        if(value != -1)
            updateOrder(key);
        System.out.println(value);

        return value;
    }

    public void put(int key, int value) {
        if (size == 0) head.val = key;
        updateCacheIfFull(key);
        //remove and adjust order
        if (cache.containsKey(key))
            updateOrder(key);
        else
            adjustCacheOrder(key);
        cache.put(key,value);
        size++;

    }

    private void updateCacheIfFull(int key) {
        if (size < capacity || cache.containsKey(key)) {
            return;
        }
        size --;
        head = head.next;
    }

    private void adjustCacheOrder (int key) {
        if(key == tail.val) return;
         tail.next = new ListNode(key);
         head = head.next;
         tail = tail.next;
    }
    private void updateOrder (int key) {

        ListNode current = head;
        ListNode dummy = new ListNode(0);
        dummy.next = head;
        ListNode prev = dummy;
        while(current != null) {

            if(current.val == key) {
                prev.next = current.next;
                current.next = null;
                break;
            }
            prev = current;
            current = current.next;

        }
        tail.next = current;
        tail = tail.next;
        head = dummy.next;

    }
    public static void main(String[] args) {
        LRUCache lRUCache = new LRUCache(4);
        lRUCache.put(2, 6); // cache is {1=1}
        lRUCache.put(1, 2); // cache is {1=1, 2=2}
        lRUCache.put(3, 5); // LRU key was 2, evicts key 2, cache is {1=1, 3=3}
        lRUCache.put(4, 5); // LRU key was 2, evicts key 2, cache is {1=1, 3=3}
        lRUCache.put(1, 7); // LRU key was 2, evicts key 2, cache is {1=1, 3=3}
        lRUCache.get(1);    // returns -1 (not found)
        lRUCache.get(2);    // returns -1 (not found)

        lRUCache.put(8,1);
        lRUCache.get(3);
    }

}
