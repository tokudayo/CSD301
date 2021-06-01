import java.util.ArrayList;

/**
 * Implementation of level M, degree 2*M+1 B-Tree.
 * The tree is initialized with a value for M.
 * The tree supports 3 operation: get the Node containing a 
 * key, insert a key and delete a key from the tree.
 * @author To Duc
 * @since 2021-6-1
 */
public class BTree {
    class Node {
        Node parent;
        ArrayList<Integer> key;
        ArrayList<Node> son;

        public Node() {
            Node parent = null;
            key = new ArrayList<>();
            son = new ArrayList<>();
        }

        public int sonSize() { return son.size(); }

        public Node getSon(int pos) { return son.get(pos); }

        public int keySize() { return key.size(); }

        public int getKey(int pos) { return key.get(pos); }

        public int findKey(int k) {
            if (key.contains(k)) return key.indexOf(k);
            for (int i = 0; i < key.size(); i++) {
                if (getKey(i) > k) return -i - 1;
            }
            return -key.size() - 1;
        }

        @Override
        public String toString() {
            String info = "Node{" +
                    "parent=" + parent.key +
                    ", key=" + key +
                    ", son=";
            for (Node x : son) {
                info += "\n" + x.key;
            }
            return info + "}";
        }

        private boolean overFlow() {
            return keySize() > maxK;
        }

        private boolean underFlow() {
            return keySize() < minK;
        }

        private boolean almostUnderFlow() { return keySize() == minK;}

        private void addSon(Node newSon) {
            newSon.parent = this;
            int leftKey = newSon.getKey(0);
            int pos = -findKey(leftKey) - 1;
            son.add(pos, newSon);
        }

        private void addKey(int k) {
            if (key.contains(k)) return;
            key.add(-findKey(k) - 1, k);
        }
    }

    public final int level, degree;
    private final int minK, maxK;
    Node root;

    public BTree(int level) {
        this.level = level;
        minK = level;
        maxK = level*2;
        degree = maxK + 1;
        root = null;
    }

    // Returns the node containing x, null if x not in the tree
    public Node search(int x) {
        Node cur = root;
        while (true) {
            int pos = cur.findKey(x);
            if (pos >= 0) return cur;
            if (cur.son.size() == 0) return null;
            cur = cur.getSon(-pos - 1);
        }
    }

    // Insert x into the tree
    public void insert(int x) {
        if (root == null) {
            root = new Node();
            root.addKey(x);
            return;
        }
        Node loc = search(x);
        // No insertion if the x already exists
        if (loc != null) return;
        // Find the appropriate node to insert, then re-balance after insertion
        loc = find(x);
        loc.addKey(x);
        insertionBalance(loc);
    }

    // Delete an existing key x
    public void delete(int x) {
        Node loc = search(x);
        if (loc == null) return;
        int keyPos = loc.findKey(x);
        // If it's not a leaf then replace it with right most child in left subtree
        Node cur = loc;
        if (loc.sonSize() != 0) {
            cur = loc.getSon(keyPos); // left son of key x
            while (cur.sonSize() > 0) cur = cur.getSon(cur.sonSize() - 1); // keep going to the right
            loc.key.set(keyPos, cur.getKey(cur.keySize() - 1)); // swap key
            cur.key.remove(cur.keySize() - 1);
        }
        else loc.key.remove(keyPos);
        // Re-balance after deletion
        deletionBalance(cur);
    }

    // Find the leaf where x is supposed to be inserted to
    private Node find(int x) {
        Node cur = root;
        // Keep going until we hit a leaf
        while (cur.son.size() != 0) {
            int pos = cur.findKey(x);
            cur = cur.getSon(-pos - 1);
        }
        return cur;
    }

    // Bottom-up balancing after insertion
    private void insertionBalance(Node loc) {
        // If no overflow problem exists then it's all good
        if (!loc.overFlow()) return;

        int mid = (maxK + 1)/2;
        int midKey = loc.getKey(mid);
        // Split node loc at mid key
        Node r = new Node();
        // Move right side key to new node
        for (int i = mid + 1; i < loc.keySize(); i++) r.addKey(loc.getKey(i));
        for (int i = mid + 1; i < loc.sonSize(); i++) r.addSon(loc.getSon(i));
        // Remove these in left node
        while (loc.keySize() > mid) loc.key.remove(loc.keySize() - 1);
        while (loc.sonSize() > mid + 1) loc.son.remove(loc.sonSize() - 1);

        // If parent is root
        Node par;
        if (loc.parent == null) {
            par = new Node();
            par.addKey(midKey);
            par.addSon(loc);
            par.addSon(r);
            root = par;
        }
        else {
            loc.parent.addKey(midKey);
            loc.parent.addSon(r);
            insertionBalance(loc.parent);
        }
    }

    // Bottom-up balancing after deletion
    private void deletionBalance(Node loc) {
        // If we have to balance the root with no keys left
        if (loc == root && loc.keySize() == 0) {
            // No son means the last element is being deleted
            if (root.sonSize() == 0) root = null;
            else {
                root = root.getSon(0);
                root.parent = null;
            }
            return;
        }
        // If no underflow problem exists then it's all good
        if (!loc.underFlow()) return;

        Node leftSib = leftSibling(loc), rightSib = rightSibling(loc);
        Node p = loc.parent;
        if (p == null) return;
        int pos = p.son.indexOf(loc);
        // Borrow a key from either sibling if it won't be underflow
        if (leftSib != null && !leftSib.almostUnderFlow()) {
            loc.addKey(p.getKey(pos - 1));
            p.key.set(pos - 1, leftSib.getKey(leftSib.keySize() - 1));
            if (leftSib.sonSize() > 0) {
                loc.addSon(leftSib.getSon(leftSib.sonSize() - 1));
                leftSib.son.remove(leftSib.sonSize() - 1);
            }
            leftSib.key.remove(leftSib.keySize() - 1);
        }
        else if (rightSib != null && !rightSib.almostUnderFlow()) {
            loc.addKey(p.getKey(pos));
            p.key.set(pos, rightSib.getKey(0));
            if (rightSib.sonSize() > 0) {
                loc.addSon(rightSib.getSon(0));
                rightSib.son.remove(0);
            }
            rightSib.key.remove(0);

        }
        // Can't borrow a key from either siblings. The node must merge with 1 of them
        else if (leftSib != null) {
            // Merge with left sibling
            loc.key.add(0, p.getKey(pos - 1));
            loc.key.addAll(0, leftSib.key);
            loc.son.addAll(0, leftSib.son);
            for (Node child : leftSib.son) child.parent = loc;
            p.son.remove(pos - 1);
            p.key.remove(pos - 1);
            deletionBalance(p);
        }
        else if (rightSib != null) {
            // Merge with right sibling
            loc.addKey(p.getKey(pos));
            loc.key.addAll(rightSib.key);
            loc.son.addAll(rightSib.son);
            for (Node child : rightSib.son) child.parent = loc;
            p.son.remove(pos + 1);
            p.key.remove(pos);
            deletionBalance(p);
        }
    }

    // Returns left sibling of a node
    private Node leftSibling(Node loc) {
        if (loc.parent == null) return null;
        Node p = loc.parent;
        int pos = p.son.indexOf(loc);
        if (pos > 0) return p.getSon(pos - 1); else return null;
    }

    // Returns left sibling of a node
    private Node rightSibling(Node loc) {
        if (loc.parent == null) return null;
        Node p = loc.parent;
        int pos = p.son.indexOf(loc);
        if (pos < p.sonSize() - 1) return p.getSon(pos + 1); else return null;
    }
}