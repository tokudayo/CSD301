import java.util.Random;

/**
 * Find the k-th smallest element of an array using modified quick sort.
 * Time complexity: O(n.logn). It is usually really fast though.
 * @author To Duc
 * @since 2021-6-1
 */
public class Main {
	// Find the pos-th element in ascending order in interval [l;r]
	public static int quickFind(int[] arr, int l, int r, int pos) {
		// Choose random pivot
		Random gen = new Random();
		int pivot = gen.nextInt(r - l + 1) + l;

		// Put the pivot element to the end
		swap(arr, r, pivot);

		// Partitioning
		int i = l, j = r - 1;
		while(i <= j) {
			if(arr[i] < arr[r]) i++;
			else swap(arr, i, j--);
		}

		// Put the pivot back to correct position
		swap(arr, i, r);

		if (i - l + 1 == pos) return arr[i];
		
		if (i - l + 1 > pos) return quickFind(arr, l, i - 1, pos);
		else return quickFind(arr, i + 1, r, pos - (i - l + 1));
	}

	// Swap 2 elements
	public static void swap(int arr[], int i, int j) {
		int temp = arr[i];
		arr[i] = arr[j];
		arr[j] = temp;
	}

	// Driver program
	public static void main(String []args) {
		int[] arr = {10, 5, 6, 2, 4, 3, 1, 7, 9, 8, 15, 16, 12, 14, 13, 11, 17, 19, 18, 20};
		// Find the 4-th smallest element in arr
		System.out.println(quickFind(arr, 0, arr.length - 1, 4));
	}
}
