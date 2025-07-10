package gui;

import controller.Controller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Classe GUI che rappresenta la dashboard dell'utente generico.
 * Permette l'interazione con le funzionalità principali: visualizzazione e prenotazione voli, gestione bagagli, ecc.
 * Comunica con il {@link controller.Controller} per l'esecuzione delle logiche applicative.
 */
public class DashBoardUser {

    private JPanel homePage;
    private JPanel welcomePanel;
    private JPanel welcomeText;
    private JButton logoutUser;
    private JLabel welcomeTextLabel;
    private JComboBox<String> comboBox1;
    private JLabel outputLabel;
    private String username;
    private JDialog choiceDialog = null;
    private Controller controller;

    /**
     * Costruttore della dashboard per utente generico.
     * Inizializza i componenti grafici, popola il menu a tendina con le azioni disponibili
     * e configura i listener per la gestione degli eventi.
     *
     * @param username   identificativo dell'utente (es. ID o nome)
     * @param controller riferimento al controller per eseguire le azioni richieste
     */
    public DashBoardUser(String username, Controller controller) {
        this.controller = controller;
        String[] choices = {"", "Visualizza Prenotazioni", "Prenota Volo", "Cerca Prenotazione", "Cerca Bagagli", "Segnala Smarrimento"};
        comboBox1.setModel(new DefaultComboBoxModel<>(choices));
        this.username = username;
        comboBox1.setEnabled(true);
        comboBox1.setBackground(new Color(107, 112, 119));
        comboBox1.setForeground(new Color(0, 0, 0));
        outputLabel.setVisible(false);

        comboBox1.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                String itemSelezionato = (String) e.getItem();
                if (!itemSelezionato.isEmpty()) {
                    controller.selectedItem(itemSelezionato, DashBoardUser.this);
                    comboBox1.setSelectedIndex(0);
                }
            }
        });

        welcomeTextLabel.setText("ID: " + username);
        logoutUser.addActionListener(e -> System.exit(0));
        controller.visualizzaVoli(this);
    }

    /**
     * Restituisce il pannello principale della GUI utente.
     *
     * @return pannello principale {@code homePage}
     */
    public JPanel getHomePage() {
        return homePage;
    }

    /**
     * Restituisce la combo box che contiene le azioni disponibili per l'utente.
     *
     * @return {@code JComboBox} con le opzioni di scelta
     */
    public JComboBox<String> getComboBox1() {
        return comboBox1;
    }

    /**
     * Restituisce la label utilizzata per mostrare messaggi o risultati dinamici all'utente.
     *
     * @return {@code JLabel} per output testuale
     */
    public JLabel getOutputLabel() {
        return outputLabel;
    }

    /**
     * Restituisce il dialog modale attualmente associato alla dashboard.
     * Utilizzato per mostrare finestre secondarie (es. popup di conferma o moduli).
     *
     * @return {@code JDialog} corrente
     */
    public JDialog getChoiceDialog() {
        return choiceDialog;
    }

    /**
     * Imposta il dialog modale da utilizzare per l'interazione secondaria dell'interfaccia.
     *
     * @param choiceDialog nuovo {@code JDialog} da associare
     */
    public void setChoiceDialog(JDialog choiceDialog) {
        this.choiceDialog = choiceDialog;
    }
}
