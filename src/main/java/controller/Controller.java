package controller;

import model.*;
import java.util.*;

public class Controller {
    private Sistema sistema;

    public Controller(){
        this.sistema = new Sistema();
    }


    public Utente login(String login, String password){
        return sistema.authenticateUser(login, password);
    }

    public List<Volo> visualizzaVoli(){
        return sistema.getVoliDisponibili();
    }

    public void prenotavolo(Utente_Generico utente, Volo volo, Passeggero passeggero){
        utente.prenota_Volo(volo, passeggero);
    }

    public Prenotazione cercaPrenotazionePerNome(Utente_Generico utente, String nome){
        return utente.cerca_Prenotazione(nome);
    }

    public Prenotazione cercaPrenotazionePerCodice(Utente_Generico utente, int codice){
        return utente.cerca_Prenotazione(codice);
    }
    public void segnalaSmarrimento(Utente_Generico utente, Bagaglio bagaglio){
        utente.segnala_Smarrimento(bagaglio);
    }

    public void aggiornavolo(Amministratore amministratore, Volo volo){
        amministratore.aggiorna_Volo(volo);
    }

    public void modificaGate(Amministratore amministratore, Volo_In_Partenza volo){
        amministratore.modifica_Gate(volo);
    }

    public void aggiornaBagaglio(Amministratore amministratore, Bagaglio bagaglio, Stato_Bagaglio stato){
        amministratore.aggiorna_Bagaglio(bagaglio, stato);
    }

    public List<Bagaglio> visualizzaSmarrimenti(Amministratore amministratore){
        return amministratore.visualizza_Smarrimento();
    }


}
