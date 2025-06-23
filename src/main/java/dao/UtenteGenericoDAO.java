package dao;

import model.*;
import model.Volo;

import java.util.ArrayList;

public interface UtenteGenericoDAO extends UtenteDAO {

    public ArrayList<Prenotazione> listaPrenotazioni(UtenteGenerico utente);

    public void prenotaVolo(UtenteGenerico ug, Volo volo, Passeggero p, ArrayList<Bagaglio> ab);

    public ArrayList<Bagaglio> cercaBagaglio(Volo v, Passeggero p);

    public ArrayList<Bagaglio> cercaBagaglio(Bagaglio b);


    public void modificaPrenotazione(Prenotazione prenotazione, ArrayList<Bagaglio> ab);

    public void segnalaSmarrimento(Bagaglio Bagaglio);


}
