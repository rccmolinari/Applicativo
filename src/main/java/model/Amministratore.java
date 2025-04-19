package model;

public class Amministratore extends Utente {

    public void inserisci_Volo(Volo volo) {}
    public void aggiorna_Volo(Volo volo) {}
    public void modifica_Gate(Volo volo) {}
    public void aggiorna_Bagaglio(Bagaglio bagaglio) {}
    public void visualizza_Smarrimento(Bagaglio bagaglio) {}

    public Amministratore() {}

    public Amministratore(String login, String password) {
        super(login, password);
    }
}
