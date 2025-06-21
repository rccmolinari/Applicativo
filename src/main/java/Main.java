import model.*;

/**
 * The type Main.
 */
public class Main {
    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        Passeggero p = new Passeggero();
        Bagaglio b = new Bagaglio();
        Prenotazione p1 = new Prenotazione();
        Utente u1 = new Utente();
        Amministratore a1 = new Amministratore();
        UtenteGenerico u2 = new UtenteGenerico();
        Volo volo = new Volo();
        VoloInArrivo v1 = new VoloInArrivo();
        VoloInPartenza v2 = new VoloInPartenza();
        p.setNome("");
        b.setPrenotazione(null);
        p1.setNumeroAssegnato(0);
        u1.setLogin("");
        a1.setLogin("");
        u2.cercaPrenotazione(0);
        volo.setCodiceVolo(1);
        v1.setCodiceVolo(1);
        v2.setGate(2);
        System.out.println("Ciao! non ci sono errori");

    }
}
