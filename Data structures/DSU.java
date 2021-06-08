/**
 * Disjoint-set implementation where initially
 * each element has its own set. Operations run
 * at constant time O(1).
 * @author To Duc
 * @since 2021-6-8
 */
public class DSU {
    int u[];

    // Each element initially belongs to its own set.
    public DSU(int[] a) {
        u = new int[a.length];
        for (int i = 0; i < a.length; i++) u[i] = i;
    }

    // Gets ancestor of set containing a.
    public int find(int a) {
        if (u[a] == a) return a;
        else return u[a] = find(u[a]);
    }

    // Joins 2 sets containing a and b.
    public void join(int a, int b) {
        u[find(a)] = find(b);
    }
}
