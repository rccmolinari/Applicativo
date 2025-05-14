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
    protected int id_Documento;
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
    protected Date data_Nascita;
    /**
     * The Lista prenotazioni.
     */
    protected ArrayList<Prenotazione> lista_Prenotazioni;

    /**
     * Gets id documento.
     *
     * @return the id documento
     */
    public int getId_Documento() {
        return id_Documento;
    }

    /**
     * Sets id documento.
     *
     * @param id_Documento the id documento
     */
    public void setId_Documento(int id_Documento) {
        this.id_Documento = id_Documento;
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
    public Date getData_Nascita() {
        return data_Nascita;
    }

    /**
     * Sets data nascita.
     *
     * @param data_Nascita the data nascita
     */
    public void setData_Nascita(Date data_Nascita) {
        this.data_Nascita = data_Nascita;
    }

    /**
     * Gets lista prenotazioni.
     *
     * @return the lista prenotazioni
     */
    public ArrayList<Prenotazione> getLista_Prenotazioni() {
        return lista_Prenotazioni;
    }

    /**
     * Sets lista prenotazioni.
     *
     * @param lista_Prenotazioni the lista prenotazioni
     */
    public void setLista_Prenotazioni(ArrayList<Prenotazione> lista_Prenotazioni) {
        this.lista_Prenotazioni = lista_Prenotazioni;
    }

    /**
     * Instantiates a new Passeggero.
     *
     * @param id_Documento       the id documento
     * @param nome               the nome
     * @param cognome            the cognome
     * @param data_Nascita       the data nascita
     * @param lista_Prenotazioni the lista prenotazioni
     */
    public Passeggero(int id_Documento, String nome, String cognome, Date data_Nascita, ArrayList<Prenotazione> lista_Prenotazioni) {
        this.id_Documento = id_Documento;
        this.nome = nome;
        this.cognome = cognome;
        this.data_Nascita = data_Nascita;
        this.lista_Prenotazioni = lista_Prenotazioni;
    }

    /**
     * Instantiates a new Passeggero.
     */
    public Passeggero() {}
}
