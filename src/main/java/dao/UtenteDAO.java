package dao;

import model.Bagaglio;
import model.Passeggero;
import model.Volo;

import java.util.ArrayList;
import java.util.List;

public interface UtenteDAO {

    public ArrayList<Volo> visualizzaVoli();
    public ArrayList<Bagaglio> cercaBagaglio(Volo v, Passeggero p);
    public ArrayList<Bagaglio> cercaBagaglio(Bagaglio b);

}
