// O. Bittel;
// 19.03.2018

package graph;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * Implementierung von DirectedGraph
 * mit einer doppelten TreeMap für die Nachfolgerknoten
 * und einer einer doppelten TreeMap für die Vorgängerknoten.
 * <p>
 * Beachte: V muss vom Typ Comparable&lt;V&gt; sein.
 * <p>
 * Entspicht einer Adjazenzlisten-Implementierung
 * mit schnellem Zugriff auf die Knoten.
 *
 * @param <V> Knotentyp.
 * @author Oliver Bittel
 * @since 19.03.2018
 */
public class AdjacencyListDirectedGraph<V extends Comparable<? super V>> implements DirectedGraph<V> {
    // doppelte Map für die Nachfolgerknoten:
    private final Map<V, Map<V, Double>> succ = new TreeMap<>();

    // doppelte Map für die Vorgängerknoten:
    private final Map<V, Map<V, Double>> pred = new TreeMap<>();

    private int numberEdge = 0;

    /**
     * Fügt neuen Knoten zum Graph dazu.
     *
     * @param v Knoten
     * @return true, falls Knoten noch nicht vorhanden war.
     */
    @Override
    public boolean addVertex(V v) {
        if (succ.containsKey(v) && pred.containsKey(v))
            return false;
        succ.put(v, new TreeMap<>());
        pred.put(v, new TreeMap<>());
        return true;
    }

    /**
     * Fügt neue Kante mit Gewicht weight zum Graph dazu.
     * Falls einer der beiden Knoten noch nicht im Graphen vorhanden ist,
     * dann wird er dazugefügt.
     * Falls die Kante schon vorhanden ist, dann wird das Gewicht
     * mit weight überschrieben.
     *
     * @param v      Startknoten
     * @param w      Zielknoten
     * @param weight Gewicht
     * @return true, falls Kante noch nicht vorhanden war.
     */
    @Override
    public boolean addEdge(V v, V w, double weight) {
        this.addVertex(v);
        this.addVertex(w);

        if (succ.get(v).containsKey(w)) {
            succ.get(v).put(w, weight);
            pred.get(w).put(v, weight);
            return false;
        } else {
            succ.get(v).put(w, weight);
            pred.get(w).put(v, weight);
        }
        numberEdge++;
        return true;
    }

    /**
     * Fügt neue Kante (mit Gewicht 1) zum Graph dazu.
     * Falls einer der beiden Knoten noch nicht im Graphen vorhanden ist,
     * dann wird er dazugefügt.
     * Falls die Kante schon vorhanden ist, dann wird das Gewicht
     * mit 1 überschrieben.
     *
     * @param v Startknoten
     * @param w Zielknoten
     * @return true, falls Kante noch nicht vorhanden war.
     */
    @Override
    public boolean addEdge(V v, V w) {
        return addEdge(v, w, 1);
    }

    /**
     * Prüft ob Knoten v im Graph vorhanden ist.
     *
     * @param v Knoten
     * @return true, falls Knoten vorhanden ist.
     */
    @Override
    public boolean containsVertex(V v) {
        return succ.containsKey(v);
    }

    /**
     * Prüft ob Kante im Graph vorhanden ist.
     *
     * @param v Startknoten
     * @param w Endknoten
     * @return true, falls Kante vorhanden ist.
     */
    @Override
    public boolean containsEdge(V v, V w) {
        return succ.get(v) != null && succ.get(v).get(w) != null;
    }

    /**
     * Liefert Gewicht der Kante zurück.
     *
     * @param v Startknoten
     * @param w Endknoten
     * @return Gewicht der Kante.
     * @throws IllegalArgumentException falls die Kante nicht existiert.
     */
    @Override
    public double getWeight(V v, V w) {
        return succ.get(v).get(w);
    }


    /**
     * Liefert Eingangsgrad des Knotens v zurück.
     * Das ist die Anzahl der Kanten mit Zielknoten v.
     *
     * @param v Knoten
     * @return Knoteneingangsgrad
     * @throws IllegalArgumentException falls Knoten v
     *                                  nicht im Graph vorhanden ist.
     */
    @Override
    public int getInDegree(V v) {
        return pred.get(v).size();
    }

    /**
     * Liefert Ausgangsgrad des Knotens v zurück.
     * Das ist die Anzahl der Kanten mit Quellknoten v.
     *
     * @param v Knoten
     * @return Knotenausgangsgrad
     * @throws IllegalArgumentException falls Knoten v
     *                                  nicht im Graph vorhanden ist.
     */
    @Override
    public int getOutDegree(V v) {
        return succ.get(v).size();
    }

    /**
     * Liefert eine nicht modifizierbare Sicht (unmodifiable view)
     * auf die Menge aller Knoten im Graph zurück.
     *
     * @return Knotenmenge
     */
    @Override
    public Set<V> getVertexSet() {
        return Collections.unmodifiableSet(succ.keySet()); // nicht modifizierbare Sicht
    }

    /**
     * Liefert eine nicht modifizierbare Sicht (unmodifiable view) auf
     * die Menge aller Vorgängerknoten von v zurück.
     * Das sind alle die Knoten, von denen eine Kante zu v führt.
     *
     * @param v Knoten
     * @return Knotenmenge
     * @throws IllegalArgumentException falls Knoten v
     *                                  nicht im Graph vorhanden ist.
     */
    @Override
    public Set<V> getPredecessorVertexSet(V v) {
        return Collections.unmodifiableSet(pred.get(v).keySet());
    }

    @Override
    public Set<V> getSuccessorVertexSet(V v) {
        return Collections.unmodifiableSet(succ.get(v).keySet());
    }

    /**
     * Liefert Anzahl der Knoten im Graph zurück.
     *
     * @return Knotenzahl.
     */
    @Override
    public int getNumberOfVertexes() {
        return succ.size();
    }

    /**
     * Liefert Anzahl der Kanten im Graph zurück.
     *
     * @return Kantenzahl.
     */
    @Override
    public int getNumberOfEdges() {
//        int sum = 0;
//        for (Map.Entry<V, Map<V, Double>> s : succ.entrySet())
//            sum += s.getValue().size();
        return numberEdge;
    }

    /**
     * Erzeugt einen invertierten Graphen,
     * indem jede Kante dieses Graphens in umgekehrter Richtung abgespeichert wird.
     *
     * @return invertierter Graph
     */
    @Override
    public DirectedGraph<V> invert() {
        DirectedGraph<V> invGraph = new AdjacencyListDirectedGraph<>();
        for (Map.Entry<V, Map<V, Double>> e : succ.entrySet())
            e.getValue().forEach((key, value) -> invGraph.addEdge(key, e.getKey(), value));
        return invGraph;
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        for (var e : succ.entrySet())
            e.getValue().forEach((key, value) -> str.append(e.getKey()).append(" --> ").append(key).append(" weight = ").append(value).append("\n"));
        return str.toString();
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
        g.addEdge(7, 4);


        System.out.println(g.getNumberOfVertexes());    // 7
        System.out.println(g.getNumberOfEdges());        // 8
        System.out.println(g.getVertexSet());    // 1, 2, ..., 7
        System.out.println(g);
        // 1 --> 2 weight = 1.0
        // 2 --> 5 weight = 1.0
        // 2 --> 6 weight = 1.0
        // 3 --> 7 weight = 1.0
        // ...

        System.out.println(g.getOutDegree(2));                // 2
        System.out.println(g.getSuccessorVertexSet(2));    // 5, 6
        System.out.println(g.getInDegree(6));                // 2
        System.out.println(g.getPredecessorVertexSet(6));    // 2, 4

        System.out.println(g.containsEdge(1, 2));    // true
        System.out.println(g.containsEdge(2, 1));    // false
        System.out.println(g.getWeight(1, 2));    // 1.0
        g.addEdge(1, 2, 5.0);
        System.out.println(g.getWeight(1, 2));    // 5.0

        System.out.println(g.invert());
        // 1 --> 5 weight = 1.0
        // 2 --> 1 weight = 1.0
        // 3 --> 4 weight = 1.0
        // 4 --> 7 weight = 1.0
        // ...

        Set<Integer> s = g.getSuccessorVertexSet(2);
        System.out.println(s);
        s.remove(5);    // Laufzeitfehler! Warum?
    }
}
