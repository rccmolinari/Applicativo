package dao;

import model.Bagaglio;
import model.StatoBagaglio;
import model.Volo;
import model.VoloInPartenza;

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

}
