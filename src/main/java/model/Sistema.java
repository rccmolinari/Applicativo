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

    public boolean authenticateUser(String email, String password) {
        for (Utente utente : utenti)
            if (utente.getLogin().equals(email) && utente.getPassword().equals(password)) {
                return true;
            }
        return false;
    }
}
