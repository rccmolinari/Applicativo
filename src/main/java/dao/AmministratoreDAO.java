package dao;

import model.Bagaglio;
import model.StatoBagaglio;
import model.Volo;
import model.VoloInPartenza;

import java.util.ArrayList;
import java.util.List;

public interface AmministratoreDAO extends UtenteDAO {
    public void inserisciVolo(Volo volo);
    public void aggiornaVolo(Volo volo);

    /**
     * Modifica il gate del volo.
     *
     * @param volo the volo
     */
    public void modificaGate(VoloInPartenza volo);
    public void aggiornaBagaglio(Bagaglio bagaglio, StatoBagaglio stato);
    public ArrayList<Bagaglio> visualizzaSmarrimento();

}
