package model;

/**
 * The type Bagaglio.
 */
public class Bagaglio {

    /**
     * The Codice bagaglio.
     */
    protected int codice_Bagaglio;
    /**
     * The Stato bagaglio.
     */
    protected Stato_Bagaglio stato_Bagaglio;
    /**
     * The Prenotazione.
     */
    protected Prenotazione prenotazione;

    /**
     * Gets codice bagaglio.
     *
     * @return the codice bagaglio
     */
    public int getCodice_Bagaglio() {
        return codice_Bagaglio;
    }

    /**
     * Sets codice bagaglio.
     *
     * @param codice_Bagaglio the codice bagaglio
     */
    public void setCodice_Bagaglio(int codice_Bagaglio) {
        this.codice_Bagaglio = codice_Bagaglio;
    }

    /**
     * Gets stato bagaglio.
     *
     * @return the stato bagaglio
     */
    public Stato_Bagaglio getStato_Bagaglio() {
        return stato_Bagaglio;
    }

    /**
     * Sets stato bagaglio.
     *
     * @param stato_Bagaglio the stato bagaglio
     */
    public void setStato_Bagaglio(Stato_Bagaglio stato_Bagaglio) {
        this.stato_Bagaglio = stato_Bagaglio;
    }

    /**
     * Gets prenotazione.
     *
     * @return the prenotazione
     */
    public Prenotazione getPrenotazione() {
        return prenotazione;
    }

    /**
     * Sets prenotazione.
     *
     * @param prenotazione the prenotazione
     */
    public void setPrenotazione(Prenotazione prenotazione) {
        this.prenotazione = prenotazione;
    }

    /**
     * Instantiates a new Bagaglio.
     *
     * @param stato_Bagaglio  the stato bagaglio
     * @param codice_Bagaglio the codice bagaglio
     * @param prenotazione    the prenotazione
     */
    public Bagaglio(Stato_Bagaglio stato_Bagaglio, int codice_Bagaglio, Prenotazione prenotazione) {
        this.stato_Bagaglio = stato_Bagaglio;
        this.codice_Bagaglio = codice_Bagaglio;
        this.prenotazione = prenotazione;
    }

    /**
     * Instantiates a new Bagaglio.
     */
    public Bagaglio() {}
}
