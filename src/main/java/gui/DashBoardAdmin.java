package gui;

import controller.Controller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

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
    private JButton LOGOUTUSER;

    // Menu a tendina con le azioni che l’admin può scegliere di fare (es. visualizza voli, aggiorna voli...)
    private JComboBox comboBox1;

    // Label che usiamo per mostrare messaggi di output o feedback dopo un’azione
    private JLabel outputLabel;

    // Dialog modale per finestre di scelta o popup particolari, gestito dinamicamente
    private JDialog choiceDialog = null;

    // Username o ID dell’admin, usato per personalizzare la UI e identificare l’utente
    private String username;

    // Riferimento al controller, così possiamo far gestire a lui la logica quando l’utente fa qualcosa
    private Controller controller;

    // Tutti i getter e setter servono per accedere o modificare i componenti GUI da altre parti del programma

    public JLabel getAdminLabel() {
        return adminLabel;
    }

    public void setAdminLabel(JLabel adminLabel) {
        this.adminLabel = adminLabel;
    }

    public JPanel getHomePage() {
        return homePage;
    }

    public void setHomePage(JPanel homePage) {
        this.homePage = homePage;
    }

    public JPanel getWelcomePanel() {
        return welcomePanel;
    }

    public void setWelcomePanel(JPanel welcomePanel) {
        this.welcomePanel = welcomePanel;
    }

    public JPanel getWelcomeText() {
        return welcomeText;
    }

    public void setWelcomeText(JPanel welcomeText) {
        this.welcomeText = welcomeText;
    }

    public JLabel getWelcomeTextLabel() {
        return welcomeTextLabel;
    }

    public void setWelcomeTextLabel(JLabel welcomeTextLabel) {
        this.welcomeTextLabel = welcomeTextLabel;
    }

    public JButton getLOGOUTUSER() {
        return LOGOUTUSER;
    }

    public void setLOGOUTUSER(JButton LOGOUTUSER) {
        this.LOGOUTUSER = LOGOUTUSER;
    }

    public JComboBox getComboBox1() {
        return comboBox1;
    }

    public void setComboBox1(JComboBox comboBox1) {
        this.comboBox1 = comboBox1;
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
        String [] choices = {"", "Visualizza Voli", "Inserisci Volo", "Aggiorna Volo", "Modifica Gate", "Aggiorna Bagaglio", "Modifica Gate", "Visualizza Smarrimenti"};

        // Abilito la comboBox, la riempio con le scelte e nascondo la label output che non serve subito
        comboBox1.setEnabled(true);
        outputLabel.setVisible(false);
        comboBox1.setModel(new DefaultComboBoxModel(choices));

        // Quando cambia la selezione, mando la scelta al controller per far partire l’azione giusta
        comboBox1.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    controller.selectedItem((String) e.getItem(), DashBoardAdmin.this);
                }
            }
        });

        // Faccio vedere in alto chi è loggato (l’admin)
        welcomeTextLabel.setText("ID: " + username);

        // Il bottone logout per ora chiude semplicemente tutto (da migliorare in futuro)
        LOGOUTUSER.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
    }

    // Metodo per ottenere il pannello principale della dashboard, così si può inserire nel frame principale
    public JPanel getDashboardAdminPage() {
        return dashboardAdminPage;
    }

    public void setDashboardAdminPage(JPanel dashboardAdminPage) {
        this.dashboardAdminPage = dashboardAdminPage;
    }

    // Per gestire il dialog di scelta da altre classi se serve aprirlo o chiuderlo
    public JDialog getChoiceDialog() {
        return choiceDialog;
    }

    public void setChoiceDialog(JDialog choiceDialog) {
        this.choiceDialog = choiceDialog;
    }

}
