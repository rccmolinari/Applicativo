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
    String fontSerif = "SansSerif";
    String codiceBagaglio = "Codice Bagaglio";
    private Amministratore admin;
    private UtenteGenerico utente;

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
    }

    public void handlerPrenotaVolo(JPanel panel, JDialog dialog, DashBoardUser d) {
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
                JOptionPane.showMessageDialog(dialog, "Errore: " + ex.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
            }
        }, dialog);

        formPanel.add(prenotaButton);
        panel.add(formPanel);

        visualizzaVoli(d);
    }

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

        JButton cercaBtn = creaBottoneConAzione("Cerca", () -> {
            try {
                String biglietto = campoBiglietto.getText().trim();
                String nome = campoNome.getText().trim();
                String cognome = campoCognome.getText().trim();

                boolean haBiglietto = !biglietto.isEmpty();
                boolean haNomeCognome = !nome.isEmpty() && !cognome.isEmpty();

                if (haBiglietto && haNomeCognome) {
                    JOptionPane.showMessageDialog(dialog,
                            "Scegli solo una modalità di ricerca.",
                            "Errore", JOptionPane.ERROR_MESSAGE);
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
                    JOptionPane.showMessageDialog(dialog, "Nessuna prenotazione trovata.", "Risultato", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }

                mostraPopupPrenotazioni(risultati, label);

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Numero biglietto non valido.", "Errore", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Errore: " + ex.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
            }
        }, dialog);

        formPanel.add(cercaBtn);
        panel.add(formPanel);
        visualizzaVoli(d);
    }

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

        JPanel riga = new JPanel(new BorderLayout());
        riga.setBackground(new Color(60, 63, 65));
        riga.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(80, 80, 80)),
                BorderFactory.createEmptyBorder(6, 12, 6, 12)
        ));
        riga.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        label.setText(testo);
        label.setForeground(Color.WHITE);

        JButton modificaBtn = new JButton("Modifica");
        modificaBtn.setFocusPainted(false);
        modificaBtn.setBackground(new Color(255, 162, 35));
        modificaBtn.setForeground(Color.BLACK);
        modificaBtn.addActionListener(e -> mostraPopupModificaPrenotazione(p));

        riga.add(label, BorderLayout.CENTER);
        riga.add(modificaBtn, BorderLayout.EAST);

        return riga;
    }
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

        JButton cercaButton = creaBottoneConAzione("Cerca", () -> {
            try {
                ArrayList<Bagaglio> risultati = eseguiRicercaBagagli(idBagaglioField, numeroPrenotazioneField);
                mostraPopupRisultati(risultati);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(dialog, "Valore numerico non valido.", "Errore", JOptionPane.ERROR_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(dialog, "Errore: " + e.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
            }
        }, dialog);

        formPanel.add(cercaButton);
        panel.add(formPanel);
    }

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
    private void mostraPopupRisultati(ArrayList<Bagaglio> lista) {
        if (lista.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Nessun bagaglio trovato.", "Risultato", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

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

            listaPanel.add(riga);
            listaPanel.add(Box.createVerticalStrut(8));
        }

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
    }


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
                JOptionPane.showMessageDialog(dialog, "Errore durante la segnalazione: " + ex.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
            }
        }, dialog);

        formPanelSS.add(codiceBagaglioField);
        formPanelSS.add(segnalaBtn);
        panel.add(formPanelSS);

        visualizzaVoli(d);
    }
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



            default:
        }

        // Configura e mostra il dialog modale centrato rispetto alla finestra padre.
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(d.getComboBox1());
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setContentPane(panel);
        dialog.pack();
        dialog.setLocationRelativeTo(frame);
        dialog.setVisible(true);
    }

    public void handlerInserisciVolo(JPanel panel, JDialog dialog, DashBoardAdmin d) {
        panel.setLayout(new GridLayout(1, 1));

        JPanel formPanel = creaFormPanel();

        JTextField codiceField = creaCampoStandard("Codice Volo");
        JTextField compagniaField = creaCampoStandard("Compagnia");
        JTextField dataField = creaCampoStandard("Data Partenza (yyyy-mm-dd)");
        JTextField orarioField = creaCampoStandard("Orario Partenza (hh:mm:ss)");
        JCheckBox inArrivoCheck = creaCheckbox("In arrivo");
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
                JOptionPane.showMessageDialog(dialog, "Errore: " + ex.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
            }

            visualizzaVoli(d);
        }, dialog);

        aggiungiComponentiForm(formPanel, codiceField, compagniaField, dataField, orarioField,
                inArrivoCheck, localitaField, gateField, inserisciBtn);

        panel.add(formPanel);
        SwingUtilities.invokeLater(() -> inArrivoCheck.requestFocusInWindow());
    }

    private JPanel creaFormPanel() {
        JPanel form = new JPanel(new GridLayout(8, 1, 10, 10));
        form.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        form.setBackground(new Color(43, 48, 52));
        return form;
    }

    private JTextField creaCampoStandard(String placeholder) {
        JTextField campo = creaCampo(placeholder);
        campo.setForeground(Color.WHITE);
        campo.setBackground(new Color(60, 63, 65));
        campo.setCaretColor(Color.WHITE);
        return campo;
    }

    private JCheckBox creaCheckbox(String label) {
        JCheckBox check = new JCheckBox(label);
        check.setForeground(Color.WHITE);
        check.setBackground(new Color(43, 48, 52));
        return check;
    }

    private void configuraCheckbox(JCheckBox check, JTextField localitaField, JTextField gateField) {
        check.addActionListener(e -> {
            boolean inArrivo = check.isSelected();
            localitaField.setText(inArrivo ? "Origine" : "Destinazione");
            gateField.setEnabled(!inArrivo);
            gateField.setText(inArrivo ? "-1" : "Numero Gate:");
        });
    }

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
                throw new IllegalArgumentException("Errore: gate non valido");
            }
            gate = Integer.parseInt(gateField.getText());
            if (gate <= 0) {
                throw new IllegalArgumentException("Errore: il numero di gate deve essere maggiore di 0");
            }
        }

        if (inArrivo) {
            return new VoloInArrivo(codice, compagnia, data, orario, 0, StatoVolo.PROGRAMMATO, localita, new ArrayList<>());
        } else {
            return new VoloInPartenza(codice, compagnia, data, orario, 0, StatoVolo.PROGRAMMATO, localita, new ArrayList<>(), gate);
        }
    }

    private void aggiungiComponentiForm(JPanel form, JComponent... componenti) {
        for (JComponent c : componenti) {
            form.add(c);
        }
    }


    public void handlerAggiornaVolo(DashBoardAdmin d) {
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
    }

    public void handlerModificaGate(JPanel panel, JDialog dialog, DashBoardAdmin d) {
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
                JOptionPane.showMessageDialog(dialog, "Errore: " + ex.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
            }
        }, dialog);

        formPanelMG.add(codiceFieldGate);
        formPanelMG.add(gateFieldGate);
        formPanelMG.add(modificaBtn);

        panel.add(formPanelMG);
    }
    public void handlerCercaBagagloAdmin(JPanel panel, JDialog dialog, DashBoardAdmin d) {
        panel.setLayout(new GridLayout(1, 1));

        JPanel formPanel = creaFormPanel(3);
        JTextField campoIdBagaglio = creaCampoStandard("ID Bagaglio (opzionale)");
        JTextField campoNumeroPrenotazione = creaCampoStandard("Numero Prenotazione (opzionale)");
        gestisciMutuaEsclusioneCampi(campoIdBagaglio, campoNumeroPrenotazione);

        JButton cercaBtn = creaBottoneConAzione("Cerca", () -> {
            try {
                ArrayList<Bagaglio> risultati = eseguiRicercaBagagli(campoIdBagaglio, campoNumeroPrenotazione);
                mostraRisultatiBagagli(risultati, dialog);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Valore numerico non valido", "Errore", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Errore", "Errore", JOptionPane.ERROR_MESSAGE);
            }
        }, dialog);

        formPanel.add(campoIdBagaglio);
        formPanel.add(campoNumeroPrenotazione);
        formPanel.add(cercaBtn);

        panel.add(formPanel);
        visualizzaVoli(d);
    }

    private JPanel creaFormPanel(int righe) {
        JPanel form = new JPanel(new GridLayout(righe, 1, 10, 10));
        form.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        form.setBackground(new Color(43, 48, 52));
        return form;
    }


    private void gestisciMutuaEsclusioneCampi(JTextField campo1, JTextField campo2) {
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

    private void mostraRisultatiBagagli(ArrayList<Bagaglio> risultati, JDialog dialog) {
        if (risultati.isEmpty()) {
            JOptionPane.showMessageDialog(dialog, "Nessun bagaglio trovato.", "Risultato", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        JPanel listaPanel = new JPanel();
        listaPanel.setLayout(new BoxLayout(listaPanel, BoxLayout.Y_AXIS));
        listaPanel.setBackground(new Color(43, 48, 52));

        for (Bagaglio b : risultati) {
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

            listaPanel.add(riga);
            listaPanel.add(Box.createVerticalStrut(8));
        }

        JScrollPane scrollPane = new JScrollPane(listaPanel);
        scrollPane.setPreferredSize(new Dimension(700, 350));
        scrollPane.getViewport().setBackground(new Color(43, 48, 52));
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        JDialog popup = new JDialog();
        popup.setTitle("Risultati Ricerca Bagagli");
        popup.setModal(true);
        popup.setContentPane(scrollPane);
        popup.pack();
        popup.setLocationRelativeTo(null);
        popup.setVisible(true);
    }

    public void handlerAggiornaBagaglio(JPanel panel, JDialog dialog, DashBoardAdmin d) {
        panel.setLayout(new GridLayout(1, 1));

        JPanel formPanelAB = new JPanel(new GridLayout(3, 1, 10, 10));
        formPanelAB.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        formPanelAB.setBackground(new Color(43, 48, 52));

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
    }

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
    }
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
            default:
                System.out.println("Default");
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

