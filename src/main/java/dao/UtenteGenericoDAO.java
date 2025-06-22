package dao;

import model.*;
import model.Volo;

import java.util.ArrayList;

public interface UtenteGenericoDAO {
    public ArrayList<Prenotazione> ListaPrenotazioni(UtenteGenerico utente);

    public void prenotaVolo(UtenteGenerico ug, Volo volo, Passeggero p, ArrayList<Bagaglio> ab);



    public ArrayList<Prenotazione> cercaPrenotazione(UtenteGenerico utente, String nomePasseggero, String cognomePasseggero);

    public ArrayList<Prenotazione> cercaPrenotazione(UtenteGenerico utente, int codiceVolo);


    public void segnalaSmarrimento(Bagaglio Bagaglio);


}
