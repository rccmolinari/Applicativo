package model;

import java.util.Date;
import java.util.ArrayList;

/**
 * Rappresenta un volo disponibile per la prenotazione.
 */
public class Volo {

    /**
     * Codice del volo.
     */
    protected int codice_Volo;
    /**
     * Compagnia aerea.
     */
    protected String compagnia;
    /**
     * Data
     */
    protected Date data;
    /**
     * The Orario.
     */
    protected String orario;
    /**
     * The Ritardo.
     */
    protected int ritardo;
    /**
     * The Stato.
     */
    protected Stato_Volo stato;
    /**
     * The Origine.
     */
    protected String origine;
    /**
     * The Destinazione.
     */
    protected String destinazione;
    /**
     * The Lista prenotazioni.
     */
    protected ArrayList<Prenotazione> lista_Prenotazioni;

    /**
     * Restituisce il codice identificativo del volo.
     *
     * @return the codice volo
     */
    public int getCodice_Volo() {
        return codice_Volo;
    }

    /**
     * Imposta il codice identificativo del volo.
     *
     * @param codice_Volo the codice volo
     */
    public void setCodice_Volo(int codice_Volo) {
        this.codice_Volo = codice_Volo;
    }

    /**
     * Restituice la compagnia aerea.
     *
     * @return the compagnia
     */
    public String getCompagnia() {
        return compagnia;
    }

    /**
     * Imposta la compagnia aerea.
     *
     * @param compagnia the compagnia
     */
    public void setCompagnia(String compagnia) {
        this.compagnia = compagnia;
    }

    /**
     * Restituisce la data del volo.
     *
     * @return the data
     */
    public Date getData() {
        return data;
    }

    /**
     * Imposta la data del volo.
     *
     * @param data the data
     */
    public void setData(Date data) {
        this.data = data;
    }

    /**
     * Restituisce l'orario di partenza del volo.
     *
     * @return the orario
     */
    public String getOrario() {
        return orario;
    }

    /**
     * Imposta l'orario di partenza del volo.
     *
     * @param orario the orario
     */
    public void setOrario(String orario) {
        this.orario = orario;
    }

    /**
     * Restituisce l'eventuale ritardo del volo.
     *
     * @return the ritardo
     */
    public int getRitardo() {
        return ritardo;
    }

    /**
     * Imposta l'eventuale ritardo del volo.
     *
     * @param ritardo the ritardo
     */
    public void setRitardo(int ritardo) {
        this.ritardo = ritardo;
    }

    /**
     * Restituisce lo stato del volo.
     *
     * @return the stato
     */
    public Stato_Volo getStato() {
        return stato;
    }

    /**
     * Imposta lo stato del volo.
     *
     * @param stato the stato
     */
    public void setStato(Stato_Volo stato) {
        this.stato = stato;
    }

    /**
     * Restituisce la città di origine.
     *
     * @return the origine
     */
    public String getOrigine() {
        return origine;
    }

    /**
     * Imposta la città di origine.
     *
     * @param origine the origine
     */
    public void setOrigine(String origine) {
        this.origine = origine;
    }

    /**
     * Restituisce la città di destinazione.
     *
     * @return the destinazione
     */
    public String getDestinazione() {
        return destinazione;
    }

    /**
     * Imposta la città di destinazione.
     *
     * @param destinazione the destinazione
     */
    public void setDestinazione(String destinazione) {
        this.destinazione = destinazione;
    }

    /**
     * Restituisce la lista delle prenotazioni.
     *
     * @return the lista prenotazioni
     */
    public ArrayList<Prenotazione> getLista_Prenotazioni() {
        return lista_Prenotazioni;
    }

    /**
     * Imposta la lista delle prenotazioni.
     *
     * @param lista_Prenotazioni the lista prenotazioni
     */
    public void setLista_Prenotazioni(ArrayList<Prenotazione> lista_Prenotazioni) {
        this.lista_Prenotazioni = lista_Prenotazioni;
    }

    /**
     * Costruttore completo per inizializzare tutti i campi del volo.
     *
     * @param codice_Volo        the codice volo
     * @param compagnia          the compagnia
     * @param data               the data
     * @param orario             the orario
     * @param ritardo            the ritardo
     * @param stato              the stato
     * @param origine            the origine
     * @param destinazione       the destinazione
     * @param lista_Prenotazioni the lista prenotazioni
     */
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

    /**
     * Costruttore di default.
     */
    public Volo() {}

}
