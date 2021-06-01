/**
 * Implementation of red black tree map.
 * This implementation treats NIL nodes as real nodes.
 * The map supports 3 main operation: get the value 
 * corresponding to a key, insert a key-value pair
 * and delete a key-value pair.
 * @author To Duc
 * @since 2021-6-1
 */
public class RBTree {
    class Node {
        int key;
        int value;
        boolean NIL, RED;
        Node l, r, parent;

        public Node(int k, int v) {
            key = k;
            value = v;
            l = r = parent = null;
            NIL = false;
        }

        public Node() {
            NIL = true;
            RED = false;
            l = r = null;
        }
    }

    Node root;
    private int treeSize;

    public RBTree() {
        root = null;
        treeSize = 0;
    }

    public int size() {
        return treeSize;
    }

    // Insert a <k, v> pair to the tree map
    public void insert(int k, int v) {
        treeSize++;
        Node nn = new Node(k, v);
        Node lNIL = new Node(), rNIL = new Node();
        lNIL.parent = rNIL.parent = nn;
        nn.l = lNIL;
        nn.r = rNIL;
        nn.RED = true;

        // Initialize root if tree is empty
        if (root == null) {
            root = nn;
            nn.parent = null;
            nn.RED = false;
            return;
        }

        Node cur = root;
        boolean insertToLeft;

        // Traverse the tree to find where to insert <k, v>
        while (true) {
            if (cur.key >= k) {
                if (cur.l.NIL) {
                    insertToLeft = true;
                    break;
                } else cur = cur.l;
            }
            else {
                if (cur.r.NIL) {
                    insertToLeft = false;
                    break;
                } else cur = cur.r;
            }
        }
        if (insertToLeft) cur.l = nn; else cur.r = nn;
        nn.parent = cur;
        insertFix(nn);
    }

    // Get value of a key
    public int get(int k) {
        Node cur = root;
        while (!cur.NIL) {
            if (cur.key == k) return cur.value;
            else if (cur.key > k) cur = cur.l;
            else cur = cur.r;
        }
        return 0;
    }

    // Delete a key
    public void delete(int k) {
        Node x = find(k);
        if (x != null) delete(x);
    }

    // Return the node that contains key k
    private Node find(int k) {
        Node cur = root;
        while (!cur.NIL) {
            if (cur.key == k) return cur;
            else if (cur.key > k) cur = cur.l;
            else cur = cur.r;
        }
        return null;
    }

    // BST right rotation
    private void rotateRight(Node x) {
        Node p = x.parent;
        Node c = x.l;
        Node cr = c.r;

        if (p != null) {
            c.parent = p;
            if (isLeftChild(x)) p.l = c;
            else p.r = c;
        }
        else {
            c.parent = null;
            root = c;
        }

        x.parent = c;
        x.l = cr;
        cr.parent = x;
        c.r = x;
    }

    // BST left rotation
    private void rotateLeft(Node x) {
        Node p = x.parent;
        Node c = x.r;
        Node cl = c.l;

        if (p != null) {
            c.parent = p;
            if (p.l == x) p.l = c;
            else p.r = c;
        }
        else {
            c.parent = null;
            root = c;
        }

        x.parent = c;
        x.r = cl;
        cl.parent = x;
        c.l = x;
    }

    // Return true if node x is the left child of its parent
    private boolean isLeftChild(Node x) {
        return x.parent != null && x.parent.l == x;
    }

    // Fix the tree after insertion
    private void insertFix(Node x) {
        if (x == root) {
            return;
        }
        // If parent is black then OK
        if (!x.parent.RED) return;

        // Re-coloring and rotation
        Node p = x.parent;
        Node gp = p.parent;
        if (gp == null) return;

        // Proceed only if x has grandpa & uncle
        Node u;
        if (isLeftChild(p)) u = gp.r;
        else u = gp.l;

        if (u.RED) {
            p.RED = u.RED = false;
            gp.RED = true;
            insertFix(gp);
        }
        else {
            if (isLeftChild(p)) {
                if (!isLeftChild(x)) {
                    rotateLeft(p);
                    p = x;
                }
                rotateRight(gp);
                boolean temp = gp.RED;
                gp.RED = p.RED;
                p.RED = temp;
            }
            else {
                if (isLeftChild(x)) {
                    rotateRight(p);
                    p = x;
                }
                rotateLeft(gp);
                boolean temp = gp.RED;
                gp.RED = p.RED;
                p.RED = temp;
            }
        }
    }

    // Swap the content of 2 nodes x, y
    private void swap(Node x, Node y) {
        int temp = x.key;
        x.key = y.key;
        y.key = temp;
        temp = x.value;
        x.value = y.value;
        y.value = temp;
        boolean tempState = x.NIL;
        x.NIL = y.NIL;
        y.NIL = tempState;
    }

    // Fix the "double-black" problem after deletion
    private void doubleBlackFix(Node x) {
        if (x == root) return;
        Node p = x.parent;
        Node sib;
        if (isLeftChild(x)) sib = x.parent.r; else sib = x.parent.l;
        if (sib.NIL) return;

        // If sibling is black
        if (!sib.RED) {
            // If either child is red
            if (sib.l.RED || sib.r.RED) {
                if (!isLeftChild(sib) && sib.r.RED) {
                    rotateLeft(p);
                    sib.r.RED = false;
                } else if (!isLeftChild(sib) && sib.l.RED) {
                    rotateRight(sib);
                    rotateLeft(p);
                    sib.l.RED = false;
                }
                else if (isLeftChild(sib) && sib.l.RED) {
                    rotateRight(p);
                    sib.l.RED = false;
                }
                else if (isLeftChild(sib) && sib.r.RED) {
                    rotateLeft(sib);
                    rotateRight(p);
                    sib.r.RED = false;
                }
            }
            // If no child is red
            else {
                sib.RED = true;
                if (!p.RED) doubleBlackFix(p);
                else p.RED = false;
            }
        }
        // If sibling is red
        else {
            if (!isLeftChild(sib)) {
                rotateLeft(p);
            }
            else {
                rotateRight(p);
            }
            p.RED = true;
            sib.RED = false;
            doubleBlackFix(x);
        }
    }

    // Fix the tree after deletion
    private void deleteFix(Node x) {
        // x should have 1 or no real (non-NIL) children
        // Denote c as the child to replace x
        Node c = x.l;
        if (!x.r.NIL) c = x.r;

        boolean xRED = x.RED, cRED = c.RED;
        swap(x, c);
        c.NIL = true;
        c.RED = false;
        // If node x is now NIL then delete redundant NIL children
        if (x.NIL) {
            x.l = null;
            x.r = null;
        }
        // If either node or its child is red
        if (xRED || cRED) {
            x.RED = false;
        }
        // If x and c are black
        else {
            doubleBlackFix(x);
        }
    }

    // Delete a node
    private void delete(Node x) {
        treeSize--;
        if (treeSize == 0) {
            root = null;
            return;
        }
        // 2 children case
        if (!x.l.NIL && !x.r.NIL) {
            Node cur = x.r;
            while (!cur.l.NIL) cur = cur.l;
            swap(x, cur);
            deleteFix(cur);
        }
        else deleteFix(x);
    }
}
