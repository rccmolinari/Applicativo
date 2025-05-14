package model;
import java.util.ArrayList;

/**
 * Estende {@link Utente} per rappsentare un utente generico che può eddettuare prenotazioni.
 */
public class Utente_Generico extends Utente {

    /**
     * Lista dei voli prenotati dall'utente.
     */
    protected ArrayList<Volo> lista_Prenotazioni;

    /**
     * Restituisce la lista delle prenotazioni dell'utente.
     *
     * @return the lista prenotazioni
     */
    public ArrayList<Volo> getLista_Prenotazioni() {
        return lista_Prenotazioni;
    }

    /**
     * Imposta la lista delle prenotazioni.
     *
     * @param lista_Prenotazioni the lista prenotazioni
     */
    public void setLista_Prenotazioni(ArrayList<Volo> lista_Prenotazioni) {
        this.lista_Prenotazioni = lista_Prenotazioni;
    }

    /**
     * Effettua uuna prenotazione per un determinato volo.
     *
     * @param volo the volo
     */
    public void prenota_Volo(Volo volo) {}

    /**
     * Cerca una prenotazione tramite il nome del passeggero.
     *
     * @param nome_Passeggero the nome passeggero
     */
    public void cerca_Prenotazione(String nome_Passeggero) {}

    /**
     * Cerca una prenotazione in base al codice del volo.
     *
     * @param codice_Volo the codice volo
     */
    public void cerca_Prenotazione(int codice_Volo) {}

    /**
     * Segnala lo smarrimento di un bagaglio.
     *
     * @param Bagaglio the bagaglio
     */
    public void segnala_Smarrimento(Bagaglio Bagaglio) {}


    /**
     * Costruttore completo.
     *
     * @param login              the login
     * @param password           the password
     * @param lista_Prenotazioni the lista prenotazioni
     */
    public Utente_Generico(String login, String password, ArrayList<Volo> lista_Prenotazioni) {
        super(login, password);
        this.lista_Prenotazioni = lista_Prenotazioni;
    }

    /**
     * Costruttore di default.
     */
    public Utente_Generico() {
    }


}
