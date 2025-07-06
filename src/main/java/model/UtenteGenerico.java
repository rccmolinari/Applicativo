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
    protected List<Volo> listaPrenotazioni;

    /**
     * Restituisce la lista delle prenotazioni dell'utente.
     *
     * @return the lista prenotazioni
     */
    public List<Volo>getListaPrenotazioni() {
        return listaPrenotazioni;
    }

    /**
     * Imposta la lista delle prenotazioni.
     *
     * @param listaPrenotazioni the lista prenotazioni
     */
    public void setListaPrenotazioni(List<Volo> listaPrenotazioni) {
        this.listaPrenotazioni = listaPrenotazioni;
    }
    /**
     * Costruttore completo.
     *
     * @param login              the login
     * @param password           the password
     * @param listaPrenotazioni the lista prenotazioni
     */
    public UtenteGenerico(String login, String password, List<Volo> listaPrenotazioni) {
        super(login, password);
        this.listaPrenotazioni = listaPrenotazioni;
    }

    /**
     * Costruttore di default.
     */
    public UtenteGenerico(String login, String password) {
        super(login, password);
    }


}
