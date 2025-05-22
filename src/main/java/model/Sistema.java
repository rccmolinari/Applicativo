package model;

import java.util.ArrayList;
import java.util.List;

public class Sistema {
    private List<Utente> utenti;
    private List<Volo> volo;
    private List<Prenotazione> prenotazioni;
    private List<Bagaglio> bagagliSmarriti;

    public Sistema() {
        this.utenti = new ArrayList<>();
        this.volo = new ArrayList<>();
        this.prenotazioni = new ArrayList<>();
        this.bagagliSmarriti = new ArrayList<>();

    }

    public Utente authenticateUser(String email, String password) {
       if (email.equals("admin") && password.equals("admin")) {
           return new Amministratore(email, password);
       } else if (email.equals("user") && password.equals("user")) {
           return new Utente(email, password);
       }
       return null;
    }


    public Utente getuserbylogin(String login, String password) {
        for(Utente utente : utenti) {
            if (utente.getLogin().equals(login) && utente.getPassword().equals(password)) {
                return utente;
            }
        }
        return null;
    }

    public List<Volo> getVoliDisponibili() {
        return null;
    }


}
