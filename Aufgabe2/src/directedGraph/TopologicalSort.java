// O. Bittel;
// 22.02.2017

package directedGraph;

import java.util.*;

/**
 * Klasse zur Erstellung einer topologischen Sortierung.
 *
 * @param <V> Knotentyp.
 * @author Oliver Bittel
 * @since 22.02.2017
 */
public class TopologicalSort<V> {
    private List<V> ts = new LinkedList<>(); // topologisch sortierte Folge
    private Map<V, Integer> inDegree = new TreeMap<>(); //Abbildung der Values mit der Anzahl ihrer noch nicht besuchten Vorgängern
    private DirectedGraph<V> myGraph;

    /**
     * Führt eine topologische Sortierung für g durch.
     *
     * @param g gerichteter Graph.
     */
    public TopologicalSort(DirectedGraph<V> g) {
        myGraph = g;
    }

    /**
     * Liefert eine nicht modifizierbare Liste (unmodifiable view) zurück,
     * die topologisch sortiert ist.
     *
     * @return topologisch sortierte Liste
     */
    public List<V> topologicalSortedList() {
        Queue<V> q = new LinkedList<>();

        for (V v : myGraph.getVertexSet()) {
            int ins = myGraph.getInDegree(v);
            System.out.printf("%d ", ins);
            inDegree.put(v, ins); //Anzahl der Kanten zum Zielknoten v in inDegree abbilden
            if (ins == 0) q.add(v);
        }


        while (!q.isEmpty()) {
            V v = q.remove();
            ts.add(v);
            for (V w : myGraph.getSuccessorVertexSet(v)) {
                inDegree.put(v, inDegree.get(w) - 1);
                if (inDegree.get(w) == 0)
                    q.add(w);
            }
        }

        if (ts.size() != myGraph.getNumberOfVertexes())
            return null;

        return Collections.unmodifiableList(ts);
    }


    public static void main(String[] args) {
        DirectedGraph<Integer> g = new AdjacencyListDirectedGraph<>();
        g.addEdge(1, 2);
        g.addEdge(2, 3);
        g.addEdge(3, 4);
        g.addEdge(3, 5);
        g.addEdge(4, 6);
        g.addEdge(5, 6);
        g.addEdge(6, 7);

        TopologicalSort<Integer> ts = new TopologicalSort<>(g);

        if (ts.topologicalSortedList() != null) {
            System.out.println(ts.topologicalSortedList()); // [1, 2, 3, 4, 5, 6, 7]
        }
    }
}
