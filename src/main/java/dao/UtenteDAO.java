package dao;

import model.Bagaglio;
import model.Passeggero;
import model.Prenotazione;
import model.Volo;

import java.util.ArrayList;
import java.util.List;

public interface UtenteDAO {

    public ArrayList<Volo> visualizzaVoli();
    public ArrayList<Bagaglio> cercaBagaglio(Prenotazione p);
    public ArrayList<Bagaglio> cercaBagaglio(Bagaglio b);

}
