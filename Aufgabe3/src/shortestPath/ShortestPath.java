// O. Bittel;
// 28.02.2019

package shortestPath;


import graph.DirectedGraph;
import sim.SYSimulation;

import java.util.*;


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
        LinkedList<Map.Entry<V, Double>> kandidatenListe = new LinkedList<>();
        start = s;
        end = g;

        for (V v : myGraph.getVertexSet()) {
            dist.put(v, (double) Integer.MAX_VALUE); //Distanz zu Start -> Start nicht relevant => infinity
            pred.put(v, null); //Vorgänger -> Vorgänger nicht bekannt => null
        }

        dist.put(s, 0.0);
        kandidatenListe.add(Map.entry(s, dist.get(s)));

        while (!kandidatenListe.isEmpty()) {
            Map.Entry<V, Double> min;
            if (myHeuristic != null) {
                min = kandidatenListe.stream()
                        .min(Comparator.comparing(o -> o.getValue() + myHeuristic.estimatedCost(o.getKey(), g)))
                        .get();
                kandidatenListe.remove(min);
            } else {
                System.out.println(kandidatenListe);
                Collections.sort(kandidatenListe, Map.Entry.comparingByValue(Comparator.reverseOrder()));
                min = kandidatenListe.poll();
            }

            System.out.printf("Besuche Knoten %s mit d = %.2f\n", min.getKey(), dist.get(min.getKey()));
            if (min.getKey().equals(g)) return;

            for (V w : myGraph.getSuccessorVertexSet(min.getKey())) {
                if (dist.get(w).equals((double) Integer.MAX_VALUE))
                    kandidatenListe.add(Map.entry(w, dist.get(w)));
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
        V next = end;
        do {
            shortestPath.add(next);
            next = pred.get(next);
        } while (next != start);
        shortestPath.add(next);
        Collections.reverse(shortestPath);
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
        return dist.get(end);
    }

}
