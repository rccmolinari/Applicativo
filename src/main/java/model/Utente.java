package model;

/**
 * Rappresenta un utente base del sistema.
 * Contiene credenziali di accesso e funzionalità comuni a tutti gli utenti.
 */
public class Utente {

    /** Login dell'utente (username). */
    protected String login;

    /** Password associata all'utente. */
    protected String password;

    /**
     * Restituisce il login dell'utente.
     *
     * @return stringa contenente il login (username)
     */
    public String getLogin() {
        return login;
    }

    /**
     * Imposta il login dell'utente.
     *
     * @param login nuovo valore per il login
     */
    public void setLogin(String login) {
        this.login = login;
    }

    /**
     * Restituisce la password dell'utente.
     *
     * @return stringa contenente la password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Imposta la password dell'utente.
     *
     * @param password nuova password da assegnare
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Costruttore vuoto dell'utente.
     * Utile per framework di serializzazione/deserializzazione.
     */
    public Utente() {}

    /**
     * Costruttore completo dell'utente.
     *
     * @param login    nome utente per l'accesso
     * @param password password associata all'account
     */
    public Utente(String login, String password) {
        this.login = login;
        this.password = password;
    }
}
