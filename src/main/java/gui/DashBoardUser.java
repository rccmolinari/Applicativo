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
    private JButton logoutUser;

    // Label dove mostriamo il nome o ID dell'utente loggato
    private JLabel welcomeTextLabel;

    // ComboBox che dà all’utente una lista di azioni possibili (tipo visualizza voli, prenota ecc.)
    private JComboBox<String> comboBox1;

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
        String [] choices = {"", "Visualizza Prenotazioni", "Prenota Volo", "Cerca Prenotazione", "Cerca Bagagli", "Segnala Smarrimento"};

        // Impostiamo la comboBox con queste opzioni
        comboBox1.setModel(new DefaultComboBoxModel<>(choices));

        this.username = username;

        // Abilitiamo la comboBox e gli diamo un colore un po’ più gradevole
        comboBox1.setEnabled(true);
        comboBox1.setBackground(new Color(107, 112, 119));
        comboBox1.setForeground(new Color(0, 0, 0));

        // Di default non mostriamo output, lo attiveremo solo se serve
        outputLabel.setVisible(false);

        // Quando l’utente cambia selezione nella comboBox, passiamo la scelta al controller
        comboBox1.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                String itemSelezionato = (String) e.getItem();

                if (!itemSelezionato.isEmpty()) {
                    controller.selectedItem(itemSelezionato, DashBoardUser.this);

                    // Torna al valore di default
                    comboBox1.setSelectedIndex(0);
                }
            }
        });


        // Mostriamo l’ID o nome dell’utente nell’etichetta di benvenuto
        welcomeTextLabel.setText("ID: "+username);

        // Il bottone logout chiude tutto (da migliorare magari con logout vero)
        logoutUser.addActionListener(e -> System.exit(0));

        // Carica i voli di default alla prima apertura
        controller.visualizzaVoli(this);

    }

    // Qui sotto ci sono tutti i getter e setter per i componenti, utili per modificarli o leggerli da fuori

    public JPanel getHomePage() {
        return homePage;
    }

    public JComboBox<String> getComboBox1() {
        return comboBox1;
    }


    public JLabel getOutputLabel() {
        return outputLabel;
    }

    public JDialog getChoiceDialog() {
        return choiceDialog;
    }

    public void setChoiceDialog(JDialog choiceDialog) {
        this.choiceDialog = choiceDialog;
    }


}
