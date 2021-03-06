import static java.lang.Math.min;

/**
 * Find the k-th smallest element of an array using heap.
 * Time complexity: O(n.logn).
 * @author To Duc
 * @since 2021-6-1
 */
public class Main {
	// k-th element using Heap
    public static void main(String[] args) {
		int[] a = {3, 1, 4, 2, 6, 7, 10, 0, 0, 0, 5, 9, 8, 8, 2};
		int k = 8;
		Help h = new Heap(a);
		for (int i = 0; i < k - 1; i++) h.get();
		System.out.println(h.get());
    }
}

/**
 * Copy-pasted code from my min heap implementation
 */
class Heap {
	private int[] h; // Heap as array
	private int vacant = 1; // Next available position

	public Heap(int[] arr) {
		h = new int[arr.length*2];
		for (int elem : arr) insert(elem);
	}

	public int get () {
		int globalMin = h[1];
		h[1] = h[--vacant];
		top_down(1);
		return globalMin;
	}

	public void insert (int x) {
		h[vacant] = x;
		bottom_up(vacant++);
	}

	private void bottom_up (int node) {
		if (node == 0) return;
		int parent = node/2;
		if (h[parent] > h[node]) {
			swap(parent, node);
			bottom_up(parent);
		}
	}

	private void top_down (int node) {
		int lChild = node*2;
		int rChild = node*2 + 1;

		// No children
		if (rChild > vacant) return;

		// Left child only
		if (rChild == vacant) {
			if (h[lChild] < h[node]) {
				swap(lChild, node);
				top_down(lChild);
			}
		}

		// Both left and right children exist
		int branchMin = min(h[lChild], h[rChild]);
		branchMin = min(branchMin, h[node]);
		if (branchMin == h[lChild]) {
			swap(lChild, node);
			top_down(lChild);
		}
		else if (branchMin == h[rChild]) {
			swap(rChild, node);
			top_down(rChild);
		}
	}

	private void swap(int posA, int posB) {
		int buffer = h[posA];
		h[posA] = h[posB];
		h[posB] = buffer;
	}
}