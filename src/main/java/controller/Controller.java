package controller;

import model.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
        JPanel panel = new JPanel();
        panel.setBackground(new Color(43, 48, 52));
        JLabel label = new JLabel(selected + " work in progress king...");
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setForeground(Color.WHITE);

        d.setChoiceDialog(new JDialog());
        JDialog dialog = d.getChoiceDialog();

        switch (item) {
            case "Visualizza Voli":
                panel.setLayout(new GridLayout(1, 5, 5, 5));
                for (int i = 1; i <= 5; i++) {
                    JButton button = creaBottoneConAzione("Volo " + i, "Hai selezionato il volo " + i, dialog);
                    panel.add(button);
                }
                break;

            case "Prenota Volo":
                panel.setLayout(new GridLayout(6, 1, 5, 5));
                panel.add(creaCampo("Id_Documento"));
                panel.add(creaCampo("Data_Nascita"));
                panel.add(creaCampo("Nome"));
                panel.add(creaCampo("Cognome"));
                panel.add(creaCampo("Codice Volo"));
                panel.add(creaBottoneConAzione("Prenota", "Prenotazione effettuata", dialog));
                break;

            case "Cerca Prenotazione":
                panel.setLayout(new GridLayout(2, 1, 5, 5));
                panel.add(creaCampo("Numero Biglietto"));
                panel.add(creaBottoneConAzione("Cerca", "Prenotazione trovata", dialog));
                break;

            case "Segnala Smarrimento":
                panel.setLayout(new GridLayout(2, 1, 5, 5));
                panel.add(creaCampo("Codice Bagaglio"));
                panel.add(creaBottoneConAzione("Invia segnalazione", "Segnalazione inviata", dialog));
                break;

            default:
                panel.setLayout(new BorderLayout());
                panel.add(label, BorderLayout.CENTER);
        }

        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(d.getComboBox1());
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setContentPane(panel);
        dialog.pack();
        dialog.setLocationRelativeTo(frame);
        dialog.setVisible(true);
    }

    public void selectedItem(String item, DashBoardAdmin d) {
        String selected = (String) d.getComboBox1().getSelectedItem();
        JPanel panel = new JPanel();
        panel.setBackground(new Color(43, 48, 52));
        JLabel label = new JLabel(selected + " work in progress king...");
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setForeground(Color.WHITE);

        d.setChoiceDialog(new JDialog());
        JDialog dialog = d.getChoiceDialog();

        switch (item) {
            case "Visualizza Voli":
                panel.setLayout(new GridLayout(1, 5, 5, 5));
                for (int i = 1; i <= 5; i++) {
                    JButton button = creaBottoneConAzione("Volo " + i, "Hai selezionato Volo " + i, dialog);
                    panel.add(button);
                }
                break;

            case "Inserisci Volo":
                panel.setLayout(new GridLayout(6, 1, 5, 5));
                panel.add(creaCampo("Codice Volo"));
                panel.add(creaCampo("Destinazione"));
                panel.add(creaCampo("Data Partenza"));
                panel.add(creaCampo("Orario Partenza"));
                panel.add(creaCampo("Numero Gate"));
                panel.add(creaBottoneConAzione("Inserisci", "Volo inserito correttamente", dialog));
                break;

            case "Aggiorna Volo":
                panel.setLayout(new GridLayout(3, 1, 5, 5));
                panel.add(creaCampo("Codice Volo"));
                panel.add(creaCampo("Nuova Destinazione / Data / Ora"));
                panel.add(creaBottoneConAzione("Aggiorna", "Volo aggiornato correttamente", dialog));
                break;

            case "Modifica Gate":
                panel.setLayout(new GridLayout(3, 1, 5, 5));
                panel.add(creaCampo("Codice Volo"));
                panel.add(creaCampo("Nuovo Gate"));
                panel.add(creaBottoneConAzione("Modifica", "Gate modificato correttamente", dialog));
                break;

            case "Aggiorna Bagaglio":
                panel.setLayout(new GridLayout(3, 1, 5, 5));
                panel.add(creaCampo("ID Bagaglio"));
                panel.add(creaCampo("Nuovo Stato"));
                panel.add(creaBottoneConAzione("Aggiorna", "Stato bagaglio aggiornato", dialog));
                break;

            case "Visualizza Smarrimenti":
                panel.setLayout(new BorderLayout());
                JTextArea textArea = new JTextArea("Elenco smarrimenti...");
                textArea.setEditable(false);
                textArea.setBackground(new Color(107, 112, 119));
                textArea.setForeground(Color.WHITE);
                panel.add(new JScrollPane(textArea), BorderLayout.CENTER);
                break;

            default:
                panel.setLayout(new BorderLayout());
                panel.add(label, BorderLayout.CENTER);
        }

        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(d.getComboBox1());
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setContentPane(panel);
        dialog.pack();
        dialog.setLocationRelativeTo(frame);
        dialog.setVisible(true);
    }

    private JTextField creaCampo(String placeholder) {
        JTextField field = new JTextField(placeholder);
        field.setBackground(new Color(107, 112, 119));
        field.setForeground(Color.WHITE);
        return field;
    }

    private JButton creaBottoneConAzione(String testo, String messaggio, JDialog dialog) {
        JButton btn = new JButton(testo);
        btn.setBackground(new Color(255, 162, 35));
        btn.setForeground(Color.BLACK);
        btn.addActionListener(e -> {
            JOptionPane.showMessageDialog(null, messaggio, "Info", JOptionPane.INFORMATION_MESSAGE);
            dialog.dispose();
        });
        return btn;
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
    public void interfacciaRegistrazione(Login l) {
        JDialog dialog = new JDialog();
        dialog.setTitle("Registrazione Utente");
        dialog.setModal(true);

        JPanel panel = new JPanel();
        panel.setBackground(new Color(43, 48, 52));
        panel.setLayout(new GridLayout(13, 1, 5, 5));

        JLabel labelEmail = new JLabel("Email/Username");
        labelEmail.setForeground(Color.WHITE);
        JTextField emailField = creaCampo(null);

        JLabel labelPassword = new JLabel("Password");
        labelPassword.setForeground(Color.WHITE);
        JPasswordField passwordField = new JPasswordField();
        passwordField.setBackground(new Color(107, 112, 119));
        passwordField.setForeground(Color.WHITE);

        JLabel labelIdDoc = new JLabel("ID nul");
        labelIdDoc.setForeground(Color.WHITE);
        JTextField idDocField = creaCampo(null);

        JLabel labelNome = new JLabel("Nome");
        labelNome.setForeground(Color.WHITE);
        JTextField nomeField = creaCampo(null);

        JLabel labelCognome = new JLabel("Cognome");
        labelCognome.setForeground(Color.WHITE);
        JTextField cognomeField = creaCampo(null);

        JLabel labelDataNascita = new JLabel("Data di nascita (formato: yyyy-mm-dd)");
        labelDataNascita.setForeground(Color.WHITE);
        JTextField dataNascitaField = creaCampo(null);

        JButton confermaButton = new JButton("Conferma Registrazione");
        confermaButton.setBackground(new Color(255, 162, 35));
        confermaButton.setForeground(Color.BLACK);

        panel.add(labelEmail);
        panel.add(emailField);
        panel.add(labelPassword);
        panel.add(passwordField);
        panel.add(labelIdDoc);
        panel.add(idDocField);
        panel.add(labelNome);
        panel.add(nomeField);
        panel.add(labelCognome);
        panel.add(cognomeField);
        panel.add(labelDataNascita);
        panel.add(dataNascitaField);
        panel.add(confermaButton);

        dialog.setContentPane(panel);
        dialog.pack();
        dialog.setLocationRelativeTo(null);

        confermaButton.addActionListener(ev -> {
            String email = emailField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();
            String idDoc = idDocField.getText().trim();
            String nome = nomeField.getText().trim();
            String cognome = cognomeField.getText().trim();
            String dataNascita = dataNascitaField.getText().trim();

            if (email.isEmpty() || password.isEmpty() || idDoc.isEmpty() || nome.isEmpty() || cognome.isEmpty() || dataNascita.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Tutti i campi sono obbligatori!");
                return;
            }

            boolean successo = registraUtente(email, password, idDoc, nome, cognome, dataNascita);

            if (successo) {
                JOptionPane.showMessageDialog(dialog, "Registrazione completata!");
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog, "Registrazione fallita: utente già esistente?");
            }
        });

        dialog.setVisible(true);
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
