// O. Bittel;
// 19.03.2018

package directedGraph;

import java.util.*;

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
        if (succ.containsKey(v))
            return false;
        else
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
        if (succ.get(v) == null) this.addVertex(v);
        if (pred.get(w) == null) this.addVertex(w);

        if (succ.get(v).containsKey(w) && pred.get(w).containsKey(v)) {
            succ.get(v).put(w, weight);
            pred.get(w).put(v, weight);
            return false;
        } else {
            succ.get(v).putIfAbsent(w, weight);
            pred.get(w).putIfAbsent(v, weight);
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
     * @param v Knoten
     * @return true, falls Knoten vorhanden ist.
     */
    @Override
    public boolean containsVertex(V v) {
        return succ.containsKey(v);
    }

    /**
     * Prüft ob Kante im Graph vorhanden ist.
     * @param v Startknoten
     * @param w Endknoten
     * @return true, falls Kante vorhanden ist.
     */
    @Override
    public boolean containsEdge(V v, V w) {
        return succ.get(v).containsKey(w);
    }

    /**
     * Liefert Gewicht der Kante zurück.
     * @param v Startknoten
     * @param w Endknoten
     * @throws IllegalArgumentException falls die Kante nicht existiert.
     * @return Gewicht der Kante.
     */
    @Override
    public double getWeight(V v, V w) {
        return succ.get(v).get(w);
    }


    /**
     * Liefert Eingangsgrad des Knotens v zurück.
     * Das ist die Anzahl der Kanten mit Zielknoten v.
     * @param v Knoten
     * @throws IllegalArgumentException falls Knoten v
     * nicht im Graph vorhanden ist.
     * @return Knoteneingangsgrad
     */
    @Override
    public int getInDegree(V v) {
        return pred.get(v).size();
    }

    /**
     * Liefert Ausgangsgrad des Knotens v zurück.
     * Das ist die Anzahl der Kanten mit Quellknoten v.
     * @param v Knoten
     * @throws IllegalArgumentException falls Knoten v
     * nicht im Graph vorhanden ist.
     * @return Knotenausgangsgrad
     */
    @Override
    public int getOutDegree(V v) {
        return succ.get(v).size();
    }

    @Override
    public Set<V> getVertexSet() {
        return Collections.unmodifiableSet(succ.keySet()); // nicht modifizierbare Sicht
    }

    /**
     * Liefert eine nicht modifizierbare Sicht (unmodifiable view) auf
     * die Menge aller Vorgängerknoten von v zurück.
     * Das sind alle die Knoten, von denen eine Kante zu v führt.
     * @param v Knoten
     * @throws IllegalArgumentException falls Knoten v
     * nicht im Graph vorhanden ist.
     * @return Knotenmenge
     */
    @Override
    public Set<V> getPredecessorVertexSet(V v) {
        return pred.get(v).keySet();
    }

    @Override
    public Set<V> getSuccessorVertexSet(V v) {
        return succ.get(v).keySet();
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
        int sum = 0;
        for (Map.Entry<V, Map<V, Double>> s : succ.entrySet())
            sum += s.getValue().size();
        return sum;
    }

    /**
     * Erzeugt einen invertierten Graphen,
     * indem jede Kante dieses Graphens in umgekehrter Richtung abgespeichert wird.
     * @return invertierter Graph
     */
    @Override
    public DirectedGraph<V> invert() {
        DirectedGraph<V> invGraph = new AdjacencyListDirectedGraph<>();

        for (Map.Entry<V, Map<V, Double>> e : succ.entrySet())
            for (var succs : e.getValue().entrySet())
                invGraph.addEdge(succs.getKey(), e.getKey(), succs.getValue());

        return invGraph;
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        for (var e : succ.entrySet())
            for (var succs : e.getValue().entrySet())
                str.append(e.getKey()).append(" --> ").append(succs.getKey()).append(" weight = ").append(succs.getValue()).append("\n");
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

        System.out.println("");
        System.out.println(g.getOutDegree(2));                // 2
        System.out.println(g.getSuccessorVertexSet(2));    // 5, 6
        System.out.println(g.getInDegree(6));                // 2
        System.out.println(g.getPredecessorVertexSet(6));    // 2, 4

        System.out.println("");
        System.out.println(g.containsEdge(1, 2));    // true
        System.out.println(g.containsEdge(2, 1));    // false
        System.out.println(g.getWeight(1, 2));    // 1.0
        g.addEdge(1, 2, 5.0);
        System.out.println(g.getWeight(1, 2));    // 5.0

        System.out.println("");
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
