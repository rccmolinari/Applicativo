package model;

import java.util.ArrayList;
import java.util.Date;
public class Volo_In_Arrivo extends Volo {

    public Volo_In_Arrivo(int codice_Volo, String compagnia, Date data, String orario, int ritardo, Stato_Volo stato_Volo, String origine, ArrayList<Prenotazione> lista_Prenotazioni) {
        super(codice_Volo, compagnia, data, orario, ritardo, stato_Volo, origine, "Napoli", lista_Prenotazioni);
    }

    public Volo_In_Arrivo() {}
}
