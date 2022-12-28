package src.com.ds.tress;

import java.util.Objects;
import java.util.Optional;

public class Node<T> {
    T data;
    private Node<T> left;
    private Node<T> right;

    public Node(T data) {
        this.data = data;
    }

    public Node(T data, Node left, Node right) {
        this.data = data;
        this.left = left;
        this.right = right;
    }

    public T getData() {
        return data;
    }

    public Node<T> getLeft() {
        return left;
    }

    public void setLeft(Node left) {
        this.left = left;
    }

    public Node<T> getRight() {
        return right;
    }

    public void setRight(Node right) {
        this.right = right;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node<T> node = (Node<T>) o;
        return Objects.equals(Optional.ofNullable(data).orElse((T)(Object)0), node.getData())
                && Objects.equals(Optional.ofNullable(left).map(Node::getData).orElse((T)(Object)0), Optional.ofNullable(node.left).map(Node::getData).orElse((T)(Object)0))
                && Objects.equals(Optional.ofNullable(right).map(Node::getData).orElse((T)(Object)0), Optional.ofNullable(node.right).map(Node::getData).orElse((T)(Object)0));
    }


    @Override
    public String toString() {
        return "Node{" +
                "value=" + data +
                '}';
    }
    public void process(){
        System.out.print(data +"->");
    }

}
