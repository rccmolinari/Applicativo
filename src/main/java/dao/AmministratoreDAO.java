package dao;

import model.*;

import java.util.ArrayList;

public interface AmministratoreDAO extends UtenteDAO {
    void inserisciVolo(Volo volo);
    void aggiornaVolo(Volo volo);

    /**
     * Modifica il gate del volo.
     *
     * @param volo the volo
     */
    void modificaGate(VoloInPartenza volo);
    void aggiornaBagaglio(Bagaglio bagaglio, StatoBagaglio stato);
    ArrayList<Bagaglio> visualizzaSmarrimento();
    ArrayList<Prenotazione> cercaPasseggero(Passeggero passeggero);

}
