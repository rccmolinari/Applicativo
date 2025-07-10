package model;
import java.util.ArrayList;
import java.util.List;

/**
 * Estende {@link Utente} per rappresentare un utente generico che può effettuare prenotazioni di voli ed effetuare operazioni derivate,
 * quali ricerca bagagli, ricerca prenotazioni ecc.
 * Questo tipo di utente ha accesso alle funzionalità standard e possiede una lista di voli prenotati.
 */
public class UtenteGenerico extends Utente {

    /** Lista dei voli prenotati dall'utente. */
    protected List<Prenotazione> listaPrenotazioni;

    /**
     * Restituisce la lista delle prenotazioni dell'utente.
     *
     * @return lista di oggetti {@code Volo} prenotati
     */
    public List<Prenotazione> getListaPrenotazioni() {
        return listaPrenotazioni;
    }

    /**
     * Imposta la lista delle prenotazioni dell'utente.
     *
     * @param listaPrenotazioni lista di voli da assegnare
     */
    public void setListaPrenotazioni(List<Prenotazione> listaPrenotazioni) {
        this.listaPrenotazioni = listaPrenotazioni;
    }

    /**
     * Costruttore completo.
     *
     * @param login nome utente
     * @param password password dell'utente
     * @param listaPrenotazioni lista dei voli prenotati
     */
    public UtenteGenerico(String login, String password, List<Prenotazione> listaPrenotazioni) {
        super(login, password);
        this.listaPrenotazioni = listaPrenotazioni;
    }

    /**
     * Costruttore con solo credenziali, utile per login o registrazione iniziale.
     *
     * @param login nome utente
     * @param password password dell'utente
     */
    public UtenteGenerico(String login, String password) {
        super(login, password);
    }
}