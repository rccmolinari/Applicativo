package model;
import java.util.ArrayList;
public class Prenotazione {

    protected Utente_Generico utente;
    protected Volo volo;
    protected int numero_Biglietto;
    protected int numero_Assegnato;
    protected Stato_Prenotazione stato_Prenotazione;
    protected Passeggero passeggero;
    protected ArrayList<Bagaglio> lista_Bagagli;

    public Prenotazione(Utente_Generico utente, Volo volo, int numero_Assegnato, int numero_Biglietto, Stato_Prenotazione stato_Prenotazione, Passeggero passeggero, ArrayList<Bagaglio> lista_Bagagli) {
        this.utente = utente;
        this.volo = volo;
        this.numero_Assegnato = numero_Assegnato;
        this.numero_Biglietto = numero_Biglietto;
        this.stato_Prenotazione = stato_Prenotazione;
        this.passeggero = passeggero;
        this.lista_Bagagli = lista_Bagagli;
    }

    public Prenotazione() {}

}
