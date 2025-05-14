package model;

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
    public void modifica_Gate(Volo volo) {}

    /**
     * Aggiorna lo stato di un bagaglio.
     *
     * @param bagaglio the bagaglio
     */
    public void aggiorna_Bagaglio(Bagaglio bagaglio) {}

    /**
     * Visualizza lo smarrimento di un bagaglio.
     *
     * @param bagaglio the bagaglio
     */
    public void visualizza_Smarrimento(Bagaglio bagaglio) {}

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
