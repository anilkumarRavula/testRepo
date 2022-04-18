package src.com.ds.tress;

import java.util.*;
import java.util.function.BiPredicate;

/**
 * For the purposes of this challenge, we define a binary search tree to be a binary tree with the following properties:
 *
 * The  value of every node in a node's left subtree is less than the data value of that node.
 * The  value of every node in a node's right subtree is greater than the data value of that node.
 * The  value of every node is distinct.
 * For example, the image on the left below is a valid BST. The one on the right fails on several counts:
 * - All of the numbers on the right branch from the root are not larger than the root.
 * - All of the numbers on the right branch from node 5 are not larger than 5.
 * - All of the numbers on the left branch from node 5 are not smaller than 5.
 * - The data value 1 is repeated.
 */
public class CheckTreeIsBST {

    static BiPredicate<Node<Integer>,Node<Integer>> isValidRightNode = (current, right)-> right.getData() >= current.getData();
    static BiPredicate<Node<Integer> ,Node<Integer>> isRightGratherThanCurrentParent = (parent,right)-> right.getData() >= parent.getData();
    static BiPredicate<Node<Integer> ,Node<Integer>> isLeftGratherThanCurrentParent = (parent,left)-> parent == left || left.getData() <= parent.getData();

    public static void main(String[] args) {
        Node n = new Node(4,new Node(2,new Node(1),new Node(3)),
                new Node(6,new Node(5,new Node(4),null),new Node(7)));
        System.out.println(checkBST(n));

    }
    private static boolean isValidRightNode(Node<Integer> cuurent,Node<Integer> right,Map<Integer, Node<Integer>> parentNodes ) {
           return isValidRightNode.test(cuurent,right) && isRightGratherThanCurrentParent.test(parentNodes.get(cuurent.getData()),cuurent) ?
                    isRightGratherThanCurrentParent.test(parentNodes.get(cuurent.getData()),right) :
                    isRightGratherThanCurrentParent.negate().test(parentNodes.get(cuurent.getData()),right);


    }



    private static boolean isValidLeftNode(Node<Integer> cuurent,Node<Integer> left,Map<Integer, Node<Integer>> parentNodes ) {
        return isValidRightNode.negate().test(cuurent,left) &&
                isLeftGratherThanCurrentParent.test(parentNodes.get(cuurent.getData()),cuurent) ?
                isRightGratherThanCurrentParent.negate().test(parentNodes.get(cuurent.getData()),left) :
                isRightGratherThanCurrentParent.test(parentNodes.get(cuurent.getData()),left);
    }
    private static boolean checkBST(Node<Integer> root) {
        Deque<Node> nodes = new ArrayDeque<>();
        nodes.addFirst(root);
        Map<Integer, Node<Integer>> parentNodes = new HashMap<>();
        while (true) {
            Integer nodeCount = nodes.size();
            if (nodeCount == 0) return true;
            while (nodeCount > 0) {
                Node<Integer> cuNode = nodes.pop();
                System.out.println(cuNode.getData());
                parentNodes.putIfAbsent(cuNode.getData(), cuNode);
                if (cuNode.getRight() != null ) {
                    if (!isValidRightNode(cuNode, cuNode.getRight(), parentNodes))  return false;
                    nodes.push(cuNode.getRight());
                    parentNodes.put(cuNode.getRight().getData(), cuNode);

                }
                if (cuNode.getLeft() != null ) {
                    if (!isValidLeftNode(cuNode, cuNode.getLeft(), parentNodes))  return false;
                    nodes.push(cuNode.getLeft());
                    parentNodes.put(cuNode.getLeft().getData(), cuNode);
                }

                nodeCount--;
            }

        }

    }
}
