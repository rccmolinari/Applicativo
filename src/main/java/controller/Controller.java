package controller;

import model.*;

import java.awt.*;
import java.util.*;
import gui.*;

import javax.swing.*;

public class Controller {
    //private Sistema sistema;
    private Utente user;
    private Amministratore admin;
    private Utente_Generico utente;

    public void login(String login, String password){
        if (login.equals("admin") && password.equals("admin")) {
            this.admin =  new Amministratore(login, password);
        } else {
            this.user =  new Utente(login, password);
        }
    }

    public void selectedItem(String item, DashBoardUser d) {
        String selected = (String) d.getComboBox1().getSelectedItem();
        JPanel panel = new JPanel(); // inizializzazione base
        JLabel label = new JLabel(selected + " work in progress king...");
        label.setHorizontalAlignment(SwingConstants.CENTER);

        // In base alla scelta, creo il pannello da mostrare
        if (item.equals("Visualizza Voli")) {
            return;
        } else if (item.equals("Prenota Volo")) {
            panel.setLayout(new GridLayout(6, 1, 5, 5)); // 5 JTextField + 1 JButton = 6 righe
            panel.add(new JTextField("Id_Documento"));
            panel.add(new JTextField("Data_Nascita"));
            panel.add(new JTextField("Nome"));
            panel.add(new JTextField("Cognome"));
            panel.add(new JTextField("Volo"));
            panel.add(new JButton("Prenota"));
        } else if (item.equals("Cerca Prenotazione")) {
            panel.setLayout(new GridLayout(2, 1, 5, 5));
            panel.add(new JTextField("Numero Biglietto"));
            panel.add(new JButton("Cerca"));
        } else if (item.equals("Segnala Smarrimento")) {
            panel.setLayout(new GridLayout(2, 1, 5, 5));
            panel.add(new JTextField("Codice Bagaglio"));
            panel.add(new JButton("Invia segnalazione"));
        } else {
            panel.setLayout(new BorderLayout());
            panel.add(label, BorderLayout.CENTER);
        }

        // Creo il dialog e lo imposto
        d.setChoiceDialog(new JDialog());
        JDialog dialog = d.getChoiceDialog();

        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(d.getComboBox1());
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setContentPane(panel);
        dialog.pack();
        dialog.setLocationRelativeTo(frame);
        dialog.setVisible(true);
    }
    public void selectedItem(String item, DashBoardAdmin d) {
        String selected = (String) d.getComboBox1().getSelectedItem();
        JPanel panel = new JPanel(); // inizializzazione base
        JLabel label = new JLabel(selected + " work in progress king...");
        label.setHorizontalAlignment(SwingConstants.CENTER);

        if (item.equals("Visualizza Voli")) {
            panel.setLayout(new GridLayout(1, 5, 5, 5));
            for (int i = 1; i <= 5; i++) {
                JButton button = new JButton("Admin Btn " + i);
                panel.add(button);
            }
        } else if (item.equals("Prenota Volo")) {
            panel.setLayout(new GridLayout(6, 1, 5, 5));
            panel.add(new JTextField("Id_Documento"));
            panel.add(new JTextField("Data_Nascita"));
            panel.add(new JTextField("Nome"));
            panel.add(new JTextField("Cognome"));
            panel.add(new JTextField("Volo"));
            panel.add(new JButton("Prenota"));
        } else if (item.equals("Cerca Prenotazione")) {
            panel.setLayout(new GridLayout(2, 1, 5, 5));
            panel.add(new JTextField("Numero Biglietto"));
            panel.add(new JButton("Cerca"));
        } else if (item.equals("Segnala Smarrimento")) {
            panel.setLayout(new GridLayout(2, 1, 5, 5));
            panel.add(new JTextField("Codice Bagaglio"));
            panel.add(new JButton("Invia segnalazione"));
        } else {
            panel.setLayout(new BorderLayout());
            panel.add(label, BorderLayout.CENTER);
        }

        d.setChoiceDialog(new JDialog());
        JDialog dialog = d.getChoiceDialog();

        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(d.getComboBox1());
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setContentPane(panel);
        dialog.pack();
        dialog.setLocationRelativeTo(frame);
        dialog.setVisible(true);
    }



    public Utente getUser() {
        return user;
    }

    public void setUser(Utente user) {
        this.user = user;
    }

    public Amministratore getAdmin() {
        return admin;
    }

    public void setAdmin(Amministratore admin) {
        this.admin = admin;
    }

    public Utente_Generico getUtente() {
        return utente;
    }

    public void setUtente(Utente_Generico utente) {
        this.utente = utente;
    }
    public boolean registraUtente(String email, String password, String idDoc, String nome, String cognome, String dataNascita) {
        // Simuliamo la registrazione: accettiamo tutto tranne se l'email è "admin" o vuota
        if (email == null || email.isEmpty() || "admin".equalsIgnoreCase(email)) {
            return false;
        }
        return true;
    }

    /*
    public Controller(){
        this.sistema = new Sistema();
    }


    public List<Volo> visualizzaVoli(){
        return sistema.getVoliDisponibili();
    }

    public void prenotavolo(Utente_Generico utente, Volo volo, Passeggero passeggero){
        utente.prenota_Volo(volo, passeggero);
    }

    public Prenotazione cercaPrenotazionePerNome(Utente_Generico utente, String nome){
        return utente.cerca_Prenotazione(nome);
    }

    public Prenotazione cercaPrenotazionePerCodice(Utente_Generico utente, int codice){
        return utente.cerca_Prenotazione(codice);
    }
    public void segnalaSmarrimento(Utente_Generico utente, Bagaglio bagaglio){
        utente.segnala_Smarrimento(bagaglio);
    }

    public void aggiornavolo(Amministratore amministratore, Volo volo){
        amministratore.aggiorna_Volo(volo);
    }

    public void modificaGate(Amministratore amministratore, Volo_In_Partenza volo){
        amministratore.modifica_Gate(volo);
    }

    public void aggiornaBagaglio(Amministratore amministratore, Bagaglio bagaglio, Stato_Bagaglio stato){
        amministratore.aggiorna_Bagaglio(bagaglio, stato);
    }

    public List<Bagaglio> visualizzaSmarrimenti(Amministratore amministratore){
        return amministratore.visualizza_Smarrimento();
    }
*/


}
