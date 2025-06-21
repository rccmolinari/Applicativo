package model;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;

/**
 * The type Volo in arrivo.
 */
public class VoloInArrivo extends Volo {

    /**
     * Instantiates a new Volo in arrivo.
     *
     * @param codice_Volo        the codice volo
     * @param compagnia          the compagnia
     * @param data               the data
     * @param orario             the orario
     * @param ritardo            the ritardo
     * @param stato_Volo         the stato volo
     * @param origine            the origine
     * @param lista_Prenotazioni the lista prenotazioni
     */
    public VoloInArrivo(int codice_Volo, String compagnia, Date data, Time orario, int ritardo, StatoVolo stato_Volo, String origine, ArrayList<Prenotazione> lista_Prenotazioni) {
        super(codice_Volo, compagnia, data, orario, ritardo, stato_Volo, origine, "NAP", lista_Prenotazioni);
    }

    /**
     * Instantiates a new Volo in arrivo.
     */
    public VoloInArrivo() {
    }
}
