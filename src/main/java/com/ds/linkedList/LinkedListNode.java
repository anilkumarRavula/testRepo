package com.ds.linkedList;

import java.util.Objects;
import java.util.Optional;

public class LinkedListNode<T> {
    private T value;
    private LinkedListNode next;

    public LinkedListNode(T value) {
        this.value = value;
    }

    public LinkedListNode(LinkedListNode next) {
        this.next = next;
    }

    public LinkedListNode(T value, LinkedListNode next) {
        this.value = value;
        this.next = next;
    }

    public T val() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public LinkedListNode next() {
        return next;
    }

    public void next(LinkedListNode next) {
        this.next = next;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LinkedListNode<?> linkedListNode = (LinkedListNode<?>) o;
        return Objects.equals(Optional.ofNullable(value).orElse((T)(Object)0), linkedListNode.val());
    }


    @Override
    public String toString() {
        return "" + value + "->" +Optional.ofNullable(next).map(LinkedListNode::val).orElse("null");

    }
}
