package model;
import java.util.ArrayList;

/**
 * The type Prenotazione.
 */
public class Prenotazione {

    /**
     * The Utente.
     */
    protected Utente_Generico utente;
    /**
     * The Volo.
     */
    protected Volo volo;
    /**
     * The Numero biglietto.
     */
    protected int numero_Biglietto;
    /**
     * The Numero assegnato.
     */
    protected int numero_Assegnato;
    /**
     * The Stato prenotazione.
     */
    protected Stato_Prenotazione stato_Prenotazione;
    /**
     * The Passeggero.
     */
    protected Passeggero passeggero;
    /**
     * The Lista bagagli.
     */
    protected ArrayList<Bagaglio> lista_Bagagli;

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
    public Utente_Generico getUtente() {
        return utente;
    }

    /**
     * Sets utente.
     *
     * @param utente the utente
     */
    public void setUtente(Utente_Generico utente) {
        this.utente = utente;
    }

    /**
     * Gets numero biglietto.
     *
     * @return the numero biglietto
     */
    public int getNumero_Biglietto() {
        return numero_Biglietto;
    }

    /**
     * Sets numero biglietto.
     *
     * @param numero_Biglietto the numero biglietto
     */
    public void setNumero_Biglietto(int numero_Biglietto) {
        this.numero_Biglietto = numero_Biglietto;
    }

    /**
     * Gets numero assegnato.
     *
     * @return the numero assegnato
     */
    public int getNumero_Assegnato() {
        return numero_Assegnato;
    }

    /**
     * Sets numero assegnato.
     *
     * @param numero_Assegnato the numero assegnato
     */
    public void setNumero_Assegnato(int numero_Assegnato) {
        this.numero_Assegnato = numero_Assegnato;
    }

    /**
     * Gets stato prenotazione.
     *
     * @return the stato prenotazione
     */
    public Stato_Prenotazione getStato_Prenotazione() {
        return stato_Prenotazione;
    }

    /**
     * Sets stato prenotazione.
     *
     * @param stato_Prenotazione the stato prenotazione
     */
    public void setStato_Prenotazione(Stato_Prenotazione stato_Prenotazione) {
        this.stato_Prenotazione = stato_Prenotazione;
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
    public ArrayList<Bagaglio> getLista_Bagagli() {
        return lista_Bagagli;
    }

    /**
     * Sets lista bagagli.
     *
     * @param lista_Bagagli the lista bagagli
     */
    public void setLista_Bagagli(ArrayList<Bagaglio> lista_Bagagli) {
        this.lista_Bagagli = lista_Bagagli;
    }

    /**
     * Instantiates a new Prenotazione.
     *
     * @param utente             the utente
     * @param volo               the volo
     * @param numero_Assegnato   the numero assegnato
     * @param numero_Biglietto   the numero biglietto
     * @param stato_Prenotazione the stato prenotazione
     * @param passeggero         the passeggero
     * @param lista_Bagagli      the lista bagagli
     */
    public Prenotazione(Utente_Generico utente, Volo volo, int numero_Assegnato, int numero_Biglietto, Stato_Prenotazione stato_Prenotazione, Passeggero passeggero, ArrayList<Bagaglio> lista_Bagagli) {
        this.utente = utente;
        this.volo = volo;
        this.numero_Assegnato = numero_Assegnato;
        this.numero_Biglietto = numero_Biglietto;
        this.stato_Prenotazione = stato_Prenotazione;
        this.passeggero = passeggero;
        this.lista_Bagagli = lista_Bagagli;
    }

    /**
     * Instantiates a new Prenotazione.
     */
    public Prenotazione() {}

}
