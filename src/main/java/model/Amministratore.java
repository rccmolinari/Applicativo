package model;


/**
 * Classe rappresentante l'amministratore dell'aereoporto, uno dei possibili utenti del sistema.
 */
public class Amministratore extends Utente {

    public Amministratore() {}

    /**
     * Costruttore completo della classe, permette il login dell'Amministratore.
     *
     * @param login email/username dell'amministratore
     * @param password password
     */
    public Amministratore(String login, String password) {
        super(login, password);
    }
}
