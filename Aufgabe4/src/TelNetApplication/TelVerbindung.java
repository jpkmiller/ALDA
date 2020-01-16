package TelNetApplication;

public class TelVerbindung {
    int c;
    TelKnoten u; //Anfangsknoten
    TelKnoten v; //Endknoten

    public TelVerbindung(TelKnoten u, TelKnoten v, int c) {
        this.c = c;
        this.u = u;
        this.v = v;
    }

    @Override
    public String toString() {
        return "(c=" + c +
                ", u=" + u +
                ", v=" + v + ")";
    }
}
