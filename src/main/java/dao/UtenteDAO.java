package dao;

import model.*;
import model.Volo;

import java.util.ArrayList;

public interface UtenteDAO {

    ArrayList<Volo> visualizzaVoli();
    ArrayList<Bagaglio> cercaBagaglio(Prenotazione p, UtenteGenerico u);
    ArrayList<Bagaglio> cercaBagaglio(Bagaglio b, UtenteGenerico u);
    ArrayList<Volo>  cercaVolo(Volo v);

}
