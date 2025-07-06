package dao;

import model.*;
import model.Volo;

import java.util.ArrayList;
import java.util.List;

public interface UtenteDAO {

    public ArrayList<Volo> visualizzaVoli();
    public ArrayList<Bagaglio> cercaBagaglio(Prenotazione p, UtenteGenerico u);
    public ArrayList<Bagaglio> cercaBagaglio(Bagaglio b, UtenteGenerico u);

}
