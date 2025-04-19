package model;

import java.util.Date;
import java.util.ArrayList;
public class Volo {

    protected int codice_Volo;
    protected String compagnia;
    protected Date data;
    protected String orario;
    protected int ritardo;
    protected Stato_Volo stato;
    protected String origine;
    protected String destinazione;
    protected ArrayList<Prenotazione> lista_Prenotazioni;

    public int getCodice_Volo() {
        return codice_Volo;
    }

    public void setCodice_Volo(int codice_Volo) {
        this.codice_Volo = codice_Volo;
    }

    public String getCompagnia() {
        return compagnia;
    }

    public void setCompagnia(String compagnia) {
        this.compagnia = compagnia;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public String getOrario() {
        return orario;
    }

    public void setOrario(String orario) {
        this.orario = orario;
    }

    public int getRitardo() {
        return ritardo;
    }

    public void setRitardo(int ritardo) {
        this.ritardo = ritardo;
    }

    public Stato_Volo getStato() {
        return stato;
    }

    public void setStato(Stato_Volo stato) {
        this.stato = stato;
    }

    public String getOrigine() {
        return origine;
    }

    public void setOrigine(String origine) {
        this.origine = origine;
    }

    public String getDestinazione() {
        return destinazione;
    }

    public void setDestinazione(String destinazione) {
        this.destinazione = destinazione;
    }

    public ArrayList<Prenotazione> getLista_Prenotazioni() {
        return lista_Prenotazioni;
    }

    public void setLista_Prenotazioni(ArrayList<Prenotazione> lista_Prenotazioni) {
        this.lista_Prenotazioni = lista_Prenotazioni;
    }

    public Volo(int codice_Volo, String compagnia, Date data, String orario, int ritardo, Stato_Volo stato, String origine, String destinazione, ArrayList<Prenotazione> lista_Prenotazioni) {
        this.codice_Volo = codice_Volo;
        this.compagnia = compagnia;
        this.data = data;
        this.orario = orario;
        this.ritardo = ritardo;
        this.stato = stato;
        this.origine = origine;
        this.destinazione = destinazione;
        this.lista_Prenotazioni = lista_Prenotazioni;
    }

    public Volo() {}

}
