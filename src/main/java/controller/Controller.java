package controller;

import implementazionePostgresDAO.AmministratoreImplementazionePostgresDAO;
import model.*;
import java.awt.*;
import java.sql.Time;
import java.util.ArrayList;
import java.sql.Date;

import gui.*;
import javax.swing.*;

public class Controller {
    // Variabili d'istanza per tenere traccia dell'utente connesso.
    // Può essere un Utente "normale" o un Amministratore.
    // Inoltre c'è un riferimento a UtenteGenerico, forse per una classe base o interfaccia.
    private Utente user;
    private Amministratore admin;
    private UtenteGenerico utente;

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
            /*case "Visualizza Voli":
                // Mostra 5 pulsanti, uno per ogni volo fittizio.
                // Potrebbe essere migliorato con dati reali da modello.
                panel.setLayout(new GridLayout(1, 5, 5, 5));
                for (int i = 1; i <= 5; i++) {
                    JButton button = creaBottoneConAzione("Volo " + i, "Hai selezionato il volo " + i, dialog);
                    panel.add(button);
                }
                break;
    */

            case "Prenota Volo":
                // Form semplice per inserire dati di prenotazione con vari campi testuali.
                // Non c'è validazione sui dati inseriti, né salvataggio reale.
                panel.setLayout(new GridLayout(6, 1, 5, 5));
                panel.add(creaCampo("Id_Documento"));
                panel.add(creaCampo("Data_Nascita"));
                panel.add(creaCampo("Nome"));
                panel.add(creaCampo("Cognome"));
                panel.add(creaCampo("Codice Volo"));
                //panel.add(creaBottoneConAzione("Prenota", "Prenotazione effettuata", dialog));
                break;

            case "Cerca Prenotazione":
                // Campo per inserire il numero del biglietto e bottone per ricerca
                panel.setLayout(new GridLayout(2, 1, 5, 5));
                panel.add(creaCampo("Numero Biglietto"));
                //panel.add(creaBottoneConAzione("Cerca", "Prenotazione trovata", dialog));
                break;

            case "Segnala Smarrimento":
                // Permette all'utente di segnalare un bagaglio smarrito.
                panel.setLayout(new GridLayout(2, 1, 5, 5));
                panel.add(creaCampo("Codice Bagaglio"));
                //panel.add(creaBottoneConAzione("Invia segnalazione", "Segnalazione inviata", dialog));
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
        AmministratoreImplementazionePostgresDAO a = new AmministratoreImplementazionePostgresDAO();
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
                    //JButton button = creaBottoneConAzione("Volo " + i, "Hai selezionato Volo " + i, dialog);
                    //panel.add(button);
                }
                break;

            case "Inserisci Volo":
                panel.setLayout(new GridLayout(6, 1, 5, 5));

                JTextField codiceField = creaCampo("Codice Volo");
                JTextField compagniaField = creaCampo("Compagnia");
                JTextField dataField = creaCampo("Data Partenza (yyyy-mm-dd)");
                JTextField orarioField = creaCampo("Orario Partenza (hh:mm:ss)");
                JTextField origineField = creaCampo("Origine");
                JTextField destinazioneField = creaCampo("Destinazione");
                JTextField gateField = creaCampo("Numero Gate");

                panel.add(codiceField);
                panel.add(compagniaField);
                panel.add(dataField);
                panel.add(orarioField);
                panel.add(origineField);
                panel.add(destinazioneField);
                panel.add(gateField);

                panel.add(creaBottoneConAzione("Inserisci", () -> {
                    try {
                        int codice = Integer.parseInt(codiceField.getText());
                        String compagnia = compagniaField.getText();
                        Date data = Date.valueOf(dataField.getText());
                        Time orario = Time.valueOf(orarioField.getText());
                        int gate = Integer.parseInt(gateField.getText());
                        String destinazione = destinazioneField.getText();
                        String origine = origineField.getText();
                        if(destinazione.equals("NAP")) {
                            VoloInArrivo volo = new VoloInArrivo(
                                    codice,
                                    compagnia,
                                    data,
                                    orario,
                                    0,
                                    StatoVolo.PROGRAMMATO,
                                    origine, // destinazione
                                    new ArrayList<>()
                            );
                            new AmministratoreImplementazionePostgresDAO().inserisciVolo(volo);
                        } else {
                            VoloInPartenza volo = new VoloInPartenza(
                                    codice,
                                    compagnia,
                                    data,
                                    orario,
                                    0,
                                    StatoVolo.PROGRAMMATO,
                                    destinazione, // destinazione
                                    new ArrayList<>(),
                                    gate
                            );
                            new AmministratoreImplementazionePostgresDAO().inserisciVolo(volo);
                        }


                        JOptionPane.showMessageDialog(null, "Volo inserito correttamente!");
                        dialog.dispose();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(null, "Errore: " + ex.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
                    }
                    visualizzaVoli(d);
                    }, dialog));
                break;

            case "Aggiorna Volo":
                ArrayList<Volo> voli = new AmministratoreImplementazionePostgresDAO().visualizzaVoli();

                JPanel listaPanel = new JPanel();
                listaPanel.setLayout(new GridLayout(voli.size(), 1, 5, 5));
                listaPanel.setBackground(new Color(43, 48, 52));

                for (Volo v : voli) {
                    JPanel riga = new JPanel(new FlowLayout(FlowLayout.LEFT));
                    riga.setBackground(new Color(107, 112, 119));

                    label = new JLabel("Volo " + v.getCodiceVolo() + " → " + v.getDestinazione() + " (" + v.getData() + ")");
                    label.setForeground(Color.WHITE);
                    JButton modificaBtn = new JButton("Modifica");
                    modificaBtn.setBackground(new Color(255, 162, 35));
                    modificaBtn.setForeground(Color.BLACK);

                    modificaBtn.addActionListener(ev -> {

                        d.getChoiceDialog().dispose();
                        if(v.getDestinazione().equals("NAP")) {
                            VoloInArrivo voloina = (VoloInArrivo) v;
                            mostraPopupModificaVolo(voloina);

                        } else {
                            VoloInPartenza voloinp = (VoloInPartenza) v;
                            mostraPopupModificaVolo(voloinp);
                        }
                    });


                    riga.add(label);
                    riga.add(modificaBtn);
                    listaPanel.add(riga);
                }

                JScrollPane scrollPane = new JScrollPane(listaPanel);
                scrollPane.setPreferredSize(new Dimension(500, 400));

                JDialog voloDialog = new JDialog();
                voloDialog.setTitle("Seleziona Volo da Modificare");
                voloDialog.setModal(true);
                voloDialog.setContentPane(scrollPane);
                voloDialog.pack();
                voloDialog.setLocationRelativeTo(null);
                voloDialog.setVisible(true);
                break;


            case "Modifica Gate":
                // Cambia il gate di partenza di un volo
                panel.setLayout(new GridLayout(3, 1, 5, 5));
                panel.add(creaCampo("Codice Volo"));
                panel.add(creaCampo("Nuovo Gate"));
                //panel.add(creaBottoneConAzione("Modifica", "Gate modificato correttamente", dialog));
                break;

            case "Aggiorna Bagaglio":
                // Permette di aggiornare lo stato di un bagaglio smarrito o ritrovato
                panel.setLayout(new GridLayout(3, 1, 5, 5));
                panel.add(creaCampo("ID Bagaglio"));
                panel.add(creaCampo("Nuovo Stato"));
                //panel.add(creaBottoneConAzione("Aggiorna", "Stato bagaglio aggiornato", dialog));
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
    private JButton creaBottoneConAzione(String testo, Runnable azione, JDialog dialog) {
        JButton btn = new JButton(testo);
        btn.setBackground(new Color(255, 162, 35));
        btn.setForeground(Color.BLACK);
        btn.addActionListener(e -> {
            azione.run(); // Esegui la query specifica
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
    public UtenteGenerico getUtente() {
        return utente;
    }
    public void setUtente(UtenteGenerico utente) {
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
    public void visualizzaVoli(DashBoardUser view) {
        AmministratoreImplementazionePostgresDAO adao = new AmministratoreImplementazionePostgresDAO();
        ArrayList<Volo> voli = adao.visualizzaVoli();

        StringBuilder html = new StringBuilder("<html><body style='color:white;font-family:sans-serif;'>");
        html.append("<table border='1' cellpadding='4' cellspacing='0'>")
                .append("<tr>")
                .append("<th>Codice</th>")
                .append("<th>Compagnia</th>")
                .append("<th>Data</th>")
                .append("<th>Orario</th>")
                .append("<th>Ritardo</th>")
                .append("<th>Stato</th>")
                .append("<th>Origine</th>")
                .append("<th>Destinazione</th>")
                .append("<th>Gate</th>")
                .append("</tr>");

        for (Volo v : voli) {
            html.append("<tr>")
                    .append("<td>").append(v.getCodiceVolo()).append("</td>")
                    .append("<td>").append(v.getCompagnia()).append("</td>")
                    .append("<td>").append(v.getData()).append("</td>")
                    .append("<td>").append(v.getOrario()).append("</td>")
                    .append("<td>").append(v.getRitardo()).append("</td>")
                    .append("<td>").append(v.getStato()).append("</td>")
                    .append("<td>").append(v.getOrigine()).append("</td>")
                    .append("<td>").append(v.getDestinazione()).append("</td>")
                    .append("<td>").append((v instanceof VoloInPartenza) ? ((VoloInPartenza) v).getGate() : "-").append("</td>")
                    .append("</tr>");
        }

        html.append("</table></body></html>");

        JLabel output = view.getOutputLabel();
        output.setText(html.toString());
        output.setVisible(true);
    }


    public void visualizzaVoli(DashBoardAdmin view) {
        AmministratoreImplementazionePostgresDAO adao = new AmministratoreImplementazionePostgresDAO();
        ArrayList<Volo> voli = adao.visualizzaVoli();

        StringBuilder html = new StringBuilder("<html><body style='color:white;font-family:sans-serif;'>");
        html.append("<table border='1' cellpadding='4' cellspacing='0'>")
                .append("<tr>")
                .append("<th>Codice</th>")
                .append("<th>Compagnia</th>")
                .append("<th>Data</th>")
                .append("<th>Orario</th>")
                .append("<th>Ritardo</th>")
                .append("<th>Stato</th>")
                .append("<th>Origine</th>")
                .append("<th>Destinazione</th>")
                .append("<th>Gate</th>")
                .append("</tr>");

        for (Volo v : voli) {
            html.append("<tr>")
                    .append("<td>").append(v.getCodiceVolo()).append("</td>")
                    .append("<td>").append(v.getCompagnia()).append("</td>")
                    .append("<td>").append(v.getData()).append("</td>")
                    .append("<td>").append(v.getOrario()).append("</td>")
                    .append("<td>").append(v.getRitardo()).append("</td>")
                    .append("<td>").append(v.getStato()).append("</td>")
                    .append("<td>").append(v.getOrigine()).append("</td>")
                    .append("<td>").append(v.getDestinazione()).append("</td>")
                    .append("<td>").append((v instanceof VoloInPartenza) ? ((VoloInPartenza) v).getGate() : "-").append("</td>")
                    .append("</tr>");
        }

        html.append("</table></body></html>");

        JLabel output = view.getOutputLabel();
        output.setText(html.toString());
        output.setVisible(true);
    }




    private void mostraPopupModificaVolo(Volo volo) {
        JDialog dialog = new JDialog();
        dialog.setTitle("Modifica Volo");
        dialog.setModal(true);

        JPanel panel = new JPanel(new GridLayout(0, 2, 5, 5));
        panel.setBackground(new Color(43, 48, 52));

        JTextField codiceField = creaCampo(String.valueOf(volo.getCodiceVolo()));
        JTextField compagniaField = creaCampo(volo.getCompagnia());
        JTextField dataField = creaCampo(volo.getData().toString()); // yyyy-mm-dd
        JTextField orarioField = creaCampo(volo.getOrario().toString()); // HH:mm:ss
        JTextField ritardoField = creaCampo(String.valueOf(volo.getRitardo()));
        JTextField statoField = creaCampo(volo.getStato());
        JTextField origineField = creaCampo(volo.getOrigine());
        JTextField destinazioneField = creaCampo(volo.getDestinazione());
        JTextField gateField = null;

        panel.add(new JLabel("Codice Volo:")); panel.add(codiceField);
        panel.add(new JLabel("Compagnia:")); panel.add(compagniaField);
        panel.add(new JLabel("Data (YYYY-MM-DD):")); panel.add(dataField);
        panel.add(new JLabel("Orario (HH:MM:SS):")); panel.add(orarioField);
        panel.add(new JLabel("Ritardo (minuti):")); panel.add(ritardoField);
        panel.add(new JLabel("Stato:")); panel.add(statoField);
        panel.add(new JLabel("Origine:")); panel.add(origineField);
        panel.add(new JLabel("Destinazione:")); panel.add(destinazioneField);

        if (volo instanceof VoloInPartenza) {
            VoloInPartenza v = (VoloInPartenza) volo;
            gateField = creaCampo(String.valueOf(v.getGate()));
            panel.add(new JLabel("Gate:")); panel.add(gateField);
        }

        JButton conferma = new JButton("Conferma modifica");
        conferma.setBackground(new Color(255, 162, 35));
        conferma.setForeground(Color.BLACK);

        JTextField finalGateField = gateField; // per usarlo nel lambda

        conferma.addActionListener(e -> {
            try {
                int nuovoCodice = Integer.parseInt(codiceField.getText());
                String compagnia = compagniaField.getText();
                Date data = Date.valueOf(dataField.getText());
                Time orario = Time.valueOf(orarioField.getText());
                int ritardo = Integer.parseInt(ritardoField.getText());
                StatoVolo stato = StatoVolo.valueOf(statoField.getText().toUpperCase());
                String origine = origineField.getText();
                String destinazione = destinazioneField.getText();

                if (volo instanceof VoloInPartenza) {
                    int gate = Integer.parseInt(finalGateField.getText());
                    VoloInPartenza vPartModificato = new VoloInPartenza(
                            nuovoCodice, compagnia, data, orario, ritardo,
                            stato, destinazione, new ArrayList<>(), gate
                    );
                    new AmministratoreImplementazionePostgresDAO().aggiornaVolo(vPartModificato);
                } else {
                    VoloInArrivo vArrModificato = new VoloInArrivo(
                            nuovoCodice, compagnia, data, orario, ritardo,
                            stato, origine, new ArrayList<>()
                    );
                    new AmministratoreImplementazionePostgresDAO().aggiornaVolo(vArrModificato);
                }

                JOptionPane.showMessageDialog(null, "Volo aggiornato con successo!");
                dialog.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Errore: " + ex.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
            }
        });

        panel.add(new JLabel()); // spaziatore
        panel.add(conferma);

        dialog.setContentPane(panel);
        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
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

    public void prenotavolo(UtenteGenerico utente, Volo volo, Passeggero passeggero){
        utente.prenota_Volo(volo, passeggero);
    }

    public Prenotazione cercaPrenotazionePerNome(UtenteGenerico utente, String nome){
        return utente.cerca_Prenotazione(nome);
    }

    public Prenotazione cercaPrenotazionePerCodice(UtenteGenerico utente, int codice){
        return utente.cerca_Prenotazione(codice);
    }
    public void segnalaSmarrimento(UtenteGenerico utente, Bagaglio bagaglio){
        utente.segnala_Smarrimento(bagaglio);
    }

    public void aggiornavolo(Amministratore amministratore, Volo volo){
        amministratore.aggiorna_Volo(volo);
    }

    public void modificaGate(Amministratore amministratore, VoloInPartenza volo){
        amministratore.modifica_Gate(volo);
    }

    public void aggiornaBagaglio(Amministratore amministratore, Bagaglio bagaglio, StatoBagaglio stato){
        amministratore.aggiorna_Bag
        }
     */
