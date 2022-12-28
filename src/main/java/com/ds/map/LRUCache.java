package src.com.ds.map;

import java.util.HashMap;
import java.util.Map;

public class LRUCache {
    private final int size;
    Map<Integer,Node>  hashMap = new HashMap<>();
    LRUCache(int size) {
        this.size = size;
    }

    public class Node {
        int data;
        Node next;
        Node prev;
        Node(int data) {this.data = data;}

        @Override
        public String toString() {
            return  data +
                    "--->" + next ;

        }
    }
    Node tail;
    Node head;
    private int get(int data) {
        if(hashMap.containsKey(data) && head.data != data) {
            //remove and add to head
            Node curent = hashMap.get(data);
            //delink and assign
            curent.prev.next = curent.next;
            if (tail.data == curent.data) {
                removeNAssignTail();
            }else   curent.next.prev = curent.prev;

             addAsHead(curent);
        }

        return hashMap.get(data).data;
    }
    private void put(int data) {
        Node n = new Node(data);
        if(hashMap.size() == size) {
            // remove tail
            hashMap.remove(tail.data);
            removeNAssignTail();
        }
        if(tail == null) tail = head;

        addAsHead(n);
        hashMap.put(data,n);
    }

    private void removeNAssignTail() {
        tail = tail.prev;
        tail.next =  null;
    }

    private void addAsHead(Node n) {
        if(head != null) {
            n.next = head;
            head.prev = n;
        }
        head = n;
    }

    void print(){
    System.out.println(head);
    System.out.println(tail);
}
    public static void main(String[] args) {
        LRUCache cache = new LRUCache(3);
        cache.put(1);
        cache.put(2);cache.put(3);
        //cache.print();
        cache.get(2);
        cache.put(4);
        cache.print();
        cache.get(3);
        cache.put(1);
        cache.print();

    }

}
