package TelNetApplication;

import java.util.Arrays;
import java.util.Iterator;

/**
 * @author Josef
 * @since 23.12.2019
 */
public class UnionFind {

    private int size;
    private Integer[] tree;

    public UnionFind(int n) {
        tree = new Integer[n];
        Arrays.fill(tree, -1);
        size = n;
    }

    public static void main(String[] args) {
        UnionFind unionFind = new UnionFind(10);

        //kontrolliere größe
        System.out.printf("%d = %d\n", unionFind.size(), 10);
        System.out.println();

        //kontrolliere ob richtig gefüllt
        System.out.println(unionFind);
        System.out.println();

        //kontrolliere union
        unionFind.union(1, 3);
        System.out.println(unionFind);

        unionFind.union(5, 7);
        unionFind.union(8, 6);
        System.out.println(unionFind);

        //kontrolle ob Repräsentanten richtig
        unionFind.union(1, 5);
        unionFind.union(1, 8);
        System.out.println(unionFind);
        System.out.println();
        System.out.printf("Repräsentant 1 = %d\n", unionFind.find(8));
        System.out.printf("Repräsentant 1 = %d\n", unionFind.find(6));
        System.out.println();

        unionFind.union(0, 4);
        unionFind.union(9, 0);
        System.out.printf("Repräsentant 0 = %d\n", unionFind.find(9));
        System.out.println();

        unionFind.union(2, 0);
        System.out.printf("Repräsentant 0 = %d\n", unionFind.find(2));
        System.out.println(unionFind);
        System.out.println();

        unionFind.union(0, 1);
        System.out.printf("Repräsentant 1 = %d\n", unionFind.find(0));
        System.out.println(unionFind);
        System.out.println();
    }

    /**
     * Liefert den Repräsentanten der Menge zurück, zu der e gehört.
     *
     * @param e ist ein Wert der Menge
     * @return Präsentant der Menge, zu der e gehört
     */
    public int find(int e) throws IllegalArgumentException {
        while (tree[e] >= 0)
            e = tree[e];
        return e;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        Iterator<String> it = StringIterator();
        while (it.hasNext())
            stringBuilder.append(it.next());
        return stringBuilder.toString();
    }

    /**
     * Liefert die Anzahl der Mengen in der Partitionierung zurück.
     *
     * @return Anzahl der Mengen
     */
    public int size() {
        return size;
    }

    /**
     * Vereinigt die beiden Menge s1 und s2. s1 und s2 müssen Repräsentanten der jeweiligen Menge sein.
     * Die Vereinigung wird nur durchgeführt, falls s1 und s2 unterschiedlich sind.
     * Es wird union-by-height durchgeführt.
     *
     * @param s1 Element, das eine Menge repräsentiert
     * @param s2 Element, das eine Menge repräsentiert
     */
    public void union(int s1, int s2) throws IllegalArgumentException {
        if (s1 == s2) throw new IllegalArgumentException();
        if (tree[s1] >= 0 || tree[s2] >= 0) return;

        if (-tree[s1] < -tree[s2])
            tree[s1] = s2;
        else {
            if (-tree[s1] == -tree[s2])
                tree[s1]--;
            tree[s2] = s1;
        }
        size--;
    }

    public Iterator<Integer> iterator() {
        return new Iterator<>() {
            int i = 0;

            @Override
            public boolean hasNext() {
                return i < size;
            }

            @Override
            public Integer next() {
                System.out.printf("[%d] = ", i);
                return tree[i++];
            }
        };
    }

    public Iterator<String> StringIterator() {
        return new Iterator<>() {
            int i = 0;

            @Override
            public boolean hasNext() {
                return i < size;
            }

            @Override
            public String next() {
                String color = tree[i] <= 0 ? "\u001B[3" + -tree[i] + "m" : "";
                return color + "[" + i + "] = " + tree[i++] + "\u001B[0m" + " \t";
            }
        };
    }

}
