package model;

import java.util.ArrayList;
import java.util.Date;

/**
 * The type Volo in arrivo.
 */
public class Volo_In_Arrivo extends Volo {

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
    public Volo_In_Arrivo(int codice_Volo, String compagnia, Date data, String orario, int ritardo, Stato_Volo stato_Volo, String origine, ArrayList<Prenotazione> lista_Prenotazioni) {
        super(codice_Volo, compagnia, data, orario, ritardo, stato_Volo, origine, "Napoli", lista_Prenotazioni);
    }

    /**
     * Instantiates a new Volo in arrivo.
     */
    public Volo_In_Arrivo() {
    }
}
