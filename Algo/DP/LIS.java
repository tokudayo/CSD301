import static java.lang.Math.max;

/**
 * Problem: Find the longest increasing subsequence (LIS) of a given array of integers.
 * Time complexity: O(n^2)
 * @author To Duc
 * @since 2021-6-2
 */
public class Main {
    // Returns the LIS of an array of integers.
    static int[] LIS(int[] a) {
        int n = a.length;
        // Let f[i] be the length of LIS from a[0] to a[i]
        int f[] = new int[n];
        int lis = 1;
        int maxIndex = 0;
        // DP
        for (int i = 0; i < n; i++) {
            f[i] = 1;
            for (int j = 0; j < i; j++)
                if (a[j] < a[i]) f[i] = max(f[i], f[j] + 1);
        }
        // Find max among f[0]..f[n - 1]
        for (int i = 0; i < n; i++) {
            if (f[i] > lis) {
                lis = f[i];
                maxIndex = i;
            }
        }
        // LIS retrieval
        int seqLength = lis;
        int curNum = a[maxIndex];
        int sol[] = new int[seqLength];
        sol[seqLength - 1] = curNum;
        for (int i = maxIndex - 1; i > -1; i--) {
            if (f[i] == seqLength - 1 && a[i] < curNum) {
                seqLength--;
                curNum = a[i];
                sol[seqLength - 1] = curNum;
            }
        }
        return sol;
    }

    // Driver program
    public static void main(String[] args) {
        // Pseudo input
        int a[] = {5, 6, 4, 1, 2, 5, 2, 6, 11, 3, 4, 4, 9, 10};
        for (int elem : LIS(a)) System.out.print(elem + " ");
    }
}
