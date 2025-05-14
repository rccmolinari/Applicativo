package model;
import java.util.ArrayList;
import java.util.Date;

/**
 * The type Volo in partenza.
 */
public class Volo_In_Partenza extends Volo {

    /**
     * The Gate.
     */
    protected int gate;

    /**
     * Gets gate.
     *
     * @return the gate
     */
    public int getGate() {
        return gate;
    }

    /**
     * Sets gate.
     *
     * @param gate the gate
     */
    public void setGate(int gate) {
        this.gate = gate;
    }

    /**
     * Instantiates a new Volo in partenza.
     *
     * @param codice_Volo        the codice volo
     * @param compagnia          the compagnia
     * @param data               the data
     * @param orario             the orario
     * @param ritardo            the ritardo
     * @param stato_Volo         the stato volo
     * @param destinazione       the destinazione
     * @param lista_Prenotazioni the lista prenotazioni
     * @param gate               the gate
     */
    public Volo_In_Partenza(int codice_Volo, String compagnia, Date data, String orario, int ritardo, Stato_Volo stato_Volo, String destinazione, ArrayList<Prenotazione> lista_Prenotazioni, int gate) {
        super(codice_Volo, compagnia, data, orario, ritardo, stato_Volo, "Napoli", destinazione, lista_Prenotazioni);
        this.gate = gate;
    }

    /**
     * Instantiates a new Volo in partenza.
     */
    public Volo_In_Partenza() {
    }


}
