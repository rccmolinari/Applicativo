package controller;

import model.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import gui.*;
import javax.swing.*;

public class Controller {
    // Variabili d'istanza per tenere traccia dell'utente connesso.
    // Può essere un Utente "normale" o un Amministratore.
    // Inoltre c'è un riferimento a Utente_Generico, forse per una classe base o interfaccia.
    private Utente user;
    private Amministratore admin;
    private Utente_Generico utente;

    /**
     * Metodo di login molto semplice:
     * - Se l'username e password sono "admin" viene creato un oggetto Amministratore.
     * - Altrimenti viene creato un oggetto Utente generico.
     *
     * Questo metodo gestisce la distinzione tra tipi di utenti ma senza autenticazione reale.
     * In un'applicazione reale servirebbe una verifica su database o altro sistema persistente.
     */
    public void login(String login, String password) {
        if (login.equals("admin") && password.equals("admin")) {
            this.admin = new Amministratore(login, password);
        } else {
            this.user = new Utente(login, password);
        }
    }

    /**
     * Metodo per gestire l'interazione con il dashboard dell'utente normale.
     * In base all'elemento selezionato (ad esempio "Visualizza Voli", "Prenota Volo", ecc),
     * costruisce dinamicamente un pannello Swing che viene mostrato in una finestra di dialogo.
     *
     * Vengono creati campi di input (JTextField) e pulsanti per le azioni.
     * Quando l'utente clicca sul pulsante, viene mostrato un messaggio di conferma e si chiude la finestra.
     *
     * Il codice usa uno switch-case per scegliere quale UI costruire.
     * La UI è piuttosto semplice, senza validazione o collegamenti a backend.
     */
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
                // Mostra 5 pulsanti, uno per ogni volo fittizio.
                // Potrebbe essere migliorato con dati reali da modello.
                panel.setLayout(new GridLayout(1, 5, 5, 5));
                for (int i = 1; i <= 5; i++) {
                    JButton button = creaBottoneConAzione("Volo " + i, "Hai selezionato il volo " + i, dialog);
                    panel.add(button);
                }
                break;

            case "Prenota Volo":
                // Form semplice per inserire dati di prenotazione con vari campi testuali.
                // Non c'è validazione sui dati inseriti, né salvataggio reale.
                panel.setLayout(new GridLayout(6, 1, 5, 5));
                panel.add(creaCampo("Id_Documento"));
                panel.add(creaCampo("Data_Nascita"));
                panel.add(creaCampo("Nome"));
                panel.add(creaCampo("Cognome"));
                panel.add(creaCampo("Codice Volo"));
                panel.add(creaBottoneConAzione("Prenota", "Prenotazione effettuata", dialog));
                break;

            case "Cerca Prenotazione":
                // Campo per inserire il numero del biglietto e bottone per ricerca
                panel.setLayout(new GridLayout(2, 1, 5, 5));
                panel.add(creaCampo("Numero Biglietto"));
                panel.add(creaBottoneConAzione("Cerca", "Prenotazione trovata", dialog));
                break;

            case "Segnala Smarrimento":
                // Permette all'utente di segnalare un bagaglio smarrito.
                panel.setLayout(new GridLayout(2, 1, 5, 5));
                panel.add(creaCampo("Codice Bagaglio"));
                panel.add(creaBottoneConAzione("Invia segnalazione", "Segnalazione inviata", dialog));
                break;

            default:
                // Per opzioni non gestite mostra messaggio di "work in progress"
                panel.setLayout(new BorderLayout());
                panel.add(label, BorderLayout.CENTER);
        }

        // Configura e mostra il dialog modale centrato rispetto alla finestra padre.
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(d.getComboBox1());
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setContentPane(panel);
        dialog.pack();
        dialog.setLocationRelativeTo(frame);
        dialog.setVisible(true);
    }

    /**
     * Metodo simile a quello per l'utente, ma per il dashboard amministratore.
     * Offre funzionalità più complesse, come inserimento, aggiornamento voli, modifica gate,
     * aggiornamento bagagli e visualizzazione degli smarrimenti.
     *
     * Anche qui l'interfaccia è costruita dinamicamente e si limita a mostrare messaggi di conferma.
     * Un sistema reale dovrebbe collegare questi input ai modelli e al backend per modifiche persistenti.
     */
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
                // Form per inserire i dati di un nuovo volo
                panel.setLayout(new GridLayout(6, 1, 5, 5));
                panel.add(creaCampo("Codice Volo"));
                panel.add(creaCampo("Destinazione"));
                panel.add(creaCampo("Data Partenza"));
                panel.add(creaCampo("Orario Partenza"));
                panel.add(creaCampo("Numero Gate"));
                panel.add(creaBottoneConAzione("Inserisci", "Volo inserito correttamente", dialog));
                break;

            case "Aggiorna Volo":
                // Form per aggiornare i dettagli di un volo esistente
                panel.setLayout(new GridLayout(3, 1, 5, 5));
                panel.add(creaCampo("Codice Volo"));
                panel.add(creaCampo("Nuova Destinazione / Data / Ora"));
                panel.add(creaBottoneConAzione("Aggiorna", "Volo aggiornato correttamente", dialog));
                break;

            case "Modifica Gate":
                // Cambia il gate di partenza di un volo
                panel.setLayout(new GridLayout(3, 1, 5, 5));
                panel.add(creaCampo("Codice Volo"));
                panel.add(creaCampo("Nuovo Gate"));
                panel.add(creaBottoneConAzione("Modifica", "Gate modificato correttamente", dialog));
                break;

            case "Aggiorna Bagaglio":
                // Permette di aggiornare lo stato di un bagaglio smarrito o ritrovato
                panel.setLayout(new GridLayout(3, 1, 5, 5));
                panel.add(creaCampo("ID Bagaglio"));
                panel.add(creaCampo("Nuovo Stato"));
                panel.add(creaBottoneConAzione("Aggiorna", "Stato bagaglio aggiornato", dialog));
                break;

            case "Visualizza Smarrimenti":
                // Mostra una textarea con la lista degli smarrimenti, non editabile
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

    /**
     * Metodo helper per creare JTextField con colori personalizzati
     * Sfondo grigio e testo bianco per migliorare la leggibilità su sfondo scuro.
     * Il testo passato è usato come "placeholder" che però non scompare se si scrive.
     */
    private JTextField creaCampo(String placeholder) {
        JTextField field = new JTextField(placeholder);
        field.setBackground(new Color(107, 112, 119));
        field.setForeground(Color.WHITE);
        return field;
    }

    /**
     * Metodo helper per creare JButton con azione associata.
     * Al click mostra un messaggio di conferma (JOptionPane) e chiude la finestra di dialogo.
     *
     * Questo semplifica la gestione degli eventi, ma non esegue azioni reali.
     */
    private JButton creaBottoneConAzione(String testo, String messaggio, JDialog dialog) {
        JButton btn = new JButton(testo);
        btn.setBackground(new Color(255, 162, 35));  // Colore arancione vivace per evidenziare il bottone
        btn.setForeground(Color.BLACK);
        btn.addActionListener(e -> {
            JOptionPane.showMessageDialog(null, messaggio, "Info", JOptionPane.INFORMATION_MESSAGE);
            dialog.dispose();
        });
        return btn;
    }

    // Getter e setter per gestire gli utenti attivi nel controller
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

    /**
     * Metodo che crea una finestra di dialogo modale per la registrazione di un nuovo utente.
     * Contiene campi di testo per email, password, dati personali.
     *
     * La conferma verifica che nessun campo sia vuoto e simula la registrazione chiamando
     * un metodo che può rigettare registrazioni con email "admin" o email vuote.
     *
     * Non c'è collegamento reale a un database, quindi è una simulazione.
     */
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

        JLabel labelDataNascita = new JLabel("Data Nascita (aaaa-mm-gg)");
        labelDataNascita.setForeground(Color.WHITE);
        JTextField dataNascitaField = creaCampo(null);

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

        JButton registrati = new JButton("Registrati");
        registrati.setBackground(new Color(255, 162, 35));
        registrati.setForeground(Color.BLACK);

        registrati.addActionListener(e -> {
            if (emailField.getText().isEmpty() || passwordField.getPassword().length == 0
                    || idDocField.getText().isEmpty() || nomeField.getText().isEmpty()
                    || cognomeField.getText().isEmpty() || dataNascitaField.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Compila tutti i campi", "Errore", JOptionPane.ERROR_MESSAGE);
            } else {
                try {
                    // Simula la registrazione utente tramite un metodo di Login
                    registraUtente(emailField.getText(), new String(passwordField.getPassword()), idDocField.getText(),
                            nomeField.getText(), cognomeField.getText(), dataNascitaField.getText());
                    JOptionPane.showMessageDialog(null, "Registrazione effettuata con successo!", "Info",
                            JOptionPane.INFORMATION_MESSAGE);
                    dialog.dispose();
                } catch (IllegalArgumentException ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        panel.add(registrati);

        dialog.setContentPane(panel);
        dialog.pack();
        dialog.setLocationRelativeTo(null);
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
