package model;

public class Bagaglio {

    protected int codice_Bagaglio;
    protected Stato_Bagaglio stato_Bagaglio;

    public Bagaglio(Stato_Bagaglio stato_Bagaglio, int codice_Bagaglio) {
        this.stato_Bagaglio = stato_Bagaglio;
        this.codice_Bagaglio = codice_Bagaglio;
    }

    public Bagaglio() {}
}
