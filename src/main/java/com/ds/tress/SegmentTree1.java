package com.ds.tress;

/**
 *
 *                          tree[0] = 36   <- Root (sum of entire array)
 *                         /              \
 *                 tree[1] = 10        tree[2] = 26
 *                /          \         /          \
 *           tree[3] = 3  tree[4] = 7 tree[5] = 15 tree[6] = 11
 *          /       \     /      \    /      \      /      \
 *     tree[7]=1 tree[8]=2 tree[9]=3 tree[10]=4 tree[11]=5 tree[12]=6 tree[13]=7 tree[14]=8
 */
class SegmentTree1 {
    private int[] tree;   // Array to store the segment tree
    private int n;        // Size of the original array

    // Constructor: Initializes the tree with the given array
    public SegmentTree1(int[] arr) {
        n = arr.length;
        tree = new int[4 * n];  // Segment Tree size is 4 * n
        build(arr, 0, 0, n - 1);  // Build the tree
    }

    // Function to build the segment tree
    private void build(int[] arr, int node, int start, int end) {
        if (start == end) {
            // Leaf node: Store the array element in the tree
            tree[node] = arr[start];
        } else {
            int mid = (start + end) / 2;
            int leftChild = 2 * node + 1;
            int rightChild = 2 * node + 2;

            // Recursively build the left and right subtrees
            build(arr, leftChild, start, mid);
            build(arr, rightChild, mid + 1, end);

            // Internal node: The sum of the left and right children
            tree[node] = tree[leftChild] + tree[rightChild];
        }
    }

    // Function to query the sum of elements in the range [l, r]
    public int query(int l, int r) {
        return query(0, 0, n - 1, l, r);  // Start with the root node
    }

    // Recursive function to perform the range sum query
    private int query(int node, int start, int end, int l, int r) {
        if (r < start || end < l) {
            return 0;  // No overlap
        }
        if (l <= start && end <= r) {
            return tree[node];  // Complete overlap
        }

        // Partial overlap: Query the left and right children
        int mid = (start + end) / 2;
        int leftChild = 2 * node + 1;
        int rightChild = 2 * node + 2;

        int leftQuery = query(leftChild, start, mid, l, r);
        int rightQuery = query(rightChild, mid + 1, end, l, r);

        return leftQuery + rightQuery;
    }

    // Function to update an element in the array and adjust the segment tree
    public void update(int index, int value) {
        update(0, 0, n - 1, index, value);
    }

    // Recursive function to update an element and propagate the change up the tree
    private void update(int node, int start, int end, int index, int value) {
        if (start == end) {
            // Leaf node: Update the array element and store the new value in the tree
            tree[node] = value;
        } else {
            int mid = (start + end) / 2;
            int leftChild = 2 * node + 1;
            int rightChild = 2 * node + 2;

            if (index <= mid) {
                // Update the left child
                update(leftChild, start, mid, index, value);
            } else {
                // Update the right child
                update(rightChild, mid + 1, end, index, value);
            }

            // After updating the child, update the current node's value
            tree[node] = tree[leftChild] + tree[rightChild];
        }
    }


    public static void main(String[] args) {
       // int[] arr = {1, 2, 3, 4, 5, 6, 7, 8}; // Example array
        int[] arr = {2, 4, 1, 3, 5};

        SegmentTree1 st = new SegmentTree1(arr);

        // Query sum of elements in the range [2, 5]
        System.out.println("Sum of elements in range [2, 5]: " + st.query(2, 5));

        // Update element at index 3 to 10
        st.update(3, 10);

        // Query again after the update
        System.out.println("Sum of elements in range [2, 5] after update: " + st.query(2, 5));
    }
}