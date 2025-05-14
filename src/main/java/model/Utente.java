package model;

/**
 * Rappresenta un utente base del sistema.
 * Contiene credenziali di accesso e funzionalità comuni a tutti gli utenti.
 */
public class Utente {

    /**
     * Login dell'utente (username).
     */
    protected String login;
    /**
     * Password associata all'utente
     */
    protected String password;

    /**
     * Restituisce il login dell'utente.
     *
     * @return the login
     */
    public String getLogin() {
        return login;
    }

    /**
     * Imposta il login dell'utente.
     *
     * @param login the login
     */
    public void setLogin(String login) {
        this.login = login;
    }

    /**
     * Restituisce la password dell'utente.
     *
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Imposta la password dell'utente.
     *
     * @param password the password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Metodo per visualizzare i voli disponibili.
     *
     * @return the string
     */
    public String visualizza_Voli() {
        return "";
    }

    /**
     * Costruttore di default.
     */
    public Utente() {}

    /**
     * Costruttore con parametri.
     *
     * @param login    the login
     * @param password the password
     */
    public Utente(String login, String password) {
        this.login = login;
        this.password = password;
    }


}
