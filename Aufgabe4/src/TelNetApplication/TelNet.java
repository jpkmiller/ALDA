package TelNetApplication;

import java.util.*;

public class TelNet {
    private final int lbg;
    private Map<TelKnoten, Integer> telMap;
    private PriorityQueue<TelVerbindung> telQueue;
    private List<TelVerbindung> optTelNet;
    private int size;

    public TelNet(int lbg) {
        telMap = new TreeMap<>();
        optTelNet = new LinkedList<>();
        telQueue = new PriorityQueue<>(Comparator.comparing(x -> x.c));
        this.lbg = lbg;
    }

    public static void main(String[] args) {
        TelNet telNet = new TelNet(10);
        telNet.generateRandomTelNet(10, 4, 3);
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

    public boolean computeOptTelNet() {
        UnionFind forest = new UnionFind(lbg);

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

    public void drawOptTelNet(int xMax, int yMax) throws IllegalStateException {

    }

    public void generateRandomTelNet(int n, int xMax, int yMax) {
        Random random = new Random();
        for (int i = 0; i < n; i++) {
            int rx = random.nextInt(xMax);
            int ry = random.nextInt(yMax);
            addTelKnoten(rx, ry);
        }

    }

    public List<TelVerbindung> getOptTelNet() throws IllegalStateException {
        return optTelNet;
    }

    public int getOptTelNetKosten() throws IllegalStateException {
        return optTelNet.stream().mapToInt(telVerbindung -> telVerbindung.c).sum();
    }

    public int size() {
        return optTelNet.size();
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
