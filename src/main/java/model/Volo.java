package model;

import java.sql.Time;
import java.util.Date;
import java.util.List;

/**
 * Rappresenta un volo disponibile per la prenotazione.
 * Contiene informazioni dettagliate come compagnia, orario, stato e prenotazioni associate.
 */
public class Volo {

    /** Codice del volo. */
    protected int codiceVolo;

    /** Compagnia aerea. */
    protected String compagnia;

    /** Data del volo. */
    protected Date data;

    /** Orario di partenza del volo. */
    protected Time orario;

    /** Eventuale ritardo in minuti. */
    protected int ritardo;

    /** Stato corrente del volo. */
    protected StatoVolo stato;

    /** Aeroporto o città di origine. */
    protected String origine;

    /** Aeroporto o città di destinazione. */
    protected String destinazione;

    /** Lista di prenotazioni associate al volo. */
    protected List<Prenotazione> listaPrenotazioni;

    /**
     * Restituisce il codice identificativo del volo.
     *
     * @return codice volo
     */
    public int getCodiceVolo() {
        return codiceVolo;
    }

    /**
     * Imposta il codice identificativo del volo.
     *
     * @param codiceVolo codice da assegnare
     */
    public void setCodiceVolo(int codiceVolo) {
        this.codiceVolo = codiceVolo;
    }

    /**
     * Restituisce la compagnia aerea.
     *
     * @return nome della compagnia
     */
    public String getCompagnia() {
        return compagnia;
    }

    /**
     * Imposta la compagnia aerea.
     *
     * @param compagnia nome della compagnia
     */
    public void setCompagnia(String compagnia) {
        this.compagnia = compagnia;
    }

    /**
     * Restituisce la data del volo.
     *
     * @return data del volo
     */
    public Date getData() {
        return data;
    }

    /**
     * Imposta la data del volo.
     *
     * @param data data da assegnare
     */
    public void setData(Date data) {
        this.data = data;
    }

    /**
     * Restituisce l'orario di partenza.
     *
     * @return orario del volo
     */
    public Time getOrario() {
        return orario;
    }

    /**
     * Imposta l'orario di partenza del volo.
     *
     * @param orario orario da assegnare
     */
    public void setOrario(Time orario) {
        this.orario = orario;
    }

    /**
     * Restituisce l'entità del ritardo del volo.
     *
     * @return minuti di ritardo
     */
    public int getRitardo() {
        return ritardo;
    }

    /**
     * Imposta l'entità del ritardo del volo.
     *
     * @param ritardo minuti di ritardo
     */
    public void setRitardo(int ritardo) {
        this.ritardo = ritardo;
    }

    /**
     * Restituisce lo stato attuale del volo.
     *
     * @return oggetto {@code StatoVolo}
     */
    public StatoVolo getStato() {
        return stato;
    }

    /**
     * Imposta lo stato attuale del volo.
     *
     * @param stato stato da assegnare
     */
    public void setStato(StatoVolo stato) {
        this.stato = stato;
    }

    /**
     * Restituisce la città di origine.
     *
     * @return origine del volo
     */
    public String getOrigine() {
        return origine;
    }

    /**
     * Imposta la città di origine.
     *
     * @param origine origine da assegnare
     */
    public void setOrigine(String origine) {
        this.origine = origine;
    }

    /**
     * Restituisce la città di destinazione.
     *
     * @return destinazione del volo
     */
    public String getDestinazione() {
        return destinazione;
    }

    /**
     * Imposta la città di destinazione.
     *
     * @param destinazione destinazione da assegnare
     */
    public void setDestinazione(String destinazione) {
        this.destinazione = destinazione;
    }

    /**
     * Restituisce la lista delle prenotazioni associate al volo.
     *
     * @return lista di {@code Prenotazione}
     */
    public List<Prenotazione> getListaPrenotazioni() {
        return listaPrenotazioni;
    }

    /**
     * Imposta la lista delle prenotazioni associate al volo.
     *
     * @param listaPrenotazioni lista da associare
     */
    public void setListaPrenotazioni(List<Prenotazione> listaPrenotazioni) {
        this.listaPrenotazioni = listaPrenotazioni;
    }

    /**
     * Costruttore completo del volo.
     *
     * @param codiceVolo codice identificativo
     * @param compagnia nome compagnia
     * @param data data del volo
     * @param orario orario di partenza
     * @param ritardo eventuale ritardo in minuti
     * @param stato stato del volo
     * @param origine città di origine
     * @param destinazione città di destinazione
     * @param listaPrenotazioni lista delle prenotazioni
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
