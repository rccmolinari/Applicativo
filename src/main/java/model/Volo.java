package model;

import java.sql.Time;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * Rappresenta un volo disponibile per la prenotazione.
 */
public class Volo {

    /**
     * Codice del volo.
     */
    protected int codiceVolo;
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
    protected Time orario;
    /**
     * The Ritardo.
     */
    protected int ritardo;
    /**
     * The Stato.
     */
    protected StatoVolo stato;
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
    protected List<Prenotazione> listaPrenotazioni;

    /**
     * Restituisce il codice identificativo del volo.
     *
     * @return the codice volo
     */
    public int getCodiceVolo() {
        return codiceVolo;
    }

    /**
     * Imposta il codice identificativo del volo.
     *
     * @param codiceVolo the codice volo
     */
    public void setCodiceVolo(int codiceVolo) {
        this.codiceVolo = codiceVolo;
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
    public Time getOrario() {
        return orario;
    }

    /**
     * Imposta l'orario di partenza del volo.
     *
     * @param orario the orario
     */
    public void setOrario(Time orario) {
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
    public StatoVolo getStato() {
        return stato;
    }

    /**
     * Imposta lo stato del volo.
     *
     * @param stato the stato
     */
    public void setStato(StatoVolo stato) {
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
    public List<Prenotazione> getListaPrenotazioni() {
        return listaPrenotazioni;
    }

    /**
     * Imposta la lista delle prenotazioni.
     *
     * @param listaPrenotazioni the lista prenotazioni
     */
    public void setListaPrenotazioni(List<Prenotazione> listaPrenotazioni) {
        this.listaPrenotazioni = listaPrenotazioni;
    }

    /**
     * Costruttore completo per inizializzare tutti i campi del volo.
     *
     * @param codiceVolo        the codice volo
     * @param compagnia          the compagnia
     * @param data               the data
     * @param orario             the orario
     * @param ritardo            the ritardo
     * @param stato              the stato
     * @param origine            the origine
     * @param destinazione       the destinazione
     * @param listaPrenotazioni the lista prenotazioni
     */
    public Volo(int codiceVolo, String compagnia, Date data, Time orario, int ritardo, StatoVolo stato, String origine, String destinazione, List<Prenotazione> listaPrenotazioni) {
        this.codiceVolo = codiceVolo;
        this.compagnia = compagnia;
        this.data = data;
        this.orario = orario;
        this.ritardo = ritardo;
        this.stato = stato;
        this.origine = origine;
        this.destinazione = destinazione;
        this.listaPrenotazioni = listaPrenotazioni;
    }

    /**
     * Costruttore di default.
     */
    public Volo() {}

}
