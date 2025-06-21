package dao;

import model.Bagaglio;
import model.Passeggero;
import model.Prenotazione;
import model.Volo;

import java.util.ArrayList;
import java.util.List;

public interface UtenteGenerico {
    public ArrayList<Volo> getLista_Prenotazioni();

    public void prenota_Volo(Volo volo, Passeggero p);

    public Prenotazione cerca_Prenotazione(String nome_Passeggero);

    public Prenotazione cerca_Prenotazione(int codice_Volo);


    public void segnala_Smarrimento(Bagaglio Bagaglio);


}
