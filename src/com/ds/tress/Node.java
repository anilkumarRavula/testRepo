package src.com.ds.tress;

import java.util.Objects;
import java.util.Optional;

public class Node<T> {
    private T value;
    private Node left;
    private Node right;

    public Node(T value) {
        this.value = value;
    }

    public Node(T value, Node left, Node right) {
        this.value = value;
        this.left = left;
        this.right = right;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public Node getLeft() {
        return left;
    }

    public void setLeft(Node left) {
        this.left = left;
    }

    public Node getRight() {
        return right;
    }

    public void setRight(Node right) {
        this.right = right;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node<?> node = (Node<?>) o;
        return Objects.equals(Optional.ofNullable(value).orElse((T)(Object)0), node.getValue())
                && Objects.equals(Optional.ofNullable(left).map(Node::getValue).orElse(0), Optional.ofNullable(node.left).map(Node::getValue).orElse(0))
                && Objects.equals(Optional.ofNullable(right).map(Node::getValue).orElse(0), Optional.ofNullable(node.right).map(Node::getValue).orElse(0));
    }


    @Override
    public String toString() {
        return "Node{" +
                "value=" + value +
                '}';
    }
    public void process(){
        System.out.print(value+"->");
    }

}
