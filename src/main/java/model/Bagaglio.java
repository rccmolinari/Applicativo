package model;

/**
 * The type Bagaglio.
 */
public class Bagaglio {

    /**
     * The Codice bagaglio.
     */
    protected int codiceBagaglio;
    /**
     * The Stato bagaglio.
     */
    protected StatoBagaglio statoBagaglio;
    /**
     * The Prenotazione.
     */
    protected Prenotazione prenotazione;

    /**
     * Gets codice bagaglio.
     *
     * @return the codice bagaglio
     */
    public int getCodiceBagaglio() {
        return codiceBagaglio;
    }

    /**
     * Sets codice bagaglio.
     *
     * @param codiceBagaglio the codice bagaglio
     */
    public void setCodiceBagaglio(int codiceBagaglio) {
        this.codiceBagaglio = codiceBagaglio;
    }

    /**
     * Gets stato bagaglio.
     *
     * @return the stato bagaglio
     */
    public StatoBagaglio getStatoBagaglio() {
        return statoBagaglio;
    }

    /**
     * Sets stato bagaglio.
     *
     * @param statoBagaglio the stato bagaglio
     */
    public void setStatoBagaglio(StatoBagaglio statoBagaglio) {
        this.statoBagaglio = statoBagaglio;
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
     * @param statoBagaglio  the stato bagaglio
     * @param codiceBagaglio the codice bagaglio
     * @param prenotazione    the prenotazione
     */
    public Bagaglio(StatoBagaglio statoBagaglio, int codiceBagaglio, Prenotazione prenotazione) {
        this.statoBagaglio = statoBagaglio;
        this.codiceBagaglio = codiceBagaglio;
        this.prenotazione = prenotazione;
    }



    /**
     * Instantiates a new Bagaglio.
     */
    public Bagaglio() {}
}
