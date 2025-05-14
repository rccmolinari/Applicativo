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
        Utente_Generico u2 = new Utente_Generico();
        Volo volo = new Volo();
        Volo_In_Arrivo v1 = new Volo_In_Arrivo();
        Volo_In_Partenza v2 = new Volo_In_Partenza();
        p.setNome("");
        b.setPrenotazione(null);
        p1.setNumero_Assegnato(0);
        u1.setLogin("");
        a1.setLogin("");
        u2.cerca_Prenotazione(0);
        volo.setCodice_Volo(1);
        v1.setCodice_Volo(1);
        v2.setGate(2);
        System.out.println("Ciao! non ci sono errori");

    }
}
