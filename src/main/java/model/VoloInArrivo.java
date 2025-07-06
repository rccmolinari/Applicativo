package model;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * The type Volo in arrivo.
 */
public class VoloInArrivo extends Volo {

    /**
     * Instantiates a new Volo in arrivo.
     *
     * @param codiceVolo        the codice volo
     * @param compagnia          the compagnia
     * @param data               the data
     * @param orario             the orario
     * @param ritardo            the ritardo
     * @param statoVolo         the stato volo
     * @param origine            the origine
     * @param listaPrenotazioni the lista prenotazioni
     */
    public VoloInArrivo(int codiceVolo, String compagnia, Date data, Time orario, int ritardo, StatoVolo statoVolo, String origine, List<Prenotazione> listaPrenotazioni) {
        super(codiceVolo, compagnia, data, orario, ritardo, statoVolo, origine, "NAP", listaPrenotazioni);
    }

    /**
     * Instantiates a new Volo in arrivo.
     */
    public VoloInArrivo() {
    }
}
