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
    public static int xMax = 1000;
    public static int yMax = 1000;
    public static int n = 1000;

    public TelNet(int lbg) {
        telMap = new HashMap<>();
        optTelNet = new LinkedList<>();
        telQueue = new PriorityQueue<>(Comparator.comparing(x -> x.c));
        this.lbg = lbg;
        this.size = 0;
    }

    public static void main(String[] args) {
//        test1();
        test2();
    }

    private static void test2() {
        TelNet telNet = new TelNet(100);
        telNet.generateRandomTelNet(n, xMax, yMax);
        telNet.computeOptTelNet();
        System.out.println("optTelNet = " + telNet.getOptTelNet());
        System.out.println("Size = " + telNet.size);
        System.out.println("optCost = " + telNet.getOptTelNetKosten());
        telNet.drawOptTelNet(xMax, yMax);
    }

    private static void test1() {
        TelNet telNet = new TelNet(7);

        telNet.addTelKnoten(1, 1);
        telNet.addTelKnoten(3, 1);
        telNet.addTelKnoten(4, 2);
        telNet.addTelKnoten(3, 4);
        telNet.addTelKnoten(2, 6);
        telNet.addTelKnoten(4, 7);
        telNet.addTelKnoten(7, 5);
        telNet.computeOptTelNet();

        System.out.println("optTelNet = " + telNet.getOptTelNet());
        System.out.println("Size = " + telNet.size);
        System.out.println("optCost = " + telNet.getOptTelNetKosten());
        telNet.drawOptTelNet(7, 7);
    }


    private int dist(TelKnoten a, TelKnoten b) {
        return Math.abs(a.x - b.x) + Math.abs(a.y - b.y);
    }

    private int cost(TelKnoten a, TelKnoten b) {
        if (dist(a, b) <= lbg)
            return dist(a, b);
        return Integer.MAX_VALUE;
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

    private double factorize(int xy, int xyMax) {
        return (1.0 / xyMax) * xy;
    }

    /**
     * Fügt einen neuen Telefonknoten mit Koordinate (x,y) dazu
     *
     * @param x Koordinate
     * @param y Koordinate
     * @return true, falls die Koordinate neu ist
     */
    private boolean addTelKnoten(int x, int y) {
        TelKnoten knoten = new TelKnoten(x, y);
        if (telMap.containsKey(knoten))
            return false;
        telMap.put(knoten, size++);
        for (TelKnoten andereKnoten : telMap.keySet()) {
            if (knoten.x != andereKnoten.x && knoten.y != andereKnoten.y)
                addTelVerbindung(andereKnoten.x, andereKnoten.y, knoten.x, knoten.y);
        }
        return true;
    }

    public void generateRandomTelNet(int n, int xMax, int yMax) {
        Random random = new Random();
        for (int i = 0; i < n; i++) {
            int rx1 = random.nextInt(xMax);
            int ry1 = random.nextInt(yMax);
            addTelKnoten(rx1, ry1);
        }
    }

    private void addTelVerbindung(int x1, int y1, int x2, int y2) {
        TelKnoten t1 = new TelKnoten(x1, y1);
        TelKnoten t2 = new TelKnoten(x2, y2);
        if (cost(t1, t1) <= lbg && cost(t1, t2) < Integer.MAX_VALUE) {
            TelVerbindung telVerbindung1 = new TelVerbindung(t1, t2, cost(t1, t2));
            TelVerbindung telVerbindung2 = new TelVerbindung(t2, t1, cost(t2, t1));
            if (!telQueue.contains(telVerbindung1)) {
                telQueue.add(telVerbindung1);
                telQueue.add(telVerbindung2);
            }
        }
    }

    public void drawOptTelNet(int xMax, int yMax) throws IllegalStateException {
        if (optTelNet.isEmpty()) throw new IllegalStateException();
        StdDraw.setCanvasSize(xMax, yMax);

        for (TelVerbindung v : optTelNet) {
            double x1 = factorize(v.u.x, xMax);
            double y1 = factorize(v.u.y, yMax);
            double x2 = factorize(v.v.x, xMax);
            double y2 = factorize(v.v.y, yMax);
            StdDraw.setPenRadius(0.001);
            StdDraw.setPenColor(Color.RED);
            StdDraw.filledSquare(x1, y1, 0.001);
            StdDraw.filledSquare(x2, y2, 0.001);
            StdDraw.setPenColor(Color.BLACK);
            StdDraw.line(x1, y1, x2, y1);
            StdDraw.line(x2, y1, x2, y2);
        }
        StdDraw.show();
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
        StringBuilder s = new StringBuilder();
        telMap.forEach((x, y) -> s.append(y));
        return s.toString();
    }
}
