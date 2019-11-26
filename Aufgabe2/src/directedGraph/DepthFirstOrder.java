// O. Bittel;
// 22.02.2017
package directedGraph;

import java.util.*;

/**
 * Klasse für Tiefensuche.
 *
 * @param <V> Knotentyp.
 * @author Oliver Bittel
 * @since 22.02.2017
 */
public class DepthFirstOrder<V> {

    private final List<V> preOrder = new LinkedList<>();
    private final List<V> postOrder = new LinkedList<>();
    private final DirectedGraph<V> myGraph;
    private int numberOfDFTrees = 0;

    /**
     * Führt eine Tiefensuche für g durch.
     *
     * @param g gerichteter Graph.
     */
    public DepthFirstOrder(DirectedGraph<V> g) {
        myGraph = g;
        visitPrePost();
    }

    public static void main(String[] args) {
        DirectedGraph<Integer> g = new AdjacencyListDirectedGraph<>();
        g.addEdge(1, 2);
        g.addEdge(2, 5);
        g.addEdge(5, 1);
        g.addEdge(2, 6);
        g.addEdge(3, 7);
        g.addEdge(4, 3);
        g.addEdge(4, 6);
        g.addEdge(7, 3);
        g.addEdge(7, 4);

        DepthFirstOrder<Integer> dfs = new DepthFirstOrder<>(g);
        System.out.println(dfs.numberOfDFTrees());    // 2
        System.out.println(dfs.preOrder());        // [1, 2, 5, 6, 3, 7, 4]
        System.out.println(dfs.postOrder());        // [5, 6, 2, 1, 4, 7, 3]

    }

    /**
     * Liefert eine nicht modifizierbare Liste (unmodifiable view) mit einer
     * Pre-Order-Reihenfolge zurück.
     *
     * @return Pre-Order-Reihenfolge der Tiefensuche.
     */
    public List<V> preOrder() {
        return Collections.unmodifiableList(preOrder);
    }

    /**
     * Liefert eine nicht modifizierbare Liste (unmodifiable view) mit einer
     * Post-Order-Reihenfolge zurück.
     *
     * @return Post-Order-Reihenfolge der Tiefensuche.
     */
    public List<V> postOrder() {
        return Collections.unmodifiableList(postOrder);
    }

    /**
     * Diese Methode iteriert über das Set, das vom Graphen zurückgegeben wird
     * und überprüft immer, ob der Knoten bereits besucht wurde. Das ist dann praktisch,
     * wenn der Graph aus zwei Zusammenhangskomponenten besteht. Dann ist es nämlich schwierig,
     * durch einfache rekursion auf den anderen Graphen zu gelangen
     */
    private void visitPrePost() {
        Set<V> besucht = new TreeSet<>();
        int noTree = 0;
        for (V vertexes : myGraph.getVertexSet())
            if (!besucht.contains(vertexes)) {
                besucht.addAll(visitPrePost(vertexes, besucht));
                noTree++;
            }
        numberOfDFTrees = noTree;
    }

    /**
     * Diese Methode besucht den ersten Knoten und fügt ihn zu einem Set besucht hinzu. Dieses wird benötigt,
     * um zu entscheiden, ob der Knoten bereits besucht wurde.
     * Die Iteration findet über die Nachfolgerknoten von v statt. Durch Rekursion wird damit dann sichergestellt,
     * dass alle diese besucht werden. Durch das Set kann auch sichergestellt werden, dass die Knoten nicht noch einmal besucht werden.
     */
    private Set<V> visitPrePost(V v, Set<V> besucht) {
        besucht.add(v);

        preOrder.add(v);

        for (V w : myGraph.getSuccessorVertexSet(v)) {
            if (!besucht.contains(w))
                visitPrePost(w, besucht);
        }
        postOrder.add(v);
        return besucht;
    }


    /**
     * @return Anzahl der Bäume des Tiefensuchwalds.
     */
    public int numberOfDFTrees() {
        return numberOfDFTrees;
    }
}
