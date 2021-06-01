import static java.lang.Math.min;

/**
 * Implementation of integer min heap.
 * The heap is initialized empty with a fixed size.
 * It supports 2 main operation: insert and get the min element.
 * @author To Duc
 * @since 2021-6-1
*/
public class Heap {
    private int[] h; // Heap as array
    private int vacant = 1; // Next available position

    public Heap(int size) {
        h = new int[size];
    }

    // Get the min element in the heap
    public int get () {
        if (vacant == 1) return 0;
        return h[1];
    }

    // Get and remove the min element in the heap
    public int pop () {
        if (vacant == 1) return 0;
        int globalMin = h[1];
        h[1] = h[--vacant];
        topDown(1);
        return globalMin;
    }

    // Insert an element to the heap
    public void insert (int x) {
        h[vacant] = x;
        bottomUp(vacant++);
    }

    // Insert an array of element to the heap
    public void insert(int[] arr) {
        for (int elem : arr) insert(elem);
    }

    // Bottom-up heapify
    private void bottomUp (int node) {
        if (node == 0) return;
        int parent = node/2;
        if (h[parent] > h[node]) {
            swap(parent, node);
            bottomUp(parent);
        }
    }

    // Top-down heapify
    private void topDown (int node) {
        int lChild = node*2;
        int rChild = node*2 + 1;

        // No children
        if (rChild > vacant) return;

        // Left child only
        if (rChild == vacant) {
            if (h[lChild] < h[node]) {
                swap(lChild, node);
                topDown(lChild);
            }
        }

        // Both left and right children exist
        int branchMin = min(h[lChild], h[rChild]);
        branchMin = min(branchMin, h[node]);
        if (branchMin == h[lChild]) {
            swap(lChild, node);
            topDown(lChild);
        }
        else if (branchMin == h[rChild]) {
            swap(rChild, node);
            topDown(rChild);
        }
    }

    // Swap the value of 2 heap nodes
    private void swap(int posA, int posB) {
        int buffer = h[posA];
        h[posA] = h[posB];
        h[posB] = buffer;
    }
}
