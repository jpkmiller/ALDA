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
    private List<V> ts; // topologisch sortierte Folge
    private Map<V, Integer> inDegree; //Abbildung der Values mit der Anzahl ihrer noch nicht besuchten Vorgängern
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
        ts = new LinkedList<>();
        inDegree = new TreeMap<>();


        //Anzahl der Kanten zum Zielknoten v in inDegree abbilden
        for (V v : myGraph.getVertexSet()) {
            int ins = myGraph.getInDegree(v);
//            System.out.printf("%d ", ins);
            inDegree.put(v, ins);
            if (ins == 0) q.add(v); //Wenn 0 Eingangskanten -> in Schlange einfügen
        }


        while (!q.isEmpty()) {
            V v = q.remove(); //Entferne V aus der Queue, da es jetzt der erste Eintrag ist, der 0 Vorgänger hat
            ts.add(v); //Füge V zur Liste hinzu
            for (V w : myGraph.getSuccessorVertexSet(v)) { //Iteriere über alle Nachfolger von V und aktualisiere diese, da diese jetzt alle einen Vorgänger weniger haben
                inDegree.put(w, inDegree.get(w) - 1); //aktualisiere alle Nachfolgereinträgen mit der Anzahl der Vorgangknoten - 1 -> Vorgängerknoten V wurde entfernt
                if (inDegree.get(w) == 0) //Wenn der Nachfolger von V 0 geworden ist, wird er zur Queue hinzugefügt
                    q.add(w);
            }
        }

        return ts.size() != myGraph.getNumberOfVertexes() ? null : Collections.unmodifiableList(ts);
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
