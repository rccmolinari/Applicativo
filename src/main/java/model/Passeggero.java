package model;

import java.util.Date;
import java.util.ArrayList;

public class Passeggero {

    protected int id_Documento;
    protected String nome;
    protected String cognome;
    protected Date data_Nascita;
    protected ArrayList<Prenotazione> lista_Prenotazioni;

    public Passeggero(int id_Documento, String nome, String cognome, Date data_Nascita, ArrayList<Prenotazione> lista_Prenotazioni) {
        this.id_Documento = id_Documento;
        this.nome = nome;
        this.cognome = cognome;
        this.data_Nascita = data_Nascita;
        this.lista_Prenotazioni = lista_Prenotazioni;
    }

    public Passeggero() {}
}
