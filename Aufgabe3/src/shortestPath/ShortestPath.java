// O. Bittel;
// 28.02.2019

package shortestPath;


import graph.DirectedGraph;
import sim.SYSimulation;

import java.awt.*;
import java.util.List;
import java.util.Queue;
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
    private LinkedList<V> weg;


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
        weg = new LinkedList<>();
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

    public void searchShortestPath(V start, V g) {
        searchShortestPathMain(start, g);
//        if (sim != null) simulateShortestPath();
    }

    private void simulateShortestPath() {
        sim.startSequence("Kürzester Weg von " + start + " nach " + end);
        Iterator<V> iterator = weg.iterator();
        V nextStation = iterator.next();
        while (iterator.hasNext()) {
            V thisStation = nextStation;
            nextStation = iterator.next();
            visitStation(thisStation);
            driveToStation(thisStation, nextStation);
        }
        visitStation(nextStation);
        weg.clear();
        sim.stopSequence();
    }

    private void visitStation(V thisStation) {
        if (myHeuristic != null) sim.visitStation((int) thisStation, Color.GREEN);
        else sim.visitStation((int) thisStation, Color.BLUE);
    }

    private void driveToStation(V thisStation, V nextStation) {
        sim.drive((int) thisStation, (int) nextStation);
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
    public void searchShortestPathMain(V s, V g) {
        Queue<V> kandidatenListe = new PriorityQueue<>(Comparator.comparing(o -> dist.get(o)));
        start = s;
        end = g;

        for (V v : myGraph.getVertexSet()) {
            dist.put(v, (double) Integer.MAX_VALUE); //Distanz zu Start -> Start nicht relevant => infinity
            pred.put(v, null); //Vorgänger -> Vorgänger nicht bekannt => null
        }

        dist.put(s, 0.0);
        kandidatenListe.add(s);

        while (!kandidatenListe.isEmpty()) {
            V min;
            if (myHeuristic != null) {
                min = kandidatenListe.stream()
                        .min(Comparator.comparing(o -> dist.get(o) + myHeuristic.estimatedCost(o, g)))
                        .get();
                kandidatenListe.remove(min);
            } else {
                min = kandidatenListe.poll();
            }

//            System.out.printf("Besuche Knoten %s mit d = %.2f", min, dist.get(min));
//            if (myHeuristic != null) {
//                System.out.printf(" -> %.2f", myHeuristic.estimatedCost(min, g));
//            }
//            System.out.print("\n");
            if (min.equals(g)) return;

            if (sim != null) {
                Color c = myHeuristic != null ? Color.GREEN : Color.BLUE;
                sim.visitStation((int) min, c);
            }

            for (V w : myGraph.getSuccessorVertexSet(min)) {
                if (dist.get(w).equals((double) Integer.MAX_VALUE))
                    kandidatenListe.add(w);
                if (dist.get(min) + myGraph.getWeight(min, w) < dist.get(w)) {
                    pred.put(w, min);
                    dist.put(w, dist.get(min) + myGraph.getWeight(min, w));
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
