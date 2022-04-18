package src.com.ds.tress;

public class PrintNodesAtKDistance {
    public static void main(String[] args) {
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

        Node n = new Node(1,new Node(2,new Node(4,new Node(11,new Node(15),null),new Node(12)),new Node(5)),
                new Node(3,new Node(6,new Node(8),new Node(9)),new Node(7)));
        fromRootDistance(n,4);
        System.out.println();
        fromGivenNodeDistance(n,n.getRight(),2,3);
    }

    public static void fromRootDistance(Node n, int distance) {
        if(n == null) return;
        if(distance == 0){
            n.process();
            return;
        }
        fromRootDistance(n.getLeft(),distance-1);
        fromRootDistance(n.getRight(),distance-1);
    }
    public static void fromGivenNodeDistance(Node n, Node target, int targetDistance, int distance) {

        if(n == null) return;
        if(distance == targetDistance){
            n.process();
            return;
        }
        if(n.equals(target)) distance = 0;
        fromGivenNodeDistance(n.getLeft(),target,targetDistance,distance+1);
        fromGivenNodeDistance(n.getRight(),target,targetDistance,distance+1);
    }


}
