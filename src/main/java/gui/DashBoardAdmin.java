package gui;

import controller.Controller;

import javax.swing.*;
import java.awt.event.ItemEvent;

/**
 * Classe GUI che rappresenta la dashboard dell'amministratore.
 * Permette l'interazione con le funzionalità di gestione dei voli, bagagli e smarrimenti.
 * Si interfaccia con {@link controller.Controller} per eseguire la logica applicativa.
 */
public class DashBoardAdmin {

    private JPanel dashboardAdminPage;
    private JLabel adminLabel;
    private JPanel homePage;
    private JPanel welcomePanel;
    private JPanel welcomeText;
    private JLabel welcomeTextLabel;
    private JButton logoutUser;
    private JComboBox<String> comboBox1;
    private JLabel outputLabel;
    private JDialog choiceDialog = null;
    private String username;
    private Controller controller;

    /**
     * Costruttore della dashboard per amministratore.
     * Inizializza la GUI, imposta le azioni disponibili nella combo box,
     * mostra l'identità dell'amministratore e attiva i listener per la gestione eventi.
     *
     * @param username   identificativo dell'amministratore
     * @param controller riferimento al controller per le operazioni logiche
     */

    public DashBoardAdmin(String username, Controller controller) {
        this.controller = controller;
        this.username = username;

        String[] choices = {"", "Inserisci Volo", "Aggiorna Volo", "Modifica Gate", "Cerca Bagagli", "Aggiorna Bagaglio", "Visualizza Smarrimenti"};

        comboBox1.setEnabled(true);
        outputLabel.setVisible(false);
        comboBox1.setModel(new DefaultComboBoxModel<>(choices));

        comboBox1.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                String itemSelezionato = (String) e.getItem();
                if (!itemSelezionato.isEmpty()) {
                    controller.selectedItem(itemSelezionato, DashBoardAdmin.this);
                }
                comboBox1.setSelectedIndex(0);
                controller.visualizzaVoli(DashBoardAdmin.this);
            }
        });

        welcomeTextLabel.setText("ID: " + username);
        logoutUser.addActionListener(e -> System.exit(0));
        controller.visualizzaVoli(this);
    }

    /**
     * Restituisce il pannello principale della dashboard.
     *
     * @return {@code JPanel} principale dell'interfaccia admin
     */
    public JPanel getDashboardAdminPage() {
        return dashboardAdminPage;
    }

    /**
     * Restituisce la combo box contenente le azioni selezionabili dall'amministratore.
     *
     * @return {@code JComboBox<String>} delle scelte
     */
    public JComboBox<String> getComboBox1() {
        return comboBox1;
    }

    /**
     * Restituisce la label per l'output testuale verso l'utente.
     *
     * @return {@code JLabel} per i messaggi
     */
    public JLabel getOutputLabel() {
        return outputLabel;
    }


    /**
     * Restituisce il dialog modale attualmente utilizzato dalla dashboard.
     *
     * @return {@code JDialog} corrente
     */
    public JDialog getChoiceDialog() {
        return choiceDialog;
    }

    /**
     * Imposta il dialog modale da utilizzare nella dashboard per popup o moduli.
     *
     * @param choiceDialog nuovo {@code JDialog} da assegnare
     */
    public void setChoiceDialog(JDialog choiceDialog) {
        this.choiceDialog = choiceDialog;
    }
}