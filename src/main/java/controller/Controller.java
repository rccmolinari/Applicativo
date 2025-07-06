package controller;

import implementazionePostgresDAO.AmministratoreImplementazionePostgresDAO;
import implementazionePostgresDAO.LoginImplementazionePostgresDAO;
import implementazionePostgresDAO.UtenteGenericoImplementazionePostgresDAO;
import model.*;
import java.awt.*;
import java.sql.Time;
import java.util.ArrayList;
import java.sql.Date;

import gui.*;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class Controller {
    // Variabili d'istanza per tenere traccia dell'utente connesso.
    // Può essere un Utente "normale" o un Amministratore.
    // Inoltre c'è un riferimento a UtenteGenericoDAO, forse per una classe base o interfaccia.
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
        if (selected == null || selected.trim().isEmpty()) return;
        JPanel panel = new JPanel();
        panel.setBackground(new Color(43, 48, 52));
        JLabel label = new JLabel(selected + " work in progress king...");
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setForeground(Color.WHITE);

        d.setChoiceDialog(new JDialog());
        JDialog dialog = d.getChoiceDialog();

        switch (item) {
            case "Visualizza Prenotazioni":
                ArrayList<Prenotazione> prenotazioni = new UtenteGenericoImplementazionePostgresDAO().listaPrenotazioni(utente);

                String[] colonne = {"Biglietto", "Nome", "Cognome", "Posto", "Stato", "Bagagli"};
                String[][] dati = new String[prenotazioni.size()][colonne.length];

                for (int i = 0; i < prenotazioni.size(); i++) {
                    Prenotazione p = prenotazioni.get(i);
                    Passeggero passeggero = p.getPasseggero();
                    dati[i] = new String[] {
                            String.valueOf(p.getNumeroBiglietto()),
                            passeggero.getNome(),
                            passeggero.getCognome(),
                            String.valueOf(p.getNumeroAssegnato()),
                            p.getStatoPrenotazione().toString(),
                            String.valueOf(p.getListaBagagli().size())
                    };
                }

                JTable tabella = new JTable(dati, colonne);
                tabella.setEnabled(false);
                tabella.setFont(new Font("SansSerif", Font.PLAIN, 14));
                tabella.setBackground(new Color(107, 112, 119));
                tabella.setForeground(Color.WHITE);
                tabella.setGridColor(Color.GRAY);
                tabella.getTableHeader().setFont(new Font("SansSerif", Font.PLAIN, 14));
                tabella.getTableHeader().setBackground(new Color(60, 63, 65));
                tabella.getTableHeader().setForeground(Color.WHITE);

                JScrollPane scrollPane = new JScrollPane(tabella);
                scrollPane.setPreferredSize(new Dimension(600, 300));
                scrollPane.getViewport().setBackground(new Color(43, 48, 52));

                JDialog popup = new JDialog();
                popup.setTitle("Prenotazioni Effettuate");
                popup.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
                popup.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                popup.setSize(650, 350);
                popup.setLocationRelativeTo(null);

                JPanel contenitore = new JPanel(new GridBagLayout());
                contenitore.setBackground(new Color(43, 48, 52));
                GridBagConstraints gbc = new GridBagConstraints();
                gbc.fill = GridBagConstraints.BOTH;
                gbc.weightx = 1;
                gbc.weighty = 1;
                contenitore.add(scrollPane, gbc);

                popup.setContentPane(contenitore);
                popup.setVisible(true);
                visualizzaVoli(d);
                return;


            case "Prenota Volo":
                panel.setLayout(new GridLayout(1, 1));

                JPanel formPanel = new JPanel(new GridLayout(7, 1, 10, 10));
                formPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
                formPanel.setBackground(new Color(43, 48, 52));

                JTextField idDocumentoField = creaCampo("Id_Documento");
                JTextField dataNascitaField = creaCampo("Data_Nascita (yyyy-mm-dd)");
                JTextField nomeField = creaCampo("Nome");
                JTextField cognomeField = creaCampo("Cognome");
                JTextField codiceVoloField = creaCampo("Codice Volo");
                JTextField numeroBagagliField = creaCampo("Numero Bagagli");

                formPanel.add(idDocumentoField);
                formPanel.add(dataNascitaField);
                formPanel.add(nomeField);
                formPanel.add(cognomeField);
                formPanel.add(codiceVoloField);
                formPanel.add(numeroBagagliField);

                JButton prenotaButton = creaBottoneConAzione("Prenota", () -> {
                    try {
                        Passeggero passeggero = new Passeggero();
                        passeggero.setIdDocumento(idDocumentoField.getText().trim());
                        passeggero.setNome(nomeField.getText().trim());
                        passeggero.setCognome(cognomeField.getText().trim());
                        passeggero.setDataNascita(java.sql.Date.valueOf(dataNascitaField.getText().trim()));

                        Volo volo = new Volo();
                        volo.setCodiceVolo(Integer.parseInt(codiceVoloField.getText().trim()));

                        int numeroBagagli = Integer.parseInt(numeroBagagliField.getText().trim());
                        if (numeroBagagli < 0) throw new IllegalArgumentException("Il numero di bagagli deve essere >= 0");

                        ArrayList<Bagaglio> bagagli = new ArrayList<>();
                        for (int i = 0; i < numeroBagagli; i++) {
                            Bagaglio b = new Bagaglio();
                            b.setStatoBagaglio(StatoBagaglio.REGISTRATO);
                            bagagli.add(b);
                        }

                        UtenteGenericoImplementazionePostgresDAO dao = new UtenteGenericoImplementazionePostgresDAO();
                        dao.prenotaVolo(utente, volo, passeggero, bagagli);

                        JOptionPane.showMessageDialog(dialog, "Prenotazione effettuata con " + numeroBagagli + " bagagli.");
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(dialog, "Errore: " + ex.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
                    }
                }, dialog);

                formPanel.add(prenotaButton);
                panel.add(formPanel);

                visualizzaVoli(d);
                break;



            case "Cerca Prenotazione":
                panel.setLayout(new GridLayout(1, 1));
                JPanel formPanel1 = new JPanel(new GridLayout(5, 1, 10, 10));
                formPanel1.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
                formPanel1.setBackground(new Color(43, 48, 52));

                JTextField campoBigliettoUnico = creaCampo("Numero Biglietto (opzionale)");
                JTextField campoNomePasseggeroUnico = creaCampo("Nome Passeggero (opzionale)");
                JTextField campoCognomePasseggeroUnico = creaCampo("Cognome Passeggero (opzionale)");

                // Svuota campo biglietto se si scrive in nome o cognome
                campoNomePasseggeroUnico.getDocument().addDocumentListener(new DocumentListener() {
                    public void insertUpdate(DocumentEvent e) { campoBigliettoUnico.setText(""); }
                    public void removeUpdate(DocumentEvent e) { campoBigliettoUnico.setText(""); }
                    public void changedUpdate(DocumentEvent e) { campoBigliettoUnico.setText(""); }
                });
                campoCognomePasseggeroUnico.getDocument().addDocumentListener(new DocumentListener() {
                    public void insertUpdate(DocumentEvent e) { campoBigliettoUnico.setText(""); }
                    public void removeUpdate(DocumentEvent e) { campoBigliettoUnico.setText(""); }
                    public void changedUpdate(DocumentEvent e) { campoBigliettoUnico.setText(""); }
                });

                formPanel1.add(campoBigliettoUnico);
                formPanel1.add(campoNomePasseggeroUnico);
                formPanel1.add(campoCognomePasseggeroUnico);

                JButton cercaButton = creaBottoneConAzione("Cerca", () -> {
                    try {
                        UtenteGenericoImplementazionePostgresDAO daoUtenteGen = new UtenteGenericoImplementazionePostgresDAO();
                        ArrayList<Prenotazione> risultatiRicerca = new ArrayList<>();

                        boolean ricercaPerBiglietto = !campoBigliettoUnico.getText().trim().isEmpty();
                        boolean ricercaPerNomeECognome = !campoNomePasseggeroUnico.getText().trim().isEmpty()
                                && !campoCognomePasseggeroUnico.getText().trim().isEmpty();

                        if (ricercaPerBiglietto && ricercaPerNomeECognome) {
                            JOptionPane.showMessageDialog(dialog,
                                    "Scegli solo una modalità di ricerca: o per numero biglietto o per nome e cognome.",
                                    "Errore", JOptionPane.ERROR_MESSAGE);
                            return;
                        }

                        if (ricercaPerBiglietto) {
                            int numeroBiglietto = Integer.parseInt(campoBigliettoUnico.getText().trim());
                            risultatiRicerca = daoUtenteGen.cercaPrenotazione(utente, numeroBiglietto);
                        } else if (ricercaPerNomeECognome) {
                            risultatiRicerca = daoUtenteGen.cercaPrenotazione(
                                    utente,
                                    campoNomePasseggeroUnico.getText().trim(),
                                    campoCognomePasseggeroUnico.getText().trim()
                            );
                        } else {
                            JOptionPane.showMessageDialog(dialog,
                                    "Compila almeno il numero biglietto oppure nome e cognome.",
                                    "Campi vuoti", JOptionPane.WARNING_MESSAGE);
                            return;
                        }

                        if (risultatiRicerca.isEmpty()) {
                            JOptionPane.showMessageDialog(dialog,
                                    "Nessuna prenotazione trovata.",
                                    "Risultato", JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            JPanel listaPanel = new JPanel();
                            listaPanel.setLayout(new BoxLayout(listaPanel, BoxLayout.Y_AXIS));
                            listaPanel.setBackground(new Color(43, 48, 52));

                            for (Prenotazione p : risultatiRicerca) {
                                Passeggero passeggero = p.getPasseggero();

                                String testo = String.format(
                                        "Codice Volo: %d - Biglietto: %d - %s %s (%s) - Stato: %s - Bagagli: %d",
                                        p.getVolo().getCodiceVolo(),
                                        p.getNumeroBiglietto(),
                                        passeggero.getNome(),
                                        passeggero.getCognome(),
                                        passeggero.getDataNascita(),
                                        p.getStatoPrenotazione(),
                                        p.getListaBagagli().size()
                                );

                                JPanel riga = new JPanel(new BorderLayout());
                                riga.setBackground(new Color(60, 63, 65));
                                riga.setBorder(BorderFactory.createCompoundBorder(
                                        BorderFactory.createLineBorder(new Color(80, 80, 80)),
                                        BorderFactory.createEmptyBorder(6, 12, 6, 12)
                                ));
                                riga.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40)); // Impedisce altezza esagerata

                                label.setText(testo);
                                label.setForeground(Color.WHITE);

                                JButton modificaBtn = new JButton("Modifica");
                                modificaBtn.setFocusPainted(false);
                                modificaBtn.setBackground(new Color(255, 162, 35));
                                modificaBtn.setForeground(Color.BLACK);
                                modificaBtn.addActionListener(e2 -> mostraPopupModificaPrenotazione(p));
                                // Non impostare dimensioni fisse, lascia adattare

                                riga.add(label, BorderLayout.CENTER);
                                riga.add(modificaBtn, BorderLayout.EAST);

                                listaPanel.add(riga);
                                listaPanel.add(Box.createVerticalStrut(8)); // Spazio tra righe
                            }

                            JScrollPane scrollPane1 = new JScrollPane(listaPanel);
                            scrollPane1.setPreferredSize(new Dimension(700, 350));
                            scrollPane1.getViewport().setBackground(new Color(43, 48, 52));
                            scrollPane1.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);


                            JDialog popupRisultati = new JDialog();
                            popupRisultati.setTitle("Risultati Prenotazioni");
                            popupRisultati.setModal(true);
                            popupRisultati.setContentPane(scrollPane1);
                            popupRisultati.pack();
                            popupRisultati.setLocationRelativeTo(null);
                            popupRisultati.setVisible(true);
                        }

                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(dialog, "Numero biglietto non valido.", "Errore", JOptionPane.ERROR_MESSAGE);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(dialog, "Errore: " + ex.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
                    }
                }, dialog);

                formPanel1.add(cercaButton);
                panel.add(formPanel1);

                visualizzaVoli(d);
                break;


            case "Cerca Bagagli":
                panel.setLayout(new GridLayout(1, 1));

                JPanel formPanelCB = new JPanel(new GridLayout(3, 1, 10, 10));
                formPanelCB.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
                formPanelCB.setBackground(new Color(43, 48, 52));

                JTextField campoIdBagaglio = creaCampo("ID Bagaglio (opzionale)");
                JTextField campoNumeroPrenotazione = creaCampo("Numero Prenotazione (opzionale)");

                // Flag per evitare trigger incrociati
                final boolean[] bloccoModifica = {false};

                // Sincronizzazione dei campi
                DocumentListener listener = new DocumentListener() {
                    public void insertUpdate(DocumentEvent e) { aggiorna(); }
                    public void removeUpdate(DocumentEvent e) { aggiorna(); }
                    public void changedUpdate(DocumentEvent e) { aggiorna(); }

                    private void aggiorna() {
                        if (bloccoModifica[0]) return;
                        bloccoModifica[0] = true;
                        if (!campoIdBagaglio.getText().trim().isEmpty()) {
                            campoNumeroPrenotazione.setText("");
                        } else if (!campoNumeroPrenotazione.getText().trim().isEmpty()) {
                            campoIdBagaglio.setText("");
                        }
                        bloccoModifica[0] = false;
                    }
                };

                campoIdBagaglio.getDocument().addDocumentListener(listener);
                campoNumeroPrenotazione.getDocument().addDocumentListener(listener);

                formPanelCB.add(campoIdBagaglio);
                formPanelCB.add(campoNumeroPrenotazione);

                JButton cercaBagagliButton = creaBottoneConAzione("Cerca", () -> {
                    try {
                        UtenteGenericoImplementazionePostgresDAO dao = new UtenteGenericoImplementazionePostgresDAO();
                        ArrayList<Bagaglio> risultatiRicerca = new ArrayList<>();

                        boolean cercaPerId = !campoIdBagaglio.getText().trim().isEmpty();
                        boolean cercaPerPrenotazione = !campoNumeroPrenotazione.getText().trim().isEmpty();

                        if (cercaPerId && cercaPerPrenotazione) {
                            JOptionPane.showMessageDialog(dialog, "Inserisci solo uno dei due campi.", "Errore", JOptionPane.ERROR_MESSAGE);
                            return;
                        }

                        if (cercaPerId) {
                            Bagaglio b = new Bagaglio();
                            b.setCodiceBagaglio(Integer.parseInt(campoIdBagaglio.getText().trim()));
                            risultatiRicerca = dao.cercaBagaglio(b, utente);
                        } else if (cercaPerPrenotazione) {
                            Prenotazione p = new Prenotazione();
                            p.setNumeroBiglietto(Integer.parseInt(campoNumeroPrenotazione.getText().trim()));
                            risultatiRicerca = dao.cercaBagaglio(p, utente);
                        } else {
                            JOptionPane.showMessageDialog(dialog, "Inserisci almeno uno dei due campi.", "Campi vuoti", JOptionPane.WARNING_MESSAGE);
                            return;
                        }

                        if (risultatiRicerca.isEmpty()) {
                            JOptionPane.showMessageDialog(dialog, "Nessun bagaglio trovato.", "Risultato", JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            JPanel listaPanel = new JPanel();
                            listaPanel.setLayout(new BoxLayout(listaPanel, BoxLayout.Y_AXIS));
                            listaPanel.setBackground(new Color(43, 48, 52));

                            for (Bagaglio b : risultatiRicerca) {
                                String testo = String.format(
                                        "ID Bagaglio: %d - Stato: %s - Prenotazione: %d",
                                        b.getCodiceBagaglio(),
                                        b.getStatoBagaglio(),
                                        b.getPrenotazione() != null ? b.getPrenotazione().getNumeroBiglietto() : null
                                );

                                JPanel riga = new JPanel(new BorderLayout());
                                riga.setBackground(new Color(60, 63, 65));
                                riga.setBorder(BorderFactory.createCompoundBorder(
                                        BorderFactory.createLineBorder(new Color(80, 80, 80)),
                                        BorderFactory.createEmptyBorder(6, 12, 6, 12)
                                ));
                                riga.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

                                JLabel label5 = new JLabel(testo);
                                label5.setForeground(Color.WHITE);
                                riga.add(label5, BorderLayout.CENTER);

                                listaPanel.add(riga);
                                listaPanel.add(Box.createVerticalStrut(8));
                            }


                            JScrollPane scrollPane2 = new JScrollPane(listaPanel);
                            scrollPane2.setPreferredSize(new Dimension(700, 300));
                            scrollPane2.getViewport().setBackground(new Color(43, 48, 52));
                            scrollPane2.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

                            JDialog popupRisultati = new JDialog();
                            popupRisultati.setTitle("Risultati Ricerca Bagagli");
                            popupRisultati.setModal(true);
                            popupRisultati.setContentPane(scrollPane2);
                            popupRisultati.pack();
                            popupRisultati.setLocationRelativeTo(null);
                            popupRisultati.setVisible(true);
                        }

                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(dialog, "Valore numerico non valido.", "Errore", JOptionPane.ERROR_MESSAGE);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(dialog, "Errore: " + ex.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
                    }
                }, dialog);

                formPanelCB.add(cercaBagagliButton);
                panel.add(formPanelCB);
                break;

            case "Segnala Smarrimento":
                panel.setLayout(new GridLayout(1, 1));

                JPanel formPanelSS = new JPanel(new GridLayout(2, 1, 10, 10));
                formPanelSS.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
                formPanelSS.setBackground(new Color(43, 48, 52));

                JTextField codiceBagaglioField = creaCampo("Codice Bagaglio");

                JButton segnalaBtn = creaBottoneConAzione("Invia segnalazione", () -> {
                    try {
                        String input = codiceBagaglioField.getText().trim();

                        if (input.isEmpty()) {
                            JOptionPane.showMessageDialog(dialog, "Inserisci il codice del bagaglio.", "Campo vuoto", JOptionPane.WARNING_MESSAGE);
                            return;
                        }

                        int codiceBagaglio = Integer.parseInt(input);

                        Bagaglio b = new Bagaglio();
                        b.setCodiceBagaglio(codiceBagaglio);

                        UtenteGenericoImplementazionePostgresDAO dao = new UtenteGenericoImplementazionePostgresDAO();
                        dao.segnalaSmarrimento(b, utente);

                        JOptionPane.showMessageDialog(dialog, "Segnalazione inviata correttamente!", "Successo", JOptionPane.INFORMATION_MESSAGE);
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(dialog, "Il codice deve essere un numero intero.", "Errore di formato", JOptionPane.ERROR_MESSAGE);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(dialog, "Errore durante la segnalazione: " + ex.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
                    }
                }, dialog);

                formPanelSS.add(codiceBagaglioField);
                formPanelSS.add(segnalaBtn);
                panel.add(formPanelSS);

                visualizzaVoli(d);
                break;



            default:
                /*
                panel.setLayout(new BorderLayout());
                panel.add(label, BorderLayout.CENTER); */
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
        if (item == null || item.trim().isEmpty()) return;

        AmministratoreImplementazionePostgresDAO a = new AmministratoreImplementazionePostgresDAO();
        String selected = (String) d.getComboBox1().getSelectedItem();

        JPanel panel = new JPanel();
        panel.setBackground(new Color(43, 48, 52));

        d.setChoiceDialog(new JDialog());
        JDialog dialog = d.getChoiceDialog();

        switch (item) {
            /*
            case "Visualizza Voli":
                panel.setLayout(new GridLayout(1, 5, 5, 5));
                for (int i = 1; i <= 5; i++) {
                    //JButton button = creaBottoneConAzione("Volo " + i, "Hai selezionato Volo " + i, dialog);
                    //panel.add(button);
                }
                break;
                */
            case "Inserisci Volo":
                panel.setLayout(new GridLayout(1, 1));

                JPanel formPanelIV = new JPanel(new GridLayout(8, 1, 10, 10));
                formPanelIV.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
                formPanelIV.setBackground(new Color(43, 48, 52));

                // Campi input
                JTextField codiceField = creaCampo("Codice Volo");
                JTextField compagniaField = creaCampo("Compagnia");
                JTextField dataField = creaCampo("Data Partenza (yyyy-mm-dd)");
                JTextField orarioField = creaCampo("Orario Partenza (hh:mm:ss)");
                JCheckBox inArrivoCheck = new JCheckBox("In arrivo");

                JTextField localitaField = creaCampo("Destinazione"); // Cambierà in base a checkbox
                JTextField gateField = creaCampo("Numero Gate:");

                // Colori e cursori coerenti
                JTextField[] campi = {codiceField, compagniaField, dataField, orarioField, localitaField, gateField};
                for (JTextField campo : campi) {
                    campo.setForeground(Color.WHITE);
                    campo.setBackground(new Color(60, 63, 65));
                    campo.setCaretColor(Color.WHITE);
                }

                // Checkbox
                inArrivoCheck.setForeground(Color.WHITE);
                inArrivoCheck.setBackground(new Color(43, 48, 52));

                // Azione checkbox "In arrivo"
                inArrivoCheck.addActionListener(e -> {
                    boolean inArrivo = inArrivoCheck.isSelected();
                    localitaField.setText(inArrivo ? "Origine" : "Destinazione");
                    gateField.setEnabled(!inArrivo);
                    gateField.setText(inArrivo ? "-1" : "Numero Gate:");
                });

                // Bottone
                JButton inserisciBtn = creaBottoneConAzione("Inserisci", () -> {
                    try {
                        int codice = Integer.parseInt(codiceField.getText());
                        String compagnia = compagniaField.getText();
                        Date data = Date.valueOf(dataField.getText());
                        Time orario = Time.valueOf(orarioField.getText());
                        boolean inArrivo = inArrivoCheck.isSelected();
                        String localita = localitaField.getText();
                        int gate = -1;

                        if (!inArrivo) {
                            if (gateField.getText().isBlank()) {
                                JOptionPane.showMessageDialog(dialog, "Errore: gate non valido", "Errore", JOptionPane.ERROR_MESSAGE);
                                gateField.setText("Numero Gate:");
                                gateField.requestFocusInWindow();
                                return;
                            }
                            gate = Integer.parseInt(gateField.getText());
                            if (gate <= 0) {
                                JOptionPane.showMessageDialog(dialog, "Errore: il numero di gate deve essere maggiore di 0", "Errore", JOptionPane.ERROR_MESSAGE);
                                gateField.setText("Numero Gate:");
                                gateField.requestFocusInWindow();
                                return;
                            }
                        }

                        if (inArrivo) {
                            VoloInArrivo volo = new VoloInArrivo(
                                    codice, compagnia, data, orario, 0,
                                    StatoVolo.PROGRAMMATO, localita, new ArrayList<>()
                            );
                            new AmministratoreImplementazionePostgresDAO().inserisciVolo(volo);
                        } else {
                            VoloInPartenza volo = new VoloInPartenza(
                                    codice, compagnia, data, orario, 0,
                                    StatoVolo.PROGRAMMATO, localita, new ArrayList<>(), gate
                            );
                            new AmministratoreImplementazionePostgresDAO().inserisciVolo(volo);
                        }

                        JOptionPane.showMessageDialog(dialog, "Volo inserito correttamente!");
                        dialog.dispose();

                    } catch (Exception ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(dialog, "Errore: " + ex.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
                    }

                    visualizzaVoli(d);
                }, dialog);

                // Aggiunta componenti al formPanel
                formPanelIV.add(codiceField);
                formPanelIV.add(compagniaField);
                formPanelIV.add(dataField);
                formPanelIV.add(orarioField);
                formPanelIV.add(inArrivoCheck);
                formPanelIV.add(localitaField);
                formPanelIV.add(gateField);
                formPanelIV.add(inserisciBtn);

                panel.add(formPanelIV);

                // Focus su checkbox inizialmente
                SwingUtilities.invokeLater(() -> inArrivoCheck.requestFocusInWindow());

                break;


            case "Aggiorna Volo":
                ArrayList<Volo> voli = new AmministratoreImplementazionePostgresDAO().visualizzaVoli();

                JPanel listaPanel = new JPanel();
                listaPanel.setLayout(new GridLayout(voli.size(), 1, 5, 5));
                listaPanel.setBackground(new Color(43, 48, 52));

                for (Volo v : voli) {
                    JPanel riga = new JPanel(new FlowLayout(FlowLayout.LEFT));
                    riga.setBackground(new Color(107, 112, 119));

                    JLabel label = new JLabel("Volo " + v.getCodiceVolo() + " → " + v.getDestinazione() + " (" + v.getData() + ")");
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
                visualizzaVoli(d);
                return;

            case "Modifica Gate":
                panel.setLayout(new GridLayout(1, 1));

                JPanel formPanelMG = new JPanel(new GridLayout(3, 1, 10, 10));
                formPanelMG.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
                formPanelMG.setBackground(new Color(43, 48, 52));

                JTextField codiceFieldGate = creaCampo("Codice Volo");
                JTextField gateFieldGate = creaCampo("Nuovo Gate");

                // Colori coerenti
                JTextField[] campiMG = {codiceFieldGate, gateFieldGate};
                for (JTextField campo : campiMG) {
                    campo.setForeground(Color.WHITE);
                    campo.setBackground(new Color(60, 63, 65));
                    campo.setCaretColor(Color.WHITE);
                }

                JButton modificaBtn = creaBottoneConAzione("Inserisci", () -> {
                    try {
                        int codice = Integer.parseInt(codiceFieldGate.getText().trim());
                        int nuovoGate = Integer.parseInt(gateFieldGate.getText().trim());

                        if (nuovoGate <= 0) {
                            JOptionPane.showMessageDialog(dialog, "Il numero di gate deve essere maggiore di 0.", "Errore", JOptionPane.ERROR_MESSAGE);
                            return;
                        }

                        VoloInPartenza volo = new VoloInPartenza();
                        volo.setCodiceVolo(codice);
                        volo.setGate(nuovoGate);

                        new AmministratoreImplementazionePostgresDAO().modificaGate(volo);
                        JOptionPane.showMessageDialog(dialog, "Gate aggiornato correttamente.", "Successo", JOptionPane.INFORMATION_MESSAGE);

                        visualizzaVoli(d);
                        dialog.dispose();
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(dialog, "Inserisci valori numerici validi.", "Errore", JOptionPane.ERROR_MESSAGE);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(dialog, "Errore: " + ex.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
                    }
                }, dialog);

                formPanelMG.add(codiceFieldGate);
                formPanelMG.add(gateFieldGate);
                formPanelMG.add(modificaBtn);

                panel.add(formPanelMG);
                break;

            case "Cerca Bagagli":
                panel.setLayout(new GridLayout(1, 1));

                JPanel formPanelCB = new JPanel(new GridLayout(3, 1, 10, 10));
                formPanelCB.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
                formPanelCB.setBackground(new Color(43, 48, 52));

                JTextField campoIdBagaglio = creaCampo("ID Bagaglio (opzionale)");
                JTextField campoNumeroPrenotazione = creaCampo("Numero Prenotazione (opzionale)");

                // Colori coerenti
                JTextField[] campiCB = {campoIdBagaglio, campoNumeroPrenotazione};
                for (JTextField campo : campiCB) {
                    campo.setForeground(Color.WHITE);
                    campo.setBackground(new Color(60, 63, 65));
                    campo.setCaretColor(Color.WHITE);
                }

                // Evita trigger incrociati tra i due campi
                final boolean[] bloccoModifica = {false};

                campoIdBagaglio.getDocument().addDocumentListener(new DocumentListener() {
                    public void insertUpdate(DocumentEvent e) { aggiorna(campoIdBagaglio, campoNumeroPrenotazione); }
                    public void removeUpdate(DocumentEvent e) { aggiorna(campoIdBagaglio, campoNumeroPrenotazione); }
                    public void changedUpdate(DocumentEvent e) { aggiorna(campoIdBagaglio, campoNumeroPrenotazione); }
                    private void aggiorna(JTextField attivo, JTextField daSvuotare) {
                        if (bloccoModifica[0]) return;
                        if (!attivo.getText().trim().isEmpty()) {
                            bloccoModifica[0] = true;
                            daSvuotare.setText("");
                            bloccoModifica[0] = false;
                        }
                    }
                });

                campoNumeroPrenotazione.getDocument().addDocumentListener(new DocumentListener() {
                    public void insertUpdate(DocumentEvent e) { aggiorna(campoNumeroPrenotazione, campoIdBagaglio); }
                    public void removeUpdate(DocumentEvent e) { aggiorna(campoNumeroPrenotazione, campoIdBagaglio); }
                    public void changedUpdate(DocumentEvent e) { aggiorna(campoNumeroPrenotazione, campoIdBagaglio); }
                    private void aggiorna(JTextField attivo, JTextField daSvuotare) {
                        if (bloccoModifica[0]) return;
                        if (!attivo.getText().trim().isEmpty()) {
                            bloccoModifica[0] = true;
                            daSvuotare.setText("");
                            bloccoModifica[0] = false;
                        }
                    }
                });

                JButton cercaBtn = creaBottoneConAzione("Cerca", () -> {
                    try {
                        AmministratoreImplementazionePostgresDAO dao = new AmministratoreImplementazionePostgresDAO();
                        ArrayList<Bagaglio> risultatiRicerca = new ArrayList<>();

                        boolean cercaPerId = !campoIdBagaglio.getText().trim().isEmpty();
                        boolean cercaPerPrenotazione = !campoNumeroPrenotazione.getText().trim().isEmpty();

                        if (cercaPerId && cercaPerPrenotazione) {
                            JOptionPane.showMessageDialog(dialog, "Inserisci solo uno dei due campi.", "Errore", JOptionPane.ERROR_MESSAGE);
                            return;
                        }

                        if (cercaPerId) {
                            Bagaglio b = new Bagaglio();
                            b.setCodiceBagaglio(Integer.parseInt(campoIdBagaglio.getText().trim()));
                            risultatiRicerca = dao.cercaBagaglio(b, null);
                        } else if (cercaPerPrenotazione) {
                            Prenotazione p = new Prenotazione();
                            p.setNumeroBiglietto(Integer.parseInt(campoNumeroPrenotazione.getText().trim()));
                            risultatiRicerca = dao.cercaBagaglio(p, null);
                        } else {
                            JOptionPane.showMessageDialog(dialog, "Inserisci almeno uno dei due campi.", "Campi vuoti", JOptionPane.WARNING_MESSAGE);
                            return;
                        }

                        if (risultatiRicerca.isEmpty()) {
                            JOptionPane.showMessageDialog(dialog, "Nessun bagaglio trovato.", "Risultato", JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            JPanel listaPanel2 = new JPanel();
                            listaPanel2.setLayout(new BoxLayout(listaPanel2, BoxLayout.Y_AXIS));
                            listaPanel2.setBackground(new Color(43, 48, 52));

                            for (Bagaglio b : risultatiRicerca) {
                                String testo = String.format(
                                        "ID Bagaglio: %d - Stato: %s - Prenotazione: %d",
                                        b.getCodiceBagaglio(),
                                        b.getStatoBagaglio(),
                                        b.getPrenotazione() != null ? b.getPrenotazione().getNumeroBiglietto() : null
                                );

                                JPanel riga = new JPanel(new BorderLayout());
                                riga.setBackground(new Color(60, 63, 65));
                                riga.setBorder(BorderFactory.createCompoundBorder(
                                        BorderFactory.createLineBorder(new Color(80, 80, 80)),
                                        BorderFactory.createEmptyBorder(6, 12, 6, 12)
                                ));
                                riga.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

                                JLabel label = new JLabel(testo);
                                label.setForeground(Color.WHITE);
                                riga.add(label, BorderLayout.CENTER);

                                listaPanel2.add(riga);
                                listaPanel2.add(Box.createVerticalStrut(8));
                            }

                            JScrollPane scrollPane4 = new JScrollPane(listaPanel2);
                            scrollPane4.setPreferredSize(new Dimension(700, 350));
                            scrollPane4.getViewport().setBackground(new Color(43, 48, 52));
                            scrollPane4.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

                            JDialog popupRisultati = new JDialog();
                            popupRisultati.setTitle("Risultati Ricerca Bagagli");
                            popupRisultati.setModal(true);
                            popupRisultati.setContentPane(scrollPane4);
                            popupRisultati.pack();
                            popupRisultati.setLocationRelativeTo(null);
                            popupRisultati.setVisible(true);
                        }

                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(dialog, "Valore numerico non valido.", "Errore", JOptionPane.ERROR_MESSAGE);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(dialog, "Errore: " + ex.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
                    }
                }, dialog);

                formPanelCB.add(campoIdBagaglio);
                formPanelCB.add(campoNumeroPrenotazione);
                formPanelCB.add(cercaBtn);
                panel.add(formPanelCB);

                visualizzaVoli(d);
                break;


            case "Aggiorna Bagaglio":
                panel.setLayout(new GridLayout(1, 1));

                JPanel formPanelAB = new JPanel(new GridLayout(3, 1, 10, 10));
                formPanelAB.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
                formPanelAB.setBackground(new Color(43, 48, 52));

                JTextField codiceBagaglioField = creaCampo("Codice Bagaglio");
                codiceBagaglioField.setForeground(Color.WHITE);
                codiceBagaglioField.setBackground(new Color(60, 63, 65));
                codiceBagaglioField.setCaretColor(Color.WHITE);

                JComboBox<String> statoComboBox = new JComboBox<>();
                for (StatoBagaglio s : StatoBagaglio.values()) {
                    statoComboBox.addItem(s.toString());
                }
                statoComboBox.setBackground(new Color(60, 63, 65));
                statoComboBox.setForeground(Color.WHITE);

                JButton modificaBtn2 = creaBottoneConAzione("Modifica", () -> {
                    try {
                        int codice = Integer.parseInt(codiceBagaglioField.getText().trim());
                        String statoSelezionato = (String) statoComboBox.getSelectedItem();
                        StatoBagaglio stato = StatoBagaglio.fromString(statoSelezionato);

                        Bagaglio b = new Bagaglio();
                        b.setCodiceBagaglio(codice);
                        b.setStatoBagaglio(stato);

                        new AmministratoreImplementazionePostgresDAO().aggiornaBagaglio(b, stato);
                        JOptionPane.showMessageDialog(dialog, "Stato del bagaglio aggiornato correttamente.");
                    } catch (NumberFormatException e1) {
                        JOptionPane.showMessageDialog(dialog, "Codice bagaglio non valido.", "Errore", JOptionPane.ERROR_MESSAGE);
                    } catch (IllegalArgumentException ex) {
                        JOptionPane.showMessageDialog(dialog, "Errore durante l'assegnazione dello stato.", "Errore", JOptionPane.ERROR_MESSAGE);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(dialog, "Errore: " + ex.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
                    }
                }, dialog);

                formPanelAB.add(codiceBagaglioField);
                formPanelAB.add(statoComboBox);
                formPanelAB.add(modificaBtn2);
                panel.add(formPanelAB);

                visualizzaVoli(d);
                break;

            case "Visualizza Smarrimenti": {
                ArrayList<Bagaglio> listaSmarriti = new AmministratoreImplementazionePostgresDAO().visualizzaSmarrimento();

                String[] colonne = { "Codice Bagaglio", "Stato", "Numero Biglietto" };
                String[][] dati = new String[listaSmarriti.size()][3];

                for (int i = 0; i < listaSmarriti.size(); i++) {
                    Bagaglio b = listaSmarriti.get(i);
                    dati[i][0] = String.valueOf(b.getCodiceBagaglio());
                    dati[i][1] = b.getStatoBagaglio().toString();
                    dati[i][2] = String.valueOf(b.getPrenotazione().getNumeroBiglietto());
                }

                JTable tabella = new JTable(dati, colonne);
                tabella.setEnabled(false);
                tabella.setBackground(new Color(107, 112, 119));
                tabella.setForeground(Color.WHITE);
                tabella.setFont(new Font("SansSerif", Font.PLAIN, 14));
                tabella.setGridColor(Color.GRAY);

                tabella.getTableHeader().setBackground(new Color(255, 162, 35));
                tabella.getTableHeader().setForeground(Color.BLACK);
                tabella.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14));

                scrollPane = new JScrollPane(tabella);
                scrollPane.setPreferredSize(new Dimension(600, 300));
                scrollPane.getViewport().setBackground(new Color(43, 48, 52));

                JPanel contenitore = new JPanel(new GridBagLayout());
                contenitore.setBackground(new Color(43, 48, 52));

                GridBagConstraints gbc = new GridBagConstraints();
                gbc.fill = GridBagConstraints.BOTH;
                gbc.weightx = 1;
                gbc.weighty = 1;
                contenitore.add(scrollPane, gbc);

                JDialog popup = new JDialog();
                popup.setTitle("Bagagli Smarriti");
                popup.setModal(true);
                popup.setContentPane(contenitore);
                popup.pack();
                popup.setLocationRelativeTo(null);
                popup.setVisible(true);

                visualizzaVoli(d);
                return;
            }

        }

        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(d.getComboBox1());
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setContentPane(panel);
        dialog.pack();
        dialog.setLocationRelativeTo(frame);
        dialog.setVisible(true);
        visualizzaVoli(d);
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

        JPanel panel = new JPanel(new GridLayout(1, 1));
        panel.setBackground(new Color(43, 48, 52));

        JPanel formPanel = new JPanel(new GridLayout(5, 1, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        formPanel.setBackground(new Color(43, 48, 52));

        // Email / Username
        JLabel labelEmail = new JLabel("Email / Username");
        labelEmail.setForeground(Color.WHITE);
        JTextField emailField = creaCampo(null);
        emailField.setForeground(Color.WHITE);
        emailField.setBackground(new Color(60, 63, 65));
        emailField.setCaretColor(Color.WHITE);

        // Password
        JLabel labelPassword = new JLabel("Password");
        labelPassword.setForeground(Color.WHITE);
        JPasswordField passwordField = new JPasswordField();
        passwordField.setBackground(new Color(60, 63, 65));
        passwordField.setForeground(Color.WHITE);
        passwordField.setCaretColor(Color.WHITE);

        // Bottone registrazione
        JButton registrati = new JButton("Registrati");
        registrati.setBackground(new Color(255, 162, 35));
        registrati.setForeground(Color.BLACK);
        registrati.setFocusPainted(false);

        registrati.addActionListener(e -> {
            if (emailField.getText().trim().isEmpty() || passwordField.getPassword().length == 0) {
                JOptionPane.showMessageDialog(dialog, "Compila tutti i campi", "Errore", JOptionPane.ERROR_MESSAGE);
            } else {
                try {
                    boolean result = registraUtente(emailField.getText().trim(), new String(passwordField.getPassword()));
                    if (result) {
                        JOptionPane.showMessageDialog(dialog, "Registrazione effettuata con successo!", "Info", JOptionPane.INFORMATION_MESSAGE);
                        dialog.dispose();
                    } else {
                        JOptionPane.showMessageDialog(dialog, "Errore registrazione, l'utente già esiste", "Errore", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (IllegalArgumentException ex) {
                    JOptionPane.showMessageDialog(dialog, ex.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Aggiunta componenti
        formPanel.add(labelEmail);
        formPanel.add(emailField);
        formPanel.add(labelPassword);
        formPanel.add(passwordField);
        formPanel.add(registrati);

        panel.add(formPanel);

        dialog.setContentPane(panel);
        dialog.pack();
        dialog.setResizable(false);
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }


    public boolean registraUtente(String email, String password) {
        LoginImplementazionePostgresDAO tmp =  new LoginImplementazionePostgresDAO();
        return tmp.registrazione(email, password);
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

        JPanel panel = new JPanel(new GridLayout(0, 2, 10, 10));
        panel.setBackground(new Color(43, 48, 52));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Campi
        JTextField codiceField = creaCampo(String.valueOf(volo.getCodiceVolo()));
        codiceField.setEditable(false);
        JTextField compagniaField = creaCampo(volo.getCompagnia());
        JTextField dataField = creaCampo(volo.getData().toString());
        JTextField orarioField = creaCampo(volo.getOrario().toString());
        JTextField ritardoField = creaCampo(String.valueOf(volo.getRitardo()));
        JTextField statoField = creaCampo(volo.getStato().name());
        JTextField origineField = creaCampo(volo.getOrigine());
        JTextField destinazioneField = creaCampo(volo.getDestinazione());
        JTextField gateField = null;

        // Etichette bianche
        panel.add(creaEtichetta("Codice Volo:")); panel.add(codiceField);
        panel.add(creaEtichetta("Compagnia:")); panel.add(compagniaField);
        panel.add(creaEtichetta("Data (YYYY-MM-DD):")); panel.add(dataField);
        panel.add(creaEtichetta("Orario (HH:MM:SS):")); panel.add(orarioField);
        panel.add(creaEtichetta("Ritardo (minuti):")); panel.add(ritardoField);
        panel.add(creaEtichetta("Stato:")); panel.add(statoField);
        panel.add(creaEtichetta("Origine:")); panel.add(origineField);
        panel.add(creaEtichetta("Destinazione:")); panel.add(destinazioneField);

        if (volo instanceof VoloInPartenza) {
            VoloInPartenza v = (VoloInPartenza) volo;
            gateField = creaCampo(String.valueOf(v.getGate()));
            panel.add(creaEtichetta("Gate:")); panel.add(gateField);
        }

        JButton conferma = new JButton("Conferma modifica");
        conferma.setBackground(new Color(255, 162, 35));
        conferma.setForeground(Color.BLACK);

        JTextField finalGateField = gateField;

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

        panel.add(new JLabel()); // spazio vuoto
        panel.add(conferma);

        dialog.setContentPane(panel);
        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }

    // Etichetta bianca personalizzata
    private JLabel creaEtichetta(String testo) {
        JLabel label = new JLabel(testo);
        label.setForeground(Color.WHITE);
        return label;
    }


    public void login(String email, String password) {
        this.utente = null;
        this.admin = null;

        LoginImplementazionePostgresDAO loginDAO = new LoginImplementazionePostgresDAO();
        int result = loginDAO.login(email, password);

        if (result == 1) {
            this.utente = new UtenteGenerico(email, password);
        } else if (result == 2) {
            this.admin = new Amministratore(email, password);
        }
    }

    private void mostraPopupModificaPrenotazione(Prenotazione p) {
        JDialog dialog = new JDialog();
        dialog.setTitle("Modifica Prenotazione");
        dialog.setModal(true);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(43, 48, 52));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;

        JLabel docLabel = new JLabel("ID Documento:");
        docLabel.setForeground(Color.WHITE);
        panel.add(docLabel, gbc);
        gbc.gridx = 1;
        JTextField documentoField = new JTextField(p.getPasseggero().getIdDocumento());
        documentoField.setEditable(false);  // Solo lettura
        panel.add(documentoField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        JLabel nomeLabel = new JLabel("Nome:");
        nomeLabel.setForeground(Color.WHITE);
        panel.add(nomeLabel, gbc);
        gbc.gridx = 1;
        JTextField nomeField = new JTextField(p.getPasseggero().getNome());
        nomeField.setEditable(false);  // Solo lettura
        panel.add(nomeField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        JLabel cognomeLabel = new JLabel("Cognome:");
        cognomeLabel.setForeground(Color.WHITE);
        panel.add(cognomeLabel, gbc);
        gbc.gridx = 1;
        JTextField cognomeField = new JTextField(p.getPasseggero().getCognome());
        cognomeField.setEditable(false);  // Solo lettura
        panel.add(cognomeField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        JLabel bagagliLabel = new JLabel("Numero Bagagli:");
        bagagliLabel.setForeground(Color.WHITE);
        panel.add(bagagliLabel, gbc);
        gbc.gridx = 1;
        JTextField bagagliField = new JTextField(String.valueOf(p.getListaBagagli().size()));
        panel.add(bagagliField, gbc);

        // Bottone conferma
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton conferma = new JButton("Conferma");
        conferma.setBackground(new Color(255, 162, 35));
        conferma.setForeground(Color.BLACK);
        panel.add(conferma, gbc);

        conferma.addActionListener(e -> {
            try {
                // Non modificare passeggero, solo bagagli
                int nBagagli = Integer.parseInt(bagagliField.getText());
                if (nBagagli < 0) throw new NumberFormatException("Numero bagagli negativo");

                ArrayList<Bagaglio> nuoviBagagli = new ArrayList<>();
                for (int i = 0; i < nBagagli; i++) {
                    Bagaglio b = new Bagaglio();
                    b.setStatoBagaglio(StatoBagaglio.REGISTRATO);
                    nuoviBagagli.add(b);
                }

                // Modifica solo i bagagli
                new UtenteGenericoImplementazionePostgresDAO().modificaPrenotazione(
                        p, nuoviBagagli
                );

                JOptionPane.showMessageDialog(null, "Numero bagagli aggiornato con successo!");
                dialog.dispose();
            } catch (NumberFormatException nfe) {
                JOptionPane.showMessageDialog(null, "Inserisci un numero valido di bagagli.", "Errore", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Errore durante l'aggiornamento.", "Errore", JOptionPane.ERROR_MESSAGE);
            }
        });

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

    public void prenotavolo(UtenteGenericoDAO utente, Volo volo, Passeggero passeggero){
        utente.prenota_Volo(volo, passeggero);
    }

    public Prenotazione cercaPrenotazionePerNome(UtenteGenericoDAO utente, String nome){
        return utente.cerca_Prenotazione(nome);
    }

    public Prenotazione cercaPrenotazionePerCodice(UtenteGenericoDAO utente, int codice){
        return utente.cerca_Prenotazione(codice);
    }
    public void segnalaSmarrimento(UtenteGenericoDAO utente, Bagaglio bagaglio){
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
