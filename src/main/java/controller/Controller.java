package controller;

import model.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import gui.*;

import javax.swing.*;

public class Controller {
    // Variabili per memorizzare l'utente corrente: può essere un Utente o un Amministratore
    private Utente user;
    private Amministratore admin;
    private Utente_Generico utente;

    // Metodo per gestire il login: se login e password sono "admin" crea un Amministratore, altrimenti un Utente generico
    public void login(String login, String password) {
        if (login.equals("admin") && password.equals("admin")) {
            this.admin = new Amministratore(login, password);
        } else {
            this.user = new Utente(login, password);
        }
    }

    // Metodo per gestire la selezione di un elemento nel dashboard utente e mostrare la finestra corrispondente
    public void selectedItem(String item, DashBoardUser d) {
        // Ottiene l'elemento selezionato nella ComboBox
        String selected = (String) d.getComboBox1().getSelectedItem();

        // Crea un pannello di base con sfondo scuro
        JPanel panel = new JPanel();
        panel.setBackground(new Color(43, 48, 52));

        // Etichetta di default per azioni non implementate
        JLabel label = new JLabel(selected + " work in progress king...");
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setForeground(Color.WHITE);

        // Prepara una nuova finestra di dialogo (modal)
        d.setChoiceDialog(new JDialog());
        JDialog dialog = d.getChoiceDialog();

        // Switch sulle possibili azioni dell'utente
        switch (item) {
            case "Visualizza Voli":
                // Layout a griglia con 5 pulsanti per 5 voli fittizi
                panel.setLayout(new GridLayout(1, 5, 5, 5));
                for (int i = 1; i <= 5; i++) {
                    JButton button = creaBottoneConAzione("Volo " + i, "Hai selezionato il volo " + i, dialog);
                    panel.add(button);
                }
                break;

            case "Prenota Volo":
                // Form di prenotazione con campi per dati personali e codice volo + bottone prenota
                panel.setLayout(new GridLayout(6, 1, 5, 5));
                panel.add(creaCampo("Id_Documento"));
                panel.add(creaCampo("Data_Nascita"));
                panel.add(creaCampo("Nome"));
                panel.add(creaCampo("Cognome"));
                panel.add(creaCampo("Codice Volo"));
                panel.add(creaBottoneConAzione("Prenota", "Prenotazione effettuata", dialog));
                break;

            case "Cerca Prenotazione":
                // Campo per inserire numero biglietto e bottone cerca
                panel.setLayout(new GridLayout(2, 1, 5, 5));
                panel.add(creaCampo("Numero Biglietto"));
                panel.add(creaBottoneConAzione("Cerca", "Prenotazione trovata", dialog));
                break;

            case "Segnala Smarrimento":
                // Campo per codice bagaglio e bottone invia segnalazione
                panel.setLayout(new GridLayout(2, 1, 5, 5));
                panel.add(creaCampo("Codice Bagaglio"));
                panel.add(creaBottoneConAzione("Invia segnalazione", "Segnalazione inviata", dialog));
                break;

            default:
                // Per altre opzioni mostra messaggio di lavoro in corso
                panel.setLayout(new BorderLayout());
                panel.add(label, BorderLayout.CENTER);
        }

        // Configura la finestra di dialogo e la mostra centrata rispetto alla finestra principale
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(d.getComboBox1());
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setContentPane(panel);
        dialog.pack();
        dialog.setLocationRelativeTo(frame);
        dialog.setVisible(true);
    }

    // Metodo simile per il dashboard amministratore, con opzioni differenti
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
                // Form per inserire un nuovo volo con i relativi dati e bottone inserisci
                panel.setLayout(new GridLayout(6, 1, 5, 5));
                panel.add(creaCampo("Codice Volo"));
                panel.add(creaCampo("Destinazione"));
                panel.add(creaCampo("Data Partenza"));
                panel.add(creaCampo("Orario Partenza"));
                panel.add(creaCampo("Numero Gate"));
                panel.add(creaBottoneConAzione("Inserisci", "Volo inserito correttamente", dialog));
                break;

            case "Aggiorna Volo":
                // Form per aggiornare un volo esistente
                panel.setLayout(new GridLayout(3, 1, 5, 5));
                panel.add(creaCampo("Codice Volo"));
                panel.add(creaCampo("Nuova Destinazione / Data / Ora"));
                panel.add(creaBottoneConAzione("Aggiorna", "Volo aggiornato correttamente", dialog));
                break;

            case "Modifica Gate":
                // Form per modificare il gate di un volo
                panel.setLayout(new GridLayout(3, 1, 5, 5));
                panel.add(creaCampo("Codice Volo"));
                panel.add(creaCampo("Nuovo Gate"));
                panel.add(creaBottoneConAzione("Modifica", "Gate modificato correttamente", dialog));
                break;

            case "Aggiorna Bagaglio":
                // Form per aggiornare lo stato di un bagaglio
                panel.setLayout(new GridLayout(3, 1, 5, 5));
                panel.add(creaCampo("ID Bagaglio"));
                panel.add(creaCampo("Nuovo Stato"));
                panel.add(creaBottoneConAzione("Aggiorna", "Stato bagaglio aggiornato", dialog));
                break;

            case "Visualizza Smarrimenti":
                // Mostra in una textarea l'elenco degli smarrimenti
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

        // Configurazione e visualizzazione della finestra di dialogo
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(d.getComboBox1());
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setContentPane(panel);
        dialog.pack();
        dialog.setLocationRelativeTo(frame);
        dialog.setVisible(true);
    }

    // Metodo per creare un JTextField con colore di sfondo e testo bianco
    private JTextField creaCampo(String placeholder) {
        JTextField field = new JTextField(placeholder);
        field.setBackground(new Color(107, 112, 119));
        field.setForeground(Color.WHITE);
        return field;
    }

    // Metodo per creare un JButton con azione associata: mostra un messaggio e chiude il dialog
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

    // Getter e setter per gli oggetti utente e amministratore
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

    // Metodo per mostrare la finestra di registrazione utente
    public void interfacciaRegistrazione(Login l) {
        JDialog dialog = new JDialog();
        dialog.setTitle("Registrazione Utente");
        dialog.setModal(true);

        // Pannello con sfondo scuro e layout a griglia verticale
        JPanel panel = new JPanel();
        panel.setBackground(new Color(43, 48, 52));
        panel.setLayout(new GridLayout(13, 1, 5, 5));

        // Etichette e campi testo per i dati richiesti
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

        // Bottone per confermare la registrazione
        JButton confermaButton = new JButton("Conferma Registrazione");
        confermaButton.setBackground(new Color(255, 162, 35));
        confermaButton.setForeground(Color.BLACK);

        // Aggiunge tutti gli elementi al pannello
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

        // Aggiunge azione al bottone: verifica campi e chiama metodo di registrazione
        confermaButton.addActionListener(ev -> {
            String email = emailField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();
            String idDoc = idDocField.getText().trim();
            String nome = nomeField.getText().trim();
            String cognome = cognomeField.getText().trim();
            String dataNascita = dataNascitaField.getText().trim();

            // Controlla se tutti i campi sono riempiti
            if (email.isEmpty() || password.isEmpty() || idDoc.isEmpty() || nome.isEmpty() || cognome.isEmpty() || dataNascita.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Tutti i campi sono obbligatori!");
                return;
            }

            // Chiama la funzione che simula la registrazione (qui semplice controllo su email)
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

    // Metodo che simula la registrazione, rifiutando solo email vuote o "admin"
    public boolean registraUtente(String email, String password, String idDoc, String nome, String cognome, String dataNascita) {
        if (email == null || email.isEmpty() || "admin".equalsIgnoreCase(email)) {
            return false;
        }
        return true;
    }
}

    /*
    // Codice commentato per funzionalità future o gestite altrove
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
        amministratore.aggiorna_Bag
        }
     */
