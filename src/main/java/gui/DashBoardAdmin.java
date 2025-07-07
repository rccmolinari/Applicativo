package gui;

import controller.Controller;

import javax.swing.*;
import java.awt.event.ItemEvent;

public class DashBoardAdmin {

    // Il pannello principale che contiene tutta la dashboard dell’admin
    private JPanel dashboardAdminPage;

    // Label per mostrare qualcosa tipo “Admin: nomeutente” o simile
    private JLabel adminLabel;

    // Pannello della home page dentro la dashboard (dove magari ci sono i contenuti principali)
    private JPanel homePage;

    // Pannello che contiene il messaggio o l’immagine di benvenuto
    private JPanel welcomePanel;

    // Il pannello specifico solo per il testo di benvenuto
    private JPanel welcomeText;

    // Etichetta testuale che mostra il benvenuto (es. “Benvenuto Admin [username]”)
    private JLabel welcomeTextLabel;

    // Bottone per uscire o logout (qui chiude semplicemente tutto il programma)
    private JButton logoutUser;

    // Menu a tendina con le azioni che l’admin può scegliere di fare (es. visualizza voli, aggiorna voli...)
    private JComboBox<String> comboBox1;

    // Label che usiamo per mostrare messaggi di output o feedback dopo un’azione
    private JLabel outputLabel;

    // Dialog modale per finestre di scelta o popup particolari, gestito dinamicamente
    private JDialog choiceDialog = null;

    // Username o ID dell’admin, usato per personalizzare la UI e identificare l’utente
    private String username;

    // Riferimento al controller, così possiamo far gestire a lui la logica quando l’utente fa qualcosa
    private Controller controller;

    // Tutti i getter e setter servono per accedere o modificare i componenti GUI da altre parti del programma

    public JComboBox<String> getComboBox1() {
        return comboBox1;
    }



    public JLabel getOutputLabel() {
        return outputLabel;
    }

    public void setOutputLabel(JLabel outputLabel) {
        this.outputLabel = outputLabel;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Qui inizializzo la dashboard per l’admin.
     * Metto le scelte nel menu a tendina e attivo i listener per rispondere alle scelte fatte.
     * Inoltre mostro l’username in alto per far capire chi è loggato.
     */
    public DashBoardAdmin(String username, Controller controller) {
        this.controller = controller;
        this.username = username;

        // Le azioni che l’admin può scegliere dalla comboBox
        String [] choices = {"", "Inserisci Volo", "Aggiorna Volo", "Modifica Gate", "Cerca Bagagli", "Aggiorna Bagaglio", "Visualizza Smarrimenti"};

        // Abilito la comboBox, la riempio con le scelte e nascondo la label output che non serve subito
        comboBox1.setEnabled(true);
        outputLabel.setVisible(false);
        comboBox1.setModel(new DefaultComboBoxModel<>(choices));

        // Quando cambia la selezione, mando la scelta al controller per far partire l’azione giusta
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


        // Faccio vedere in alto chi è loggato (l’admin)
        welcomeTextLabel.setText("ID: " + username);

        // Il bottone logout per ora chiude semplicemente tutto (da migliorare in futuro)
        logoutUser.addActionListener(e -> System.exit(0));
        controller.visualizzaVoli(this);
    }

    // Metodo per ottenere il pannello principale della dashboard, così si può inserire nel frame principale
    public JPanel getDashboardAdminPage() {
        return dashboardAdminPage;
    }

    // Per gestire il dialog di scelta da altre classi se serve aprirlo o chiuderlo
    public JDialog getChoiceDialog() {
        return choiceDialog;
    }

    public void setChoiceDialog(JDialog choiceDialog) {
        this.choiceDialog = choiceDialog;
    }

}
