package model;
import java.sql.Time;
import java.util.Date;
import java.util.List;

/**
 * The type Volo in partenza.
 */
public class VoloInPartenza extends Volo {

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
     * @param codiceVolo        the codice volo
     * @param compagnia          the compagnia
     * @param data               the data
     * @param orario             the orario
     * @param ritardo            the ritardo
     * @param statoVolo         the stato volo
     * @param destinazione       the destinazione
     * @param listaPrenotazioni the lista prenotazioni
     * @param gate               the gate
     */
    public VoloInPartenza(int codiceVolo, String compagnia, Date data, Time orario, int ritardo, StatoVolo statoVolo, String destinazione, List<Prenotazione> listaPrenotazioni, int gate) {
        super(codiceVolo, compagnia, data, orario, ritardo, statoVolo, "NAP", destinazione, listaPrenotazioni);
        this.gate = gate;
    }

    /**
     * Instantiates a new Volo in partenza.
     */
    public VoloInPartenza() {
    }


}
