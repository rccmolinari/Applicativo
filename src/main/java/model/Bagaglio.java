package model;

/**
 * Rappresenta un bagaglio associato a una prenotazione.
 * Ogni bagaglio è identificato da un codice univoco e ha uno stato attuale (es. registrato, smarrito, recuperato).
 */
public class Bagaglio {

    /** Codice identificativo univoco del bagaglio. */
    protected int codiceBagaglio;

    /** Stato corrente del bagaglio (es. REGISTRATO, SMARRITO, RITROVATO). */
    protected StatoBagaglio statoBagaglio;

    /** Prenotazione a cui è associato il bagaglio. */
    protected Prenotazione prenotazione;

    /**
     * Costruttore completo.
     *
     * @param statoBagaglio  stato attuale del bagaglio
     * @param codiceBagaglio codice identificativo del bagaglio
     * @param prenotazione   prenotazione associata al bagaglio
     */
    public Bagaglio(StatoBagaglio statoBagaglio, int codiceBagaglio, Prenotazione prenotazione) {
        this.statoBagaglio = statoBagaglio;
        this.codiceBagaglio = codiceBagaglio;
        this.prenotazione = prenotazione;
    }

    /**
     * Costruttore vuoto per creazione e inizializzazione successiva.
     */
    public Bagaglio() {
    }

    /**
     * Restituisce il codice identificativo del bagaglio.
     *
     * @return intero con codice bagaglio
     */
    public int getCodiceBagaglio() {
        return codiceBagaglio;
    }

    /**
     * Imposta il codice identificativo del bagaglio.
     *
     * @param codiceBagaglio codice numerico del bagaglio
     */
    public void setCodiceBagaglio(int codiceBagaglio) {
        this.codiceBagaglio = codiceBagaglio;
    }

    /**
     * Restituisce lo stato attuale del bagaglio.
     *
     * @return enumerazione {@code StatoBagaglio}
     */
    public StatoBagaglio getStatoBagaglio() {
        return statoBagaglio;
    }

    /**
     * Imposta un nuovo stato per il bagaglio.
     *
     * @param statoBagaglio valore dell'enumerazione {@code StatoBagaglio}
     */
    public void setStatoBagaglio(StatoBagaglio statoBagaglio) {
        this.statoBagaglio = statoBagaglio;
    }

    /**
     * Restituisce la prenotazione associata a questo bagaglio.
     *
     * @return oggetto {@code Prenotazione}
     */
    public Prenotazione getPrenotazione() {
        return prenotazione;
    }

    /**
     * Associa una prenotazione al bagaglio.
     *
     * @param prenotazione oggetto {@code Prenotazione} da assegnare
     */
    public void setPrenotazione(Prenotazione prenotazione) {
        this.prenotazione = prenotazione;
    }
}