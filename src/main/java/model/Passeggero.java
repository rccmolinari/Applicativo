package model;

import java.util.Date;
import java.util.List;

/**
 * Rappresenta un passeggero che effettua una o più prenotazioni.
 * Contiene dati anagrafici e la lista delle sue prenotazioni.
 */
public class Passeggero {

    /** Identificativo univoco del documento del passeggero (es. passaporto, carta d'identità). */
    protected String idDocumento;

    /** Nome del passeggero. */
    protected String nome;

    /** Cognome del passeggero. */
    protected String cognome;

    /** Data di nascita del passeggero. */
    protected Date dataNascita;

    /** Lista delle prenotazioni associate a questo passeggero. */
    protected List<Prenotazione> listaPrenotazioni;

    /**
     * Restituisce l'identificativo del documento.
     *
     * @return id del documento
     */
    public String getIdDocumento() {
        return idDocumento;
    }

    /**
     * Imposta l'identificativo del documento.
     *
     * @param idDocumento identificativo del documento da assegnare
     */
    public void setIdDocumento(String idDocumento) {
        this.idDocumento = idDocumento;
    }

    /**
     * Restituisce il nome del passeggero.
     *
     * @return nome
     */
    public String getNome() {
        return nome;
    }

    /**
     * Imposta il nome del passeggero.
     *
     * @param nome nome da assegnare
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * Restituisce il cognome del passeggero.
     *
     * @return cognome
     */
    public String getCognome() {
        return cognome;
    }

    /**
     * Imposta il cognome del passeggero.
     *
     * @param cognome cognome da assegnare
     */
    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    /**
     * Restituisce la data di nascita del passeggero.
     *
     * @return data di nascita
     */
    public Date getDataNascita() {
        return dataNascita;
    }

    /**
     * Imposta la data di nascita del passeggero.
     *
     * @param dataNascita data da assegnare
     */
    public void setDataNascita(Date dataNascita) {
        this.dataNascita = dataNascita;
    }

    /**
     * Restituisce la lista delle prenotazioni associate al passeggero.
     *
     * @return lista di prenotazioni
     */
    public List<Prenotazione> getListaPrenotazioni() {
        return listaPrenotazioni;
    }

    /**
     * Imposta la lista delle prenotazioni associate al passeggero.
     *
     * @param listaPrenotazioni lista da assegnare
     */
    public void setListaPrenotazioni(List<Prenotazione> listaPrenotazioni) {
        this.listaPrenotazioni = listaPrenotazioni;
    }

    /**
     * Costruttore completo del passeggero.
     *
     * @param idDocumento identificativo del documento
     * @param nome nome del passeggero
     * @param cognome cognome del passeggero
     * @param dataNascita data di nascita
     * @param listaPrenotazioni lista delle prenotazioni
     */
    public Passeggero(String idDocumento, String nome, String cognome, Date dataNascita, List<Prenotazione> listaPrenotazioni) {
        this.idDocumento = idDocumento;
        this.nome = nome;
        this.cognome = cognome;
        this.dataNascita = dataNascita;
        this.listaPrenotazioni = listaPrenotazioni;
    }

    /**
     * Costruttore vuoto.
     */
    public Passeggero() {}
}