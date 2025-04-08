package model;

import java.util.Date;
public class Volo {

    protected int codice_Volo;
    protected String compagnia;
    protected Date data;
    protected String orario_Previsto;
    protected int ritardo = 0;
    protected Stato_Volo stato;
    protected String origine;
    protected String destinazione;

    public Volo(int codice_Volo, String compagnia, Date data, Stato_Volo stato, String origine, String destinazione) {
        this.codice_Volo = codice_Volo;
        this.compagnia = compagnia;
        this.data = data;
        this.stato = stato;
        this.origine = origine;
        this.destinazione = destinazione;
    }

    public Volo() {}

}
