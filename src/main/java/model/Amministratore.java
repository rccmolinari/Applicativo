package model;

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
    public void inserisci_Volo(Volo volo) {}

    /**
     * Con questo metodo l'amministratore può aggiornare un volo esistente.
     *
     * @param volo the volo
     */
    public void aggiorna_Volo(Volo volo) {}

    /**
     * Modifica il gate del volo.
     *
     * @param volo the volo
     */
    public void modifica_Gate(Volo_In_Partenza volo) {}

    /**
     * Aggiorna lo stato di un bagaglio.
     *
     * @param bagaglio the bagaglio
     */
    public void aggiorna_Bagaglio(Bagaglio bagaglio, Stato_Bagaglio stato) {}

    /**
     * Visualizza lo smarrimento di un bagaglio.
     *
     */
    public List<Bagaglio> visualizza_Smarrimento() {
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
