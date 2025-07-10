package model;
import java.util.ArrayList;
import java.util.List;

/**
 * Rappresenta una prenotazione effettuata da un utente per un determinato volo.
 * Ogni prenotazione è associata a un passeggero, una lista di bagagli e un numero di biglietto.
 */
public class Prenotazione {

    /** Utente che ha effettuato la prenotazione. */
    protected UtenteGenerico utente;

    /** Volo prenotato. */
    protected Volo volo;

    /** Numero del biglietto associato alla prenotazione. */
    protected int numeroBiglietto;

    /** Numero assegnato al passeggero (es. posto o riferimento interno). */
    protected int numeroAssegnato;

    /** Stato corrente della prenotazione. */
    protected StatoPrenotazione statoPrenotazione;

    /** Passeggero per cui è stata effettuata la prenotazione. */
    protected Passeggero passeggero;

    /** Lista di bagagli associati a questa prenotazione. */
    public List<Bagaglio> listaBagagli = new ArrayList<>();

    /**
     * Restituisce il volo associato.
     *
     * @return oggetto {@code Volo}
     */
    public Volo getVolo() {
        return volo;
    }

    /**
     * Imposta il volo associato alla prenotazione.
     *
     * @param volo volo da associare
     */
    public void setVolo(Volo volo) {
        this.volo = volo;
    }

    /**
     * Restituisce l'utente che ha effettuato la prenotazione.
     *
     * @return oggetto {@code UtenteGenerico}
     */
    public UtenteGenerico getUtente() {
        return utente;
    }

    /**
     * Imposta l'utente che ha effettuato la prenotazione.
     *
     * @param utente utente da associare
     */
    public void setUtente(UtenteGenerico utente) {
        this.utente = utente;
    }

    /**
     * Restituisce il numero del biglietto.
     *
     * @return numero biglietto
     */
    public int getNumeroBiglietto() {
        return numeroBiglietto;
    }

    /**
     * Imposta il numero del biglietto.
     *
     * @param numeroBiglietto numero da assegnare
     */
    public void setNumeroBiglietto(int numeroBiglietto) {
        this.numeroBiglietto = numeroBiglietto;
    }

    /**
     * Restituisce il numero assegnato (es. numero posto).
     *
     * @return numero assegnato
     */
    public int getNumeroAssegnato() {
        return numeroAssegnato;
    }

    /**
     * Imposta il numero assegnato (es. numero posto).
     *
     * @param numeroAssegnato numero da assegnare
     */
    public void setNumeroAssegnato(int numeroAssegnato) {
        this.numeroAssegnato = numeroAssegnato;
    }

    /**
     * Restituisce lo stato attuale della prenotazione.
     *
     * @return oggetto {@code StatoPrenotazione}
     */
    public StatoPrenotazione getStatoPrenotazione() {
        return statoPrenotazione;
    }

    /**
     * Imposta lo stato della prenotazione.
     *
     * @param statoPrenotazione stato da assegnare
     */
    public void setStatoPrenotazione(StatoPrenotazione statoPrenotazione) {
        this.statoPrenotazione = statoPrenotazione;
    }

    /**
     * Restituisce il passeggero associato alla prenotazione.
     *
     * @return oggetto {@code Passeggero}
     */
    public Passeggero getPasseggero() {
        return passeggero;
    }

    /**
     * Imposta il passeggero associato alla prenotazione.
     *
     * @param passeggero oggetto da associare
     */
    public void setPasseggero(Passeggero passeggero) {
        this.passeggero = passeggero;
    }

    /**
     * Restituisce la lista dei bagagli associati.
     *
     * @return lista di {@code Bagaglio}
     */
    public List<Bagaglio> getListaBagagli() {
        return listaBagagli;
    }

    /**
     * Imposta la lista dei bagagli associati.
     *
     * @param listaBagagli lista da associare
     */
    public void setListaBagagli(List<Bagaglio> listaBagagli) {
        this.listaBagagli = listaBagagli;
    }

    /**
     * Costruttore completo della prenotazione.
     *
     * @param utente utente che prenota
     * @param volo volo selezionato
     * @param numeroAssegnato numero assegnato (posto)
     * @param numeroBiglietto numero del biglietto
     * @param statoPrenotazione stato della prenotazione
     * @param passeggero passeggero associato
     * @param listaBagagli lista dei bagagli
     */
    public Prenotazione(UtenteGenerico utente, Volo volo, int numeroAssegnato, int numeroBiglietto, StatoPrenotazione statoPrenotazione, Passeggero passeggero, List<Bagaglio> listaBagagli) {
        this.utente = utente;
        this.volo = volo;
        this.numeroAssegnato = numeroAssegnato;
        this.numeroBiglietto = numeroBiglietto;
        this.statoPrenotazione = statoPrenotazione;
        this.passeggero = passeggero;
        this.listaBagagli = listaBagagli;
    }

    /**
     * Costruttore vuoto.
     */
    public Prenotazione() {}
}