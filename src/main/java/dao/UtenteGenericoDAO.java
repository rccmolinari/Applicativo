package dao;

import model.*;
import model.Volo;

import java.sql.SQLException;
import java.util.ArrayList;

public interface UtenteGenericoDAO extends UtenteDAO {

    ArrayList<Prenotazione> listaPrenotazioni(UtenteGenerico utente);

    void prenotaVolo(UtenteGenerico ug, Volo volo, Passeggero p, ArrayList<Bagaglio> ab);



    void modificaPrenotazione(Prenotazione prenotazione, ArrayList<Bagaglio> ab);

    void segnalaSmarrimento(Bagaglio Bagaglio, UtenteGenerico u) throws SQLException;

    ArrayList<Prenotazione> cercaPrenotazione(UtenteGenerico utente, String nome, String cognome);

    ArrayList<Prenotazione> cercaPrenotazione(UtenteGenerico utente, int numeroBiglietto);


}
