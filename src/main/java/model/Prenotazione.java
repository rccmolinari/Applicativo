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

    public Volo getVolo() {
        return volo;
    }

    public void setVolo(Volo volo) {
        this.volo = volo;
    }

    public Utente_Generico getUtente() {
        return utente;
    }

    public void setUtente(Utente_Generico utente) {
        this.utente = utente;
    }

    public int getNumero_Biglietto() {
        return numero_Biglietto;
    }

    public void setNumero_Biglietto(int numero_Biglietto) {
        this.numero_Biglietto = numero_Biglietto;
    }

    public int getNumero_Assegnato() {
        return numero_Assegnato;
    }

    public void setNumero_Assegnato(int numero_Assegnato) {
        this.numero_Assegnato = numero_Assegnato;
    }

    public Stato_Prenotazione getStato_Prenotazione() {
        return stato_Prenotazione;
    }

    public void setStato_Prenotazione(Stato_Prenotazione stato_Prenotazione) {
        this.stato_Prenotazione = stato_Prenotazione;
    }

    public Passeggero getPasseggero() {
        return passeggero;
    }

    public void setPasseggero(Passeggero passeggero) {
        this.passeggero = passeggero;
    }

    public ArrayList<Bagaglio> getLista_Bagagli() {
        return lista_Bagagli;
    }

    public void setLista_Bagagli(ArrayList<Bagaglio> lista_Bagagli) {
        this.lista_Bagagli = lista_Bagagli;
    }

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
