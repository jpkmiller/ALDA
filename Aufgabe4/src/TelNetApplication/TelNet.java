package TelNetApplication;

import draw.StdDraw;

import java.awt.*;
import java.util.List;
import java.util.*;

public class TelNet {
    private final int lbg;
    private Map<TelKnoten, Integer> telMap;
    private PriorityQueue<TelVerbindung> telQueue;
    private List<TelVerbindung> optTelNet;
    private int size;

    public TelNet(int lbg) {
        telMap = new HashMap<>();
        optTelNet = new LinkedList<>();
        telQueue = new PriorityQueue<>(Comparator.comparing(x -> x.c));
        this.lbg = lbg;
    }

    public static void main(String[] args) {
        TelNet telNet = new TelNet(100);
        telNet.generateRandomTelNet(1000, 1000, 1000);
        System.out.println(telNet.computeOptTelNet());
        telNet.drawOptTelNet(10, 10);
    }

    /**
     * Fügt einen neuen Telefonknoten mit Koordinate (x,y) dazu
     *
     * @param x Koordinate
     * @param y Koordinate
     * @return true, falls die Koordinate neu ist
     */
    public boolean addTelKnoten(int x, int y) {
        TelKnoten knoten = new TelKnoten(x, y);
        if (telMap.containsKey(knoten))
            return false;
        telMap.put(knoten, size++);
        return true;
    }

    private void addTelVerbindung(int x1, int y1, int x2, int y2) {
        TelKnoten t1 = new TelKnoten(x1, y1);
        TelKnoten t2 = new TelKnoten(x2, y2);
        if (dist(t1, t2) <= lbg) {
            TelVerbindung telVerbindung = new TelVerbindung(t1, t2, cost(t1, t2));
            telQueue.add(telVerbindung);
        }
    }

    private int dist(TelKnoten a, TelKnoten b) {
        return Math.abs(a.x - b.x) + Math.abs(a.y - b.y);
    }

    private int cost(TelKnoten a, TelKnoten b) {
        if (dist(a, b) <= lbg) return dist(a, b);
        else return Integer.MAX_VALUE;
    }

    public boolean computeOptTelNet() {
        UnionFind forest = new UnionFind(size());

        while (forest.size() != 1 && !telQueue.isEmpty()) {
            TelVerbindung min = telQueue.poll();
            int t1 = forest.find(telMap.get(min.u));
            int t2 = forest.find(telMap.get(min.v));
            if (t1 != t2) {
                forest.union(t1, t2);
                optTelNet.add(min);
            }
        }

        return !telQueue.isEmpty() || forest.size() == 1;
    }

    double factorize(int xy, int xyMax) {
        return (1.0 / xyMax) * xy;
    }

    public void drawOptTelNet(int xMax, int yMax) throws IllegalStateException {
        StdDraw.setXscale(0, xMax);
        StdDraw.setYscale(0, yMax);

        for (TelVerbindung v : optTelNet) {
            System.out.println(v);
            double x1 = factorize(v.u.x, xMax);
            double y1 = factorize(v.u.y, xMax);
            double x2 = factorize(v.v.x, xMax);
            double y2 = factorize(v.v.y, xMax);

            StdDraw.filledSquare(x1, y1, 0.1);
            StdDraw.filledSquare(x2, y2, 0.1);
            StdDraw.setPenColor(Color.BLACK);
            StdDraw.line(x1, y1, x2, y1);
            StdDraw.line(x2, y1, x2, y2);

        }
    }

    public void generateRandomTelNet(int n, int xMax, int yMax) {
        Random random = new Random();
        for (int i = 0; i < n; i++) {
            int rx1 = random.nextInt(xMax);
            int ry1 = random.nextInt(yMax);
            int rx2 = random.nextInt(xMax);
            int ry2 = random.nextInt(yMax);
            addTelKnoten(rx1, ry1);
            addTelKnoten(rx2, ry2);
            addTelVerbindung(rx1, ry1, rx2, ry2);
            addTelVerbindung(rx2, rx1, rx1, ry1);
        }
    }

    public List<TelVerbindung> getOptTelNet() throws IllegalStateException {
        return optTelNet;
    }

    public int getOptTelNetKosten() throws IllegalStateException {
        return optTelNet.stream().mapToInt(telVerbindung -> telVerbindung.c).sum();
    }

    public int size() {
        return size;
    }

    @Override
    public String toString() {
        return "TelNet{" +
                "lbg=" + lbg +
                ", telMap=" + telMap +
                ", telQueue=" + telQueue +
                ", optTelNet=" + optTelNet +
                '}';
    }
}
