package model;

import java.util.Date;
import java.util.ArrayList;

/**
 * The type Passeggero.
 */
public class Passeggero {

    /**
     * The Id documento.
     */
    protected int idDocumento;
    /**
     * The Nome.
     */
    protected String nome;
    /**
     * The Cognome.
     */
    protected String cognome;
    /**
     * The Data nascita.
     */
    protected Date dataNascita;
    /**
     * The Lista prenotazioni.
     */
    protected ArrayList<Prenotazione> listaPrenotazioni;

    /**
     * Gets id documento.
     *
     * @return the id documento
     */
    public int getIdDocumento() {
        return idDocumento;
    }

    /**
     * Sets id documento.
     *
     * @param idDocumento the id documento
     */
    public void setIdDocumento(int idDocumento) {
        this.idDocumento = idDocumento;
    }

    /**
     * Gets nome.
     *
     * @return the nome
     */
    public String getNome() {
        return nome;
    }

    /**
     * Sets nome.
     *
     * @param nome the nome
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * Gets cognome.
     *
     * @return the cognome
     */
    public String getCognome() {
        return cognome;
    }

    /**
     * Sets cognome.
     *
     * @param cognome the cognome
     */
    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    /**
     * Gets data nascita.
     *
     * @return the data nascita
     */
    public Date getDataNascita() {
        return dataNascita;
    }

    /**
     * Sets data nascita.
     *
     * @param dataNascita the data nascita
     */
    public void setDataNascita(Date dataNascita) {
        this.dataNascita = dataNascita;
    }

    /**
     * Gets lista prenotazioni.
     *
     * @return the lista prenotazioni
     */
    public ArrayList<Prenotazione> getListaPrenotazioni() {
        return listaPrenotazioni;
    }

    /**
     * Sets lista prenotazioni.
     *
     * @param listaPrenotazioni the lista prenotazioni
     */
    public void setListaPrenotazioni(ArrayList<Prenotazione> listaPrenotazioni) {
        this.listaPrenotazioni = listaPrenotazioni;
    }

    /**
     * Instantiates a new Passeggero.
     *
     * @param idDocumento       the id documento
     * @param nome               the nome
     * @param cognome            the cognome
     * @param dataNascita       the data nascita
     * @param listaPrenotazioni the lista prenotazioni
     */
    public Passeggero(int idDocumento, String nome, String cognome, Date dataNascita, ArrayList<Prenotazione> listaPrenotazioni) {
        this.idDocumento = idDocumento;
        this.nome = nome;
        this.cognome = cognome;
        this.dataNascita = dataNascita;
        this.listaPrenotazioni = listaPrenotazioni;
    }

    /**
     * Instantiates a new Passeggero.
     */
    public Passeggero() {}
}
