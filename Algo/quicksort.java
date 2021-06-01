import java.util.Random;

/**
 * Implementation of randomized quick sort algorithm.
 * Time complexity (average): O(n.logn)
 * @author To Duc
 * @since 2021-6-1
 */
public class Main{
    // Sort array arr in [l;r] interval using quick sort.
    public static void quickSort(int[] arr, int l, int r) {
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

        if (l < i - 1) quickSort(arr, l, i - 1);
        if (i + 1 < r) quickSort(arr, i + 1, r);
    }

    // Swap 2 elements
    public static void swap(int arr[], int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }
    

    // Driver program
    static int N = 20;

    public static void main(String []args) {
        Random gen = new Random();
        int[] arr = new int[N];
        for (int i = 0; i < N; i++) arr[i] = gen.nextInt(N*10);
        
        System.out.println("Before sorting: ");
        printArr(arr);
        
        quickSort(arr, 0, N - 1);
        
        System.out.println("After sorting: ");
        printArr(arr);
    }

    // Auxiliary function
    public static void printArr(int[] arr) {
        for (int i = 0; i < arr.length; i++) System.out.print(arr[i] + " ");
        System.out.println();
    }
}
