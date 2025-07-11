package controller;

import implementazionePostgresDAO.AmministratoreImplementazionePostgresDAO;
import implementazionePostgresDAO.LoginImplementazionePostgresDAO;
import implementazionePostgresDAO.UtenteGenericoImplementazionePostgresDAO;
import model.*;
import java.awt.*;
import java.sql.Time;
import java.util.ArrayList;
import java.sql.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import gui.*;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * La classe {@code Controller} gestisce la logica applicativa dell'interfaccia grafica per un sistema
 * di gestione voli, prenotazioni, bagagli e utenti. Funziona come coordinatore tra la GUI e le DAO
 * che interagiscono con il database PostgreSQL.
 * Supporta sia operazioni lato utente (visualizzazione prenotazioni, ricerca, prenotazione voli, ecc.),
 * che lato amministratore (gestione voli, aggiornamento bagagli, gate, smarrimenti, ecc.).
 * Dipendenze principali:
 *   -@code [{mministratoreImplementazionePostgresDAO} per operazioni admin
 *   -@code {UtenteGenericoImplementazionePostgresDAO} per operazioni utente
 *   -@code {LoginImplementazionePostgresDAO} per autenticazione e registrazione
 *
 */
public class Controller {
    String fontSerif = "SansSerif";
    String codiceBagaglio = "Codice Bagaglio";
    private Amministratore admin;
    private UtenteGenerico utente;
    String tabletd = "<td>";
    String tabletr = "<tr>";
    String closetabletd = "</td>";
    String closetabletr = "</tr>";
    String cerca = "Cerca";
    String errore = "Errore: ";
    String modifica = "Modifica";
    String risultato ="Risultato";
    String codiceVolo = "Codice Volo";

    /**
     * Visualizza in una tabella tutte le prenotazioni effettuate dall'utente corrente.
     *
     * @param d dashboard utente da aggiornare
     */

    public void handlerVisualizzaPrenotazioni(DashBoardUser d) {
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
        tabella.setFont(new Font(fontSerif, Font.PLAIN, 14));
        tabella.setBackground(new Color(107, 112, 119));
        tabella.setForeground(Color.WHITE);
        tabella.setGridColor(Color.GRAY);
        tabella.getTableHeader().setFont(new Font(fontSerif, Font.PLAIN, 14));
        tabella.getTableHeader().setBackground(new Color(60, 63, 65));
        tabella.getTableHeader().setForeground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(tabella);
        scrollPane.setPreferredSize(new Dimension(600, 300));
        scrollPane.getViewport().setBackground(new Color(43, 48, 52));

        JDialog popup = new JDialog();
        popup.setTitle("Prenotazioni Effettuate");
        popup.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        popup.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        popup.setSize(650, 350);
        popup.setLocationRelativeTo(null);

        JPanel contenitore = initContenitore(scrollPane);
        popup.setContentPane(contenitore);
        popup.setVisible(true);
        visualizzaVoli(d);
    }

    /** Inizializza un pannello contenitore per il dialog delle prenotazioni.
     * Utilizza GridBagLayout per gestire la disposizione degli elementi.
     *
     * @param scrollPane pannello di scorrimento contenente la tabella delle prenotazioni
     * @return pannello contenitore configurato
     */

    private JPanel initContenitore(JScrollPane scrollPane) {
        JPanel contenitore = new JPanel(new GridBagLayout());
        contenitore.setBackground(new Color(43, 48, 52));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1;
        gbc.weighty = 1;
        contenitore.add(scrollPane, gbc);
        return contenitore;
    }

    /**
     * Costruisce il form per effettuare una nuova prenotazione e la salva nel database.
     *
     * @param panel  contenitore grafico
     * @param dialog dialog corrente
     * @param d      dashboard utente
     */

    public void handlerPrenotaVolo(JPanel panel, JDialog dialog, DashBoardUser d) {
        panel.setLayout(new GridLayout(1, 1));

        JPanel formPanel = new JPanel(new GridLayout(7, 1, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        formPanel.setBackground(new Color(43, 48, 52));

        JTextField idDocumentoField = creaCampo("Id_Documento");
        JTextField dataNascitaField = creaCampo("Data_Nascita (yyyy-mm-dd)");
        JTextField nomeField = creaCampo("Nome");
        JTextField cognomeField = creaCampo("Cognome");
        JTextField codiceVoloField = creaCampo(codiceVolo);
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
                JOptionPane.showMessageDialog(dialog, errore + ex.getMessage(), errore, JOptionPane.ERROR_MESSAGE);
            }
        }, dialog);

        formPanel.add(prenotaButton);
        panel.add(formPanel);

        visualizzaVoli(d);
    }

    /**
     * Consente la ricerca di prenotazioni dell’utente tramite numero biglietto o dati anagrafici.
     *
     * @param panel  contenitore grafico
     * @param label  etichetta su cui visualizzare il risultato testuale
     * @param dialog dialog corrente
     * @param d      dashboard utente
     */

    public void handlerCercaPrenotazione(JPanel panel, JLabel label, JDialog dialog, DashBoardUser d) {
        panel.setLayout(new GridLayout(1, 1));

        JPanel formPanel = new JPanel(new GridLayout(4, 1, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        formPanel.setBackground(new Color(43, 48, 52));

        JTextField campoBiglietto = creaCampo("Numero Biglietto (opzionale)");
        JTextField campoNome = creaCampo("Nome Passeggero (opzionale)");
        JTextField campoCognome = creaCampo("Cognome Passeggero (opzionale)");

        formPanel.add(campoBiglietto);
        formPanel.add(campoNome);
        formPanel.add(campoCognome);

        // Svuota il campo biglietto se viene scritto nome o cognome
        DocumentListener svuotaBigliettoListener = new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { campoBiglietto.setText(""); }
            public void removeUpdate(DocumentEvent e) { campoBiglietto.setText(""); }
            public void changedUpdate(DocumentEvent e) { campoBiglietto.setText(""); }
        };
        campoNome.getDocument().addDocumentListener(svuotaBigliettoListener);
        campoCognome.getDocument().addDocumentListener(svuotaBigliettoListener);

        JButton cercaBtn = creaBottoneConAzione(cerca, () -> {
            try {
                String biglietto = campoBiglietto.getText().trim();
                String nome = campoNome.getText().trim();
                String cognome = campoCognome.getText().trim();

                boolean haBiglietto = !biglietto.isEmpty();
                boolean haNomeCognome = !nome.isEmpty() && !cognome.isEmpty();

                if (haBiglietto && haNomeCognome) {
                    JOptionPane.showMessageDialog(dialog,
                            "Scegli solo una modalità di ricerca.",
                            errore, JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (!haBiglietto && !haNomeCognome) {
                    JOptionPane.showMessageDialog(dialog,
                            "Compila almeno un criterio di ricerca.",
                            "Campi vuoti", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                UtenteGenericoImplementazionePostgresDAO dao = new UtenteGenericoImplementazionePostgresDAO();
                ArrayList<Prenotazione> risultati = haBiglietto ?
                        dao.cercaPrenotazione(utente, Integer.parseInt(biglietto)) :
                        dao.cercaPrenotazione(utente, nome, cognome);

                if (risultati.isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "Nessuna prenotazione trovata.", risultato, JOptionPane.INFORMATION_MESSAGE);
                    return;
                }

                mostraPopupPrenotazioni(risultati, label);

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Numero biglietto non valido.", errore, JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, errore + ex.getMessage(), errore, JOptionPane.ERROR_MESSAGE);
            }
        }, dialog);

        formPanel.add(cercaBtn);
        panel.add(formPanel);
        visualizzaVoli(d);
    }

    /**
     * Mostra un popup con l'elenco delle prenotazioni trovate.
     * Ogni riga include un bottone per modificarne il numero di bagagli.
     *
     * @param risultati elenco delle prenotazioni trovate
     * @param label     etichetta associata alla visualizzazione testuale
     */

    private void mostraPopupPrenotazioni(ArrayList<Prenotazione> risultati, JLabel label) {
        JPanel listaPanel = new JPanel();
        listaPanel.setLayout(new BoxLayout(listaPanel, BoxLayout.Y_AXIS));
        listaPanel.setBackground(new Color(43, 48, 52));

        for (Prenotazione p : risultati) {
            JPanel riga = creaRigaPrenotazione(p, label);
            listaPanel.add(riga);
            listaPanel.add(Box.createVerticalStrut(8));
        }

        JScrollPane scrollPane = new JScrollPane(listaPanel);
        scrollPane.setPreferredSize(new Dimension(700, 350));
        scrollPane.getViewport().setBackground(new Color(43, 48, 52));
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        JDialog popup = new JDialog();
        popup.setTitle("Risultati Prenotazioni");
        popup.setModal(true);
        popup.setContentPane(scrollPane);
        popup.pack();
        popup.setLocationRelativeTo(null);
        popup.setVisible(true);
    }

    /**
     * Crea una riga grafica contenente i dati sintetici di una prenotazione e un bottone di modifica.
     *
     * @param p     prenotazione da rappresentare
     * @param label etichetta in cui mostrare il testo della riga
     * @return pannello rappresentante la riga
     */

    private JPanel creaRigaPrenotazione(Prenotazione p, JLabel label) {
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

        JPanel riga = initRiga();

        label.setText(testo);
        label.setForeground(Color.WHITE);

        JButton modificaBtn = new JButton(modifica);
        modificaBtn.setFocusPainted(false);
        modificaBtn.setBackground(new Color(255, 162, 35));
        modificaBtn.setForeground(Color.BLACK);
        modificaBtn.addActionListener(e -> mostraPopupModificaPrenotazione(p));

        riga.add(label, BorderLayout.CENTER);
        riga.add(modificaBtn, BorderLayout.EAST);

        return riga;
    }

    /** Inizializza una riga con border e colore di sfondo specifici.
     *
     * @return pannello riga inizializzato
     */

    public JPanel initRiga() {
        JPanel riga = new JPanel(new BorderLayout());
        riga.setBackground(new Color(60, 63, 65));
        riga.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(80, 80, 80)),
                BorderFactory.createEmptyBorder(6, 12, 6, 12)
        ));
        riga.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        return riga;
    }
    /**
     * Permette la ricerca di un bagaglio associato alle proprie prenotazioni.
     * La ricerca è mutuale tra ID bagaglio e numero prenotazione.
     *
     * @param panel  contenitore grafico
     * @param dialog dialog corrente
     */

    public void handlerCercaBagaglio(JPanel panel, JDialog dialog) {
        panel.setLayout(new GridLayout(1, 1));

        JPanel formPanel = new JPanel(new GridLayout(3, 1, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        formPanel.setBackground(new Color(43, 48, 52));

        JTextField idBagaglioField = creaCampo("ID Bagaglio (opzionale)");
        JTextField numeroPrenotazioneField = creaCampo("Numero Prenotazione (opzionale)");
        formPanel.add(idBagaglioField);
        formPanel.add(numeroPrenotazioneField);

        sincronizzaCampi(idBagaglioField, numeroPrenotazioneField);

        JButton cercaButton = creaBottoneConAzione(cerca, () -> {
            try {
                ArrayList<Bagaglio> risultati = eseguiRicercaBagagli(idBagaglioField, numeroPrenotazioneField);
                mostraPopupRisultati(risultati);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(dialog, "Valore numerico non valido.", errore, JOptionPane.ERROR_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(dialog, errore + e.getMessage(), errore, JOptionPane.ERROR_MESSAGE);
            }
        }, dialog);

        formPanel.add(cercaButton);
        panel.add(formPanel);
    }

    /**
     * Sincronizza due campi testo in modo che solo uno dei due possa contenere testo alla volta.
     * Quando l'utente scrive nel primo campo, il secondo viene svuotato automaticamente, e viceversa.
     *
     * @param campo1 primo campo di testo
     * @param campo2 secondo campo di testo
     */

    private void sincronizzaCampi(JTextField campo1, JTextField campo2) {
        final boolean[] blocco = {false};
        DocumentListener listener = new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { aggiorna(); }
            public void removeUpdate(DocumentEvent e) { aggiorna(); }
            public void changedUpdate(DocumentEvent e) { aggiorna(); }

            private void aggiorna() {
                if (blocco[0]) return;
                blocco[0] = true;
                if (!campo1.getText().trim().isEmpty()) campo2.setText("");
                if (!campo2.getText().trim().isEmpty()) campo1.setText("");
                blocco[0] = false;
            }
        };
        campo1.getDocument().addDocumentListener(listener);
        campo2.getDocument().addDocumentListener(listener);
    }

    /** Inzializza una Lista di bagagli in un pannello con righe.
     *
     * @param lista lista di bagagli da visualizzare
     * @return pannello contenente le righe dei bagagli
     */

    private JPanel initListaPanel(ArrayList<Bagaglio> lista) {
        JPanel listaPanel = new JPanel();
        listaPanel.setLayout(new BoxLayout(listaPanel, BoxLayout.Y_AXIS));
        listaPanel.setBackground(new Color(43, 48, 52));

        for (Bagaglio b : lista) {
            String testo = String.format(
                    "ID Bagaglio: %d - Stato: %s - Prenotazione: %d",
                    b.getCodiceBagaglio(),
                    b.getStatoBagaglio(),
                    b.getPrenotazione() != null ? b.getPrenotazione().getNumeroBiglietto() : null
            );

            JPanel riga = initRiga();

            JLabel label = new JLabel(testo);
            label.setForeground(Color.WHITE);
            riga.add(label, BorderLayout.CENTER);

            listaPanel.add(riga);
            listaPanel.add(Box.createVerticalStrut(8));
        }
        return listaPanel;
    }

    /** Inizializza un pannello di scorrimento per visualizzare i risultati della ricerca bagagli.
     *
     * @param listaPanel pannello contenente le righe dei bagagli trovati
     * @return JScrollPane configurato con il pannello dei risultati
     */

    private JScrollPane initScrollPanel(JPanel listaPanel) {
        JScrollPane scrollPane = new JScrollPane(listaPanel);
        scrollPane.setPreferredSize(new Dimension(700, 300));
        scrollPane.getViewport().setBackground(new Color(43, 48, 52));
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        JDialog popup = new JDialog();
        popup.setTitle("Risultati Ricerca Bagagli");
        popup.setModal(true);
        popup.setContentPane(scrollPane);
        popup.pack();
        popup.setLocationRelativeTo(null);
        popup.setVisible(true);
        return scrollPane;
    }

    /**
     * Mostra i risultati di una ricerca bagagli per utente in una finestra modale.
     *
     * @param lista lista di bagagli da visualizzare
     */

    private void mostraPopupRisultati(ArrayList<Bagaglio> lista) {
        if (lista.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Nessun bagaglio trovato.", risultato, JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        JPanel listaPanel = initListaPanel(lista);

        JScrollPane scrollPane =  initScrollPanel(listaPanel);
        scrollPane.setVisible(true);
    }

    /**
     * Consente all’utente autenticato di segnalare lo smarrimento di un proprio bagaglio.
     *
     * @param panel  contenitore grafico
     * @param dialog dialog corrente
     * @param d      dashboard utente
     */

    public void handlerSegnalaSmarrimento(JPanel panel, JDialog dialog, DashBoardUser d) {
        panel.setLayout(new GridLayout(1, 1));

        JPanel formPanelSS = new JPanel(new GridLayout(2, 1, 10, 10));
        formPanelSS.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        formPanelSS.setBackground(new Color(43, 48, 52));

        JTextField codiceBagaglioField = creaCampo(codiceBagaglio);

        JButton segnalaBtn = creaBottoneConAzione("Invia segnalazione", () -> {
            try {
                String input = codiceBagaglioField.getText().trim();

                if (input.isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "Inserisci il codice del bagaglio.", "Campo vuoto", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                int codiceBagaglioS = Integer.parseInt(input);

                Bagaglio b = new Bagaglio();
                b.setCodiceBagaglio(codiceBagaglioS);

                UtenteGenericoImplementazionePostgresDAO dao = new UtenteGenericoImplementazionePostgresDAO();
                dao.segnalaSmarrimento(b, utente);

                JOptionPane.showMessageDialog(dialog, "Segnalazione inviata correttamente!", "Successo", JOptionPane.INFORMATION_MESSAGE);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Il codice deve essere un numero intero.", "Errore di formato", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Errore durante la segnalazione: " + ex.getMessage(), errore, JOptionPane.ERROR_MESSAGE);
            }
        }, dialog);

        formPanelSS.add(codiceBagaglioField);
        formPanelSS.add(segnalaBtn);
        panel.add(formPanelSS);

        visualizzaVoli(d);
    }
    public void handlerCercaVolo(JPanel panel, JDialog dialog) {
        panel.setLayout(new GridLayout(1, 1));
        JPanel formPanel = new JPanel(new GridLayout(4, 1, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        formPanel.setBackground(new Color(43, 48, 52));

        JTextField codiceVoloField = creaCampo("Codice Volo (opzionale)");
        JTextField origineField = creaCampo("Origine (opzionale)");
        JTextField destinazioneField = creaCampo("Destinazione (opzionale)");

        sincronizzaCampi(codiceVoloField, origineField);
        sincronizzaCampi(codiceVoloField, destinazioneField);

        JButton cercaBtn = creaBottoneConAzione(cerca, () ->
                        eseguiRicercaVolo(codiceVoloField, origineField, destinazioneField, dialog),
                dialog
        );

        formPanel.add(codiceVoloField);
        formPanel.add(origineField);
        formPanel.add(destinazioneField);
        formPanel.add(cercaBtn);
        panel.add(formPanel);
    }

    private void eseguiRicercaVolo(JTextField codiceField, JTextField origineField, JTextField destinazioneField, JDialog dialog) {
        try {
            String codStr = codiceField.getText().trim();
            String origine = origineField.getText().trim();
            String destinazione = destinazioneField.getText().trim();

            boolean cercaPerCodice = !codStr.isEmpty();
            boolean cercaPerOrigDest = !origine.isEmpty() && !destinazione.isEmpty();

            if (cercaPerCodice && cercaPerOrigDest) {
                JOptionPane.showMessageDialog(dialog, "Compila solo una modalità di ricerca.", errore, JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!cercaPerCodice && !cercaPerOrigDest) {
                JOptionPane.showMessageDialog(dialog, "Inserisci almeno un criterio di ricerca.", "Campi vuoti", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Volo v = new Volo();
            if (cercaPerCodice) {
                int codice;
                try {
                    codice = Integer.parseInt(codStr);
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(dialog, "Il codice volo deve essere un numero intero.", errore, JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (codice <= 0) {
                    JOptionPane.showMessageDialog(dialog, "Il codice volo deve essere un numero positivo.", errore, JOptionPane.ERROR_MESSAGE);
                    return;
                }

                v.setCodiceVolo(codice);
            } else {
                v.setOrigine(origine);
                v.setDestinazione(destinazione);
            }

            ArrayList<Volo> risultati = new UtenteGenericoImplementazionePostgresDAO().cercaVolo(v);

            if (risultati.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Nessun volo trovato.", "Risultato", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            mostraRisultatiNelPopup(risultati, dialog);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(dialog, errore + ex.getMessage(), errore, JOptionPane.ERROR_MESSAGE);
        }
    }

    private void mostraRisultatiNelPopup(ArrayList<Volo> voli, JDialog parentDialog) {
        JDialog popup = new JDialog(parentDialog, "Risultati Ricerca Volo", true);
        popup.setSize(700, 500);
        popup.setLocationRelativeTo(parentDialog);
        popup.setResizable(false);
        popup.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        JPanel contenuto = new JPanel(new BorderLayout());
        contenuto.setBackground(new Color(43, 48, 52));
        contenuto.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JTextArea area = new JTextArea();
        area.setEditable(false);
        area.setFont(new Font("Monospaced", Font.PLAIN, 14));
        area.setBackground(new Color(43, 48, 52));
        area.setForeground(Color.WHITE);
        area.setBorder(null);

        StringBuilder sb = new StringBuilder();
        for (Volo v : voli) {
            sb.append(costruisciDettagliVolo(v)).append("\n------------------------------\n");
        }
        area.setText(sb.toString());

        JScrollPane scrollPane = new JScrollPane(area);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        contenuto.add(scrollPane, BorderLayout.CENTER);
        popup.setContentPane(contenuto);
        popup.setVisible(true);
    }

    private String costruisciDettagliVolo(Volo v) {
        StringBuilder msg = new StringBuilder();
        msg.append("Codice: ").append(v.getCodiceVolo()).append("\n")
                .append("Compagnia: ").append(v.getCompagnia()).append("\n")
                .append("Data: ").append(v.getData()).append("\n")
                .append("Orario: ").append(v.getOrario()).append("\n")
                .append("Ritardo: ").append(v.getRitardo()).append(" min\n")
                .append("Stato: ").append(v.getStato()).append("\n")
                .append("Origine: ").append(v.getOrigine()).append("\n")
                .append("Destinazione: ").append(v.getDestinazione()).append("\n");

        if (v instanceof VoloInPartenza vpart) {
            msg.append("Gate: ").append(vpart.getGate()).append("\n");
        }

        return msg.toString();
    }

    /**
     * Gestisce la selezione di un'azione dall'interfaccia utente.
     * In base all'elemento selezionato, apre il dialog appropriato per l'azione richiesta.
     *
     * @param item elemento selezionato dalla combo box
     * @param d    dashboard utente corrente
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
                handlerVisualizzaPrenotazioni(d);
                return;


            case "Prenota Volo":
                handlerPrenotaVolo(panel, dialog, d);
                break;

            case "Cerca Prenotazione":
                handlerCercaPrenotazione(panel, label, dialog, d);
                break;


            case "Cerca Bagagli":
                handlerCercaBagaglio(panel, dialog);
                break;

            case "Segnala Smarrimento":
                handlerSegnalaSmarrimento(panel, dialog, d);
                break;

            case "Cerca Volo":
                handlerCercaVolo(panel, dialog);
                break;




            default:
        }

        // Configura e mostra il dialog modale centrato rispetto alla finestra padre.
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(d.getComboBox1());
        dialog.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        dialog.setContentPane(panel);
        dialog.pack();
        dialog.setLocationRelativeTo(frame);
        dialog.setVisible(true);
    }

    /**
     * Crea e inserisce un nuovo volo nel database tramite DAO.
     * Supporta voli in arrivo e in partenza con campi condizionati.
     *
     * @param panel  contenitore grafico
     * @param dialog dialog corrente
     * @param d      dashboard admin
     */

    public void handlerInserisciVolo(JPanel panel, JDialog dialog, DashBoardAdmin d) {
        panel.setLayout(new GridLayout(1, 1));

        JPanel formPanel = creaFormPanel();

        JTextField codiceField = creaCampoStandard(codiceVolo);
        JTextField compagniaField = creaCampoStandard("Compagnia");
        JTextField dataField = creaCampoStandard("Data Partenza (yyyy-mm-dd)");
        JTextField orarioField = creaCampoStandard("Orario Partenza (hh:mm:ss)");
        JCheckBox inArrivoCheck = creaCheckbox();
        JTextField localitaField = creaCampoStandard("Destinazione");
        JTextField gateField = creaCampoStandard("Numero Gate:");

        configuraCheckbox(inArrivoCheck, localitaField, gateField);

        JButton inserisciBtn = creaBottoneConAzione("Inserisci", () -> {
            try {
                Volo volo = costruisciVoloDaInput(
                        codiceField, compagniaField, dataField, orarioField,
                        inArrivoCheck.isSelected(), localitaField, gateField
                );

                new AmministratoreImplementazionePostgresDAO().inserisciVolo(volo);

                JOptionPane.showMessageDialog(dialog, "Volo inserito correttamente!");
                dialog.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, errore + ex.getMessage(), errore, JOptionPane.ERROR_MESSAGE);
            }

            visualizzaVoli(d);
        }, dialog);

        aggiungiComponentiForm(formPanel, codiceField, compagniaField, dataField, orarioField,
                inArrivoCheck, localitaField, gateField, inserisciBtn);

        panel.add(formPanel);
        SwingUtilities.invokeLater(inArrivoCheck::requestFocusInWindow);
    }

    /**
     * Crea un pannello verticale a una colonna per moduli grafici.
     * Versione fissa con 8 righe.
     *
     * @return pannello form configurato
     */

    private JPanel creaFormPanel() {
        JPanel form = new JPanel(new GridLayout(8, 1, 10, 10));
        form.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        form.setBackground(new Color(43, 48, 52));
        return form;
    }

    /**
     * Crea un campo di immissione con stile coerente all'interfaccia scura (sfondo scuro e testo bianco).
     *
     * @param placeholder testo iniziale o suggerimento
     * @return campo di testo configurato
     */

    private JTextField creaCampoStandard(String placeholder) {
        JTextField campo = creaCampo(placeholder);
        campo.setForeground(Color.WHITE);
        campo.setBackground(new Color(60, 63, 65));
        campo.setCaretColor(Color.WHITE);
        return campo;
    }

    /**
     * Crea una checkbox "In Arrivo" con stile grafico coerente.
     *
     * @return checkbox configurata
     */
    private JCheckBox creaCheckbox() {
        JCheckBox check = new JCheckBox("In Arrivo");
        check.setForeground(Color.WHITE);
        check.setBackground(new Color(43, 48, 52));
        return check;
    }

    /**
     * Configura il comportamento dinamico della checkbox "In arrivo".
     * Modifica i campi località e gate in base alla selezione.
     *
     * @param check          checkbox "In arrivo"
     * @param localitaField  campo località da aggiornare
     * @param gateField      campo gate da abilitare/disabilitare
     */

    private void configuraCheckbox(JCheckBox check, JTextField localitaField, JTextField gateField) {
        check.addActionListener(e -> {
            boolean inArrivo = check.isSelected();
            localitaField.setText(inArrivo ? "Origine" : "Destinazione");
            gateField.setEnabled(!inArrivo);
            gateField.setText(inArrivo ? "-1" : "Numero Gate:");
        });
    }

    /**
     * Costruisce un oggetto {@code Volo} (in arrivo o in partenza) partendo dai valori
     * inseriti in un form grafico.
     *
     * @param codiceField     campo codice volo
     * @param compagniaField  campo compagnia aerea
     * @param dataField       campo data partenza
     * @param orarioField     campo orario partenza
     * @param inArrivo        flag che indica se è un volo in arrivo
     * @param localitaField   campo origine o destinazione
     * @param gateField       campo numero gate (solo per partenza)
     * @return oggetto {@code VoloInArrivo} o {@code VoloInPartenza}
     * @throws IllegalArgumentException se i dati sono inconsistenti o incompleti
     */
    private Volo costruisciVoloDaInput(JTextField codiceField, JTextField compagniaField, JTextField dataField,
                                       JTextField orarioField, boolean inArrivo, JTextField localitaField,
                                       JTextField gateField) {
        int codice = Integer.parseInt(codiceField.getText());
        String compagnia = compagniaField.getText();
        Date data = Date.valueOf(dataField.getText());
        Time orario = Time.valueOf(orarioField.getText());
        String localita = localitaField.getText();
        int gate = -1;

        if (!inArrivo) {
            if (gateField.getText().isBlank()) {
                throw new IllegalArgumentException(errore + "gate non valido");
            }
            gate = Integer.parseInt(gateField.getText());
            if (gate <= 0) {
                throw new IllegalArgumentException(errore + "il numero di gate deve essere maggiore di 0");
            }
        }

        if (inArrivo) {
            return new VoloInArrivo(codice, compagnia, data, orario, 0, StatoVolo.PROGRAMMATO, localita, new ArrayList<>());
        } else {
            return new VoloInPartenza(codice, compagnia, data, orario, 0, StatoVolo.PROGRAMMATO, localita, new ArrayList<>(), gate);
        }
    }

    /**
     * Aggiunge in ordine tutti i componenti forniti al pannello specificato.
     *
     * @param form        pannello contenitore
     * @param componenti  componenti da aggiungere
     */

    private void aggiungiComponentiForm(JPanel form, JComponent... componenti) {
        for (JComponent c : componenti) {
            form.add(c);
        }
    }

    /**
     * Mostra la lista dei voli e consente di selezionare un volo da modificare.
     *
     * @param d dashboard admin
     */

    public void handlerAggiornaVolo(DashBoardAdmin d) {
        ArrayList<Volo> voli = new AmministratoreImplementazionePostgresDAO().visualizzaVoli();

        JPanel listaPanel = new JPanel();
        listaPanel.setLayout(new GridLayout(voli.size(), 1, 5, 5));
        listaPanel.setBackground(new Color(43, 48, 52));

        for (Volo v : voli) {
            JPanel riga = getRiga(d, v);
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
    }

    /**
     * Crea una riga grafica per un volo, con etichetta e bottone di modifica.
     *
     * @param d dashboard admin corrente
     * @param v volo da rappresentare
     * @return pannello riga configurato
     */

    private JPanel getRiga(DashBoardAdmin d, Volo v) {
        JPanel riga = new JPanel(new FlowLayout(FlowLayout.LEFT));
        riga.setBackground(new Color(107, 112, 119));

        JLabel label = new JLabel("Volo " + v.getCodiceVolo() + " → " + v.getDestinazione() + " (" + v.getData() + ")");
        label.setForeground(Color.WHITE);
        JButton modificaBtn = getJButton(d, v);

        riga.add(label);
        riga.add(modificaBtn);
        return riga;
    }

    /**
     * Crea un bottone di modifica per un volo, che apre un popup per modificarne i dettagli.
     *
     * @param d dashboard admin corrente
     * @param v volo da modificare
     * @return bottone configurato
     */

    private JButton getJButton(DashBoardAdmin d, Volo v) {
        JButton modificaBtn = new JButton(modifica);
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
        return modificaBtn;
    }

    /**
     * Permette la modifica del gate di un volo in partenza.
     *
     * @param panel  contenitore grafico
     * @param dialog dialog corrente
     * @param d      dashboard admin
     */

    public void handlerModificaGate(JPanel panel, JDialog dialog, DashBoardAdmin d) {
        panel.setLayout(new GridLayout(1, 1));

        JPanel formPanelMG = initFormPanelAB();

        JTextField codiceFieldGate = creaCampo(codiceVolo);
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
                    JOptionPane.showMessageDialog(dialog, "Il numero di gate deve essere maggiore di 0.", errore, JOptionPane.ERROR_MESSAGE);
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
                JOptionPane.showMessageDialog(dialog, "Inserisci valori numerici validi.", errore, JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, errore + ex.getMessage(), errore, JOptionPane.ERROR_MESSAGE);
            }
        }, dialog);

        formPanelMG.add(codiceFieldGate);
        formPanelMG.add(gateFieldGate);
        formPanelMG.add(modificaBtn);

        panel.add(formPanelMG);
    }

    /**
     * Ricerca bagagli tramite ID o numero prenotazione, con risultati visualizzati in tabella.
     *
     * @param panel  contenitore grafico
     * @param dialog dialog corrente
     * @param d      dashboard admin
     */

    public void handlerCercaBagagloAdmin(JPanel panel, JDialog dialog, DashBoardAdmin d) {
        panel.setLayout(new GridLayout(1, 1));

        JPanel formPanel = creaFormPanel2();
        JTextField campoIdBagaglio = creaCampoStandard("ID Bagaglio (opzionale)");
        JTextField campoNumeroPrenotazione = creaCampoStandard("Numero Prenotazione (opzionale)");
        sincronizzaCampi(campoIdBagaglio, campoNumeroPrenotazione);

        JButton cercaBtn = creaBottoneConAzione(cerca, () -> {
            try {
                ArrayList<Bagaglio> risultati = eseguiRicercaBagagli(campoIdBagaglio, campoNumeroPrenotazione);
                mostraRisultatiBagagli(risultati, dialog);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Valore numerico non valido", errore, JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, errore, errore, JOptionPane.ERROR_MESSAGE);
            }
        }, dialog);

        formPanel.add(campoIdBagaglio);
        formPanel.add(campoNumeroPrenotazione);
        formPanel.add(cercaBtn);

        panel.add(formPanel);
        visualizzaVoli(d);
    }

    /**
     * Crea un pannello verticale a una colonna con numero righe personalizzato.
     *
     * @return pannello form configurato
     */


    private JPanel creaFormPanel2() {
        JPanel form = new JPanel(new GridLayout(3, 1, 10, 10));
        form.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        form.setBackground(new Color(43, 48, 52));
        return form;
    }

    /**
     * Esegue la ricerca di bagagli tramite ID o numero prenotazione.
     * Lancia eccezioni se i campi sono entrambi vuoti o entrambi compilati.
     *
     * @param idField             campo ID bagaglio
     * @param prenotazioneField  campo numero prenotazione
     * @return lista di bagagli trovati
     * @throws IllegalArgumentException se i campi sono incompatibili
     */

    private ArrayList<Bagaglio> eseguiRicercaBagagli(JTextField idField, JTextField prenotazioneField) {
        AmministratoreImplementazionePostgresDAO dao = new AmministratoreImplementazionePostgresDAO();
        boolean perId = !idField.getText().trim().isEmpty();
        boolean perPren = !prenotazioneField.getText().trim().isEmpty();

        if (perId && perPren) {
            throw new IllegalArgumentException("Inserisci solo uno dei due campi.");
        }
        if (!perId && !perPren) {
            throw new IllegalArgumentException("Inserisci almeno uno dei due campi.");
        }

        if (perId) {
            Bagaglio b = new Bagaglio();
            b.setCodiceBagaglio(Integer.parseInt(idField.getText().trim()));
            return dao.cercaBagaglio(b, null);
        } else {
            Prenotazione p = new Prenotazione();
            p.setNumeroBiglietto(Integer.parseInt(prenotazioneField.getText().trim()));
            return dao.cercaBagaglio(p, null);
        }
    }

    /**
     * Mostra i risultati della ricerca bagagli in una finestra a scorrimento.
     *
     * @param risultati lista di bagagli trovati
     * @param dialog dialog corrente da usare come riferimento per i messaggi
     */

    private void mostraRisultatiBagagli(ArrayList<Bagaglio> risultati, JDialog dialog) {
        if (risultati.isEmpty()) {
            JOptionPane.showMessageDialog(dialog, "Nessun bagaglio trovato.", risultato, JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        JPanel listaPanel =initListaPanel(risultati);

        JScrollPane scrollPane =  initScrollPanel(listaPanel);
        scrollPane.setVisible(true);
    }

    /**
     * Inizializza un pannello per il form di modifica bagaglio.
     * Contiene 3 righe con margini e colore di sfondo specifici.
     *
     * @return pannello form configurato
     */

    private JPanel initFormPanelAB() {
        JPanel formPanel = new JPanel(new GridLayout(3, 1, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        formPanel.setBackground(new Color(43, 48, 52));
        return formPanel;
    }

    /**
     * Consente la modifica dello stato di un bagaglio (es. REGISTRATO -> IN CONSEGNA).
     *
     * @param panel  contenitore grafico
     * @param dialog dialog corrente
     * @param d      dashboard admin
     */

    public void handlerAggiornaBagaglio(JPanel panel, JDialog dialog, DashBoardAdmin d) {
        panel.setLayout(new GridLayout(1, 1));

        JPanel formPanelAB = initFormPanelAB();

        JTextField codiceBagaglioField = creaCampo(codiceBagaglio);
        codiceBagaglioField.setForeground(Color.WHITE);
        codiceBagaglioField.setBackground(new Color(60, 63, 65));
        codiceBagaglioField.setCaretColor(Color.WHITE);

        JComboBox<String> statoComboBox = new JComboBox<>();
        for (StatoBagaglio s : StatoBagaglio.values()) {
            statoComboBox.addItem(s.toString());
        }
        statoComboBox.setBackground(new Color(60, 63, 65));
        statoComboBox.setForeground(Color.WHITE);

        JButton modificaBtn2 = creaBottoneConAzione(modifica, () -> {
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
                JOptionPane.showMessageDialog(dialog, "Codice bagaglio non valido.", errore, JOptionPane.ERROR_MESSAGE);
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(dialog, "Errore durante l'assegnazione dello stato.", errore, JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Errore imprevisto durante l'aggiornamento dello stato del bagaglio", ex);
                JOptionPane.showMessageDialog(dialog, errore + ex.getMessage(), errore, JOptionPane.ERROR_MESSAGE);
            }

        }, dialog);

        formPanelAB.add(codiceBagaglioField);
        formPanelAB.add(statoComboBox);
        formPanelAB.add(modificaBtn2);
        panel.add(formPanelAB);

        visualizzaVoli(d);
    }

    /**
     * Mostra la lista dei bagagli smarriti segnalati dagli utenti.
     *
     * @param d dashboard admin
     */

    public void handlerVisualizzaSmarrimenti(DashBoardAdmin d) {
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
        tabella.setFont(new Font(fontSerif, Font.PLAIN, 14));
        tabella.setGridColor(Color.GRAY);

        tabella.getTableHeader().setBackground(new Color(255, 162, 35));
        tabella.getTableHeader().setForeground(Color.BLACK);
        tabella.getTableHeader().setFont(new Font(fontSerif, Font.BOLD, 14));

        JScrollPane scrollPane = new JScrollPane(tabella);
        scrollPane.setPreferredSize(new Dimension(600, 300));
        scrollPane.getViewport().setBackground(new Color(43, 48, 52));

        JPanel contenitore = initContenitore(scrollPane);

        JDialog popup = new JDialog();
        popup.setTitle("Bagagli Smarriti");
        popup.setModal(true);
        popup.setContentPane(contenitore);
        popup.pack();
        popup.setLocationRelativeTo(null);
        popup.setVisible(true);

        visualizzaVoli(d);
    }

    /** Metodo che gestisce la selezione di un elemento dal menu a tendina della dashboard admin.
     * In base all'elemento selezionato, apre un dialog con le opzioni corrispondenti.
     *
     * @param item elemento selezionato
     * @param d    dashboard admin corrente
     */

    public void selectedItem(String item, DashBoardAdmin d) {
        if (item == null || item.trim().isEmpty()) return;
        JPanel panel = new JPanel();
        panel.setBackground(new Color(43, 48, 52));

        d.setChoiceDialog(new JDialog());
        JDialog dialog = d.getChoiceDialog();

        switch (item) {
            case "Inserisci Volo":
                handlerInserisciVolo(panel, dialog, d);
                break;


            case "Aggiorna Volo":
                handlerAggiornaVolo(d);
                return;

            case "Modifica Gate":
                handlerModificaGate(panel, dialog, d);
                break;

            case "Cerca Bagagli":
                handlerCercaBagagloAdmin(panel, dialog, d);
                break;

            case "Aggiorna Bagaglio":
                handlerAggiornaBagaglio(panel, dialog, d);
                break;

            case "Visualizza Smarrimenti": {
                handlerVisualizzaSmarrimenti(d);
                return;
            }
            case "Cerca Volo":
                handlerCercaVolo(panel, dialog);
                break;
            default:
        }

        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(d.getComboBox1());
        dialog.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        dialog.setContentPane(panel);
        dialog.pack();
        dialog.setLocationRelativeTo(frame);
        dialog.setVisible(true);
        visualizzaVoli(d);
    }

    /**
     * Crea un {@code JTextField} con stile grafico predefinito e colore coerente.
     *
     * @param placeholder testo iniziale o indicazione del campo
     * @return campo di testo configurato
     */

    private JTextField creaCampo(String placeholder) {
        JTextField field = new JTextField(placeholder);
        field.setBackground(new Color(107, 112, 119));
        field.setForeground(Color.WHITE);
        return field;
    }

    /**
     * Crea un bottone con testo e azione associata.
     * Dopo l'esecuzione dell'azione, chiude automaticamente il dialog specificato.
     *
     * @param testo  etichetta del bottone
     * @param azione azione da eseguire al click
     * @param dialog dialog da chiudere dopo l'esecuzione
     * @return bottone configurato
     */

    private JButton creaBottoneConAzione(String testo, Runnable azione, JDialog dialog) {
        JButton btn = new JButton(testo);
        btn.setBackground(new Color(255, 162, 35));
        btn.setForeground(Color.BLACK);
        btn.addActionListener(e -> {
            azione.run();
            dialog.dispose();
        });
        return btn;
    }


    /**
     * Restituisce l'oggetto {@code Amministratore} autenticato, se presente.
     *
     * @return amministratore loggato o {@code null}
     */
    public Amministratore getAdmin() {
        return admin;
    }
    /** Restituisce l'oggetto {@code UtenteGenerico} autenticato, se presente.
     *
     * @return utente loggato o {@code null}
     */
    public UtenteGenerico getUtente() {
        return utente;
    }

    /**
     * Imposta l'oggetto {@code UtenteGenerico} autenticato.
     *
     * @param utente utente da impostare
     */

    public void setUtente(UtenteGenerico utente) {
        this.utente = utente;
    }

    /**
     * Mostra l'interfaccia grafica per la registrazione di un nuovo utente.
     * Effettua un check di esistenza e salva su database se valido.
     */

    public void interfacciaRegistrazione() {
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
        JButton registrati = getRegistrati(emailField, passwordField, dialog);

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

    /**
     * Crea un bottone di registrazione con azione associata.
     * Controlla i campi e registra l'utente se valido.
     *
     * @param emailField   campo email/username
     * @param passwordField campo password
     * @param dialog       dialog corrente da usare per i messaggi
     * @return bottone configurato
     */

    private JButton getRegistrati(JTextField emailField, JPasswordField passwordField, JDialog dialog) {
        JButton registrati = new JButton("Registrati");
        registrati.setBackground(new Color(255, 162, 35));
        registrati.setForeground(Color.BLACK);
        registrati.setFocusPainted(false);

        registrati.addActionListener(e -> {
            if (emailField.getText().trim().isEmpty() || passwordField.getPassword().length == 0) {
                JOptionPane.showMessageDialog(dialog, "Compila tutti i campi", errore, JOptionPane.ERROR_MESSAGE);
            } else {
                try {
                    boolean result = registraUtente(emailField.getText().trim(), new String(passwordField.getPassword()));
                    if (result) {
                        JOptionPane.showMessageDialog(dialog, "Registrazione effettuata con successo!", "Info", JOptionPane.INFORMATION_MESSAGE);
                        dialog.dispose();
                    } else {
                        JOptionPane.showMessageDialog(dialog, errore + "registrazione, l'utente già esiste", errore, JOptionPane.ERROR_MESSAGE);
                    }
                } catch (IllegalArgumentException ex) {
                    JOptionPane.showMessageDialog(dialog, ex.getMessage(), errore, JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        return registrati;
    }

    /**
     * Registra un nuovo utente in PostgreSQL.
     *
     * @param email    username/email da registrare
     * @param password password associata
     * @return {@code true} se la registrazione è andata a buon fine, {@code false} se l'utente già esiste
     */

    public boolean registraUtente(String email, String password) {
        LoginImplementazionePostgresDAO tmp =  new LoginImplementazionePostgresDAO();
        return tmp.registrazione(email, password);
    }

    /**
     * Costruisce l'HTML di base per la tabella dei voli.
     *
     * @return StringBuilder contenente l'HTML della tabella
     */

    private StringBuilder tableBuild() {
        StringBuilder html = new StringBuilder("<html><body style='color:white;font-family:sans-serif;'>");
        html.append("<table border='1' cellpadding='4' cellspacing='0'>")
                .append(tabletr)
                .append("<th>Codice</th>")
                .append("<th>Compagnia</th>")
                .append("<th>Data</th>")
                .append("<th>Orario</th>")
                .append("<th>Ritardo</th>")
                .append("<th>Stato</th>")
                .append("<th>Origine</th>")
                .append("<th>Destinazione</th>")
                .append("<th>Gate</th>")
                .append(closetabletr);
        return html;
    }

    /**
     * Carica e visualizza i voli in una tabella HTML nella dashboard utente.
     *
     * @param view interfaccia grafica {@code DashBoardUser} in cui mostrare i voli
     */

    public void visualizzaVoli(DashBoardUser view) {
        UtenteGenericoImplementazionePostgresDAO adao = new UtenteGenericoImplementazionePostgresDAO();
        ArrayList<Volo> voli = adao.visualizzaVoli();

        StringBuilder html = tableBuild();

        for (Volo v : voli) {
            html.append(tabletr)
                    .append(tabletd).append(v.getCodiceVolo()).append(closetabletd)
                    .append(tabletd).append(v.getCompagnia()).append(closetabletd)
                    .append(tabletd).append(v.getData()).append(closetabletd)
                    .append(tabletd).append(v.getOrario()).append(closetabletd)
                    .append(tabletd).append(v.getRitardo()).append(closetabletd)
                    .append(tabletd).append(v.getStato()).append(closetabletd)
                    .append(tabletd).append(v.getOrigine()).append(closetabletd)
                    .append(tabletd).append(v.getDestinazione()).append(closetabletd);

            // Gate con pattern matching, ma in un blocco if
            html.append(tabletd);
            if (v instanceof VoloInPartenza vinp) {
                html.append(vinp.getGate());
            } else {
                html.append("-");
            }
            html.append(closetabletd)
                    .append(closetabletr);
        }


        html.append("</table></body></html>");

        JLabel output = view.getOutputLabel();
        output.setText(html.toString());
        output.setVisible(true);
    }

    /**
     * Carica e visualizza i voli in una tabella HTML nella dashboard admin.
     *
     * @param view interfaccia grafica {@code DashBoardAdmin} in cui mostrare i voli
     */
    public void visualizzaVoli(DashBoardAdmin view) {
        AmministratoreImplementazionePostgresDAO adao = new AmministratoreImplementazionePostgresDAO();
        ArrayList<Volo> voli = adao.visualizzaVoli();

        StringBuilder html = tableBuild();

        for (Volo v : voli) {
            html.append("<tr>")
                    .append(tabletd).append(v.getCodiceVolo()).append(closetabletd)
                    .append(tabletd).append(v.getCompagnia()).append(closetabletd)
                    .append(tabletd).append(v.getData()).append(closetabletd)
                    .append(tabletd).append(v.getOrario()).append(closetabletd)
                    .append(tabletd).append(v.getRitardo()).append(closetabletd)
                    .append(tabletd).append(v.getStato()).append(closetabletd)
                    .append(tabletd).append(v.getOrigine()).append(closetabletd)
                    .append(tabletd).append(v.getDestinazione()).append(closetabletd);

            html.append(tabletd);
            if (v instanceof VoloInPartenza voloinp) {
                html.append((voloinp.getGate()));
            } else {
                html.append("-");
            }
            html.append(closetabletd);

            html.append(closetabletr);
        }


        html.append("</table></body></html>");

        JLabel output = view.getOutputLabel();
        output.setText(html.toString());
        output.setVisible(true);
    }

    /**
     * Mostra una dialog modale per la modifica dei dati di un volo esistente.
     * Consente di aggiornare il ritardo, stato, origine/destinazione e gate (solo per voli in partenza).
     * I dati modificati vengono salvati tramite il DAO amministratore.
     *
     * @param volo il volo da modificare (istanza di {@code VoloInArrivo} o {@code VoloInPartenza})
     */


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
        compagniaField.setEditable(false);
        JTextField dataField = creaCampo(volo.getData().toString());
        dataField.setEditable(false);
        JTextField orarioField = creaCampo(volo.getOrario().toString());
        orarioField.setEditable(false);
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

        if (volo instanceof VoloInPartenza v) {
            origineField.setEditable(false);
            gateField = creaCampo(String.valueOf(v.getGate()));
            panel.add(creaEtichetta("Gate:")); panel.add(gateField);
        } else {
            destinazioneField.setEditable(false);
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
                if(origine.equals(destinazione)) {
                    throw new Mpmv(errore + "origine e destinazione non possono essere uguali");
                }
                if (volo instanceof VoloInPartenza) {
                    int gate = Integer.parseInt(finalGateField.getText());
                    if(gate <= 0) {
                        throw new Mpmv(errore + "il numero di gate deve essere maggiore di 0");
                    }
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
                JOptionPane.showMessageDialog(null, errore + ex.getMessage(), errore, JOptionPane.ERROR_MESSAGE);
            }
        });

        panel.add(new JLabel()); // spazio vuoto
        panel.add(conferma);

        dialog.setContentPane(panel);
        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }

    /**
     * Crea una {@code JLabel} con il testo fornito, utilizzando uno stile coerente
     * con l'interfaccia scura dell'applicazione.
     *
     * @param testo testo da visualizzare nell'etichetta
     * @return etichetta creata
     */

    private JLabel creaEtichetta(String testo) {
        JLabel label = new JLabel(testo);
        label.setForeground(Color.WHITE);
        return label;
    }


    /**
     * Esegue l'autenticazione verificando credenziali tramite DAO.
     * Imposta internamente {@code utente} o {@code admin} se l'accesso ha successo.
     *
     * @param email    username/email dell'utente
     * @param password password in chiaro
     */

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

    /**
     * Mostra una dialog per modificare il numero di bagagli associati a una prenotazione.
     * I campi relativi al passeggero sono in sola lettura. L'aggiornamento avviene tramite DAO utente.
     *
     * @param p prenotazione da modificare
     */

    private void mostraPopupModificaPrenotazione(Prenotazione p) {
        JDialog dialog = new JDialog();
        dialog.setTitle("Modifica Prenotazione");
        dialog.setModal(true);
        dialog.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

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
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Errore durante l'aggiornamento", ex);
                JOptionPane.showMessageDialog(null, "Errore durante l'aggiornamento.", "Errore", JOptionPane.ERROR_MESSAGE);
            }
        });

        dialog.setContentPane(panel);
        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }

    /** Crea una classe di Eccezione personalizzata per gestire errori specifici nella modifica dei voli.
     * Utilizzata per segnalare problemi legati a errori runtime nel programma.
     */

    private static class Mpmv extends RuntimeException {
        public Mpmv(String message) {
            super(message);
        }
    }
}

