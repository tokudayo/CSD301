import static java.lang.Math.max;

/**
 * Problem: Find the longest common substring (LCS) of 2 strings.
 * Time complexity: O(n^2)
 * @author To Duc
 * @since 2021-6-2
 */
public class Main {
    // Returns the LCS between 2 strings
    static String LCS(String a, String b) {
        // Let f[i][j] be the length of LCS between a[0..i] and b[0..j]
        int f[][] = new int[a.length()][b.length()];
        // Atom cases
        f[0][0] = 0;
        if (a.charAt(0) == b.charAt(0)) f[0][0]++;
        for (int j = 1; j < b.length(); j++) {
            if (a.charAt(0) == b.charAt(j)) f[0][j] = 1;
            else f[0][j] = f[0][j - 1];
        }
        for (int i = 1; i < a.length(); i++) {
            if (a.charAt(i) == b.charAt(0)) f[i][0] = 1;
            else f[i][0] = f[i - 1][0];
        }
        // DP
        for (int i = 1; i < a.length(); i++) {
            for (int j = 1; j < b.length(); j++) {
                f[i][j] = max(f[i][j - 1], f[i - 1][j]);
                if (a.charAt(i) == b.charAt(j)) f[i][j] = max(f[i][j], f[i - 1][j - 1] + 1);
            }
        }
        // LCS retrieval
        int i = a.length() - 1;
        int j = b.length() - 1;
        int lcsLen = f[i][j];
        String lcs = "";
        while (lcsLen > 0) {
            if (a.charAt(i) == b.charAt(j)) {
                lcs = a.charAt(i) + lcs;
                lcsLen--;
                i--;
                j--;
            }
            else {
                // Prevent index error
                if (i == 0) {
                    lcs = a.charAt(0) + lcs;
                    break;
                }
                if (j == 0) {
                    lcs = b.charAt(0) + lcs;
                    break;
                }
                // Normal backtracking
                if (f[i - 1][j] > f[i][j - 1]) i--;
                else j--;
            }
        }
        return lcs;
    }

    // Driver program
    public static void main(String[] args) {
        // Pseudo input
        String a = "quickdraw12a";
        String b = "qcdr2rrrckra";
        System.out.println(LCS(a, b));
    }
}
