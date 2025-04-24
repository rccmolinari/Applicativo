package model;

import java.util.Date;
import java.util.ArrayList;

public class Passeggero {

    protected int id_Documento;
    protected String nome;
    protected String cognome;
    protected Date data_Nascita;
    protected ArrayList<Prenotazione> lista_Prenotazioni;

    public int getId_Documento() {
        return id_Documento;
    }

    public void setId_Documento(int id_Documento) {
        this.id_Documento = id_Documento;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public Date getData_Nascita() {
        return data_Nascita;
    }

    public void setData_Nascita(Date data_Nascita) {
        this.data_Nascita = data_Nascita;
    }

    public ArrayList<Prenotazione> getLista_Prenotazioni() {
        return lista_Prenotazioni;
    }

    public void setLista_Prenotazioni(ArrayList<Prenotazione> lista_Prenotazioni) {
        this.lista_Prenotazioni = lista_Prenotazioni;
    }

    public Passeggero(int id_Documento, String nome, String cognome, Date data_Nascita, ArrayList<Prenotazione> lista_Prenotazioni) {
        this.id_Documento = id_Documento;
        this.nome = nome;
        this.cognome = cognome;
        this.data_Nascita = data_Nascita;
        this.lista_Prenotazioni = lista_Prenotazioni;
    }

    public Passeggero() {}
}
