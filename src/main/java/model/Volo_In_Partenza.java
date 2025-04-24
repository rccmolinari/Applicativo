package model;
import java.util.ArrayList;
import java.util.Date;
public class Volo_In_Partenza extends Volo {

    protected int gate;

    public int getGate() {
        return gate;
    }

    public void setGate(int gate) {
        this.gate = gate;
    }

    public Volo_In_Partenza(int codice_Volo, String compagnia, Date data, String orario, int ritardo, Stato_Volo stato_Volo, String destinazione, ArrayList<Prenotazione> lista_Prenotazioni, int gate) {
        super(codice_Volo, compagnia, data, orario, ritardo, stato_Volo, "Napoli", destinazione, lista_Prenotazioni);
        this.gate = gate;
    }
    public Volo_In_Partenza() {
    }

    public Volo_In_Partenza() {}

}
