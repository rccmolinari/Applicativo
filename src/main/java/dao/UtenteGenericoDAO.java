package dao;

import model.*;
import model.Volo;

import java.sql.SQLException;
import java.util.ArrayList;

public interface UtenteGenericoDAO extends UtenteDAO {

    public ArrayList<Prenotazione> listaPrenotazioni(UtenteGenerico utente);

    public void prenotaVolo(UtenteGenerico ug, Volo volo, Passeggero p, ArrayList<Bagaglio> ab);



    public void modificaPrenotazione(Prenotazione prenotazione, ArrayList<Bagaglio> ab);

    public void segnalaSmarrimento(Bagaglio Bagaglio, UtenteGenerico u) throws SQLException;

    public ArrayList<Prenotazione> cercaPrenotazione(UtenteGenerico utente, String nome, String cognome);

    public ArrayList<Prenotazione> cercaPrenotazione(UtenteGenerico utente, int numeroBiglietto);


}
