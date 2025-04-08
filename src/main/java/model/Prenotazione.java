package model;
public class Prenotazione {

    protected int numero_Biglietto;
    protected int numero_Assegnato;
    protected Stato_Prenotazione stato_Prenotazione;

    public Prenotazione(int numero_Assegnato, int numero_Biglietto, Stato_Prenotazione stato_Prenotazione) {
        this.numero_Assegnato = numero_Assegnato;
        this.numero_Biglietto = numero_Biglietto;
        this.stato_Prenotazione = stato_Prenotazione;
    }

    public Prenotazione() {}

}
