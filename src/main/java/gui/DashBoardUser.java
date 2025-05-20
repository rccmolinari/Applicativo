package gui;

import javax.swing.*;
import java.awt.event.*;

public class DashBoardUser {
    private JPanel homePage;
    private JPanel welcomePanel;
    private JPanel welcomeText;
    private JButton LOGOUTUSER;
    private JLabel welcomeTextLabel;
    private JComboBox comboBox1;
    private JLabel outputLabel;
    private String username;


    public DashBoardUser(String username) {
        comboBox1.setModel(new DefaultComboBoxModel(new String[] {"", "Visualizza Voli", "Prenota Volo", "Cerca Prenotazione", "Segnala Smarrimento"}));
        this.username = username;
        comboBox1.setEnabled(true);
        outputLabel.setVisible(false);
        comboBox1.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                String item = e.getItem().toString();
                outputLabel.setVisible(true);

                outputLabel.setText("Hai scelto "+item+"... work in progress king");

            }
        });
        welcomeTextLabel.setText("Bentornato "+username+" nella homepage dell'aereoporto di Napoli");
        LOGOUTUSER.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               System.exit(0);
            }
        });
    }

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

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
