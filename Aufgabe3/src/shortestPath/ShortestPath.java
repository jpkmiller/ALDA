// O. Bittel;
// 28.02.2019

package shortestPath;


import graph.DirectedGraph;
import sim.SYSimulation;

import java.util.*;
// ...

/**
 * Kürzeste Wege in Graphen mit A*- und Dijkstra-Verfahren.
 *
 * @param <V> Knotentyp.
 * @author Oliver Bittel
 * @since 27.01.2015
 */
public class ShortestPath<V> {

    SYSimulation sim = null;

    Map<V, Double> dist; // Distanz für jeden Knoten
    Map<V, V> pred; // Vorgänger für jeden Knoten
    DirectedGraph<V> myGraph;
    Heuristic<V> myHeuristic;
    V start;
    V end;


    /**
     * Konstruiert ein Objekt, das im Graph g kuerzeste Wege
     * nach dem A*-Verfahren berechnen kann.
     * Die Heuristik h schätzt die Kosten zwischen zwei Knoten ab.
     * Wird h = null gewählt, dann ist das Verfahren identisch
     * mit dem Dijkstra-Verfahren.
     *
     * @param g Gerichteter Graph
     * @param h Heuristik. Falls h == null, werden kürzeste Wege nach
     *          dem Dijkstra-Verfahren gesucht.
     */
    public ShortestPath(DirectedGraph<V> g, Heuristic<V> h) {
        dist = new TreeMap<>();
        pred = new TreeMap<>();
        myGraph = g;
        myHeuristic = h;
    }

    /**
     * Diese Methode sollte nur verwendet werden,
     * wenn kürzeste Wege in Scotland-Yard-Plan gesucht werden.
     * Es ist dann ein Objekt für die Scotland-Yard-Simulation zu übergeben.
     * <p>
     * Ein typische Aufruf für ein SYSimulation-Objekt sim sieht wie folgt aus:
     * <p><blockquote><pre>
     *    if (sim != null)
     *       sim.visitStation((Integer) v, Color.blue);
     * </pre></blockquote>
     *
     * @param sim SYSimulation-Objekt.
     */
    public void setSimulator(SYSimulation sim) {
        this.sim = sim;
    }

    /**
     * Sucht den kürzesten Weg von Starknoten s zum Zielknoten g.
     * <p>
     * Falls die Simulation mit setSimulator(sim) aktiviert wurde, wird der Knoten,
     * der als nächstes aus der Kandidatenliste besucht wird, animiert.
     *
     * @param s Startknoten
     * @param g Zielknoten
     */
    public void searchShortestPath(V s, V g) {
        Queue<V> kandidatenListe = new LinkedList<>();
        start = s;
        end = g;

        for (V v : myGraph.getVertexSet()) {
            dist.put(v, null); //Distanz zu Start -> Start nicht relevant => null
            pred.put(v, null); //Vorgänger -> Vorgänger nicht bekannt => null
        }

        dist.put(s, 0.0);
        kandidatenListe.add(s);

//        if (o1.getValue() + myHeuristic.estimatedCost(o1.getKey(), g) > o2.getValue() + myHeuristic.estimatedCost(o2.getKey(), g))
//            return 1;
//        else if (o1.getValue() + myHeuristic.estimatedCost(o1.getKey(), g) < o2.getValue() + myHeuristic.estimatedCost(o2.getKey(), g))
//            return -1;
//        else return 0;

        while (!kandidatenListe.isEmpty()) {
            Map.Entry<V, Double> min = dist.entrySet()
                    .parallelStream()
                    .min(Comparator.comparingDouble(o -> o.getValue() + myHeuristic.estimatedCost(o.getKey(), g)))
                    .get();
            kandidatenListe.remove(min);
            if (min.equals(g)) return;
            for (V w : myGraph.getSuccessorVertexSet(min.getKey())) {
                if (dist.get(w) == null)
                    kandidatenListe.add(w);
                if (dist.get(min.getKey()) + myGraph.getWeight(min.getKey(), w) < dist.get(w)) {
                    pred.put(w, min.getKey());
                    dist.put(w, dist.get(min.getKey()) + myGraph.getWeight(min.getKey(), w));
                }
            }
        }
    }


    /**
     * Liefert einen kürzesten Weg von Startknoten s nach Zielknoten g.
     * Setzt eine erfolgreiche Suche von searchShortestPath(s,g) voraus.
     *
     * @return kürzester Weg als Liste von Knoten.
     * @throws IllegalArgumentException falls kein kürzester Weg berechnet wurde.
     */
    public List<V> getShortestPath() {
        List<V> shortestPath = new LinkedList<>();
        for (V v : dist.keySet()) shortestPath.add(v);
        return Collections.unmodifiableList(shortestPath);
    }

    /**
     * Liefert die Länge eines kürzesten Weges von Startknoten s nach Zielknoten g zurück.
     * Setzt eine erfolgreiche Suche von searchShortestPath(s,g) voraus.
     *
     * @return Länge eines kürzesten Weges.
     * @throws IllegalArgumentException falls kein kürzester Weg berechnet wurde.
     */
    public double getDistance() {
        return dist.values().stream().mapToDouble(v -> v).sum();
    }

}
