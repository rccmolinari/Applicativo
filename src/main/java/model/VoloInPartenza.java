package model;
import java.sql.Time;
import java.util.Date;
import java.util.List;

/**
 * Estende {@link Volo} per rappresentare un volo in partenza.
 * Imposta automaticamente "NAP" come città di origine.
 */
public class VoloInPartenza extends Volo {

    /** Numero del gate di imbarco assegnato al volo. */
    protected int gate;

    /**
     * Restituisce il numero del gate assegnato al volo in partenza.
     *
     * @return il numero del gate
     */
    public int getGate() {
        return gate;
    }

    /**
     * Imposta il numero del gate per il volo in partenza.
     *
     * @param gate numero del gate da assegnare
     */
    public void setGate(int gate) {
        this.gate = gate;
    }

    /**
     * Costruttore completo per inizializzare un volo in partenza.
     * L'origine è automaticamente impostata su "NAP".
     *
     * @param codiceVolo codice identificativo del volo
     * @param compagnia compagnia aerea
     * @param data data di partenza
     * @param orario orario di partenza
     * @param ritardo minuti di ritardo rispetto all'orario previsto
     * @param statoVolo stato attuale del volo
     * @param destinazione città di destinazione
     * @param listaPrenotazioni lista di prenotazioni associate al volo
     * @param gate numero del gate di imbarco
     */
    public VoloInPartenza(int codiceVolo, String compagnia, Date data, Time orario, int ritardo, StatoVolo statoVolo, String destinazione, List<Prenotazione> listaPrenotazioni, int gate) {
        super(codiceVolo, compagnia, data, orario, ritardo, statoVolo, "NAP", destinazione, listaPrenotazioni);
        this.gate = gate;
    }

    /**
     * Costruttore di default.
     */
    public VoloInPartenza() {
    }
}