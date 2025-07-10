package model;

import java.sql.Time;
import java.util.Date;
import java.util.List;

/**
 * Estende {@link Volo} per rappresentare un volo in arrivo.
 * Imposta automaticamente "NAP" come città di destinazione.
 */
public class VoloInArrivo extends Volo {

    /**
     * Costruttore completo per inizializzare un volo in arrivo.
     * La destinazione è automaticamente impostata su "NAP".
     *
     * @param codiceVolo codice identificativo del volo
     * @param compagnia nome della compagnia aerea
     * @param data data del volo
     * @param orario orario di partenza
     * @param ritardo minuti di ritardo
     * @param statoVolo stato attuale del volo
     * @param origine città di origine
     * @param listaPrenotazioni lista di prenotazioni associate
     */
    public VoloInArrivo(int codiceVolo, String compagnia, Date data, Time orario, int ritardo, StatoVolo statoVolo, String origine, List<Prenotazione> listaPrenotazioni) {
        super(codiceVolo, compagnia, data, orario, ritardo, statoVolo, origine, "NAP", listaPrenotazioni);
    }

    /**
     * Costruttore di default.
     */
    public VoloInArrivo() {
    }
}