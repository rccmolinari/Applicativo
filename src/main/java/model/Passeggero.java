package model;

import java.util.Date;

public class Passeggero {
    protected int id_Documento;
    protected String nome;
    protected String cognome;
    protected Date data_Nascita;

    public Passeggero(int id_Documento, String nome, String cognome, Date data_Nascita) {
        this.id_Documento = id_Documento;
        this.nome = nome;
        this.cognome = cognome;
        this.data_Nascita = data_Nascita;
    }

    public Passeggero() {}
}
