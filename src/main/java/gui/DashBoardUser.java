package gui;

import controller.Controller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class DashBoardUser {

    // Questo è il pannello principale che contiene tutta la pagina dell'utente
    private JPanel homePage;

    // Qui mettiamo la parte della pagina dedicata al messaggio di benvenuto
    private JPanel welcomePanel;

    // Il pannello che contiene solo il testo di benvenuto
    private JPanel welcomeText;

    // Bottone per uscire dall'app (qui chiude proprio il programma)
    private JButton LOGOUTUSER;

    // Label dove mostriamo il nome o ID dell'utente loggato
    private JLabel welcomeTextLabel;

    // ComboBox che dà all’utente una lista di azioni possibili (tipo visualizza voli, prenota ecc.)
    private JComboBox comboBox1;

    // Label nascosta che useremo per mostrare messaggi o risultati in pagina
    private JLabel outputLabel;

    // Username dell’utente, così possiamo usarlo per personalizzare un po’ l’interfaccia
    private String username;

    // Dialog modale che usiamo per finestre extra o scelte particolari
    private JDialog choiceDialog = null;

    // Riferimento al controller, serve per far partire le azioni vere quando l’utente interagisce
    private Controller controller;

    // Qui inizializziamo tutto, mettiamo la lista delle azioni a disposizione e i listener per gestire le scelte
    public DashBoardUser(String username, Controller controller) {
        this.controller = controller;
        // Scelte che l’utente può fare nel menu a tendina
        String [] choices = {"", "Visualizza Voli", "Prenota Volo", "Cerca Prenotazione", "Segnala Smarrimento"};

        // Impostiamo la comboBox con queste opzioni
        comboBox1.setModel(new DefaultComboBoxModel(choices));

        this.username = username;

        // Abilitiamo la comboBox e gli diamo un colore un po’ più gradevole
        comboBox1.setEnabled(true);
        comboBox1.setBackground(new Color(107, 112, 119));
        comboBox1.setForeground(new Color(0, 0, 0));

        // Di default non mostriamo output, lo attiveremo solo se serve
        outputLabel.setVisible(false);

        // Quando l’utente cambia selezione nella comboBox, passiamo la scelta al controller
        comboBox1.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if(e.getStateChange() == ItemEvent.SELECTED) {
                    controller.selectedItem((String) e.getItem(), DashBoardUser.this);
                }
            }
        });

        // Mostriamo l’ID o nome dell’utente nell’etichetta di benvenuto
        welcomeTextLabel.setText("ID: "+username);

        // Il bottone logout chiude tutto (da migliorare magari con logout vero)
        LOGOUTUSER.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
    }

    // Qui sotto ci sono tutti i getter e setter per i componenti, utili per modificarli o leggerli da fuori

    public JLabel getWelcomeTextLabel() {
        return welcomeTextLabel;
    }

    public void setWelcomeTextLabel(JLabel welcomeTextLabel) {
        this.welcomeTextLabel = welcomeTextLabel;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setWelcomeText(JPanel welcomeText) {
        this.welcomeText = welcomeText;
    }

    public JPanel getHomePage() {
        return homePage;
    }

    public void setHomePage(JPanel homePage) {
        this.homePage = homePage;
    }

    public JPanel getWelcomeText() {
        return welcomeText;
    }

    public JPanel getWelcomePanel() {
        return welcomePanel;
    }

    public void setWelcomePanel(JPanel welcomePanel) {
        this.welcomePanel = welcomePanel;
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

    public JDialog getChoiceDialog() {
        return choiceDialog;
    }

    public void setChoiceDialog(JDialog choiceDialog) {
        this.choiceDialog = choiceDialog;
    }

    private void createUIComponents() {
        // Se vuoi aggiungere componenti customizzati mettili qui
    }
}
