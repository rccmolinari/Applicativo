package dao;

import model.*;
import model.Volo;

import java.util.ArrayList;
import java.util.List;

public interface UtenteDAO {

    ArrayList<Volo> visualizzaVoli();
    ArrayList<Bagaglio> cercaBagaglio(Prenotazione p, UtenteGenerico u);
    ArrayList<Bagaglio> cercaBagaglio(Bagaglio b, UtenteGenerico u);

}
