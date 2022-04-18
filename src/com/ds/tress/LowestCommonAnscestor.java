package src.com.ds.tress;

import java.util.*;

public class LowestCommonAnscestor {
    /*
               1
              / \
           2      3
          / \    / \
        4    5   6     7
       / \      / \
     11    12   8  9
     /
  15
        */
    public static void main(String[] args) {
        Node n = new Node(1,new Node(2,new Node(4,new Node(11,new Node(15),null),new Node(12)),new Node(5)),
                new Node(3,new Node(6,new Node(8),new Node(9)),new Node(7)));
        System.out.println(lca(n,1,15));
    }

    private static Node lca(Node n, Integer leftValue,Integer secondVale) {
        Deque<Node> nodes = new ArrayDeque<>();
        nodes.addFirst(n);
        Map<Node, List<Node>> parentNodes = new HashMap<>();
        Node n1=null,n2 = null;
        while((n1 == null || n2 == null)) {
            Node cuNode = nodes.pop();
            // assign nodes if found
            parentNodes.putIfAbsent(cuNode,new ArrayList<>());
            parentNodes.computeIfPresent(cuNode,(k,v)-> {v.add(cuNode); return  v;});
            if(cuNode.getData().equals(leftValue)) n1= cuNode;
            if(cuNode.getData().equals(secondVale)) n2 = cuNode;

            if(cuNode.getRight() != null) {
                nodes.push(cuNode.getRight());
                parentNodes.compute(cuNode.getRight(),(node,nodeList)->
                        new ArrayList<>(parentNodes.get(cuNode)));

            }
            if(cuNode.getLeft() != null) {
                nodes.push(cuNode.getLeft());
                parentNodes.compute(cuNode.getLeft(),(node,nodeList)->
                        new ArrayList<>(parentNodes.get(cuNode)));
            }
        }
        //
        List<Node> leftLsit = parentNodes.getOrDefault(n1,new ArrayList<>());
        List<Node> rightList = parentNodes.getOrDefault(n2,new ArrayList<>());
        Node lca = null;
        for (int i = 0; i < leftLsit.size() && i < rightList.size() ; i++) {
            if(!leftLsit.get(i).equals(rightList.get(i))){
                break;
            }else {
                lca = leftLsit.get(i);
            }
        }
        return lca;
    }
}
