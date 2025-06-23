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
                panel.setLayout(new GridLayout(0, 1, 5, 5));

                JTextField idDocumentoField = creaCampo("Id_Documento");
                JTextField dataNascitaField = creaCampo("Data_Nascita (yyyy-mm-dd)");
                JTextField nomeField = creaCampo("Nome");
                JTextField cognomeField = creaCampo("Cognome");
                JTextField codiceVoloField = creaCampo("Codice Volo");
                JTextField numeroBagagliField = creaCampo("Numero Bagagli");

                panel.add(idDocumentoField);
                panel.add(dataNascitaField);
                panel.add(nomeField);
                panel.add(cognomeField);
                panel.add(codiceVoloField);
                panel.add(numeroBagagliField);

                panel.add(creaBottoneConAzione("Prenota", () -> {
                    try {
                        Passeggero passeggero = new Passeggero();
                        passeggero.setIdDocumento(idDocumentoField.getText());
                        passeggero.setNome(nomeField.getText());
                        passeggero.setCognome(cognomeField.getText());
                        passeggero.setDataNascita(java.sql.Date.valueOf(dataNascitaField.getText()));

                        Volo volo = new Volo();
                        volo.setCodiceVolo(Integer.parseInt(codiceVoloField.getText()));

                        int numeroBagagli = Integer.parseInt(numeroBagagliField.getText());
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
                        JOptionPane.showMessageDialog(dialog, "Errore: volo non esistente");
                    }
                }, dialog));
                visualizzaVoli(d);
                break;


            case "Cerca Prenotazione":
                panel.setLayout(new GridLayout(5, 1, 5, 5));

                JTextField campoBigliettoUnico = creaCampo("Numero Biglietto (opzionale)");
                JTextField campoNomePasseggeroUnico = creaCampo("Nome Passeggero (opzionale)");
                JTextField campoCognomePasseggeroUnico = creaCampo("Cognome Passeggero (opzionale)");

                // Quando scrivo in nome o cognome → svuota campo biglietto
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

                panel.add(campoBigliettoUnico);
                panel.add(campoNomePasseggeroUnico);
                panel.add(campoCognomePasseggeroUnico);

                panel.add(creaBottoneConAzione("Cerca", () -> {
                    try {
                        UtenteGenericoImplementazionePostgresDAO daoUtenteGen = new UtenteGenericoImplementazionePostgresDAO();
                        ArrayList<Prenotazione> risultatiRicerca = new ArrayList<>();

                        boolean ricercaPerBiglietto = !campoBigliettoUnico.getText().trim().isEmpty();
                        boolean ricercaPerNomeECognome = !campoNomePasseggeroUnico.getText().trim().isEmpty()
                                && !campoCognomePasseggeroUnico.getText().trim().isEmpty();

                        if (ricercaPerBiglietto && ricercaPerNomeECognome) {
                            JOptionPane.showMessageDialog(dialog,
                                    "⚠️ Scegli solo una modalità di ricerca: o per numero biglietto o per nome e cognome.",
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
                            JPanel listaPanel = new JPanel(new GridLayout(risultatiRicerca.size(), 1, 5, 5));
                            listaPanel.setBackground(new Color(43, 48, 52));

                            for (Prenotazione p : risultatiRicerca) {
                                Passeggero passeggero = p.getPasseggero();

                                String testo = String.format(
                                        "Biglietto: %d → %s %s (%s) - Stato: %s - Bagagli: %d",
                                        p.getNumeroBiglietto(),
                                        passeggero.getNome(),
                                        passeggero.getCognome(),
                                        passeggero.getDataNascita(),
                                        p.getStatoPrenotazione(),
                                        p.getListaBagagli().size()
                                );
                                //System.out.println(passeggero.getDataNascita());

                                JPanel riga = new JPanel(new BorderLayout());
                                riga.setBackground(new Color(60, 63, 65));
                                riga.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

                                JLabel label3 = new JLabel(testo);
                                label3.setForeground(Color.WHITE);

                                JButton modificaBtn = new JButton("Modifica");
                                modificaBtn.setBackground(new Color(255, 162, 35));
                                modificaBtn.setForeground(Color.BLACK);
                                modificaBtn.addActionListener(e2 -> mostraPopupModificaPrenotazione(p));

                                riga.add(label3, BorderLayout.CENTER);
                                riga.add(modificaBtn, BorderLayout.EAST);

                                listaPanel.add(riga);
                            }

                            JScrollPane scrollPane1 = new JScrollPane(listaPanel);
                            scrollPane1.setPreferredSize(new Dimension(650, 300));
                            scrollPane1.getViewport().setBackground(new Color(43, 48, 52));

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
                }, dialog));
                visualizzaVoli(d);
                break;


            case "Cerca Bagagli":
                panel.setLayout(new GridLayout(4, 1, 5, 5));

                JTextField campoIdBagaglio = creaCampo("ID Bagaglio (opzionale)");
                JTextField campoNumeroPrenotazione = creaCampo("Numero Prenotazione (opzionale)");

                // Se scrivi in ID Bagaglio → svuota Numero Prenotazione
                campoIdBagaglio.getDocument().addDocumentListener(new DocumentListener() {
                    public void insertUpdate(DocumentEvent e) { campoNumeroPrenotazione.setText(""); }
                    public void removeUpdate(DocumentEvent e) { campoNumeroPrenotazione.setText(""); }
                    public void changedUpdate(DocumentEvent e) { campoNumeroPrenotazione.setText(""); }
                });

                // Se scrivi in Numero Prenotazione → svuota ID Bagaglio
                campoNumeroPrenotazione.getDocument().addDocumentListener(new DocumentListener() {
                    public void insertUpdate(DocumentEvent e) { campoIdBagaglio.setText(""); }
                    public void removeUpdate(DocumentEvent e) { campoIdBagaglio.setText(""); }
                    public void changedUpdate(DocumentEvent e) { campoIdBagaglio.setText(""); }
                });

                panel.add(campoIdBagaglio);
                panel.add(campoNumeroPrenotazione);

                panel.add(creaBottoneConAzione("Cerca", () -> {
                    try {
                        UtenteGenericoImplementazionePostgresDAO dao = new UtenteGenericoImplementazionePostgresDAO();
                        ArrayList<Bagaglio> risultatiRicerca = new ArrayList<>();

                        boolean cercaPerId = !campoIdBagaglio.getText().trim().isEmpty();
                        boolean cercaPerPrenotazione = !campoNumeroPrenotazione.getText().trim().isEmpty();

                        if (cercaPerId && cercaPerPrenotazione) {
                            JOptionPane.showMessageDialog(dialog, "⚠️ Inserisci solo uno dei due campi.", "Errore", JOptionPane.ERROR_MESSAGE);
                            return;
                        }

                        if (cercaPerId) {
                            Bagaglio b = new Bagaglio();
                            b.setCodiceBagaglio(Integer.parseInt(campoIdBagaglio.getText().trim()));
                            risultatiRicerca = dao.cercaBagaglio(b);
                        } else if (cercaPerPrenotazione) {
                            Prenotazione p = new Prenotazione();
                            p.setNumeroBiglietto(Integer.parseInt(campoNumeroPrenotazione.getText().trim()));
                            risultatiRicerca = dao.cercaBagaglio(p);
                        } else {
                            JOptionPane.showMessageDialog(dialog, "Inserisci almeno uno dei due campi.", "Campi vuoti", JOptionPane.WARNING_MESSAGE);
                            return;
                        }

                        if (risultatiRicerca.isEmpty()) {
                            JOptionPane.showMessageDialog(dialog, "Nessun bagaglio trovato.", "Risultato", JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            JPanel listaPanel1 = new JPanel(new GridLayout(risultatiRicerca.size(), 1, 5, 5));
                            listaPanel1.setBackground(new Color(43, 48, 52));

                            for (Bagaglio b : risultatiRicerca) {
                                String testo = String.format(
                                        "ID Bagaglio: %d - Stato: %s - Prenotazione: %d",
                                        b.getCodiceBagaglio(),
                                        b.getStatoBagaglio(),
                                        b.getPrenotazione() != null ? b.getPrenotazione().getNumeroBiglietto() : null
                                );

                                JPanel riga = new JPanel(new BorderLayout());
                                riga.setBackground(new Color(60, 63, 65));
                                riga.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

                                JLabel label4 = new JLabel(testo);
                                label4.setForeground(Color.WHITE);
                                riga.add(label4, BorderLayout.CENTER);
                                listaPanel1.add(riga);
                            }

                            JScrollPane scrollPane4 = new JScrollPane(listaPanel1);
                            scrollPane4.setPreferredSize(new Dimension(650, 300));
                            scrollPane4.getViewport().setBackground(new Color(43, 48, 52));

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
                }, dialog));

                break;


            case "Segnala Smarrimento":
                panel.setLayout(new GridLayout(2, 1, 5, 5));

                JTextField codiceBagaglioField = creaCampo("Codice Bagaglio");

                panel.add(codiceBagaglioField);

                panel.add(creaBottoneConAzione("Invia segnalazione", () -> {
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
                        dao.segnalaSmarrimento(b);

                        JOptionPane.showMessageDialog(dialog, "Segnalazione inviata correttamente!", "Successo", JOptionPane.INFORMATION_MESSAGE);
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(dialog, "Il codice deve essere un numero intero.", "Errore di formato", JOptionPane.ERROR_MESSAGE);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(dialog, "Errore durante la segnalazione: " + ex.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
                    }
                }, dialog));
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
                panel.setLayout(new GridLayout(6, 1, 5, 5));

                JTextField codiceField = creaCampo("Codice Volo");
                JTextField compagniaField = creaCampo("Compagnia");
                JTextField dataField = creaCampo("Data Partenza (yyyy-mm-dd)");
                JTextField orarioField = creaCampo("Orario Partenza (hh:mm:ss)");
                JTextField origineField = creaCampo("Origine");
                JTextField destinazioneField = creaCampo("Destinazione");
                JTextField gateField = creaCampo("Numero Gate (-1 se non ancora scelto)");

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
                }, dialog));
                visualizzaVoli(d);
                return;

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
                panel.setLayout(new GridLayout(3, 1, 5, 5));
                JTextField codiceFieldGate = creaCampo("Codice Volo");
                JTextField gateFieldGate = creaCampo("Nuovo Gate");
                panel.add(codiceFieldGate);
                panel.add(gateFieldGate);
                panel.add(creaBottoneConAzione("Inserisci", () -> {
                    VoloInPartenza volo = new VoloInPartenza();
                    volo.setCodiceVolo(Integer.parseInt(codiceFieldGate.getText()));
                    volo.setGate(Integer.parseInt(gateFieldGate.getText()));
                    new AmministratoreImplementazionePostgresDAO().modificaGate(volo);
                }, dialog));
                visualizzaVoli(d);
                return;
            case "Cerca Bagagli":
                panel.setLayout(new GridLayout(4, 1, 5, 5));

                JTextField campoIdBagaglio = creaCampo("ID Bagaglio (opzionale)");
                JTextField campoNumeroPrenotazione = creaCampo("Numero Prenotazione (opzionale)");

                // Se scrivi in ID Bagaglio → svuota Numero Prenotazione
                campoIdBagaglio.getDocument().addDocumentListener(new DocumentListener() {
                    public void insertUpdate(DocumentEvent e) { campoNumeroPrenotazione.setText(""); }
                    public void removeUpdate(DocumentEvent e) { campoNumeroPrenotazione.setText(""); }
                    public void changedUpdate(DocumentEvent e) { campoNumeroPrenotazione.setText(""); }
                });

                // Se scrivi in Numero Prenotazione → svuota ID Bagaglio
                campoNumeroPrenotazione.getDocument().addDocumentListener(new DocumentListener() {
                    public void insertUpdate(DocumentEvent e) { campoIdBagaglio.setText(""); }
                    public void removeUpdate(DocumentEvent e) { campoIdBagaglio.setText(""); }
                    public void changedUpdate(DocumentEvent e) { campoIdBagaglio.setText(""); }
                });

                panel.add(campoIdBagaglio);
                panel.add(campoNumeroPrenotazione);

                panel.add(creaBottoneConAzione("Cerca", () -> {
                    try {
                        UtenteGenericoImplementazionePostgresDAO dao = new UtenteGenericoImplementazionePostgresDAO();
                        ArrayList<Bagaglio> risultatiRicerca = new ArrayList<>();

                        boolean cercaPerId = !campoIdBagaglio.getText().trim().isEmpty();
                        boolean cercaPerPrenotazione = !campoNumeroPrenotazione.getText().trim().isEmpty();

                        if (cercaPerId && cercaPerPrenotazione) {
                            JOptionPane.showMessageDialog(dialog, "⚠️ Inserisci solo uno dei due campi.", "Errore", JOptionPane.ERROR_MESSAGE);
                            return;
                        }

                        if (cercaPerId) {
                            Bagaglio b = new Bagaglio();
                            b.setCodiceBagaglio(Integer.parseInt(campoIdBagaglio.getText().trim()));
                            risultatiRicerca = dao.cercaBagaglio(b);
                        } else if (cercaPerPrenotazione) {
                            Prenotazione p = new Prenotazione();
                            p.setNumeroBiglietto(Integer.parseInt(campoNumeroPrenotazione.getText().trim()));
                            risultatiRicerca = dao.cercaBagaglio(p);
                        } else {
                            JOptionPane.showMessageDialog(dialog, "Inserisci almeno uno dei due campi.", "Campi vuoti", JOptionPane.WARNING_MESSAGE);
                            return;
                        }

                        if (risultatiRicerca.isEmpty()) {
                            JOptionPane.showMessageDialog(dialog, "Nessun bagaglio trovato.", "Risultato", JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            JPanel listaPanel1 = new JPanel(new GridLayout(risultatiRicerca.size(), 1, 5, 5));
                            listaPanel1.setBackground(new Color(43, 48, 52));

                            for (Bagaglio b : risultatiRicerca) {
                                String testo = String.format(
                                        "ID Bagaglio: %d - Stato: %s - Prenotazione: %d",
                                        b.getCodiceBagaglio(),
                                        b.getStatoBagaglio(),
                                        b.getPrenotazione() != null ? b.getPrenotazione().getNumeroBiglietto() : null
                                );

                                JPanel riga = new JPanel(new BorderLayout());
                                riga.setBackground(new Color(60, 63, 65));
                                riga.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

                                JLabel label4 = new JLabel(testo);
                                label4.setForeground(Color.WHITE);
                                riga.add(label4, BorderLayout.CENTER);
                                listaPanel1.add(riga);
                            }

                            JScrollPane scrollPane4 = new JScrollPane(listaPanel1);
                            scrollPane4.setPreferredSize(new Dimension(650, 300));
                            scrollPane4.getViewport().setBackground(new Color(43, 48, 52));

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
                }, dialog));

                break;

            case "Aggiorna Bagaglio":
                panel.setLayout(new GridLayout(3, 1, 5, 5));

                JTextField codiceBagaglioField = creaCampo("Codice Bagaglio");
                JComboBox<String> statoComboBox = new JComboBox<>();
                for (StatoBagaglio s : StatoBagaglio.values()) {
                    statoComboBox.addItem(s.toString());
                }

                panel.add(codiceBagaglioField);
                panel.add(statoComboBox);

                panel.add(creaBottoneConAzione("Modifica", () -> {
                    Bagaglio b = new Bagaglio();
                    b.setCodiceBagaglio(Integer.parseInt(codiceBagaglioField.getText()));
                    String statoSelezionato = (String) statoComboBox.getSelectedItem();
                    try {
                        StatoBagaglio stato = StatoBagaglio.fromString(statoSelezionato);
                        b.setStatoBagaglio(stato);
                        new AmministratoreImplementazionePostgresDAO().aggiornaBagaglio(b, stato);
                    } catch (IllegalArgumentException ex) {
                        JOptionPane.showMessageDialog(panel,
                                "Errore durante l'assegnazione dello stato.",
                                "Errore", JOptionPane.ERROR_MESSAGE);
                    }
                }, dialog));
                visualizzaVoli(d);
                return;

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
        panel.setLayout(new GridLayout(5, 1, 5, 5));

        // Email / Username
        JLabel labelEmail = new JLabel("Email / Username");
        labelEmail.setForeground(Color.WHITE);
        JTextField emailField = creaCampo(null);

        // Password
        JLabel labelPassword = new JLabel("Password");
        labelPassword.setForeground(Color.WHITE);
        JPasswordField passwordField = new JPasswordField();
        passwordField.setBackground(new Color(107, 112, 119));
        passwordField.setForeground(Color.WHITE);

        // Bottone registrazione
        JButton registrati = new JButton("Registrati");
        registrati.setBackground(new Color(255, 162, 35));
        registrati.setForeground(Color.BLACK);

        registrati.addActionListener(e -> {
            if (emailField.getText().isEmpty() || passwordField.getPassword().length == 0) {
                JOptionPane.showMessageDialog(null, "Compila tutti i campi", "Errore", JOptionPane.ERROR_MESSAGE);
            } else {
                try {
                    boolean result = registraUtente(emailField.getText(), new String(passwordField.getPassword()));
                    if(result) {
                        JOptionPane.showMessageDialog(null, "Registrazione effettuata con successo!", "Info",
                                JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(null, "Errore registrazione, l'utente già esiste", "Info",
                                JOptionPane.ERROR_MESSAGE);
                    }
                    dialog.dispose();
                } catch (IllegalArgumentException ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Aggiunta componenti al pannello
        panel.add(labelEmail);
        panel.add(emailField);
        panel.add(labelPassword);
        panel.add(passwordField);
        panel.add(registrati);

        // Setup finale dialog
        dialog.setContentPane(panel);
        dialog.pack();
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

        JPanel panel = new JPanel(new GridLayout(0, 2, 5, 5));
        panel.setBackground(new Color(43, 48, 52));

        JTextField codiceField = creaCampo(String.valueOf(volo.getCodiceVolo()));
        JTextField compagniaField = creaCampo(volo.getCompagnia());
        JTextField dataField = creaCampo(volo.getData().toString()); // yyyy-mm-dd
        JTextField orarioField = creaCampo(volo.getOrario().toString()); // HH:mm:ss
        JTextField ritardoField = creaCampo(String.valueOf(volo.getRitardo()));
        JTextField statoField = creaCampo(volo.getStato().name());
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
