package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe rappresentante l'amministratore dell'aereoporto, uno dei possibili utenti del sistema.
 */
public class Amministratore extends Utente {

    /**
     * L'amministratore ha la possibilità di inserire un certo volo.
     *
     * @param volo the volo
     */
    public void inserisciVolo(Volo volo) {}

    /**
     * Con questo metodo l'amministratore può aggiornare un volo esistente.
     *
     * @param volo the volo
     */
    public void aggiornaVolo(Volo volo) {}

    /**
     * Modifica il gate del volo.
     *
     * @param volo the volo
     */
    public void modificaGate(VoloInPartenza volo) {}

    /**
     * Aggiorna lo stato di un bagaglio.
     *
     * @param bagaglio the bagaglio
     */
    public void aggiornaBagaglio(Bagaglio bagaglio, StatoBagaglio stato) {}

    /**
     * Visualizza lo smarrimento di un bagaglio.
     *
     */
    public ArrayList<Bagaglio> visualizzaSmarrimento() {
        return null;
    }

    /**
     * Costruttore di default.
     */
    public Amministratore() {}

    /**
     * Costruttore completo della classe, permette il login dell'Amministratore.
     *
     * @param login    the login
     * @param password the password
     */
    public Amministratore(String login, String password) {
        super(login, password);
    }
}
