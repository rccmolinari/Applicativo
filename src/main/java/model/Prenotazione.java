package model;
import java.util.ArrayList;

/**
 * The type Prenotazione.
 */
public class Prenotazione {

    /**
     * The Utente.
     */
    protected UtenteGenerico utente;
    /**
     * The Volo.
     */
    protected Volo volo;
    /**
     * The Numero biglietto.
     */
    protected int numeroBiglietto;
    /**
     * The Numero assegnato.
     */
    protected int numeroAssegnato;
    /**
     * The Stato prenotazione.
     */
    protected StatoPrenotazione statoPrenotazione;
    /**
     * The Passeggero.
     */
    protected Passeggero passeggero;
    /**
     * The Lista bagagli.
     */
    public ArrayList<Bagaglio> listaBagagli = new ArrayList<>();

    /**
     * Gets volo.
     *
     * @return the volo
     */
    public Volo getVolo() {
        return volo;
    }

    /**
     * Sets volo.
     *
     * @param volo the volo
     */
    public void setVolo(Volo volo) {
        this.volo = volo;
    }

    /**
     * Gets utente.
     *
     * @return the utente
     */
    public UtenteGenerico getUtente() {
        return utente;
    }

    /**
     * Sets utente.
     *
     * @param utente the utente
     */
    public void setUtente(UtenteGenerico utente) {
        this.utente = utente;
    }

    /**
     * Gets numero biglietto.
     *
     * @return the numero biglietto
     */
    public int getNumeroBiglietto() {
        return numeroBiglietto;
    }

    /**
     * Sets numero biglietto.
     *
     * @param numeroBiglietto the numero biglietto
     */
    public void setNumeroBiglietto(int numeroBiglietto) {
        this.numeroBiglietto = numeroBiglietto;
    }

    /**
     * Gets numero assegnato.
     *
     * @return the numero assegnato
     */
    public int getNumeroAssegnato() {
        return numeroAssegnato;
    }

    /**
     * Sets numero assegnato.
     *
     * @param numeroAssegnato the numero assegnato
     */
    public void setNumeroAssegnato(int numeroAssegnato) {
        this.numeroAssegnato = numeroAssegnato;
    }

    /**
     * Gets stato prenotazione.
     *
     * @return the stato prenotazione
     */
    public StatoPrenotazione getStatoPrenotazione() {
        return statoPrenotazione;
    }

    /**
     * Sets stato prenotazione.
     *
     * @param statoPrenotazione the stato prenotazione
     */
    public void setStatoPrenotazione(StatoPrenotazione statoPrenotazione) {
        this.statoPrenotazione = statoPrenotazione;
    }

    /**
     * Gets passeggero.
     *
     * @return the passeggero
     */
    public Passeggero getPasseggero() {
        return passeggero;
    }

    /**
     * Sets passeggero.
     *
     * @param passeggero the passeggero
     */
    public void setPasseggero(Passeggero passeggero) {
        this.passeggero = passeggero;
    }

    /**
     * Gets lista bagagli.
     *
     * @return the lista bagagli
     */
    public ArrayList<Bagaglio> getListaBagagli() {
        return listaBagagli;
    }

    /**
     * Sets lista bagagli.
     *
     * @param listaBagagli the lista bagagli
     */
    public void setListaBagagli(ArrayList<Bagaglio> listaBagagli) {
        this.listaBagagli = listaBagagli;
    }

    /**
     * Instantiates a new Prenotazione.
     *
     * @param utente             the utente
     * @param volo               the volo
     * @param numeroAssegnato   the numero assegnato
     * @param numeroBiglietto   the numero biglietto
     * @param statoPrenotazione the stato prenotazione
     * @param passeggero         the passeggero
     * @param listaBagagli      the lista bagagli
     */
    public Prenotazione(UtenteGenerico utente, Volo volo, int numeroAssegnato, int numeroBiglietto, StatoPrenotazione statoPrenotazione, Passeggero passeggero, ArrayList<Bagaglio> listaBagagli) {
        this.utente = utente;
        this.volo = volo;
        this.numeroAssegnato = numeroAssegnato;
        this.numeroBiglietto = numeroBiglietto;
        this.statoPrenotazione = statoPrenotazione;
        this.passeggero = passeggero;
        this.listaBagagli = listaBagagli;
    }

    /**
     * Instantiates a new Prenotazione.
     */
    public Prenotazione() {}

}
