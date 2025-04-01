package com.ds.tress;

class SegmentTree {
    private int[] tree;
    private int n;

    public SegmentTree(int[] data) {
        this.n = data.length;
        this.tree = new int[2 * n];
        build(data);
    }

    private void build(int[] data) {
        for (int i = 0; i < n; i++)
            tree[n + i] = data[i];
        for (int i = n - 1; i > 0; --i)
            tree[i] = tree[i * 2] + tree[i * 2 + 1];
    }

    public void update(int idx, int value) {
        idx += n;
        tree[idx] = value;
        for (int i = idx; i > 1; i >>= 1)
            tree[i >> 1] = tree[i] + tree[i ^ 1];
    }

    public int query(int l, int r) {
        int res = 0;
        l += n;
        r += n;
        while (l < r) {
            if ((l & 1) == 1) res += tree[l++];
            if ((r & 1) == 1) res += tree[--r];
            l >>= 1;
            r >>= 1;
        }
        return res;
    }

    public static void main(String[] args) {
        int[] data = {2, 4, 1, 3, 5};
        SegmentTree1 segTree = new SegmentTree1(data);
        System.out.println(segTree.query(1, 4));  // Output: 8
        segTree.update(2, 6);
        System.out.println(segTree.query(1, 4));  // Output: 13
    }
}

