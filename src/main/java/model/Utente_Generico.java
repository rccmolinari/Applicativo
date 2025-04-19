package model;
import java.util.ArrayList;

public class Utente_Generico extends Utente {

    protected ArrayList<Volo> lista_Prenotazioni;

    public ArrayList<Volo> getLista_Prenotazioni() {
        return lista_Prenotazioni;
    }

    public void setLista_Prenotazioni(ArrayList<Volo> lista_Prenotazioni) {
        this.lista_Prenotazioni = lista_Prenotazioni;
    }

    public void prenota_Volo(Volo volo) {}

    public void cerca_Prenotazione(String nome_Passeggero) {}

    public void cerca_Prenotazione(int codice_Volo) {}

    public void segnala_Smarrimento(Bagaglio Bagaglio) {}


    public Utente_Generico(String login, String password, ArrayList<Volo> lista_Prenotazioni) {
        super(login, password);
        this.lista_Prenotazioni = lista_Prenotazioni;
    }

    public Utente_Generico() {
    }


}
