package model;
import java.util.ArrayList;
import java.util.List;

/**
 * Estende {@link Utente} per rappsentare un utente generico che può eddettuare prenotazioni.
 */
public class UtenteGenerico extends Utente {

    /**
     * Lista dei voli prenotati dall'utente.
     */
    protected ArrayList<Volo> listaPrenotazioni;

    /**
     * Restituisce la lista delle prenotazioni dell'utente.
     *
     * @return the lista prenotazioni
     */
    public ArrayList<Volo>getListaPrenotazioni() {
        return listaPrenotazioni;
    }

    /**
     * Imposta la lista delle prenotazioni.
     *
     * @param listaPrenotazioni the lista prenotazioni
     */
    public void setListaPrenotazioni(ArrayList<Volo> listaPrenotazioni) {
        this.listaPrenotazioni = listaPrenotazioni;
    }

    /**
     * Effettua uuna prenotazione per un determinato volo.
     *
     * @param volo the volo
     */
    public void prenotaVolo(Volo volo, Passeggero p) {}

    /**
     * Cerca una prenotazione tramite il nome del passeggero.
     *
     * @param nome_Passeggero the nome passeggero
     */
    public Prenotazione cercaPrenotazione(String nome_Passeggero) {
        return null;
    }

    /**
     * Cerca una prenotazione in base al codice del volo.
     *
     * @param codice_Volo the codice volo
     */
    public Prenotazione cercaPrenotazione(int codice_Volo) {
        return null;
    }

    /**
     * Segnala lo smarrimento di un bagaglio.
     *
     * @param Bagaglio the bagaglio
     */
    public void segnalaSmarrimento(Bagaglio Bagaglio) {}


    /**
     * Costruttore completo.
     *
     * @param login              the login
     * @param password           the password
     * @param listaPrenotazioni the lista prenotazioni
     */
    public UtenteGenerico(String login, String password, ArrayList<Volo> listaPrenotazioni) {
        super(login, password);
        this.listaPrenotazioni = listaPrenotazioni;
    }

    @Override
    public ArrayList<Volo> visualizzaVoli(ArrayList<Volo> voli) {
        return voli;
    }

    /**
     * Costruttore di default.
     */
    public UtenteGenerico() {
    }


}
