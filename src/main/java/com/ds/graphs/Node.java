package com.ds.graphs;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Node {
        public int val;
        public List<Node> neighbors;
        public Node() {
            val = 0;
            neighbors = new ArrayList<Node>();
        }
        public Node(int _val) {
            val = _val;
            neighbors = new ArrayList<Node>();
        }
        public Node(int _val, List<Node> _neighbors) {
            val = _val;
            neighbors = _neighbors;
        }
        public int getVal() {return  val;};

    @Override
    public String toString() {
        return
                 val +
                "-->" + neighbors.stream().map(Node::getVal).collect(Collectors.toSet()) ;
    }
}
