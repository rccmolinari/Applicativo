package model;

public class Bagaglio {

    protected int codice_Bagaglio;
    protected Stato_Bagaglio stato_Bagaglio;
    protected Prenotazione prenotazione;

    public int getCodice_Bagaglio() {
        return codice_Bagaglio;
    }

    public void setCodice_Bagaglio(int codice_Bagaglio) {
        this.codice_Bagaglio = codice_Bagaglio;
    }

    public Stato_Bagaglio getStato_Bagaglio() {
        return stato_Bagaglio;
    }

    public void setStato_Bagaglio(Stato_Bagaglio stato_Bagaglio) {
        this.stato_Bagaglio = stato_Bagaglio;
    }

    public Prenotazione getPrenotazione() {
        return prenotazione;
    }

    public void setPrenotazione(Prenotazione prenotazione) {
        this.prenotazione = prenotazione;
    }

    public Bagaglio(Stato_Bagaglio stato_Bagaglio, int codice_Bagaglio, Prenotazione prenotazione) {
        this.stato_Bagaglio = stato_Bagaglio;
        this.codice_Bagaglio = codice_Bagaglio;
        this.prenotazione = prenotazione;
    }

    public Bagaglio() {}
}
