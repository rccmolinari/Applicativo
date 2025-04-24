package model;

public class Utente {

    protected String login;
    protected String password;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String visualizza_Voli() {
        return "";
    }

    public Utente() {}

    public Utente(String login, String password) {
        this.login = login;
        this.password = password;
    }


}
